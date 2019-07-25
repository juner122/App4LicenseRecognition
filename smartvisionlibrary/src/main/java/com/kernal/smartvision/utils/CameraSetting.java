package com.kernal.smartvision.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kernal.smartvision.activity.SmartvisionCameraActivity;
import com.kernal.smartvision.view.VinCameraPreView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 相机工具类，操作相机
 */
public class CameraSetting {
    Camera.Parameters parameters;
    public int srcWidth, srcHeight;
    public int preWidth, preHeight;
    public int picWidth, picHeight;
    public int surfaceWidth, surfaceHeight;
    List<Camera.Size> list;
    private boolean isShowBorder = false;
    private Context context;
    public static int currentCameraId = -1;
    private Camera camera;
    private int screenRotation, rotation, currentType;
    WeakReference<VinCameraPreView> weakVinCameraPreView;
    WeakReference<AppCompatActivity> weakReference;
    WeakReference<SurfaceHolder> weakHolder;

    private CameraSetting(Context context) {
        this.context = context;
        setScreenSize(context);
    }

    private static CameraSetting single = null;

    // 单例模式，传 application context 防止内存泄漏
    public static CameraSetting getInstance(Context context) {
        if (single == null) {
            single = new CameraSetting(context);
        }
        return single;
    }

    /**
     * 打开相机
     *
     * @param cameraId
     * @param camera
     * @return
     */
    public Camera open(int cameraId, Camera camera) {
        this.camera = camera;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            return null;
        }
        boolean explicitRequest = cameraId >= 0;
        if (!explicitRequest) {
            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }
            cameraId = index;
        }
        if (cameraId < numCameras) {
            camera = Camera.open(cameraId);
        } else {
            if (explicitRequest) {
                camera = null;
            } else {
                cameraId = 0;
                camera = Camera.open(0);
            }
        }
        currentCameraId = cameraId;
        return camera;
    }

    /**
     * 获取相机旋转角度
     *
     * @param uiRot
     * @return
     */
    public int setCameraDisplayOrientation(int uiRot) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        camera.getCameraInfo(currentCameraId, info);
        int degrees = 0;
        switch (uiRot) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    /**
     * 开启子线程初始化相机参数
     */
    Runnable initCameraParamters = new Runnable() {
        @Override
        public void run() {
            try {
                Camera.Parameters parameters = camera.getParameters();
                list = parameters.getSupportedPreviewSizes();
                screenRotation = ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                rotation = CameraSetting.getInstance(context.getApplicationContext()).setCameraDisplayOrientation(screenRotation);
                CameraSetting.getInstance(context.getApplicationContext()).getCameraPreParameters(camera);

                if (parameters.getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                parameters.setPictureFormat(PixelFormat.JPEG);

                int srcWidth = CameraSetting.getInstance(context.getApplicationContext()).srcWidth;
                int srcHeight = CameraSetting.getInstance(context.getApplicationContext()).srcHeight;
                float ratio = (float) srcHeight / srcWidth;
                if (weakReference.get() != null) {
                    Camera.Size optimalPreviewSize = CameraSetting.getOptimalPreviewSize(weakReference.get(), camera.getParameters().getSupportedPreviewSizes(), ratio);
                    parameters.setPreviewSize(optimalPreviewSize.width, optimalPreviewSize.height);
                }
                // parameters.setExposureCompensation((int)(parameters.getMinExposureCompensation()*0.7));
                //parameters.setExposureCompensation((int) (parameters.getMaxExposureCompensation()*0.2));
                if (currentType == 2) {
                    parameters.setZoom((int) (camera.getParameters().getMaxZoom() * 0.4));
                }
                camera.setDisplayOrientation(rotation);
                if (weakReference.get() != null) {
                    camera.setPreviewCallback(weakVinCameraPreView.get());
                }
                camera.setParameters(parameters);
                if (weakHolder.get() != null) {
                    camera.setPreviewDisplay(weakHolder.get());
                }
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 相机参数初始化
     *
     * @param camera
     * @param currentType
     * @param holder
     * @param activity
     * @param vinCameraPreView
     */
    public void initCameraParamters(Camera camera, int currentType, SurfaceHolder holder, AppCompatActivity activity, VinCameraPreView vinCameraPreView) {
        this.camera = camera;
        this.currentType = currentType;
        weakHolder = new WeakReference<SurfaceHolder>(holder);
        weakReference = new WeakReference<AppCompatActivity>(activity);
        weakVinCameraPreView = new WeakReference<VinCameraPreView>(vinCameraPreView);
        ThreadManager.getInstance().execute(initCameraParamters);
    }

    /**
     * @Title: 关闭相机
     * @Description: 释放相机资源
     */
    public void closeCamera(Camera camera) {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
        }
    }

    public void stopPreview(Camera camera) {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
            }
        } catch (Exception e) {
        }
    }

    public void startPreview(Camera camera, VinCameraPreView vinCameraPreView) {
        weakVinCameraPreView = new WeakReference<VinCameraPreView>(vinCameraPreView);
        if (weakReference.get() != null) {
            camera.setPreviewCallback(weakVinCameraPreView.get());
        }
        camera.startPreview();
    }

    /**
     * @param camera 相机对象
     * @throws
     * @Description: 打开闪光灯
     */
    public void openCameraFlash(Camera camera) {
        try {
            if (camera == null)
                camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            List<String> flashList = parameters.getSupportedFlashModes();
            if (flashList != null
                    && flashList.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                parameters.setExposureCompensation((int) (parameters.getMinExposureCompensation() * 0.7));
                camera.setParameters(parameters);
            } else {
                Toast.makeText(context, context.getString(context.getResources().getIdentifier("unsupportflash", "string", context.getPackageName())), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param camera 相机对象
     * @Description: 关闭闪光灯
     */
    public void closedCameraFlash(Camera camera) {
        try {
            if (camera == null)
                camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            List<String> flashList = parameters.getSupportedFlashModes();
            if (flashList != null && flashList.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                parameters.setExposureCompensation(0);
                camera.setParameters(parameters);
            } else {
                Toast.makeText(
                        context, context.getString(context.getResources().getIdentifier(
                                "unsupportflash", "string", context.getPackageName())), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @param currentActivity
     * @param sizes           最理想的预览分辨率的宽和高
     */
    public static Camera.Size getOptimalPreviewSize(Activity currentActivity, List<Camera.Size> sizes, double targetRatio) {
        final double ASPECT_TOLERANCE = 0.23;
        if (sizes == null) {
            return null;
        }

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        // Because of bugs of overlay and layout, we sometimes will try to
        // layout the viewfinder in the portrait orientation and thus get the
        // wrong size of mSurfaceView. When we change the preview size, the
        // new overlay will be created before the old one closed, which causes
        // an exception. For now, just get the screen size

        Display display = currentActivity.getWindowManager().getDefaultDisplay();

        int targetHeight = Math.min(display.getHeight(), display.getWidth());

        if (targetHeight <= 0) {
            // We don't know the size of SurfaceView, use screen heightPercent
            targetHeight = display.getHeight();
        }

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            //预览分辨的比
            if (size.width <= 1280 || size.height <= 960) {
                double ratio = (double) size.width / size.height;

                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                    continue;
                }
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

        }

        // Cannot find the one match the aspect ratio. This should not happen.
        // Ignore the requirement.
        if (optimalSize == null) {
            System.out.println("No preview size match the aspect ratio");
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        System.out.println("预览分辨率" + optimalSize.width + "    " + optimalSize.height);
        return optimalSize;
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public Camera.Size getResolution(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        Camera.Size s = params.getPreviewSize();
        return s;
    }

    /**
     * @param camera
     * @param x           为对应到图像上的点的横坐标
     * @param y           为对应到图像上的点的纵坐标
     * @param coefficient
     * @param isPortrait
     * @return
     */
    public Rect calculateTapArea(Camera camera, float x, float y, float coefficient, boolean isPortrait) {
        float focusAreaSizeX = 400;
        float focusAreaSizeY = 200;
        Camera.Size sise = getResolution(camera);
        RectF rectF;
        int left, top;
        int areaSizeX = Float.valueOf(focusAreaSizeX * coefficient).intValue();
        int areaSizeY = Float.valueOf(focusAreaSizeY * coefficient).intValue();
        if (sise.width < areaSizeX) {
            areaSizeX = sise.width;
        }
        if (sise.height < areaSizeY) {
            areaSizeY = sise.height;
        }
        int centerX = 0;
        int centerY = 0;
        if (isPortrait) {
            centerX = (int) ((x / sise.height) * 2000 - 1000);
            centerY = (int) ((y / sise.width) * 2000 - 1000);
        } else {
            centerX = (int) ((x / sise.width) * 2000 - 1000);
            centerY = (int) ((y / sise.height) * 2000 - 1000);
        }

        if (isPortrait) {
            left = clamp(centerX - (areaSizeY / 2), -1000, 1000);
            top = clamp(centerY - (areaSizeX / 2), -1000, 1000);
            rectF = new RectF(left, top, left + areaSizeY, top + areaSizeX);
        } else {
            left = clamp(centerX - (areaSizeX / 2), -1000, 1000);
            top = clamp(centerY - (areaSizeY / 2), -1000, 1000);
            rectF = new RectF(left, top, left + areaSizeX, top + areaSizeY);
        }
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    @SuppressLint("NewApi")
    private void setScreenSize(Context context) {
        int x, y;
        WindowManager wm = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            } else {
                display.getSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            }
        } else {
            x = display.getWidth();
            y = display.getHeight();
        }

        srcWidth = x;
        srcHeight = y;
    }

    /**
     * 获取设备的预览分辨率的宽和高
     *
     * @param camera
     */
    public void getCameraPreParameters(Camera camera) {
        isShowBorder = false;
        // 荣耀七设备
        if ("PLK-TL01H".equals(Build.MODEL)) {
            preWidth = 1920;
            preHeight = 1080;
            return;
        }
        // 其他设备
        parameters = camera.getParameters();
        list = parameters.getSupportedPreviewSizes();
        float ratioScreen = (float) srcWidth / srcHeight;

        for (int i = 0; i < list.size(); i++) {
            float ratioPreview = (float) list.get(i).width / list.get(i).height;
            if (ratioScreen == ratioPreview) {// 判断屏幕宽高比是否与预览宽高比一样，如果一样执行如下代码
                if (list.get(i).width >= 1280 || list.get(i).height >= 720) {// 默认预览以1280*720为标准
                    if (preWidth == 0 && preHeight == 0) {// 初始值
                        preWidth = list.get(i).width;
                        preHeight = list.get(i).height;
                    }
                    if (list.get(0).width > list.get(list.size() - 1).width) {
                        // 如果第一个值大于最后一个值
                        if (preWidth > list.get(i).width
                                || preHeight > list.get(i).height) {
                            // 当有大于1280*720的分辨率但是小于之前记载的分辨率，我们取中间的分辨率
                            preWidth = list.get(i).width;
                            preHeight = list.get(i).height;
                        }
                    } else {
                        // 如果第一个值小于最后一个值
                        if (preWidth < list.get(i).width
                                || preHeight < list.get(i).height) {
                            // 如果之前的宽度和高度大于等于1280*720，就不需要再筛选了
                            if (preWidth >= 1280 || preHeight >= 720) {

                            } else {
                                // 为了找到合适的分辨率，如果preWidth和preHeight没有比1280*720大的就继续过滤
                                preWidth = list.get(i).width;
                                preHeight = list.get(i).height;
                            }
                        }
                    }
                }
            }
        }
        // 说明没有找到程序想要的分辨率
        if (preWidth == 0 || preHeight == 0) {
            isShowBorder = true;
            preWidth = list.get(0).width;
            preHeight = list.get(0).height;
            for (int i = 0; i < list.size(); i++) {

                if (list.get(0).width > list.get(list.size() - 1).width) {
                    // 如果第一个值大于最后一个值
                    if (preWidth >= list.get(i).width
                            || preHeight >= list.get(i).height) {
                        // 当上一个选择的预览分辨率宽或者高度大于本次的宽度和高度时，执行如下代码:
                        if (list.get(i).width >= 1280) {
                            // 当本次的预览宽度和高度大于1280*720时执行如下代码
                            preWidth = list.get(i).width;
                            preHeight = list.get(i).height;

                        }
                    }
                } else {
                    if (preWidth <= list.get(i).width
                            || preHeight <= list.get(i).height) {
                        if (preWidth >= 1280 || preHeight >= 720) {

                        } else {
                            // 当上一个选择的预览分辨率宽或者高度大于本次的宽度和高度时，执行如下代码:
                            if (list.get(i).width >= 1280) {
                                // 当本次的预览宽度和高度大于1280*720时执行如下代码
                                preWidth = list.get(i).width;
                                preHeight = list.get(i).height;

                            }
                        }

                    }
                }
            }
        }
        // 如果没有找到大于1280*720的分辨率的话，取集合中的最大值进行匹配
        if (preWidth == 0 || preHeight == 0) {
            isShowBorder = true;
            if (list.get(0).width > list.get(list.size() - 1).width) {
                preWidth = list.get(0).width;
                preHeight = list.get(0).height;
            } else {
                preWidth = list.get(list.size() - 1).width;
                preHeight = list.get(list.size() - 1).height;
            }
        }
        if (isShowBorder) {
            if (ratioScreen > (float) preWidth / preHeight) {
                surfaceWidth = (int) (((float) preWidth / preHeight) * srcHeight);
                surfaceHeight = srcHeight;
            } else {
                surfaceWidth = srcWidth;
                surfaceHeight = (int) (((float) preHeight / preWidth) * srcHeight);
            }
        } else {
            surfaceWidth = srcWidth;
            surfaceHeight = srcHeight;
        }

    }

    /**
     * 手动聚焦
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressLint("NewApi")
    public void onFocus(Camera mCamera) {
        Camera.Parameters parameters = mCamera.getParameters();
        //不支持设置自定义聚焦，则使用自动聚焦，返回
        if (parameters.getMaxNumFocusAreas() <= 0) {
            mCamera.autoFocus(null);
            return;
        }
        setScreenSize(context);
        Rect focusRect;
        Rect meteringRect;
        if (srcWidth > srcHeight) {
            focusRect = calculateTapArea(mCamera, getResolution(mCamera).width / 2, getResolution(mCamera).height / 2, 1f, false);
            meteringRect = calculateTapArea(mCamera, getResolution(mCamera).width / 2, getResolution(mCamera).height / 2, 1.5f, false);
        } else {
            focusRect = calculateTapArea(mCamera, getResolution(mCamera).height / 2, getResolution(mCamera).width / 2, 1f, true);
            meteringRect = calculateTapArea(mCamera, getResolution(mCamera).height / 2, getResolution(mCamera).width / 2, 1.5f, true);
        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        if (parameters.getMaxNumFocusAreas() > 0) {
            ArrayList focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));
            parameters.setFocusAreas(focusAreas);
        }

        if (parameters.getMaxNumMeteringAreas() > 0) {
            ArrayList meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));
            parameters.setMeteringAreas(meteringAreas);
        }
        try {
            mCamera.cancelAutoFocus();
            mCamera.setParameters(parameters);
        } catch (Exception e) {

        }
        mCamera.autoFocus(null);
    }

    /**
     * 自动对焦
     *
     * @param mCamera
     */
    public void autoFocus(Camera mCamera) {
        if (mCamera != null) {
            try {
                if (mCamera.getParameters().getSupportedFocusModes() != null
                        && mCamera.getParameters().getSupportedFocusModes()
                        .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    onFocus(mCamera);
                } else {
                    Toast.makeText(context, context.getString(context.getResources().getIdentifier(
                            "unsupport_auto_focus", "string",
                            context.getPackageName())), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("对焦失败");
            }
        }
    }
}
