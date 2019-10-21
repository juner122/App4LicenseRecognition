package com.eb.geaiche.zbar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.CouponWriteActivity;
import com.eb.geaiche.activity.CouponWriteRecordActivity;
import com.eb.geaiche.activity.MakeOrderActivity;
import com.eb.geaiche.activity.MakeOrderSuccessActivity;
import com.eb.geaiche.activity.OrderInfoActivity;
import com.eb.geaiche.activity.OrderPayActivity;
import com.eb.geaiche.api.ApiLoader;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmConperDialog;
import com.eb.geaiche.zbar.camera.CameraManager;
import com.eb.geaiche.zbar.decode.MainHandler;
import com.eb.geaiche.zbar.utils.BeepManager;
import com.juner.mvp.bean.Coupon;
import com.juner.mvp.bean.GoodsEntity;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Desc: 1:启动一个SurfaceView作为取景预览
 * 2:开启camera,在后台独立线程中完成扫描任务
 * 3:对解码返回的结果进行处理.
 * 4:释放资源
 */
public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 26;
    public static final String EXTRA_STRING = "extra_string";
    public static final int TYPE_BOOK_COVER = 0x101;
    public static final int TYPE_BOOK_CHAPTER = 0x102;
    private static final String TAG = "CaptureActivity";
    private MainHandler mainHandler;
    private SurfaceHolder mHolder;

    private CameraManager mCameraManager;
    private BeepManager beepManager;

    private SurfaceView scanPreview;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;


    EditText et_num;
    TextView enter;

    int view_type;//0 主页面  1 美容下单  2 待服务页面新增套卡


    private boolean isHasSurface = false;
    private boolean isOpenCamera = false;

    public Handler getHandler() {
        return mainHandler;
    }

    private TextView tv_record;//核销记录

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        view_type = getIntent().getIntExtra("view_type", 0);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        checkPermissionCamera();


    }

    private void initView() {
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        tv_record = (TextView) findViewById(R.id.tv_record);
        et_num = (EditText) findViewById(R.id.et_num);
        enter = (TextView) findViewById(R.id.enter);
        TextView tv_record = (TextView) findViewById(R.id.tv_record);//核销记录

        if (view_type == 0) tv_record.setVisibility(View.VISIBLE);
        else tv_record.setVisibility(View.INVISIBLE);


        findViewById(R.id.capture_imageview_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isHasSurface = false;
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);


        tv_record.setOnClickListener(v -> startActivity(new Intent(CaptureActivity.this, CouponWriteRecordActivity.class)));
        enter.setOnClickListener(v -> {
            if (TextUtils.isEmpty(et_num.getText())) {
                ToastUtils.showToast("劵号不能为空！");
                return;
            }
            toCoupon(et_num.getText().toString());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOpenCamera) {
            mHolder = scanPreview.getHolder();
            mCameraManager = new CameraManager(getApplication());
            if (isHasSurface) {
                initCamera(mHolder);
            } else {
                mHolder.addCallback(this);
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }
    }

    @Override
    public void onPause() {
        releaseCamera();
        super.onPause();
        if (scanLine != null) {
            scanLine.clearAnimation();
            scanLine.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //remove SurfaceCallback
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
    }

    //region 初始化和回收相关资源
    private void initCamera(SurfaceHolder surfaceHolder) {
        mainHandler = null;
        try {
            mCameraManager.openDriver(surfaceHolder);
            if (mainHandler == null) {
                mainHandler = new MainHandler(this, mCameraManager);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "相机被占用", ioe);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Log.e(TAG, "Unexpected error initializing camera");
            displayFrameworkBugMessageAndExit();
        }

    }

    private void releaseCamera() {
        if (null != mainHandler) {
            //关闭聚焦,停止预览,清空预览回调,quit子线程looper
            mainHandler.quitSynchronously();
            mainHandler = null;
        }
        //关闭声音
        if (null != beepManager) {
            Log.e(TAG, "releaseCamera: beepManager release");
            beepManager.releaseRing();
            beepManager = null;
        }
        //关闭相机
        if (mCameraManager != null) {
            mCameraManager.closeDriver();
            mCameraManager = null;
        }
    }
    //endregion

    //region 检查权限
    private void checkPermissionCamera() {
        int checkPermission = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            // checkPermission =ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
            checkPermission = PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                isOpenCamera = true;
            }

        } else {
            checkPermission = checkPermission(26);
            if (checkPermission == AppOpsManager.MODE_ALLOWED) {
                isOpenCamera = true;
            } else if (checkPermission == AppOpsManager.MODE_IGNORED) {
                isOpenCamera = false;
                displayFrameworkBugMessageAndExit();
            }
        }
    }

    /**
     * 反射调用系统权限,判断权限是否打开
     *
     * @param permissionCode 相应的权限所对应的code
     * @see {@link AppOpsManager }
     */
    private int checkPermission(int permissionCode) {
        int checkPermission = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager _manager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?>[] types = new Class[]{int.class, int.class, String.class};
                Object[] args = new Object[]{permissionCode, Binder.getCallingUid(), getPackageName()};
                Method method = _manager.getClass().getDeclaredMethod("noteOp", types);
                method.setAccessible(true);
                Object _o = method.invoke(_manager, args);
                if ((_o instanceof Integer)) {
                    checkPermission = (Integer) _o;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            checkPermission = 0;
        }
        return checkPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();

                } else {
                    finish();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void displayFrameworkBugMessageAndExit() {
        String per = String.format(getString(R.string.permission), getString(R.string.camera), getString(R.string.camera));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.qr_name));
        builder.setMessage(per);
        builder.setPositiveButton(getString(R.string.i_know), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CaptureActivity.this.finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                CaptureActivity.this.finish();
            }
        });
        builder.show();
    }
    //endregion

    //region 扫描结果
    public void checkResult(final String result) {
        if (beepManager != null) {
            beepManager.startRing();
        }
        mainHandler.postDelayed(() -> toCoupon(result.trim()), beepManager.getTimeDuration());
    }


    //前往优惠劵核销页面
    private void toCoupon(String trim) {

        switch (view_type) {
            case 0://主页面
                new ApiLoader(this).queryByNumber(trim).subscribe(new RxSubscribe<Coupon>(this, true) {
                    @Override
                    protected void _onNext(Coupon coupon) {

                        Intent i = new Intent(CaptureActivity.this, CouponWriteActivity.class);
                        i.putExtra("coupon", coupon);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    protected void _onError(String message) {
                        finish();
                        ToastUtils.showToast(message);
                    }
                });
                break;
            case 1://下单页面添加套卡
                new ApiLoader(this).activityqueryByNumber(trim, null).subscribe(new RxSubscribe<GoodsEntity>(this, true) {
                    @Override
                    protected void _onNext(GoodsEntity goodsEntity) {
                        ToastUtils.showToast("扫码成功！");

                        MyApplication.cartUtils.addMeal(goodsEntity);

                        finish();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(message);
                    }
                });
                break;
            case 2://待服务页面添加套卡  扫描成功弹出套卡页面
                new ApiLoader(this).activityqueryByNumber(trim, null).subscribe(new RxSubscribe<GoodsEntity>(this, true) {
                    @Override
                    protected void _onNext(GoodsEntity goodsEntity) {

                        //扫描成功
                        final ConfirmConperDialog dialog = new ConfirmConperDialog(CaptureActivity.this, goodsEntity.getActivityName(),goodsEntity.getGoodsName());
                        dialog.show();
                        dialog.setClicklistener(new ConfirmConperDialog.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                dialog.dismiss();
                                activityqueryByNumber(trim);
                            }
                        });


                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(message);
                    }
                });

                break;

        }
    }


    //传入订单id
    private void activityqueryByNumber(String trim) {
        new ApiLoader(this).activityqueryByNumber(trim, getIntent().getStringExtra("id")).subscribe(new RxSubscribe<GoodsEntity>(this, true) {
            @Override
            protected void _onNext(GoodsEntity goodsEntity) {
                MyApplication.cartUtils.addMeal(goodsEntity);


                Intent intent;


                switch (getIntent().getIntExtra("type", 0)) {

                    case 0:

                        intent = new Intent(CaptureActivity.this, MakeOrderSuccessActivity.class);
                        intent.putExtra("CaptureActivity", true);
                        startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(CaptureActivity.this, OrderInfoActivity.class);
                        intent.putExtra("CaptureActivity", true);
                        startActivity(intent);
                        break;
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    //endregion

    //region  初始化截取的矩形区域
    public Rect initCrop() {
        int cameraWidth = 0;
        int cameraHeight = 0;
        if (null != mCameraManager) {
            cameraWidth = mCameraManager.getCameraResolution().y;
            cameraHeight = mCameraManager.getCameraResolution().x;
        }

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
        return new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //endregion

    //region SurfaceHolder Callback 回调方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** 没有添加SurfaceHolder的Callback");
        }
        if (isOpenCamera) {
            if (!isHasSurface) {
                isHasSurface = true;
                initCamera(holder);
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged: ");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;

    }
    //endregion
}
