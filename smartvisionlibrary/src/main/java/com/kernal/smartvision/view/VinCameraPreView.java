package com.kernal.smartvision.view;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.kernal.smartvision.ocr.OcrRecogServer;
import com.kernal.smartvision.utils.CameraSetting;
import com.kernal.smartvision.utils.ThreadManager;
import com.kernal.smartvisionocr.utils.SharedPreferencesHelper;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by WenTong on 2018/12/5.
 */

public class VinCameraPreView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    SurfaceHolder mHolder;
    public Camera mCamera;
    public Context mContext;
    private int rotation;
    private List<Camera.Size> list;// 存放预览分辨率集合
    private int screenRotation;
    private int tempScreenRotation = -1;
    private int currentType;
    //压力测试需要，改为public
    public OcrRecogServer ocrRecogServer;
    private int oritation;
    private Camera.Size cameraSize;
    private boolean isFinishActivity = false;   //是否结束程序
    private Vibrator mVibrator;
    // 标志是否识别成功，避免线程重复执行，而跳转两次
    private boolean isRecogSuccess = false;
    private byte[] cameraData;
    private String recogResult = "";
    private boolean finishRecog = false;
    private int ocrType = 0;
    public int errorMsg = -1;
    private String resultPic = "";
    private String uploadPicPath = "";
    private boolean isSurfaceCreate = false;

    public WeakReference<AppCompatActivity> mOuter;
    public MyHandler myHandler;

    public Runnable finishActivity = new Runnable() {
        @Override
        public void run() {
            myHandler.removeCallbacksAndMessages(null);
            ocrRecogServer.freeKernalOpera(mContext.getApplicationContext());
            mOuter.get().finish();
        }
    };

    Handler handler;

    //识别完成，回传结果
    private void finishRecogEvent() {
        Intent intent = new Intent();
        intent.putExtra("RecogResult", recogResult);
        if (ocrType == 1) {//vin
            intent.putExtra("ocrType", 0);
        } else {//手机号
            intent.putExtra("ocrType", 1);
        }
        //裁切图
        intent.putExtra("resultPic", resultPic);
        //敏感区域图
        intent.putExtra("uploadPicPath", uploadPicPath);
        ocrRecogServer.freeKernalOpera(mContext.getApplicationContext());
        if (mOuter != null && mOuter.get() != null) {
            myHandler.removeMessages(1001);
            mOuter.get().setResult(Activity.RESULT_OK, intent);
            mOuter.get().finish();
        }
    }


    //识别完成，回传结果 for Handler
    private void finishRecogHandler() {

        //识别成功后返回到actvitiy
        Message message = handler.obtainMessage(1);
        message.obj = recogResult;
        handler.sendMessage(message);

    }

    public VinCameraPreView(Context context) {
        super(context);
        mContext = context;
        mOuter = new WeakReference<>((AppCompatActivity) context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        screenRotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        //当前选择的识别类型，vin 或者手机号
        currentType = SharedPreferencesHelper.getInt(mContext, "currentType", 1);
        //识别服务初始化
        ocrRecogServer = new OcrRecogServer(mContext.getApplicationContext()).initOcr();
    }

    public VinCameraPreView(Context context, Handler handler) {
        super(context);
        mContext = context;
        this.handler = handler;
        mOuter = new WeakReference<>((AppCompatActivity) context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        screenRotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        //当前选择的识别类型，vin 或者手机号
        currentType = SharedPreferencesHelper.getInt(mContext, "currentType", 1);
        //识别服务初始化
        ocrRecogServer = new OcrRecogServer(mContext.getApplicationContext()).initOcr();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceCreate = true;
        //打开相机
        if (mCamera == null) {
            mCamera = CameraSetting.getInstance(mContext.getApplicationContext()).open(0, mCamera);
        }
        //相机参数初始化
        CameraSetting.getInstance(mContext.getApplicationContext()).initCameraParamters(mCamera, currentType, mHolder, mOuter.get(), VinCameraPreView.this);
        if (myHandler == null) {
            myHandler = new MyHandler(this, mContext.getApplicationContext());
        }
        //自动对焦
        myHandler.sendEmptyMessageDelayed(1001, 0);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Message msg = new Message();
        msg.what = 1001;
        myHandler.sendMessage(msg);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceCreate = false;
        CameraSetting.getInstance(mContext.getApplicationContext()).closeCamera(mCamera);
        myHandler.removeMessages(1001);
        myHandler.removeCallbacksAndMessages(null);
        mCamera = null;

    }

    /**
     * 这里进行识别，在调用 OcrRecogServer 识别之前需要：
     * 1、设置屏幕方向，目的是计算识别区域
     * 2、设置 data 和 CameraSize,data 为数据，CameraSize 用来计算敏感区域的。
     *
     * @param data   识别需要的数据流
     * @param camera 相机
     */
    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        //识别线程
        Runnable comparisonRunnable = new Runnable() {
            @Override
            public synchronized void run() {
                // 通过 !finishRecog 可以避免多次跳转
                if (isFinishActivity && !finishRecog) {
                    mOuter.get().runOnUiThread(finishActivity);
                    finishRecog = true;
                }
                if (finishRecog) {
                    return;
                }
                //设置 data 和 CameraSize
                if (ocrRecogServer != null) {
                    recogResult = ocrRecogServer.startRecognize(cameraSize, data, rotation);
                }
                //识别结果判断
                if (recogResult != null && !recogResult.equals("")) {
                    finishRecog = true;
                    mVibrator = (Vibrator) mContext.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
                    mVibrator.vibrate(200);
                    cameraData = data;
                    isRecogSuccess = true;
                    //识别完成，界面跳转
                    resultPic = (String) ocrRecogServer.sparseArray.get(0);
                    uploadPicPath = (String) ocrRecogServer.sparseArray.get(1);

                    if (null != handler) {//跳转Handler不为空

                        finishRecogHandler();

                    } else
                        finishRecogEvent();
                } else if (recogResult == null && ocrRecogServer.error != -1) {
                    //授权错误
                    myHandler.sendEmptyMessage(1002);
                    errorMsg = ocrRecogServer.error;
                }
            }
        };
        changeScreenOritation(camera);
        //子线程识别
        if (ocrRecogServer.iTH_InitSmartVisionSDK == 0) {
            ThreadManager.getInstance().execute(comparisonRunnable);
        }
    }

    /**
     * 屏幕旋转改变相机预览方向
     *
     * @param camera
     */
    private void changeScreenOritation(Camera camera) {
        if (camera == null)
            return;
        screenRotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (screenRotation == 0 || screenRotation == 2) // 竖屏状态下
        {
            oritation = OcrRecogServer.SCREEN_ORITATION_PORTRAIT;
        } else { // 横屏状态下
            oritation = OcrRecogServer.SCREEN_ORITATION_HORIZONTAL;
        }
        //设置屏幕方向
        ocrRecogServer.setScreenOritation(oritation);
        // 屏幕旋转，旋转之后需要重置识别区域
        if (screenRotation != tempScreenRotation) {
            tempScreenRotation = screenRotation;
            rotation = CameraSetting.getInstance(mContext.getApplicationContext()).setCameraDisplayOrientation(screenRotation);
            camera.setDisplayOrientation(rotation);

//
//            camera.stopPreview();
//            camera.setDisplayOrientation(90);
//            camera.startPreview();

        }

        try {
            cameraSize = camera.getParameters().getPreviewSize();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cameraOnPause() {
        if (mCamera != null) {
            CameraSetting.getInstance(mContext.getApplicationContext()).closeCamera(mCamera);
            myHandler.removeMessages(1001);
            myHandler.removeCallbacksAndMessages(null);
        }
    }

    public void cameraOnResume() {
        Log.i("VinCameraPreview", "cameraOnResume: ");
        if (mCamera == null) {
            mCamera = CameraSetting.getInstance(mContext.getApplicationContext()).open(0, mCamera);
        }
        //相机参数初始化
        CameraSetting.getInstance(mContext.getApplicationContext()).initCameraParamters(mCamera, currentType, mHolder, mOuter.get(), VinCameraPreView.this);
//          CameraSetting.getInstance(mContext.getApplicationContext()).startPreview(mCamera,VinCameraPreView.this);
        if (myHandler == null) {
            myHandler = new MyHandler(this, mContext.getApplicationContext());
        }
        //自动对焦
        myHandler.sendEmptyMessageDelayed(1001, 0);

    }

    /**
     * 操作闪光灯
     *
     * @param open
     */
    public void operateFlash(boolean open) {
        if (open) {
            CameraSetting.getInstance(mContext.getApplicationContext()).openCameraFlash(mCamera);
        } else {
            CameraSetting.getInstance(mContext.getApplicationContext()).closedCameraFlash(mCamera);
        }
    }

    /**
     * 设置缩放
     *
     * @param zoom
     */
    public void setZoom(boolean zoom) {
        if (mCamera != null && mCamera.getParameters().isZoomSupported()) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (zoom) {
                parameters.setZoom((int) (mCamera.getParameters().getMaxZoom() * 0.4));
            } else {
                parameters.setZoom(0);
            }
            mCamera.setParameters(parameters);
        }
    }

    /**
     * 控制识别类型，vin 还是 手机号,设置完识别类型之后要重置敏感区域。
     *
     * @param type
     */
    public void setCurrentType(int type) {
        this.ocrType = type;
        ocrRecogServer.setOcr_type(type);
    }

    /**
     * 设置是否拍照
     */
    public void setTakePicture() {
        ocrRecogServer.setTakePicture();
    }

    /**
     * 结束识别
     */
    public void finishRecognize() {
        isFinishActivity = true;
    }

    /**
     * 自定义 静态 handler 主要是为了防止内存泄漏
     */
    static class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<VinCameraPreView> mOuterPreview;
        private Context appContext;
        Toast toast = null;

        public MyHandler(VinCameraPreView cameraPreView, Context mContext) {
            mOuterPreview = new WeakReference<VinCameraPreView>(cameraPreView);
            appContext = mContext;
        }

        public void handleMessage(Message msg) {
            //自动对焦
            if (msg.what == 1001) {
                if (mOuterPreview.get() != null && mOuterPreview.get().mCamera != null) {
                    CameraSetting.getInstance(appContext).autoFocus(mOuterPreview.get().mCamera);
                    mOuterPreview.get().myHandler.sendEmptyMessageDelayed(1001, 2500);
                }
            }
            //识别错误，一般是授权问题
            else if (msg.what == 1002) {
                if (mOuterPreview.get().errorMsg != -1) {
                    if (toast != null) {
                        toast.setText("识别错误，错误码：" + mOuterPreview.get().errorMsg);
                    } else {
                        toast = Toast.makeText(appContext, "识别错误，错误码：" + mOuterPreview.get().errorMsg, Toast.LENGTH_LONG);
                    }
                    toast.show();
                }
            }
        }
    }

}
