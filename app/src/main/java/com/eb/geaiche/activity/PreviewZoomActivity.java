package com.eb.geaiche.activity;

import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.Mode;
import com.otaliastudios.cameraview.PictureResult;

import java.math.BigDecimal;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class PreviewZoomActivity extends BaseActivity {

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.camera)
    CameraView cameraKitView;

    @BindView(R.id.fl)
    View fl;

    @Override
    protected void init() {
        tv_title.setText("高级扫描");
    }

    @OnClick({R.id.photo})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.photo:
                //拍照
                capturePicture();
                break;

//            case R.id.iv_Flash:
//
//                if (!isFlash) {
//                    cameraKitView.setFlash(Flash.ON);
//                    isFlash = true;
//                    iv_Flash.setImageResource(R.drawable.icon_flash_on);
//                } else {
//                    cameraKitView.setFlash(Flash.OFF);
//                    isFlash = false;
//                    iv_Flash.setImageResource(R.drawable.icon_flash_off);
//                }
//                break;
//


        }

    }

    @Override
    protected void setUpView() {
        initCamera();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                BigDecimal b1 = new BigDecimal(Integer.toString(i));
                BigDecimal b2 = new BigDecimal(Integer.toString(100));//根据控件设定的最大值来除
                float f = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).floatValue();
                Log.i("缩放级别zoom", "i=" + i + ",Zoom=" + f);
                cameraKitView.setZoom(f);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_preview_zoom;
    }

    //初始化Camera
    private void initCamera() {
        cameraKitView.setLifecycleOwner(this);
        cameraKitView.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // 双指缩放!

        cameraKitView.addCameraListener(new CameraListener() {
            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
                Log.e("Camera", "Got CameraException #" + exception.getReason());
            }

            @Override
            public void onPictureTaken(PictureResult pictureResult) {
                super.onPictureTaken(pictureResult);


                Observable.just(pictureResult.getData()).subscribeOn(Schedulers.io()).flatMap((Function<byte[], ObservableSource<CarNumberRecogResult>>) bytes -> {
                    //转为Base64

                    return Api().carLicense(bytes);
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<CarNumberRecogResult>(PreviewZoomActivity.this, true, "车牌识别中") {
                    @Override
                    protected void _onNext(CarNumberRecogResult c) {
                        ToastUtils.showToast(c.getNumber());

                        if (getIntent().getStringExtra(Configure.act_tag).equals("VIN"))
                            toActivity(CarVinDISActivity.class, Configure.car_no, c.getNumber());
                        else
                            toActivity(PreviewActivity2.class, Configure.car_no, c.getNumber());
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(message);
                    }
                });


            }

        });

    }

    //拍照
    private void capturePicture() {
        if (cameraKitView.getMode() == Mode.VIDEO) {
            ToastUtils.showToast("Can't take HQ pictures while in VIDEO mode.");
            return;
        }
        if (cameraKitView.isTakingPicture()) return;
        cameraKitView.takePicture();

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
