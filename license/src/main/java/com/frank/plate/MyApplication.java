package com.frank.plate;

import android.app.Application;

import com.frank.plate.util.CartUtils;


public class MyApplication extends Application {
    private static MyApplication app;

    public static CartUtils cartUtils;
    public static MyApplication getInstance() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        cartUtils = CartUtils.getInstance(this);

    }




}
