package com.eb.geaiche.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.MainActivity;
import com.eb.geaiche.mvp.LoginActivity2;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.PushMessage;

import net.grandcentrix.tray.AppPreferences;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class GeTuiIntentService extends GTIntentService {
    public GeTuiIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.e(TAG, "onReceiveClientId -> " + "onReceiveServicePid = " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        // 透传消息的处理，详看SDK demo
        Log.e(TAG, "onReceiveClientId -> " + "透传消息 = " + msg);

        String s = new String(msg.getPayload());

        PushMessage pm = new Gson().fromJson(s, PushMessage.class);

        getApplicationContext().sendBroadcast(new Intent(MainActivity.action));
        showNotification(pm);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        new AppPreferences(getApplicationContext()).put("cid", clientid);

        getApplicationContext().sendBroadcast(new Intent(LoginActivity2.action));
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {

        Log.e(TAG, "onReceiveClientId -> " + "cmdMessage = " + cmdMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
    }

    //显示通知栏
    private void showNotification(PushMessage pm) {

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification();

        notification.icon = R.mipmap.push_small;

        //添加声音提示

        notification.defaults = Notification.DEFAULT_SOUND;

        /* 或者使用以下几种方式

         * notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");

         * notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");

         * 如果想要让声音持续重复直到用户对通知做出反应，则可以在notification的flags字段增加"FLAG_INSISTENT"

         * 如果notification的defaults字段包括了"DEFAULT_SOUND"属性，则这个属性将覆盖sound字段中定义的声音

         */

        //audioStreamType的值必须AudioManager中的值，代表响铃模式

        notification.audioStreamType = AudioManager.ADJUST_LOWER;

        //添加LED灯提醒

        notification.defaults |= Notification.DEFAULT_LIGHTS;

        //或者可以自己的LED提醒模式:

        /*notification.ledARGB = 0xff00ff00;

        notification.ledOnMS = 300; //亮的时间

        notification.ledOffMS = 1000; //灭的时间

        notification.flags |= Notification.FLAG_SHOW_LIGHTS;*/

        //添加震动

        notification.defaults |= Notification.DEFAULT_VIBRATE;

        //或者可以定义自己的振动模式：

        /*long[] vibrate = {0,100,200,300}; //0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒

        notification.vibrate = vibrate;*/

        //状态栏提示信息

        notification.tickerText = "哥爱车新消息";

        //获取当前时间

        notification.when = System.currentTimeMillis();

        //加载自定义布局

        notification.contentView = getRemoteViews(getApplicationContext(), pm);

        // 点击清除按钮或点击通知后会自动消失

        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.flags = Notification.FLAG_SHOW_LIGHTS;//三色灯提醒，在使用三色灯提醒时候必须加该标志符
//        notification.flags = Notification.FLAG_INSISTENT;//让声音、振动无限循环，直到用户响应 （取消或者打开）
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE; //发起Notification后，铃声和震动均只执行一次
        //开始显示消息

        notificationManager.notify(0, notification);


    }


    //自定义notification布局

    public static RemoteViews getRemoteViews(Context context, PushMessage pm) {
        RemoteViews remoteviews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteviews.setImageViewResource(R.id.download_promp_icon, R.mipmap.push);
        remoteviews.setTextViewText(R.id.download_title, pm.getTitle());
        remoteviews.setTextViewText(R.id.download_promp_info, pm.getText());
        //找到对应的控件（R.id.download_notification_root），为控件添加点击事件getPendingIntent(context)
        remoteviews.setOnClickPendingIntent(R.id.download_notification_root, getPendingIntent(context, pm));

        return remoteviews;
    }


    private static PendingIntent getPendingIntent(Context context, PushMessage pm) {
        Intent intent;
        intent = new Intent(context, MainActivity.class);
        intent.putExtra("msg", "从通知栏点击进来的");
        intent.putExtra("push", true);
        intent.putExtra("PushMessage", pm);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;

    }


}
