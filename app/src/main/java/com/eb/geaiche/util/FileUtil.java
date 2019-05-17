package com.eb.geaiche.util;

import android.content.Context;

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
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }
}
