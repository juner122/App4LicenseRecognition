package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.util.MyAppPreferences;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Token;
import com.eb.geaiche.mvp.contacts.LoginContacts;
import com.eb.geaiche.mvp.model.LoginMdl;
import com.juner.mvp.utils.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.juner.mvp.Configure.JSON_CART;
import static com.juner.mvp.Configure.SHOP_TYPE;

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
    public void login(final String mobile, String authCode, String cid) {

        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showToast("请输入正确的手机号码！", context);
            return;
        }
        if (TextUtils.isEmpty(authCode)) {
            ToastUtils.showToast("请输入验证码！", context);
            return;
        }
        if (TextUtils.isEmpty(cid)) {
            ToastUtils.showToast("获取个推CID失败，接收推送功能失效！", context);

        }

        mLoginMdl.login(mobile, authCode, cid, new RxSubscribe<Token>(context, true) {
            @Override
            protected void _onNext(Token token) {
                //保存token
                mLoginMdl.saveToken(token, context);
                mLoginMdl.savePhone(mobile, context);
                getView().loginSuccess(token.getAppMenuList());


                //保存门员类型


                MyAppPreferences.putInt(Configure.user_role, null == token.getUser_role() ? 0 : token.getUser_role());

                //保存门店类型
                MyAppPreferences.putShopType(token.getShop_type() == 1);
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
