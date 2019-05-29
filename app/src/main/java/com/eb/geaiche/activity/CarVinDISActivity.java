package com.eb.geaiche.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.A2bigA;
import com.eb.geaiche.util.Base64;
import com.eb.geaiche.util.BitmapUtil;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.FileUtil;
import com.eb.geaiche.util.ImageUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.AnimationUtil;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.CarVin;
import com.juner.mvp.bean.CarVinInfo;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.Mode;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import androidx.annotation.NonNull;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.graphics.Bitmap.Config.RGB_565;

public class CarVinDISActivity extends BaseActivity {
    @BindView(R.id.camera)
    CameraView cameraKitView;


    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.sv_info)
    View sv_info;
    @BindView(R.id.ll_tv_check)
    LinearLayout ll_tv_check;

    @BindView(R.id.et_vin)
    EditText et_vin;//


    @BindView(R.id.tv_brand_name)
    TextView tv_brand_name;
    @BindView(R.id.tv_model_name)
    TextView tv_model_name;
    @BindView(R.id.tv_car_type)
    TextView tv_car_type;
    @BindView(R.id.tv_sale_name)
    TextView tv_sale_name;
    @BindView(R.id.tv_effluent_standard)
    TextView tv_effluent_standard;
    @BindView(R.id.tv_guiding_price)
    TextView tv_guiding_price;
    @BindView(R.id.tv_made_year)
    TextView tv_made_year;
    @BindView(R.id.tv_output_volume)
    TextView tv_output_volume;
    @BindView(R.id.tv_mandatory_entry)
    TextView tv_mandatory_entry;//强制录入

    @BindView(R.id.tv_check)
    TextView tv_check;//查询


    @BindView(R.id.v_preview)
    View v_preview;//拍照截取的位置视图
    int vh;
    @BindView(R.id.tv_engineSn)
    TextView tv_engineSn;

    String vin;

    CarInfoRequestParameters carInfo;//车况对象

    boolean isCheckAction;//是否是查看车架号

    private File file;

    Context context;


    @Override
    protected void init() {
        context = this;
        tv_title.setText("扫描车架号");
        et_vin.setTransformationMethod(new A2bigA());

        isCheckAction = getIntent().getBooleanExtra("isca", false);

        if (isCheckAction) {
            showInfo();//查看车架号
            queryVinInfo(getIntent().getStringExtra(Configure.CAR_VIN));
        }


        //图片截取框大小
        v_preview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            vh = v_preview.getHeight();
        });

    }


    @Override
    protected void setUpView() {

        cameraKitView.setLifecycleOwner(this);

        cameraKitView.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // 双指缩放!
        cameraKitView.addCameraListener(new CameraListener() {


            @Override
            public void onPictureTaken(PictureResult pictureResult) {
                super.onPictureTaken(pictureResult);

                try {
                    Api().carVinLicense(pictureResult.getData(), vh).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<CarNumberRecogResult>(CarVinDISActivity.this, true, "车架号识别中") {
                        @Override
                        protected void _onNext(CarNumberRecogResult c) {
                            ll_tv_check.setVisibility(View.VISIBLE);
                            et_vin.setText(c.getVin());
                            vin = c.getVin();
                            queryVinInfo(vin);
                        }

                        @Override
                        protected void _onError(String message) {
                            ToastUtils.showToast("识别失败,请重新扫描！" + message);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    protected void setUpData() {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        manualInput();
        et_vin.setText(intent.getStringExtra(Configure.car_no));
    }

    //拍照
    private void capturePicture() {
        if (cameraKitView.getMode() == Mode.VIDEO) {
            ToastUtils.showToast("Can't take HQ pictures while in VIDEO mode.");
            return;
        }
        if (cameraKitView.isTakingPicture()) return;
//        mCaptureTime = System.currentTimeMillis();
//        ToastUtils.showToast("Capturing picture...");
        try {
            cameraKitView.takePicture();
        } catch (Exception e) {
            ToastUtils.showToast("扫描功能失败，请重试！");
        }
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_vin_dis;
    }

    @OnClick({R.id.photo, R.id.input, R.id.tv_check, R.id.re_photo, R.id.enter, R.id.tv_title_r, R.id.tv_mandatory_entry})
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.photo://一键识别
                carVinLicense();
                break;
            case R.id.input://手动输入
                tv_mandatory_entry.setVisibility(View.GONE);
                tv_check.setVisibility(View.VISIBLE);
                manualInput();
                break;

            case R.id.re_photo://重扫

                ll_tv_check.setVisibility(View.GONE);
                sv_info.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);

                tv_mandatory_entry.setVisibility(View.GONE);
                tv_check.setVisibility(View.VISIBLE);
                break;
            case R.id.enter://确定

                toActivity(CarInfoInputActivity.class, carInfo, "vinInfo");
                finish();


                break;
            case R.id.tv_check://查询

                tv_mandatory_entry.setVisibility(View.GONE);
                tv_check.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(et_vin.getText())) {

                    ToastUtils.showToast("请输入车架号");
                    return;
                }
                queryVinInfo(et_vin.getText().toString());
                break;

            case R.id.tv_title_r:
                //高级扫描
                toActivity(PreviewZoomActivity.class, Configure.act_tag, "VIN");
                break;


            case R.id.tv_mandatory_entry://强制录入
                if (et_vin.getText().toString().equals("")) {
                    ToastUtils.showToast("请填写车架号！");
                }


                carInfo = new CarInfoRequestParameters();
                carInfo.setVin(et_vin.getText().toString());
                toActivity(CarInfoInputActivity.class, carInfo, "vinInfo");
                finish();

                break;
        }
    }

    //手动输入
    private void manualInput() {

        ll_tv_check.setVisibility(View.VISIBLE);
        et_vin.setFocusable(true);
        et_vin.setFocusableInTouchMode(true);
        et_vin.requestFocus();

        InputMethodManager imm = (InputMethodManager) CarVinDISActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        et_vin.setText("");

    }


    //识别vin
    private void carVinLicense() {
        capturePicture();
    }

    int query_i = 0;//查询失败次数

    //查询vin信息
    private void queryVinInfo(String vin) {

        Api().carVinInfoQuery(vin).subscribe(new RxSubscribe<CarVin>(CarVinDISActivity.this, true, "车辆信息查询中") {
            @Override
            protected void _onNext(CarVin carVin) {


                showInfo();

                setCarInfo(carVin.getShowapi_res_body());

                if (!isCheckAction)//录入车况信息 查看动作不用执行
                    toCarInfo(carVin.getShowapi_res_body());

            }

            @Override
            protected void _onError(String message) {
                Log.e("车架号vin信息查询:", message);

                query_i++;
                if (query_i == 2) {
                    query_i = 0;
                    tv_mandatory_entry.setVisibility(View.VISIBLE);

                }
                ToastUtils.showToast("查询失败,请重新查询！");

            }
        });

    }

    private void showInfo() {

        sv_info.setVisibility(View.VISIBLE);
        sv_info.setAnimation(AnimationUtil.moveToViewLocation());
        ll1.setVisibility(View.GONE);

    }

    private void setCarInfo(CarVinInfo carVinInfo) {
        tv_brand_name.setText(carVinInfo.getBrand_name());
        tv_model_name.setText(carVinInfo.getModel_name());
        tv_sale_name.setText(carVinInfo.getSale_name());
        tv_car_type.setText(carVinInfo.getCar_type());
        tv_effluent_standard.setText(carVinInfo.getEffluent_standard());
        tv_guiding_price.setText(String.format("%s万", carVinInfo.getGuiding_price()));
        tv_made_year.setText(carVinInfo.getYear());
        tv_output_volume.setText(carVinInfo.getOutput_volume());
        tv_engineSn.setText(carVinInfo.getEngine_type());
    }

    private void toCarInfo(CarVinInfo carVinInfo) {
        carInfo = new CarInfoRequestParameters();
        carInfo.setBrand(carVinInfo.getBrand_name());
        carInfo.setName(carVinInfo.getModel_name());
        carInfo.setSaleName(carVinInfo.getSale_name());
        carInfo.setCarType(carVinInfo.getCar_type());
        carInfo.setEffluentStandard(carVinInfo.getEffluent_standard());
        carInfo.setGuidingPrice(carVinInfo.getGuiding_price());
        carInfo.setYear(carVinInfo.getYear());
        carInfo.setAllJson(carVinInfo.toString());
        carInfo.setVin(carVinInfo.getVin());
        carInfo.setOutputVolume(carVinInfo.getOutput_volume());
        carInfo.setEngineSn(carVinInfo.getEngine_type());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !cameraKitView.isOpened()) {
            cameraKitView.open();
        }
    }


    private Rect previewFrame = new Rect();//摄像图像大小
    private Rect frameRect = new Rect();//截取摄像图像大小

    /**
     * 拍摄后的照片。需要进行裁剪。有些手机（比如三星）不会对照片数据进行旋转，而是将旋转角度写入EXIF信息当中，
     * 所以需要做旋转处理。
     *
     * @param outputFile 写入照片的文件。
     * @param data       原始照片数据。
     * @param rotation   照片exif中的旋转角度。
     * @return 裁剪好的bitmap。
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Bitmap crop(File outputFile, byte[] data, int rotation) {
        try {

            if (frameRect.width() == 0 || frameRect.height() == 0 || previewFrame.width() == 0 || previewFrame.height() == 0) {
                return null;
            }

            // BitmapRegionDecoder不会将整个图片加载到内存。
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(data, 0, data.length, true);


            int width = rotation % 180 == 0 ? decoder.getWidth() : decoder.getHeight();
            int height = rotation % 180 == 0 ? decoder.getHeight() : decoder.getWidth();


            int left = frameRect.left;
            int top = frameRect.top;
            int right = frameRect.right;
            int bottom = frameRect.bottom;

            // 高度大于图片
            if (previewFrame.top < 0) {
                // 宽度对齐。
                int adjustedPreviewHeight = previewFrame.height();
                int topInFrame = ((adjustedPreviewHeight - frameRect.height()) / 2);
                int bottomInFrame = ((adjustedPreviewHeight + frameRect.height()) / 2);

                // 等比例投射到照片当中。
                top = topInFrame * height / previewFrame.height();
                bottom = bottomInFrame * height / previewFrame.height();
            } else {
                // 宽度大于图片
                if (previewFrame.left < 0) {
                    // 高度对齐
                    int adjustedPreviewWidth = previewFrame.width();
                    int leftInFrame = ((adjustedPreviewWidth - frameRect.width()) / 2);
                    int rightInFrame = ((adjustedPreviewWidth + frameRect.width()) / 2);

                    // 等比例投射到照片当中。
                    left = leftInFrame * width / previewFrame.width();
                    right = rightInFrame * width / previewFrame.width();
                }
            }

            Rect region = new Rect();
            region.left = left;
            region.top = top;
            region.right = right;
            region.bottom = bottom;


            // 90度或者270度旋转
            if (rotation % 180 == 90) {
                int x = decoder.getWidth() / 2;
                int y = decoder.getHeight() / 2;

                int rotatedWidth = region.height();
                int rotated = region.width();

                // 计算，裁剪框旋转后的坐标
                region.left = x - rotatedWidth / 2;
                region.top = y - rotated / 2;
                region.right = x + rotatedWidth / 2;
                region.bottom = y + rotated / 2;
                region.sort();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();

            // 最大图片大小。
            int maxPreviewImageSize = 2560;
            int size = Math.min(decoder.getWidth(), decoder.getHeight());
            size = Math.min(size, maxPreviewImageSize);

            options.inSampleSize = ImageUtil.calculateInSampleSize(options, size, size);
            options.inScaled = true;
            options.inDensity = Math.max(options.outWidth, options.outHeight);
            options.inTargetDensity = size;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = decoder.decodeRegion(region, options);

            if (rotation != 0) {
                // 只能是裁剪完之后再旋转了。有没有别的更好的方案呢？
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                Bitmap rotatedBitmap = Bitmap.createBitmap(
                        bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                if (bitmap != rotatedBitmap) {
                    // 有时候 createBitmap会复用对象
                    bitmap.recycle();
                }
                bitmap = rotatedBitmap;
            }

            try {
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
