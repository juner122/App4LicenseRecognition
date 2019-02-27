package com.eb.new_line_seller.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {


    //显示两位小数
    public static String twoDecimal(double d) {

        DecimalFormat format = new DecimalFormat("##0.00");
        String s = format.format(d);

        return s;
    }

    //显示两位小数
    public static String twoDecimal(String s) {
        Double d = Double.parseDouble(s);
        DecimalFormat format = new DecimalFormat("##0.00");
        String s1 = format.format(d);

        return s1;
    }

    //用****替换手机号码中间4位
    public static String hidePhone(String s) {


        return s.substring(0, 3) + "****" + s.substring(7, s.length());
    }


    public static String toDate(Long l) {
        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        Date date = new Date();
        date.setTime(l);
        return simpleDateFormat.format(date);

    }

    public static String percent(int diliverNum, int queryMailNum) {

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) diliverNum / (float) queryMailNum * 100);


        return result + "%";

    }

    public static String toDate4Day(Long l) {

        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//24小时制

        Date date = new Date();
        date.setTime(l);
        return simpleDateFormat.format(date);
    }


    public static String toDate4h(Long l) {

        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//24小时制

        Date date = new Date();
        date.setTime(l);
        return simpleDateFormat.format(date);
    }

    public static String toNowDate() {

        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        Date date = new Date();

        return simpleDateFormat.format(date);
    }


    public static String toNowDate2() {

        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//24小时制

        Date date = new Date();

        return simpleDateFormat.format(date);
    }
    /*
     * 毫秒转化
     */
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        return strMinute;
    }


}
