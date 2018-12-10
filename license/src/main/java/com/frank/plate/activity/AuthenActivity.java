package com.frank.plate.activity;

import android.app.Activity;
import android.os.Bundle;

import com.frank.plate.R;

import butterknife.OnClick;

public class AuthenActivity extends BaseActivity {


    @Override
    protected void init() {
        tv_title.setText("我的认证");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_authen;
    }

    @OnClick({R.id.but_enter})
    public void onclick() {

        finish();

    }
}
