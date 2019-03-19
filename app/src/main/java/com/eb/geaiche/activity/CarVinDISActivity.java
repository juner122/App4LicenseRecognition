package com.eb.geaiche.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.camerakit.CameraKitView;
import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.A2bigA;
import com.eb.geaiche.util.BitmapUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.AnimationUtil;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.CarVin;
import com.juner.mvp.bean.CarVinInfo;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.graphics.Bitmap.Config.RGB_565;

public class CarVinDISActivity extends BaseActivity {
    @BindView(R.id.camera)
    CameraKitView cameraKitView;


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

    String vin;

    CarInfoRequestParameters carInfo;//车况对象

    @Override
    protected void init() {
        tv_title.setText("扫描车架号");
        et_vin.setTransformationMethod(new A2bigA());
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_vin_dis;
    }

    @OnClick({R.id.photo, R.id.input, R.id.tv_check, R.id.re_photo, R.id.enter})
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.photo://一键识别
                carVinLicense();
                break;
            case R.id.input://手动输入

                ll_tv_check.setVisibility(View.VISIBLE);
                et_vin.setFocusable(true);
                et_vin.setFocusableInTouchMode(true);
                et_vin.requestFocus();

                InputMethodManager imm = (InputMethodManager)CarVinDISActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                et_vin.setText("");

                break;

            case R.id.re_photo://重扫

                ll_tv_check.setVisibility(View.GONE);
                sv_info.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);


                break;
            case R.id.enter://确定

                toActivity(CarInfoInputActivity.class, carInfo, "vinInfo");
                finish();


                break;
            case R.id.tv_check://查询
                if (TextUtils.isEmpty(et_vin.getText())) {

                    ToastUtils.showToast("请输入架号");
                    return;
                }
                queryVinInfo(et_vin.getText().toString());
                break;
        }
    }

    //识别vin
    private void carVinLicense() {
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {


                Observable.just(bytes).subscribeOn(Schedulers.io()).flatMap(new Function<byte[], ObservableSource<CarNumberRecogResult>>() {
                    @Override
                    public ObservableSource<CarNumberRecogResult> apply(byte[] bytes) throws Exception {
                        //转为Base64

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = RGB_565;
                        return Api().carVinLicense(BitmapUtil.bitmapToString(BitmapUtil.createBitmapThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options), true, 530, 300)));

                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<CarNumberRecogResult>(CarVinDISActivity.this, true, "车架号识别中") {
                    @Override
                    protected void _onNext(CarNumberRecogResult c) {
                        ll_tv_check.setVisibility(View.VISIBLE);
                        et_vin.setText(c.getVin());
                        vin = c.getVin();
                        queryVinInfo(vin);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast("识别失败,请重新扫描！");
                    }
                });
            }
        });
    }

    //查询vin信息
    private void queryVinInfo(String vin) {

//        vin = "LFPM5ACP6H1A30016";
        Api().carVinInfoQuery(vin).subscribe(new RxSubscribe<CarVin>(CarVinDISActivity.this, true, "车辆信息查询中") {
            @Override
            protected void _onNext(CarVin carVin) {
                sv_info.setVisibility(View.VISIBLE);
                sv_info.setAnimation(AnimationUtil.moveToViewLocation());


                ll1.setVisibility(View.GONE);


                CarVinInfo carVinInfo = carVin.getShowapi_res_body();
                setCarInfo(carVinInfo);
                toCarInfo(carVinInfo);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查询失败,请重新查询！");
            }
        });

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
    }


    @Override
    protected void onResume() {
        super.onResume();

        cameraKitView.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }


    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
