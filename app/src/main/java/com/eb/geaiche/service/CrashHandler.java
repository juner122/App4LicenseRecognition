package com.eb.geaiche.service;

import android.content.Context;
import android.util.Log;

import com.eb.geaiche.util.ToastUtils;

/**
 * 自定义的异常处理类，实现UncaughtExceptionHandler接口
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler myCrashHandler;

    private Context mContext;

    private CrashHandler(Context context) {
        mContext = context;
    }

    public static synchronized CrashHandler getInstance(Context context) {
        if (null == myCrashHandler) {
            myCrashHandler = new CrashHandler(context);
        }
        return myCrashHandler;
    }

    /**
     * 当有未捕获异常发生，就会调用该函数，
     * 可以在该函数中对异常信息捕获并上传
     *
     * @param t 发生异常的线程
     * @param e 异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 处理异常,可以自定义弹框，可以上传异常信息
        Log.e("APP_ERROR", e.toString());


//        // 干掉当前的程序
//        android.os.Process.killProcess(android.os.Process.myPid());

        // TODO 下面捕获到异常以后要做的事情，可以重启应用，获取手机信息上传到服务器等
        Log.i("APP_ERROR", "------------------应用被重启----------------");

        // 重启应用
        mContext.startActivity(mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName()));
        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}