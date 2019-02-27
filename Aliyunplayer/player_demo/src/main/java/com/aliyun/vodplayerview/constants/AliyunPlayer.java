package com.aliyun.vodplayerview.constants;

import android.content.Context;
import android.content.Intent;

import com.aliyun.vodplayerview.activity.AliyunPlayerSkinActivity;

/**
 * 对外暴露的接口
 */
public class AliyunPlayer {

    /**
     * 跳转到播放器界面
     * @param context               context
     * @param alivcPlayerConfig     跳转到播放器提供的参数配置
     */
    public static void player(Context context,AlivcPlayerConfig alivcPlayerConfig){
        Intent intent = new Intent(context, AliyunPlayerSkinActivity.class);
        intent.putExtra("vid",alivcPlayerConfig.getVid());
        context.startActivity(intent);
    }
}
