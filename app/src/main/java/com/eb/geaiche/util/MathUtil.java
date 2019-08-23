package com.eb.geaiche.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MathUtil {
    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_15days = seconds_of_1day * 15;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * 格式化时间	 * @param mTime	 * @return
     */

    //显示两位小数
    public static String twoDecimal(double d) {

        DecimalFormat format = new DecimalFormat("##0.00");
        String s = format.format(d);

        return s;
    }

    //显示两位小数
    public static String twoDecimal(String s) {

        if (null == s)
            return "0.00";

        Double d = Double.parseDouble(s);
        DecimalFormat format = new DecimalFormat("##0.00");
        String s1 = format.format(d);

        return s1;
    }

    //用****替换手机号码中间4位
    public static String hidePhone(String s) {
        if (null == s || s.equals(""))
            return "-";

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


    //与当前时间对比
    public static String toDateFormNow(Long mTime) {


        /**除以1000是为了转换成秒*/
        long between = (System.currentTimeMillis() - mTime) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {
            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {
            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {
            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + "小时前";
        } else {
            return toDate(mTime);

        }

//
//        if (elapsedTime < seconds_of_15days) {
//            return elapsedTime / seconds_of_1day + "天前";
//        }
//        if (elapsedTime < seconds_of_30days) {
//            return "半个月前";
//        }
//        if (elapsedTime < seconds_of_6months) {
//            return elapsedTime / seconds_of_30days + "月前";
//        }
//        if (elapsedTime < seconds_of_1year) {
//            return "半年前";
//        }
//        if (elapsedTime >= seconds_of_1year) {
//            return elapsedTime / seconds_of_1year + "年前";
//        }
//        return "";
    }


    public static String toDate(String l) {
        /**
         * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        Date date = new Date();
        date.setTime(Long.valueOf(l));
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
