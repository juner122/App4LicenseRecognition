package com.eb.geaiche.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 与String相关的工具方法集
 * @author Jimmy
 *
 */
public class StringUtil {


    /**
     * @param regex 正则
     * */
    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }



}