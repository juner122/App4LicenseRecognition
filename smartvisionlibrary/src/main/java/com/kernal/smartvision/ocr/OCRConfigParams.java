package com.kernal.smartvision.ocr;

import android.content.Context;

import com.kernal.smartvisionocr.utils.SharedPreferencesHelper;

public class OCRConfigParams   {

    /**
     * OcrType 为识别类型 【0:同时使用 vin 和 手机号识别;  1: 只用 vin;  2: 只用手机号识别; 】
     * **/

    // 默认同时使用 vin 和 手机号
    public  static int OcrType = 1;

    public static int getOcrType(Context mContext){
        int currentType = 0;
        if (mContext != null){
            Context context = mContext.getApplicationContext();
            currentType = SharedPreferencesHelper.getInt(context, "SettingSelectType", 0);
        }
        if (OcrType != 0){
            return OcrType;
        }
        return currentType;
    }

}
