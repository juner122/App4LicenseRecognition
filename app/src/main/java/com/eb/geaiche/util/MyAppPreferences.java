package com.eb.geaiche.util;


import com.eb.geaiche.MyApplication;

import net.grandcentrix.tray.AppPreferences;

import static com.juner.mvp.Configure.SHOP_TYPE;


public class MyAppPreferences {


    //获取门店类型
    public static boolean getShopType() {
        return new AppPreferences(MyApplication.getInstance()).getBoolean(SHOP_TYPE, true);
    }


    public static void putShopType(boolean isAlliance) {

        new AppPreferences(MyApplication.getInstance()).put(SHOP_TYPE, isAlliance);
    }

    public static void removeShopType() {

        new AppPreferences(MyApplication.getInstance()).remove(SHOP_TYPE);//门店类型

    }


}
