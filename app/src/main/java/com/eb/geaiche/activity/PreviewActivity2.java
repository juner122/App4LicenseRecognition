package com.eb.geaiche.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.adapter.UserlistListAdapter;

import com.eb.geaiche.util.BitmapUtil;
import com.eb.geaiche.view.AnimationUtil;

import com.eb.geaiche.view.ScanCarConfirmDialog;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;

import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.QueryByCarEntity;
import com.eb.geaiche.util.ToastUtils;

import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.Mode;
import com.otaliastudios.cameraview.PictureResult;
import com.parkingwang.keyboard.KeyboardInputController;
import com.parkingwang.keyboard.OnInputChangedListener;
import com.parkingwang.keyboard.PopupKeyboard;
import com.parkingwang.keyboard.view.InputView;

import net.grandcentrix.tray.AppPreferences;

import com.otaliastudios.cameraview.CameraView;

import java.math.BigDecimal;

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

    @BindView(R.id.ll_car_list)
    View ll_car_list;
    @BindView(R.id.ll)
    View ll_button;

    @BindView(R.id.seekBar)
    SeekBar seekBar;


    @BindView(R.id.camera)
    CameraView cameraKitView;


    private PopupKeyboard mPopupKeyboard;

    boolean isFlash;//是否打开闪光灯


    @OnClick({R.id.photo, R.id.but_next, R.id.iv_Flash, R.id.but_quick, R.id.tv_title_r})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.photo:
                //拍照
                capturePicture();
                break;

            case R.id.but_next://普通接单


                if (!mInputView.isCompleted())
                    ToastUtils.showToast("请输入正确车牌号码！");
                else
                    onQueryByCar(0);
                break;

            case R.id.but_quick://快速接单


                if (!mInputView.isCompleted())
                    ToastUtils.showToast("请输入正确车牌号码！");
                else
                    onQueryByCar(1);
                break;


            case R.id.iv_Flash:

                if (!isFlash) {
                    cameraKitView.setFlash(Flash.ON);
                    isFlash = true;
                    iv_Flash.setImageResource(R.drawable.icon_flash_on);
                } else {
                    cameraKitView.setFlash(Flash.OFF);
                    isFlash = false;
                    iv_Flash.setImageResource(R.drawable.icon_flash_off);
                }
                break;

            case R.id.tv_title_r:
                //高级扫描
                toActivity(PreviewZoomActivity.class,Configure.act_tag,"CARCODE");
                break;

        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mInputView.updateNumber(intent.getStringExtra(Configure.car_no));
    }

    /**
     * @param type 0普通下单 1快速下单
     */
    private void onQueryByCar(final int type) {

        Api().queryByCar(mInputView.getNumber()).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(final QueryByCarEntity entity) {
                new AppPreferences(PreviewActivity2.this).put(Configure.car_no, mInputView.getNumber());


                if (null == entity.getUsers() || entity.getUsers().size() == 0) {

                    if (type == 1) {

                        ToastUtils.showToast("快速接单");
                        getAddUser(mInputView.getNumber());

                    } else {

                        //普通接单
                        toActivity(MemberInfoInputActivity.class);
                        finish();

                    }


                } else {


                    ll_button.setVisibility(View.GONE);
                    ll_car_list.setVisibility(View.VISIBLE);
                    ll_car_list.setAnimation(AnimationUtil.moveToViewLocation());

                    final UserlistListAdapter userlistListAdapter = new UserlistListAdapter(entity.getUsers(), PreviewActivity2.this);
                    RecyclerView rv = ll_car_list.findViewById(R.id.rv);
                    View tv_cancel = ll_car_list.findViewById(R.id.tv_cancel);//新增会员
                    rv.setLayoutManager(new LinearLayoutManager(PreviewActivity2.this));
                    rv.setAdapter(userlistListAdapter);


                    userlistListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                            Intent intent = new Intent(PreviewActivity2.this, MemberManagementInfoActivity.class);
                            intent.putExtra(Configure.user_id, userlistListAdapter.getData().get(position).getUserId());
                            intent.putExtra(Configure.car_no, mInputView.getNumber());
                            startActivity(intent);
                            finish();
                        }
                    });

                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
        setRTitle("高级扫描");
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

        initCamera();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                BigDecimal b1 = new BigDecimal(Integer.toString(i));
                BigDecimal b2 = new BigDecimal(Integer.toString(100));//根据控件设定的最大值来除
                float f = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).floatValue();

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
        if (mPopupKeyboard.isShown())
            mPopupKeyboard.dismiss(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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

//                ToastUtils.showToast("快速接单");
                getAddUser(mInputView.getNumber());

            }
        });
    }


    //快速接单 用“车牌号+车主”当用户名去生成用户id
    private void getAddUser(final String car_no) {
        Api().addUser("", car_no + "车主").subscribe(new RxSubscribe<SaveUserAndCarEntity>(this, false) {
            @Override
            protected void _onNext(final SaveUserAndCarEntity s) {

                Api().addCarInfo(makeParameters(car_no, s.getUser_id())).subscribe(new RxSubscribe<NullDataEntity>(PreviewActivity2.this, false) {
                    @Override
                    protected void _onNext(NullDataEntity Integer) {

                        toMakeOrder(s.getUser_id(), Integer.getId(), "", s.getUser_name(), car_no);

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


    //拍照
    private void capturePicture() {
        if (cameraKitView.getMode() == Mode.VIDEO) {
            ToastUtils.showToast("Can't take HQ pictures while in VIDEO mode.");
            return;
        }
        if (cameraKitView.isTakingPicture()) return;
//        mCaptureTime = System.currentTimeMillis();
//        ToastUtils.showToast("Capturing picture...");
        cameraKitView.takePicture();

    }


    //初始化Camera
    private void initCamera() {
        cameraKitView.setLifecycleOwner(this);
        cameraKitView.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // 双指缩放!
        cameraKitView.mapGesture(Gesture.TAP, GestureAction.ZOOM); // 点击缩放!
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

    }

}
