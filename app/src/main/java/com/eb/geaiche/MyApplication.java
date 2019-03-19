package com.eb.geaiche;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alivc.player.AliVcMediaPlayer;
import com.eb.geaiche.util.CartServerUtils;
import com.eb.geaiche.util.CartUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


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


        //初始化播放器（只需调用一次即可，建议在application中初始化）  阿里播放器
        AliVcMediaPlayer.init(getApplicationContext());





        //友盟
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad","http://sns.whalecloud.com");
        PlatformConfig.setQQZone("101555807", "05fc786f1284fee3ea84bc2b6a64b5c1");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//5.0以下要添加
    }
}
