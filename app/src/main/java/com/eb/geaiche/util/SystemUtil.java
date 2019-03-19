package com.eb.geaiche.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.mvp.LoginActivity2;

public class SystemUtil {


    /**
     * 判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity == null) {
                Log.w("Utility", "couldn't get connectivity manager");
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].isAvailable()) {
                            Log.d("Utility", "network is available");
                            return true;
                        }
                    }
                }
            }
        }
        Log.d("Utility", "network is not available");
        return false;
    }


    //获取应用的版本信息：
    public static String packaGetName() {
        PackageManager manager = MyApplication.getInstance().getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    //版本号versionCode
    public static int packaGetCode() {
        PackageManager manager = MyApplication.getInstance().getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(MyApplication.getInstance().getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }


    //是否要重新登录
    public static void isReLogin(String err, FragmentActivity activity) {

        if (err.contains("401")) {
            Intent intent = new Intent(activity, LoginActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        }
    }

}
