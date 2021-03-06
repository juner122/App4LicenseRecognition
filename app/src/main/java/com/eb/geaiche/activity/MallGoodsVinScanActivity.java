package com.eb.geaiche.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.A2bigA;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.AnimationUtil;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.CarVin;
import com.juner.mvp.bean.CarVinInfo;
import com.juner.mvp.bean.CarVinResult;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.Mode;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.eb.geaiche.activity.MallActivity.VIN;

public class MallGoodsVinScanActivity extends BaseActivity {

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


    @BindView(R.id.tv_check)
    TextView tv_check;//查询


    @BindView(R.id.v_preview)
    View v_preview;//拍照截取的位置视图
    int vh;
    @BindView(R.id.tv_engineSn)
    TextView tv_engineSn;

//    String vin;

    CarInfoRequestParameters carInfo;//车况对象


    Context context;


    @Override
    protected void init() {
        context = this;
        tv_title.setText("扫描车架号");
        et_vin.setTransformationMethod(new A2bigA());


        //图片截取框大小
        v_preview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            vh = v_preview.getHeight();
        });

        String vin = MyAppPreferences.getString(VIN);
        if (null != vin && !vin.equals("")) {
            et_vin.setText(vin);
            ll_tv_check.setVisibility(View.VISIBLE);
            queryVinInfo(vin);
        }

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
                    Api().carVinLicense(pictureResult.getData(), vh).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<CarNumberRecogResult>(context, true, "车架号识别中") {
                        @Override
                        protected void _onNext(CarNumberRecogResult c) {
                            ll_tv_check.setVisibility(View.VISIBLE);
                            et_vin.setText(c.getVin());

                            queryVinInfo(c.getVin());
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
        return R.layout.activity_mall_goods_vin_scan;
    }

    @OnClick({R.id.photo, R.id.input, R.id.tv_check, R.id.re_photo, R.id.enter, R.id.tv_title_r})
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.photo://一键识别
                carVinLicense();
                break;
            case R.id.input://手动输入
                tv_check.setVisibility(View.VISIBLE);
                manualInput();
                break;

            case R.id.re_photo://还原不使用车架号

                MyAppPreferences.remove(VIN);
                toActivity(MallTypeActivity.class);
                finish();

                break;
            case R.id.enter://返回页面
                toActivity(MallTypeActivity.class);
                finish();

                break;
            case R.id.tv_check://查询

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
                    return;
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

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        et_vin.setText("");

    }


    //识别vin
    private void carVinLicense() {
        capturePicture();
    }


    //查询vin信息
    private void queryVinInfo(String v) {

        Api().carVinInfoQuery(v).subscribe(new RxSubscribe<CarVin>(context, true, "车辆信息查询中") {
            @Override
            protected void _onNext(CarVin carVin) {

                if (null == carVin.getShowapi_res_body() || null == carVin.getShowapi_res_body().getBrand_name() || carVin.getShowapi_res_body().getBrand_name().equals("")) {
                    ToastUtils.showToast("车型查询失败,请强制录入车架号！");
                    return;
                }

//                vin = v;
                MyAppPreferences.putString(VIN, v);


                showInfo();
                setCarInfo(carVin.getShowapi_res_body());
                toCarInfo(carVin.getShowapi_res_body());
            }

            @Override
            protected void _onError(String message) {
                Log.e("车架号vin信息查询:", message);

                ToastUtils.showToast("查询失败,请检查车架号！" + message);
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
        carInfo.setVin(et_vin.getText().toString());
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

}
