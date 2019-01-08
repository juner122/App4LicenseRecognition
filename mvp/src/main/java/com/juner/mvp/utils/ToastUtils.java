package com.juner.mvp.utils;


import android.app.Activity;
import android.widget.Toast;


public class ToastUtils {


    private static Toast toast = null; //Toast的对象！


    public static void showToast(String id, Activity activity) {

        if (toast == null) {
            toast = Toast.makeText(activity, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }
}
