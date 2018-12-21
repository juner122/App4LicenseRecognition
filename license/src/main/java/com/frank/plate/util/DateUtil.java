package com.frank.plate.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    static String format = "yyyy-MM-dd HH:mm:ss";

    public static String getFormatedDateTime(String dateTime) {
        Long l = Long.valueOf(dateTime);

        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        return sDateFormat.format(new Date(l));
    }

    public static String getFormatedDateTime(Long dateTime) {

        if (null == dateTime) {
            return "-";
        }
        format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        return sDateFormat.format(new Date(dateTime));
    }

    public static String getFormatedDateTime(Date dateTime) {
        format = "yyyy-MM-dd";
        return new SimpleDateFormat(format).format(dateTime);
    }

    public static String getFormatedDateTime2(Date dateTime) {
        format = "yyyy-MM-dd HH:mm";
        return new SimpleDateFormat(format).format(dateTime);
    }


    public static Calendar getStartDate() {


        Calendar startDate = Calendar.getInstance();
        //正确设置方式 原因：注意事有说明
        startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE), startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE));

        return startDate;


    }

    public static Calendar getEndDate() {


        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事有说明
        endDate.set(2020, 11, 31);
        return endDate;


    }

}
