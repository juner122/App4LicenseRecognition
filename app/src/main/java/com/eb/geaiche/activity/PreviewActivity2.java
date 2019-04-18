package com.eb.geaiche.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.eb.geaiche.mvp.ActivateCardActivity;
import com.eb.geaiche.view.CarListDialog;
import com.eb.geaiche.view.ScanCarConfirmDialog;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.QueryByCarEntity;
import com.eb.geaiche.util.BitmapUtil;
import com.eb.geaiche.util.ToastUtils;

import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.juner.mvp.bean.UserEntity;
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

import static android.graphics.Bitmap.Config.RGB_565;


public class PreviewActivity2 extends BaseActivity {
    @BindView(R.id.input_view)
    InputView mInputView;

    @BindView(R.id.lock_type)
    Button lockTypeButton;


    @BindView(R.id.photo)
    Button photo;//识别按钮

    @BindView(R.id.iv_Flash)
    ImageView iv_Flash;//闪光灯

    @BindView(R.id.camera)
    CameraKitView cameraKitView;


    private PopupKeyboard mPopupKeyboard;

    boolean isFlash;//是否打开闪光灯

    @OnClick({R.id.photo, R.id.but_next, R.id.iv_Flash})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.photo:
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {

                        Observable.just(bytes).subscribeOn(Schedulers.io()).flatMap(new Function<byte[], ObservableSource<CarNumberRecogResult>>() {
                            @Override
                            public ObservableSource<CarNumberRecogResult> apply(byte[] bytes) throws Exception {
                                //转为Base64

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = RGB_565;


                                return Api().carLicense(BitmapUtil.bitmapToString(BitmapUtil.createBitmapThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options), true, 530, 300)));
//                                return Api().carLicense(carNumberRecognition_dome);
                            }
                        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<CarNumberRecogResult>(PreviewActivity2.this, true, "车牌识别中") {
                            @Override
                            protected void _onNext(CarNumberRecogResult c) {

                                mPopupKeyboard.getController().updateNumber(c.getNumber());
                                mPopupKeyboard.dismiss(PreviewActivity2.this);
                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtils.showToast(message);
                            }
                        });

                    }


                });
                break;

            case R.id.but_next:


                if (!mInputView.isCompleted())
                    ToastUtils.showToast("请输入正确车牌号码！");
                else
                    onQueryByCar();
                break;
            case R.id.iv_Flash:

                if (!isFlash) {
                    cameraKitView.setFlash(CameraKit.FLASH_ON);
                    isFlash = true;
                    iv_Flash.setImageResource(R.drawable.icon_flash_on);
                } else {
                    cameraKitView.setFlash(CameraKit.FLASH_OFF);
                    isFlash = false;
                    iv_Flash.setImageResource(R.drawable.icon_flash_off);
                }
                break;
        }

    }


    private void onQueryByCar() {

        Api().queryByCar(mInputView.getNumber()).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(final QueryByCarEntity entity) {
                new AppPreferences(PreviewActivity2.this).put(Configure.car_no, mInputView.getNumber());


                if (null == entity.getUsers() || entity.getUsers().size() == 0) {

                    //弹出下单方式对话框
                    showDialog();


                } else {
                    final CarListDialog nd = new CarListDialog(PreviewActivity2.this, entity.getUsers());
                    nd.show();
                    nd.setClicklistener(new CarListDialog.ClickListenerInterface() {

                        @Override
                        public void doSelectUser(UserEntity user) {
                            nd.cancel();
//                            toActivity(MemberManagementInfoActivity.class, Configure.user_id, user.getUserId());
                            Intent intent = new Intent(PreviewActivity2.this, MemberManagementInfoActivity.class);
                            intent.putExtra(Configure.user_id, user.getUserId());
                            intent.putExtra(Configure.car_no, mInputView.getNumber());
                            startActivity(intent);

                            finish();
                        }

                        @Override
                        public void doAddUser() {
                            nd.cancel();//旧车 新用户
                            int new_car_id = entity.getCarinfo().getId();
                            toActivity(MemberInfoInputActivity.class, "new_car_id", new_car_id);
                            finish();
                        }

                    });
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    @Override
    protected void init() {
        tv_title.setText("车牌扫描");

        // 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(this);
        mPopupKeyboard.getKeyboardView().setCNTextSize(14);
        mPopupKeyboard.getKeyboardView().setENTextSize(14);
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
                        if (!isNewEnergyType)
                            lockTypeButton.setText("新能源");
                        else
                            lockTypeButton.setText("普通车");
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

    //弹出下单方式对话框
    private void showDialog() {

        ScanCarConfirmDialog confirmDialog = new ScanCarConfirmDialog(this);
        confirmDialog.show();
        confirmDialog.setClicklistener(new ScanCarConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {

                //普通接单
                toActivity(MemberInfoInputActivity.class);
                finish();
            }


            @Override
            public void doCancel() {
                //快速接单

                ToastUtils.showToast("快速接单");
                getAddUser(mInputView.getNumber());

            }
        });
    }


    //快速接单 用“车牌号+车主”当用户名去生成用户id
    private void getAddUser(final String car_no) {
        Api().addUser("", car_no + "车主").subscribe(new RxSubscribe<SaveUserAndCarEntity>(this, true) {
            @Override
            protected void _onNext(final SaveUserAndCarEntity s) {


                Api().addCarInfo(makeParameters(car_no, s.getUser_id())).subscribe(new RxSubscribe<Integer>(PreviewActivity2.this, true) {
                    @Override
                    protected void _onNext(Integer Integer) {

                        toMakeOrder(s.getUser_id(), Integer, "", s.getUser_name(), car_no);

                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(message);

                    }
                });

            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(PreviewActivity2.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CarInfoRequestParameters makeParameters(String car_no, int id) {
        CarInfoRequestParameters parameters = new CarInfoRequestParameters();

        parameters.setUserId(String.valueOf(id));
        parameters.setCarNo(car_no);

        Log.d("CarInfoInputActivity", "请求参数:CarInfoRequestParameters==" + parameters.toString());
        return parameters;
    }
}
