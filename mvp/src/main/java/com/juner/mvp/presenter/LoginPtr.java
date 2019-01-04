package com.juner.mvp.presenter;

import android.app.Activity;

import com.juner.mvp.api.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.Token;
import com.juner.mvp.contacts.LoginContacts;
import com.juner.mvp.model.LoginMdl;

import io.reactivex.annotations.NonNull;

public class LoginPtr extends BasePresenter<LoginContacts.LoginUI, Token> implements LoginContacts.LoginPtr {
    private LoginContacts.LoginMdl mLoginMdl;

    private Activity context;

    public LoginPtr(@NonNull LoginContacts.LoginUI view) {
        super(view);
        // 实例化 Model 层
        mLoginMdl = new LoginMdl();
        context = getView().getSelfActivity();
    }

    @Override
    public void login(String mobile, String authCode) {
        //显示进度条
        getView().showLoading();
        mLoginMdl.login(mobile, authCode, new RxSubscribe<Token>(context, true) {

            @Override
            protected void _onNext(Token token) {
                getView().loginSuccess();
            }

            @Override
            protected void _onError(String message) {
                getView().loginFailure();
            }
        });
    }
}
