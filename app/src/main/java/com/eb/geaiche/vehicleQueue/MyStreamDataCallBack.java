package com.eb.geaiche.vehicleQueue;

import android.util.Log;

import com.zkzh.alpr.jni.IStreamDataCallBack;

import java.io.File;
import java.io.OutputStream;

public class MyStreamDataCallBack implements IStreamDataCallBack {

     private String path = "/storage/emulated/legacy/zkteco/"; // for Android

    private OutputStream os = null;
    private File savaImage = null;

    private int index = 0;


    public MyStreamDataCallBack() {
//        savaImage = new File(path);
//        if (!savaImage.exists())
//        {
//            savaImage.mkdirs();
//        }
    }

    @Override
    public void streamDataCallback(byte[] bytes, int nBytesLen) {
        Log.d("AlprSDK", "My stream callback bytes:" + bytes + ",len:" + nBytesLen);
//        try {
//            os = new FileOutputStream(savaImage.getPath()
//                    + File.separator
//                    + "LiveStream_" + index + ".dat");
//            // os.write(bytes);
//            os.close();
//            index++;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
