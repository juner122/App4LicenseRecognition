package com.eb.geaiche.util;


import com.eb.geaiche.MyApplication;

import net.grandcentrix.tray.AppPreferences;

import static com.juner.mvp.Configure.SHOP_TYPE;


public class MyAppPreferences {


    //获取门店类型
    public static boolean getShopType() {
        return new AppPreferences(MyApplication.getInstance()).getBoolean(SHOP_TYPE, true);//true 为联盟门店  f为直营
}


    public static void putShopType(boolean isAlliance) {

        new AppPreferences(MyApplication.getInstance()).put(SHOP_TYPE, isAlliance);
    }

    public static void removeShopType() {

        new AppPreferences(MyApplication.getInstance()).remove(SHOP_TYPE);//门店类型

    }

    public static void putString(String key, String vlues) {
        new AppPreferences(MyApplication.getInstance()).put(key, vlues);
    }

    public static void remove(String key) {
        new AppPreferences(MyApplication.getInstance()).remove(key);

    }

    public static String getString(String key) {
        return new AppPreferences(MyApplication.getInstance()).getString(key, "");
    }

    public static void putInt(String key, int vlues) {
        new AppPreferences(MyApplication.getInstance()).put(key, vlues);
    }

    public static Integer getInt(String key) {
        return new AppPreferences(MyApplication.getInstance()).getInt(key, 0);
    }

}
