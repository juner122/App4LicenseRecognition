package com.eb.geaiche.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.eb.geaiche.R;
import com.eb.geaiche.util.ToastUtils;
import com.kernal.smartvision.adapter.VinParseResultAdapter;
import com.kernal.smartvision.ocr.Devcode;
import com.kernal.smartvision.ocr.OCRConfigParams;
import com.kernal.smartvision.ocr.OcrTypeHelper;
import com.kernal.smartvision.utils.PermissionUtils;
import com.kernal.smartvision.view.RectFindView;
import com.kernal.smartvision.view.ResultLayout;
import com.kernal.smartvision.view.VinCameraPreView;
import com.kernal.smartvisionocr.utils.SharedPreferencesHelper;
import com.kernal.vinparseengine.VinParseInfo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;


/**
 * Created by WenTong on 2018/12/5.
 */

public class CarVinDISActivity2 extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    //Ocr 类型， 0：vin 和手机号都使用； 1：vin ；  2： 手机号；
    private int OcrType;
    //识别模板参数类，包括敏感区域位置等信息
    private OcrTypeHelper ocrTypeHelper;
    //当前选中类型   1:使用 vin;     2: 使用手机号码。
    private int currentType;
    private RectFindView rectFindView;
    private Animation verticalAnimation;
    private int srcWidth, srcHeight;//, screenWidth, screenHeight;
    private boolean isScreenPortrait = true;

    private static final int ScreenHorizontal = 2;
    private static final int ScreentVertical = 1;
    private int scan_line_width;
    private RelativeLayout relativeLayout;
    private ImageView scanHorizontalLineImageView, iv_camera_back, iv_camera_flash;
    private ImageButton imbtn_takepic;
    private FrameLayout surfaceContainer;
    private EditText et_vin;

    private ListView result_vin_list;
    private EditText result_view;

    private boolean isOpenFlash = false;
    VinCameraPreView vinCameraPreView;
    private DisplayMetrics dm;
    private int defaultTextSize = 12;
    int statusHeight;
    boolean isResmue = false;
    private float marginTop;


    //VIN 码解析类
    private VinParseInfo vpi;
    private VinParseResultAdapter VPRadapter;
    private List<HashMap<String, String>> vinInfo;

    public static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE, // 读取权限
            Manifest.permission.CAMERA,
    };


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);// VIN识别成功返回
            switch (msg.what) {
                case 1:

                    String recogResult = msg.obj.toString();


                    vinRefresh(recogResult);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否需要动态授权
        if (Build.VERSION.SDK_INT >= 23) {
            //先进行权限申请
            permission();
        } else {
            //不需要动态授权直接布局
            setContentView(R.layout.activity_car_vin_dis2);
            initView();
            layoutView();
        }

    }

    private void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        et_vin = findViewById(R.id.et_vin);
        tv_title.setText("扫描车架号");

        relativeLayout = findViewById(R.id.camera_re);
        result_vin_list = findViewById(R.id.result_vin_list);//车辆信息列表
        result_view = findViewById(R.id.result_vin_view);//车架号

        scanHorizontalLineImageView = (ImageView) findViewById(R.id.camera_scanHorizontalLineImageView);
        iv_camera_back = (ImageView) findViewById(R.id.iv_camera_back);
        iv_camera_back.setOnClickListener(this);
        iv_camera_flash = (ImageView) findViewById(R.id.iv_camera_flash);
        iv_camera_flash.setOnClickListener(this);
        imbtn_takepic = (ImageButton) findViewById(R.id.imbtn_takepic);
        imbtn_takepic.setOnClickListener(this);
        surfaceContainer = (FrameLayout) findViewById(R.id.camera_container);
        //动态创建 SurfaceView
        vinCameraPreView = new VinCameraPreView(this, mHandler);
        surfaceContainer.addView(vinCameraPreView);


        OcrType = OCRConfigParams.getOcrType(this);
        currentType = SharedPreferencesHelper.getInt(this, "currentType", 1);
        //判断使用的是 vin 还是手机号
        if (OcrType != 0) {

            currentType = OcrType;
            SharedPreferencesHelper.putInt(this, "currentType", currentType);
        } else {
            // 读取,默认 vin
            currentType = SharedPreferencesHelper.getInt(this, "currentType", 1);
        }


        vinCameraPreView.setCurrentType(currentType);
    }


    private void vinRefresh(String recogResult){

        result_view.setText(recogResult);
        vpi = new VinParseInfo(CarVinDISActivity2.this);
        vinInfo = vpi.getVinParseInfo(Devcode.devcode, recogResult);
        if (vinInfo != null) {
            VPRadapter = new VinParseResultAdapter(CarVinDISActivity2.this, vinInfo, srcWidth, srcHeight);
            result_vin_list.setAdapter(VPRadapter);
        }else {
            ToastUtils.showToast("信息查找失败，请检查车架号！");
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > 18) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(option);
        }

        isResmue = true;
        if (vinCameraPreView != null) {
            vinCameraPreView.cameraOnResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResmue = false;
        if (vinCameraPreView != null) {
            vinCameraPreView.cameraOnPause();
        }

    }

    //界面的布局，主要包括两方面：扫描框的布局 和 界面其他元素布局
    private void layoutView() {
        setScreenSize(this);
        statusHeight = getStatusBarHeight();
        marginTop = (this.getResources().getDisplayMetrics().density * 2 + 0.5f);
        //重新计算横竖屏
        // 获取屏幕旋转的角度
        int screenRotation = getWindowManager().getDefaultDisplay().getRotation();
        if (screenRotation == 0 || screenRotation == 2) // 竖屏状态下
        {
            isScreenPortrait = true;
        } else { // 横屏状态下
            isScreenPortrait = false;
        }
        layoutRectAndScanLineView();
        layoutNormalView();
    }

    /**
     * 扫描框及扫描线布局
     */
    private void layoutRectAndScanLineView() {

        //竖屏布局
        if (isScreenPortrait) {
            if (rectFindView != null) {
                RemoveView();
            }
            ocrTypeHelper = new OcrTypeHelper(currentType, ScreentVertical).getOcr();
            rectFindView = new RectFindView(this, ocrTypeHelper, srcWidth, srcHeight);
            relativeLayout.addView(rectFindView);
            scan_line_width = (int) (ocrTypeHelper.widthPercent * srcWidth);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scan_line_width, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 0;
            scanHorizontalLineImageView.setLayoutParams(layoutParams);
            verticalAnimation = new TranslateAnimation(ocrTypeHelper.leftPointXPercent * srcWidth, ocrTypeHelper.leftPointXPercent * srcWidth, ocrTypeHelper.leftPointYPercent * srcHeight - marginTop, (float) ((ocrTypeHelper.leftPointYPercent + ocrTypeHelper.heightPercent) * srcHeight) - marginTop);
            verticalAnimation.setDuration(1500);
            verticalAnimation.setRepeatCount(Animation.INFINITE);
            scanHorizontalLineImageView.startAnimation(verticalAnimation);

        } else {
            //横屏布局
            if (rectFindView != null) {
                RemoveView();
            }
            ocrTypeHelper = new OcrTypeHelper(currentType, ScreenHorizontal).getOcr();
            //扫描框
            rectFindView = new RectFindView(this, ocrTypeHelper, srcWidth, srcHeight);
            relativeLayout.addView(rectFindView);
            scan_line_width = (int) (ocrTypeHelper.widthPercent * srcWidth);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scan_line_width, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 0;
            scanHorizontalLineImageView.setLayoutParams(layoutParams);
            verticalAnimation = new TranslateAnimation(ocrTypeHelper.leftPointXPercent * srcWidth, ocrTypeHelper.leftPointXPercent * srcWidth, ocrTypeHelper.leftPointYPercent * srcHeight - marginTop, (float) ((ocrTypeHelper.leftPointYPercent + ocrTypeHelper.heightPercent) * srcHeight) - marginTop);
            verticalAnimation.setDuration(1500);
            verticalAnimation.setRepeatCount(Animation.INFINITE);
            scanHorizontalLineImageView.startAnimation(verticalAnimation);
        }
    }

    /**
     * 动画销毁
     */
    private void RemoveView() {
        if (rectFindView != null) {
            rectFindView.destroyDrawingCache();
            relativeLayout.removeView(rectFindView);
            scanHorizontalLineImageView.clearAnimation();
            rectFindView = null;
        }
    }

    /**
     * 界面其他元素布局，如照相按钮，返回等。
     */
    private void layoutNormalView() {
        if (isScreenPortrait) {
            //返回按钮
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (srcHeight * 0.05), (int) (srcHeight * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.1);
            layoutParams.topMargin = (int) (srcHeight * 0.05);
            iv_camera_back.setLayoutParams(layoutParams);
            //闪光灯按钮
            layoutParams = new RelativeLayout.LayoutParams((int) (srcHeight * 0.05), (int) (srcHeight * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.8);
            layoutParams.topMargin = (int) (srcHeight * 0.2);
            iv_camera_flash.setLayoutParams(layoutParams);
            //照相按钮
            //  if (srcHeight == screenHeight) {
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) (srcHeight * 0.05), (int) (srcHeight * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.86);
            int topmargin = (int) (int) (srcHeight * 0.4) + (int) (srcHeight * 0.02);
            layoutParams.topMargin = topmargin;
            imbtn_takepic.setLayoutParams(layoutParams);


            //底部的背景条
            layoutParams = new RelativeLayout.LayoutParams(srcWidth, (int) (srcHeight * 0.085));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        } else {
            //返回按钮
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (srcWidth * 0.05), (int) (srcWidth * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.05);
            layoutParams.topMargin = (int) (srcHeight * 0.05);
            iv_camera_back.setLayoutParams(layoutParams);
            //闪光灯按钮
            layoutParams = new RelativeLayout.LayoutParams((int) (srcWidth * 0.05), (int) (srcWidth * 0.05));
            layoutParams.leftMargin = (int) (srcWidth * 0.75);
            layoutParams.topMargin = (int) (srcHeight * 0.05);
            iv_camera_flash.setLayoutParams(layoutParams);

            //照相按钮

            layoutParams = new RelativeLayout.LayoutParams((int) (srcWidth * 0.05), (int) (srcWidth * 0.05));
            int topmargin = (int) (int) (srcHeight * 0.4) + (int) (srcHeight * 0.02);
            layoutParams.leftMargin = (int) (srcWidth * 0.7);
            layoutParams.topMargin = topmargin;
            imbtn_takepic.setLayoutParams(layoutParams);

            //侧边背景条
            layoutParams = new RelativeLayout.LayoutParams((int) (0.15 * srcWidth), srcHeight);
            layoutParams.leftMargin = (int) (0.85 * srcWidth) - (srcWidth - srcWidth);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


        }
    }


    /**
     * 获取屏幕信息
     *
     * @param context
     */
    public void setScreenSize(Context context) {
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

    // 屏幕旋转完成后布局
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //重绘布局
        layoutView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        //返回按钮
        if (i == R.id.iv_camera_back) {
            vinCameraPreView.finishRecognize();
            finish();
        } else if (i == R.id.iv_camera_flash) {
            //操作闪光灯
            if (isOpenFlash) {
                isOpenFlash = false;
                //关闭闪光灯
                vinCameraPreView.operateFlash(false);
                iv_camera_flash.setImageResource(R.drawable.flash_off);
            } else {
                isOpenFlash = true;
                vinCameraPreView.operateFlash(true);
                iv_camera_flash.setImageResource(R.drawable.flash_on);
            }

        } else if (i == R.id.imbtn_takepic) {
            //拍照按钮
            vinCameraPreView.setTakePicture();
        } else if (i == R.id.camera_tv_vin) {
            // 只有不是当前类型，才重新布局
            if (currentType != 1) {
                currentType = 1;
                layoutView();
                vinCameraPreView.setZoom(false);
                vinCameraPreView.setCurrentType(currentType);
            }
            SharedPreferencesHelper.putInt(this, "currentType", currentType);

        } else if (i == R.id.camera_tv_phone) {
            if (currentType != 2) {
                currentType = 2;
                layoutView();
                //手机号图像放大，识别效果会好一些
                vinCameraPreView.setZoom(true);
                vinCameraPreView.setCurrentType(currentType);
            }
            SharedPreferencesHelper.putInt(this, "currentType", currentType);
        }
    }

    // 监听返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            vinCameraPreView.finishRecognize();
            finish();
            return true;
        }
        return true;
    }

    /**
     * 权限申请操作
     */
    private void permission() {
        boolean isgranted = true;
        for (int i = 0; i < PERMISSION.length; i++) {
            if (ContextCompat.checkSelfPermission(this, PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                isgranted = false;
                break;
            }
        }

        PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
        if (!isgranted) {
            //没有授权
            PermissionUtils.requestMultiPermissions(this, mPermissionGrant);
        } else {
            //已经授权
            setContentView(R.layout.activity_car_vin_dis2);
            initView();
            layoutView();
        }
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:

                    setContentView(R.layout.activity_car_vin_dis2);
                    initView();
                    layoutView();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = this.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
