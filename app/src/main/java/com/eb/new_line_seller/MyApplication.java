package com.eb.new_line_seller;

import android.app.Application;
import android.content.Context;

import com.eb.new_line_seller.util.CartServerUtils;
import com.eb.new_line_seller.util.CartUtils;


public class MyApplication extends Application {
    private static MyApplication app;


    public static CartUtils cartUtils;
    public static CartServerUtils cartServerUtils;

    public static MyApplication getInstance() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;



        cartUtils = CartUtils.getInstance(this);
        cartServerUtils = CartServerUtils.getInstance(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
