package com.eb.geaiche;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.alivc.player.AliVcMediaPlayer;
import com.eb.geaiche.service.CrashHandler2;
import com.eb.geaiche.service.GeTuiIntentService;
import com.eb.geaiche.service.GeTuiPushService;
import com.eb.geaiche.util.CartUtils;
import com.eb.geaiche.vehicleQueue.VehicleQueueUtils;
import com.igexin.sdk.PushManager;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


public class MyApplication extends Application {
    private static MyApplication app;
    public static CartUtils cartUtils;
//    public static VehicleQueueUtils vehicleQueueUtils;//车辆进店队列


    public static MyApplication getInstance() {
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        cartUtils = CartUtils.getInstance(this);
//        vehicleQueueUtils = VehicleQueueUtils.getInstance(this);


        //初始化播放器（只需调用一次即可，建议在application中初始化）  阿里播放器
        AliVcMediaPlayer.init(getApplicationContext());


        //友盟
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wx6208849918d52d41", "6edbaaf0c484df8f5f4b1f63aebff310");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("101555807", "05fc786f1284fee3ea84bc2b6a64b5c1");
        PlatformConfig.setDing("dingoalmlnohc0wggfedpk");

        //初始化个推
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);


        //全局异常处理
//        CrashHandler handler = CrashHandler.getInstance(this);
//        Thread.setDefaultUncaughtExceptionHandler(handler);
//
//        CrashHandler2.getInstance().init(this);






    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//5.0以下要添加
    }


}
