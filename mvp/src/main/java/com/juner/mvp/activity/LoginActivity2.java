package com.juner.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.juner.mvp.R;
import com.juner.mvp.base.view.BaseActivity;
import com.juner.mvp.contacts.LoginContacts;
import com.juner.mvp.presenter.LoginPtr;


public class LoginActivity2 extends BaseActivity<LoginContacts.LoginPtr> implements LoginContacts.LoginUI {


    private static final String TAG = "LoginActivity2";
    View login;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.but_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPresenter().login("13412513007", "8888");


            }
        });
    }

    @Override
    public LoginContacts.LoginPtr onBindPresenter() {
        return new LoginPtr(this);
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginFailure() {
        Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
    }
}
