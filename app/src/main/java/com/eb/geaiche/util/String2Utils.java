package com.eb.geaiche.util;

import android.text.TextUtils;
import android.widget.EditText;

import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.MemberEntity;
import com.juner.mvp.bean.Server;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class String2Utils {


    public static String getString(List<Technician> list) {

        if (null == list || list.size() == 0) return "请选择技师";

        String s = "";
        for (Technician t : list) {
            s = String.format("%s,\t%s", s, t.getUsername());
        }

        return s.substring(1);


    }

    public static String getStringfor(List<Technician> list) {

        if (null == list || list.size() == 0) return "请选择技师";

        String s = "";
        for (Technician t : list) {
            if (t.isSelected())
                s = String.format("%s,\t%s", s, t.getUsername());
        }

        return s.substring(1);


    }

    public static String getString2(List<MemberEntity> list) {

        if (null == list || list.size() == 0) return "请选择技师";

        String s = "";
        for (MemberEntity t : list) {
            s = String.format("%s,\t%s", s, t.getUsername());
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
    public static double getOrderServicePrice(List<Server> list) {

        if (null == list || list.size() == 0) return 0.00;

        double totalPrice = 0.00d;

        for (Server g : list) {

            totalPrice = g.getPrice() * g.getNumber() + totalPrice;
        }
        return totalPrice;

    }


    public static boolean isNullCarNumber(EditText... editTexts) {

        for (EditText d : editTexts) {
            if (TextUtils.isEmpty(d.getText()))
                return false;
        }
        return true;
    }
}
