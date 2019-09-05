package com.eb.geaiche.vehicleQueue;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.eb.geaiche.R;

public class    VehicleBroadcastReceiver extends BroadcastReceiver {
    public static final String STATICACTION = "VehicleBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(STATICACTION)) {
            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder;
            //判断是否是8.0Android.O
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel chan1 = new NotificationChannel("static",
                        "Primary Channel", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(chan1);
                builder = new NotificationCompat.Builder(context, "static");
            } else {
                builder = new NotificationCompat.Builder(context);
            }

            String plate = intent.getStringExtra("plate");
            //对builder进行配置
            builder.setContentTitle("有一辆车进店了！") //设置通知栏标题
                    .setContentText(String.format("车牌：%s",plate)) //设置通知栏显示内容 显示车牌
                    .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
                    .setSmallIcon(R.mipmap.logo)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true); //设置这个标志当用户单击面板就可以将通知取消
            //绑定intent，点击图标能够进入某activity
            Intent mIntent = new Intent(context, VehicleQueueActivity.class);

            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            manager.notify(0, builder.build());
        }
    }
}
