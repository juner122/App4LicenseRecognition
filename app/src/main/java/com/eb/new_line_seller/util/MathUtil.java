package com.eb.new_line_seller.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {


    //显示两位小数
    public static String twoDecimal(double d) {

        DecimalFormat format = new DecimalFormat(",##0.00");
        String s = format.format(d);

        return s;
    }

    //显示两位小数
    public static String twoDecimal(String s) {
        Double d = Double.parseDouble(s);
        DecimalFormat format = new DecimalFormat(",##0.00");
        String s1 = format.format(d);

        return s1;
    }

    //显示两位小数
    public static String toDate(Long l) {
        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        Date date = new Date();
        date.setTime(l);
        return simpleDateFormat.format(date);
    }


}
