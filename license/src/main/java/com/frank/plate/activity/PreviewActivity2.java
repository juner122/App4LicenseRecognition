package com.frank.plate.activity;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;

import com.camerakit.CameraKitView;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.CarNumberRecogResult;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.util.BitmapUtil;
import com.frank.plate.util.ToastUtils;

import com.parkingwang.keyboard.KeyboardInputController;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

import net.grandcentrix.tray.AppPreferences;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;



public class PreviewActivity2 extends BaseActivity {
    @BindView(R.id.input_view)
    InputView mInputView;

    @BindView(R.id.lock_type)
    Button lockTypeButton;


    @BindView(R.id.photo)
    Button photo;//识别按钮

    @BindView(R.id.camera)
    CameraKitView cameraKitView;


    private PopupKeyboard mPopupKeyboard;

    private long mTestIndex = 0;

    private boolean mHideOKKey = false;


    String car_number;


    @OnClick({R.id.photo, R.id.but_next})
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.photo:
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {


//                        carLicense(BitmapUtil.bitmapToString(BitmapUtil.createBitmapThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), true, 650, 300)));


                        Observable.just(bytes).subscribeOn(Schedulers.io()).flatMap(new Function<byte[], ObservableSource<CarNumberRecogResult>>() {
                            @Override
                            public ObservableSource<CarNumberRecogResult> apply(byte[] bytes) throws Exception {
                                //转为Base64
                                return Api().carLicense(BitmapUtil.bitmapToString(BitmapUtil.createBitmapThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), true, 650, 300)));
//                                return Api().carLicense(carNumberRecognition_dome);
                            }
                        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<CarNumberRecogResult>(PreviewActivity2.this, true) {
                            @Override
                            protected void _onNext(CarNumberRecogResult c) {

                                ToastUtils.showToast(c.getNumber());
                                car_number = c.getNumber();

                                mPopupKeyboard.getController().updateNumber(car_number);//
                                mPopupKeyboard.dismiss(PreviewActivity2.this);
                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtils.showToast("识别失败~！" + message);
                            }
                        });

                    }


                });
                break;

            case R.id.but_next:
                if (car_number.length() < 7)
                    ToastUtils.showToast("请输入正确车牌号码！");
                else
                    onQueryByCar();
                break;
        }

    }


    private void onQueryByCar() {

        Api().queryByCar(car_number).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(QueryByCarEntity entity) {
                new AppPreferences(PreviewActivity2.this).put(Configure.car_no, car_number);


                if (entity.getMember() != null) {

                    toActivity(MemberManagementInfoActivity.class, Configure.user_id, entity.getMember().getUserId());
                    finish();
                } else {
                    toActivity(MemberInfoInputActivity.class);
                    finish();
                }
            }

            @Override
            protected void _onError(String message) {


                ToastUtils.showToast(message + "车牌号：" + car_number);


            }
        });
    }


    @Override
    protected void init() {


        // 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, this);

        // 隐藏确定按钮
        mPopupKeyboard.getKeyboardEngine().setHideOKKey(false);

        // KeyboardInputController提供一个默认实现的新能源车牌锁定按钮
        mPopupKeyboard.getController()
                .setDebugEnabled(true)
                .bindLockTypeProxy(new KeyboardInputController.ButtonProxyImpl(lockTypeButton) {
                    @Override
                    public void onNumberTypeChanged(boolean isNewEnergyType) {
                        super.onNumberTypeChanged(isNewEnergyType);
                    }
                });
        mPopupKeyboard.getController().addOnInputChangedListener(new OnInputChangedListener() {
            @Override
            public void onChanged(String number, boolean isCompleted) {
                if (isCompleted) {
                    mPopupKeyboard.dismiss(PreviewActivity2.this);
                }
            }

            @Override
            public void onCompleted(String number, boolean isAutoCompleted) {
                mPopupKeyboard.dismiss(PreviewActivity2.this);
            }
        });

        if (mPopupKeyboard.isShown()) {
            mPopupKeyboard.dismiss(PreviewActivity2.this);
        } else {
            mPopupKeyboard.show(PreviewActivity2.this);
        }
    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_preview2;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 默认选中第一个车牌号码输入框
        mInputView.performFirstFieldView();
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
