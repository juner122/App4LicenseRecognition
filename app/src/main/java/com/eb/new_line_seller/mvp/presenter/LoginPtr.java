package com.eb.new_line_seller.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Token;
import com.eb.new_line_seller.mvp.contacts.LoginContacts;
import com.eb.new_line_seller.mvp.model.LoginMdl;
import com.juner.mvp.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPtr extends BasePresenter<LoginContacts.LoginUI> implements LoginContacts.LoginPtr {
    private LoginContacts.LoginMdl mLoginMdl;
    private Activity context;

    final int con = 60;
    Disposable countDown_disposable;

    public LoginPtr(@NonNull LoginContacts.LoginUI view) {
        super(view);
        // 实例化 Model 层
        mLoginMdl = new LoginMdl();
        context = getView().getSelfActivity();
    }

    @Override
    public void login(final String mobile, String authCode) {

        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToast("请输入正确的手机号码！", context);
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            ToastUtils.showToast("请输入验证码！", context);
            return;
        }

        mLoginMdl.login(mobile, authCode, new RxSubscribe<Token>(context, true) {
            @Override
            protected void _onNext(Token token) {
                //保存token
                mLoginMdl.saveToken(token, context);
                mLoginMdl.savePhone(mobile, context);
                getView().loginSuccess(token.getAppMenuList());
            }

            @Override
            protected void _onError(String message) {
                getView().loginFailure(message);
            }
        });
    }

    @Override
    public void smsSendSms(String mobile) {
        if (mobile.equals("")) {
            ToastUtils.showToast("请输入验证码！", context);
            return;
        }
        mLoginMdl.smsSendSms(mobile, new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("验证码已发送！", context);
                startCountDown();
            }

            @Override
            protected void _onError(String message) {
                getView().loginFailure(message);
            }
        });

    }

    @Override
    public void stopCountDown() {
        if (null != countDown_disposable)
            countDown_disposable.dispose();
    }


    //开始倒计时
    private void startCountDown() {
        countDown_disposable = Observable //计时器
                .interval(0, 1, TimeUnit.SECONDS)
                .take(con)//次数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        Long l = con - aLong;
                        getView().countDown(l + "s");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                        getView().setCodeViewClickable(true);
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() {

                        getView().countDown("获取验证码");
                        getView().setCodeViewClickable(true);
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) {

                        getView().setCodeViewClickable(false);
                    }
                });
    }

}
