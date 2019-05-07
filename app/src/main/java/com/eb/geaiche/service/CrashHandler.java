package com.eb.geaiche.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.eb.geaiche.api.ApiLoader;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.bean.NullDataEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 自定义的异常处理类，实现UncaughtExceptionHandler接口
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String FILE_NAME_SUFFIX = ".trace";
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

        // TODO 下面捕获到异常以后要做的事情，可以重启应用，获取手机信息上传到服务器等
        Log.i("APP_ERROR", e.getMessage());

        //导出异常信息到SD卡
        exportExceptionToSDCard(e);


    }


    /**
     * 导出异常信息到SD卡
     *
     * @param e
     */
    private void exportExceptionToSDCard(@NonNull Throwable e) {

        Log.i("APP_ERROR", "全局异常捕捉Err:" + e.getLocalizedMessage());
//        e.printStackTrace();
//        ToastUtils.showToast("操作失败，请联系开发人员！", mContext);


//        //app运行异常记录
//        new ApiLoader(mContext).saveError(e.getLocalizedMessage())
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new RxSubscribe<NullDataEntity>(mContext, false) {
//            @Override
//            protected void _onNext(NullDataEntity nullDataEntity) {
//
//            }
//
//            @Override
//            protected void _onError(String message) {
//                Log.i("APP_ERROR", "保存app运行异常记录请求失败" + message);
//            }
//        });

    }

    /**
     * 获取手机信息
     */
    private String appendPhoneInfo() throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        StringBuilder sb = new StringBuilder();
        //App版本
        sb.append("App Version: ");
        sb.append(pi.versionName);
        sb.append("_");
        sb.append(pi.versionCode + "\n");

        //Android版本号
        sb.append("OS Version: ");
        sb.append(Build.VERSION.RELEASE);
        sb.append("_");
        sb.append(Build.VERSION.SDK_INT + "\n");

        //手机制造商
        sb.append("Vendor: ");
        sb.append(Build.MANUFACTURER + "\n");

        //手机型号
        sb.append("Model: ");
        sb.append(Build.MODEL + "\n");

        //CPU架构
        sb.append("CPU: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sb.append(Arrays.toString(Build.SUPPORTED_ABIS));
        } else {
            sb.append(Build.CPU_ABI);
        }
        return sb.toString();
    }
}