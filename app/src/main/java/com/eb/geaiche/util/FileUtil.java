package com.eb.geaiche.util;

import java.io.File;

public class FileUtil {

    //判断ulr是一个apk文件
    public static boolean isExistence(String strFile) {

        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }


        return true;
    }

}
