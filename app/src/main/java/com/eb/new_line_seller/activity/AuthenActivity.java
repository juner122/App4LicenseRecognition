package com.eb.new_line_seller.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.BankList;
import com.juner.mvp.bean.Card;
import com.juner.mvp.bean.NullDataEntity;
import com.eb.new_line_seller.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AuthenActivity extends BaseActivity {


    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.et4)
    EditText et4;
    @BindView(R.id.et5)
    EditText et5;
    @BindView(R.id.tv_code)
    TextView tv_code;

    @BindView(R.id.tv_info)
    TextView tv_info;

    @BindView(R.id.ll_phone_code)
    View ll_phone_code;

    @BindView(R.id.tv1)
    View tv1;

    @BindView(R.id.tv2)
    View tv2;
    @BindView(R.id.ll_enter)
    View ll_enter;


    Card card;

    @Override
    protected void init() {
        tv_title.setText("我的认证");
    }


    @Override
    protected void setUpView() {

        Api().bankList().subscribe(new RxSubscribe<BankList>(this, true) {
            @Override
            protected void _onNext(BankList bankList) {

                if (bankList.getList().size() > 0) {
                    card = bankList.getList().get(0);
                    initView();
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }


    private void initView() {
        tv_info.setVisibility(View.VISIBLE);
        if (card.getType() == 3) {
            tv_info.setText("你的认证申请未通过，请重新提交申请");
            tv_info.setTextColor(Color.parseColor("#FFF23325"));
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            ll_phone_code.setVisibility(View.VISIBLE);
            ll_enter.setVisibility(View.VISIBLE);

        } else {
            tv_info.setTextColor(Color.parseColor("#FF666666"));
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            ll_phone_code.setVisibility(View.GONE);
            ll_enter.setVisibility(View.GONE);
            et1.setText(card.getBankNum());
            et2.setText(card.getBankName());
            et3.setText(card.getBankOpenName());
            et4.setText(card.getBankTrueName());
            et1.setFocusable(false);
            et2.setFocusable(false);
            et3.setFocusable(false);
            et4.setFocusable(false);
            if (card.getType() == 1) {
                tv_info.setText("你的认证申请正在审核中请耐心等候……");
            } else {
                tv_info.setText("恭喜你认证申请已通过");
            }
        }


    }


    @Override
    protected void setUpData() {


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_authen;
    }

    int con = 60;
    Disposable disposable;

    @OnClick({R.id.but_enter, R.id.tv_code})
    public void onclick(View v) {

        switch (v.getId()) {

            case R.id.but_enter:

                auth();
                break;

            case R.id.tv_code:

                Api().sendBankSms().subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        ToastUtils.showToast("验证短信已发送到手机！");

                        disposable = Observable //计时器
                                .interval(0, 1, TimeUnit.SECONDS)
                                .take(con)//次数
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) {
                                        Long l = con - aLong;
                                        tv_code.setText(l + "s");
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {
                                        tv_code.setClickable(true);
                                        throwable.printStackTrace();
                                    }
                                }, new Action() {
                                    @Override
                                    public void run() {

                                        tv_code.setText("获取验证码");
                                        tv_code.setClickable(true);
                                    }
                                }, new Consumer<Disposable>() {
                                    @Override
                                    public void accept(Disposable disposable) {
                                        tv_code.setClickable(false);
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

    private void auth() {

        if (TextUtils.isEmpty(et1.getText())) {
            ToastUtils.showToast("银联卡不能为空！");
            return;
        }

        if (TextUtils.isEmpty(et2.getText())) {
            ToastUtils.showToast("开户行不能为空！");
            return;
        }

        if (TextUtils.isEmpty(et3.getText())) {
            ToastUtils.showToast("开户行地址不能为空！");
            return;
        }

        if (TextUtils.isEmpty(et4.getText())) {
            ToastUtils.showToast("持卡人不能为空！");
            return;
        }

        if (TextUtils.isEmpty(et5.getText())) {
            ToastUtils.showToast("手机验证码不能为空！");
            return;
        }


        card = new Card();
        card.setBankOpenName(et3.getText().toString());
        card.setBankName(et2.getText().toString());
        card.setBankNum(et1.getText().toString());
        card.setBankTrueName(et4.getText().toString());
        card.setAuthCode(et5.getText().toString());


        Api().bankSave(card).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("认证成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("认证失败:" + message);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}
