package com.frank.plate.util;

import android.text.TextUtils;
import android.widget.EditText;

import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.Technician;

import java.util.List;

public class String2Utils {


    public static String getString(List<Technician> list) {

        if (null == list || list.size() == 0) return "请选择技师";

        String s = "";
        for (Technician t : list) {
            s = String.format("%s,%s", s, t.getUsername());
        }

        return s.substring(1);


    }


    //计算商品总价
    public static double getOrderGoodsPrice(List<GoodsEntity> list) {

        if (null == list || list.size() == 0) return 0.00;

        double totalPrice = 0.00d;

        for (GoodsEntity g : list) {

                totalPrice = g.getNumber() * g.getRetail_priceTodouble() + totalPrice;
        }
        return totalPrice;

    }

    //计算商品总价
    public static double getOrderServicePrice(List<GoodsEntity> list) {

        if (null == list || list.size() == 0) return 0.00;

        double totalPrice = 0.00d;

        for (GoodsEntity g : list) {

                totalPrice = g.getPriceTodouble() + totalPrice;
        }
        return totalPrice;

    }


    public static String getPayTypeText(int payType) {

        switch (payType) {
            case 1:
                return "嗨卡";

            case 11:
                return "微信";


            case 21:
                return "掌贝";


            case 22:
                return "现金";


        }

        return "-";
    }

    public static boolean isNullCarNumber(EditText... editTexts) {

        for (EditText d : editTexts) {
            if (TextUtils.isEmpty(d.getText()))
                return false;
        }
        return true;
    }
}
