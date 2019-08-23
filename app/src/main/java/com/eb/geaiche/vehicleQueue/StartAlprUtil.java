package com.eb.geaiche.vehicleQueue;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.eb.geaiche.activity.MainActivity;
import com.zkzh.alpr.jni.AlprSDK;
import com.zkzh.alpr.jni.DEVINFO;
import com.zkzh.alpr.jni.RecogAllInfoCallback;
import com.zkzh.alpr.jni.StreamDataCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//开启中控科技车牌识别sdk工具类
public class StartAlprUtil {

    static String file = "self.pem";//授权文件名
    static String filepath = getSDPath() + File.separator + file;//手机上存放的授权文件


    //开启中控车牌识别sdk
    public static void startAlprOperation(Context context, MainActivity.MyRecogAllInfoCallback myRecogAllInfoCallback) {

        //先将assets目录下的授权文件self.pem放到手机主目录getSDPath()下  存在则跳过
            if (fileIsExists(filepath)) {
            //存在
        } else {
            //不存在
            copyFolderFromAssets(context, file, filepath);
        }

        Thread getLicenceThread = new Thread() {
            public void run() {
                int ret = -1;

                String path = getSDPath();

                //存放日志和授权文件路径
                ret = AlprSDK.AlprSDK_Startup(path);


//                ret = AlprSDK.AlprSDK_Startup("");
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_Startup failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_Startup succeed");
                }

                ret = AlprSDK.AlprSDK_InitHandle(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_InitHandle failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_InitHandle succeed");
                }

                ret = AlprSDK.AlprSDK_SetConnectTimeout(0, 10000);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_SetConnectTimeout failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_SetConnectTimeout succeed");
                }

                StreamDataCallBack steamDateCb = new StreamDataCallBack(new MyStreamDataCallBack());
                ret = AlprSDK.AlprSDK_CreateEZStreamDataCB(0, steamDateCb);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_CreateEZStreamDataCB failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_CreateEZStreamDataCB succeed");
                }

                RecogAllInfoCallback recogAllCb = new RecogAllInfoCallback(myRecogAllInfoCallback);
                ret = AlprSDK.AlprSDK_CreateRecogAllInfoTask(0, recogAllCb);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_CreateRecogAllInfoTask failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_CreateRecogAllInfoTask succeed");
                }

                ret = AlprSDK.AlprSDK_EnableP2PReconnect(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_EnableP2PReconnect failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_EnableP2PReconnect succeed");
                }

                DEVINFO devInfo = new DEVINFO();
                devInfo.u16port = 80;
                devInfo.ipAddr = "192.168.1.88";
                devInfo.ifOpenP2p = 0;
                devInfo.devUid = "0bd30123d8f5e225bf67";
                devInfo.lprDevType = 0;
                devInfo.userName = "admin";
                devInfo.password = "123456";
                devInfo.picturesSavePath = "";
                ret = AlprSDK.AlprSDK_ConnectDev(0, devInfo, 2);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_ConnectDev failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_ConnectDev succeed");
                }

                ret = AlprSDK.AlprSDK_StartVideo(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_StartVideo failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_StartVideo succeed");
                }

                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ret = AlprSDK.AlprSDK_StopVideo(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_StopVideo failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_StopVideo succeed");
                }

                ret = AlprSDK.AlprSDK_DisableP2PReconnect(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_DisableP2PReconnect failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_DisableP2PReconnect succeed");
                }

                ret = AlprSDK.AlprSDK_DisConnectDev(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_DisConnectDev failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_DisConnectDev succeed");
                }

                ret = AlprSDK.AlprSDK_ClearRecogAllInfoTask(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_ClearRecogAllInfoTask failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_ClearRecogAllInfoTask succeed");
                }

                ret = AlprSDK.AlprSDK_ClearEZStreamDataCB(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_ClearEZStreamDataCB failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_ClearEZStreamDataCB succeed");
                }

                ret = AlprSDK.AlprSDK_UnInitHandle(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_UnInitHandle failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_UnInitHandle succeed");
                }

                //   recoonect --------  start

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ret = AlprSDK.AlprSDK_InitHandle(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_InitHandle failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_InitHandle succeed");
                }

                ret = AlprSDK.AlprSDK_SetConnectTimeout(0, 10000);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_SetConnectTimeout failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_SetConnectTimeout succeed");
                }

                steamDateCb = new StreamDataCallBack(new MyStreamDataCallBack());
                ret = AlprSDK.AlprSDK_CreateEZStreamDataCB(0, steamDateCb);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_CreateEZStreamDataCB failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_CreateEZStreamDataCB succeed");
                }

                recogAllCb = new RecogAllInfoCallback(myRecogAllInfoCallback);

                ret = AlprSDK.AlprSDK_CreateRecogAllInfoTask(0, recogAllCb);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_CreateRecogAllInfoTask failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_CreateRecogAllInfoTask succeed");
                }

                ret = AlprSDK.AlprSDK_EnableP2PReconnect(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_EnableP2PReconnect failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_EnableP2PReconnect succeed");
                }

                ret = AlprSDK.AlprSDK_ConnectDev(0, devInfo, 2);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_ConnectDev failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_ConnectDev succeed");
                }

                ret = AlprSDK.AlprSDK_StartVideo(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_StartVideo failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_StartVideo succeed");
                }

                try {
                    sleep(5000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ret = AlprSDK.AlprSDK_StopVideo(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_StopVideo failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_StopVideo succeed");
                }

                ret = AlprSDK.AlprSDK_DisableP2PReconnect(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_DisableP2PReconnect failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_DisableP2PReconnect succeed");
                }

                ret = AlprSDK.AlprSDK_DisConnectDev(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_DisConnectDev failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_DisConnectDev succeed");
                }

                ret = AlprSDK.AlprSDK_ClearRecogAllInfoTask(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_ClearRecogAllInfoTask failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_ClearRecogAllInfoTask succeed");
                }

                ret = AlprSDK.AlprSDK_ClearEZStreamDataCB(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_ClearEZStreamDataCB failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_ClearEZStreamDataCB succeed");
                }

                ret = AlprSDK.AlprSDK_UnInitHandle(0);
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_UnInitHandle failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_UnInitHandle succeed");
                }

                //   recoonect --------  end

                ret = AlprSDK.AlprSDK_Cleanup();
                if (ret != 0) {
                    Log.e("AlprSDK", "AlprSDK_Cleanup failed, ret = " + ret);
                    return;
                } else {
                    Log.d("AlprSDK", "AlprSDK_Cleanup succeed");
                }
            }
        };
        getLicenceThread.start();
    }

    /**
     * 从assets目录下拷贝整个文件夹，不管是文件夹还是文件都能拷贝
     *
     * @param context                               上下文
     * @param authorization，要拷贝的目录如assets目录下有一个授权文件 "self.pem"
     * @param targetDirFullPath                     目标文件夹位置如：/Download/
     */
    public static void copyFolderFromAssets(Context context, String authorization, String targetDirFullPath) {

        Log.d("AlprSDK", "copyFolderFromAssets " + "authorization= " + authorization + " targetDirFullPath=" + targetDirFullPath);


        if (isFileByName(authorization)) {// 文件
            copyFileFromAssets(context, authorization, targetDirFullPath);
        }


    }

    /**
     * 从assets目录下拷贝文件
     *
     * @param context            上下文
     * @param assetsFilePath     文件的路径名如：SBClock/0001cuteowl/cuteowl_dot.png
     * @param targetFileFullPath 目标文件路径如：/sdcard/SBClock/0001cuteowl/cuteowl_dot.png
     */
    public static void copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {
        Log.d("AlprSDK", "copyFileFromAssets ");
        InputStream assestsFileImputStream;
        try {
            assestsFileImputStream = context.getAssets().open(assetsFilePath);
            copyFile(assestsFileImputStream, targetFileFullPath);
        } catch (IOException e) {
            Log.d("AlprSDK", "copyFileFromAssets " + "IOException-" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copyFile(InputStream in, String targetPath) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(targetPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            in.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    private static boolean isFileByName(String string) {
        if (string.contains(".")) {
            return true;
        }
        return false;
    }

    //判断是否存在文件
    private static boolean fileIsExists(String strFile) {
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
