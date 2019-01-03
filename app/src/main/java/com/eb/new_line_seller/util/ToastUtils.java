package com.eb.new_line_seller.util;


import android.widget.Toast;

import com.eb.new_line_seller.MyApplication;

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
}
