package com.frank.plate.util;

import android.widget.Toast;

import com.frank.plate.MyApplication;

public class ToastUtil {


    public static void ToastCenter(String message) {

        Toast.makeText(MyApplication.getInstance(), message, Toast.LENGTH_SHORT).show();


    }

}
