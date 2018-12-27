package com.frank.plate;

import android.app.Application;

import com.frank.plate.util.CartServerUtils;
import com.frank.plate.util.CartUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import net.grandcentrix.tray.AppPreferences;


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


}
