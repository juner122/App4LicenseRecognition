package com.frank.plate.util;

import java.text.SimpleDateFormat;
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

}
