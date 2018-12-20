package com.frank.plate.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {


    //显示两位小数
    public static String twoDecimal(double d) {

        DecimalFormat format = new DecimalFormat(",##0.00");
        String s = format.format(d);

        return s;
    }

    //显示两位小数
    public static String toDate(Long l) {
        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制

        Date date = new Date();
        date.setTime(l);
        return simpleDateFormat.format(date);
    }


}
