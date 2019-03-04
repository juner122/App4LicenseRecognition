package com.eb.new_line_seller;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alivc.player.AliVcMediaPlayer;
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


        //初始化播放器（只需调用一次即可，建议在application中初始化）
        AliVcMediaPlayer.init(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//5.0以下要添加
    }
}
