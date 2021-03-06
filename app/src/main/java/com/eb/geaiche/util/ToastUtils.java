package com.eb.geaiche.util;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.eb.geaiche.MyApplication;

public class ToastUtils {


    private static Toast toast = null; //Toast的对象！

    public static void showToast(String id) {

        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }

    public static void showToast(String id, Activity activity) {

        if (toast == null) {
            toast = Toast.makeText(activity, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    } public static void showToast(String id, Context activity) {

        if (toast == null) {
            toast = Toast.makeText(activity, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }
}
