package com.kernal.smartvision.ocr;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.util.SparseArray;
import android.widget.Toast;

import com.kernal.smartvision.utils.ThreadManager;
import com.kernal.smartvisionocr.RecogService;
import com.kernal.smartvisionocr.utils.SharedPreferencesHelper;
import com.kernal.smartvisionocr.utils.Utills;

import java.io.File;

/**
 * Created by WenTong on 2018/12/6.
 */


/**
 * 识别服务类
 */
public class OcrRecogServer {

    public static int SCREEN_ORITATION_PORTRAIT = 1;
    public static int SCREEN_ORITATION_HORIZONTAL = 2;

    //封装了配置参数，包括敏感区域坐标等。
    private OcrTypeHelper ocrTypeHelper;

    private Camera.Size cameraSize;
    //字节流数据
    private byte[] data;
    //屏幕旋转方向。
    private int screenOritation;
    //表示识别类型：1 vin; 2:手机号
    private int ocr_type;
    private boolean isResetArea = true;
    private volatile static boolean  isTakePicture = false;
    private volatile static boolean isTakePicFinish = true;
    //数据旋转角度，0,90,180 或者 270
    private int rotation;

    private  RecogService.MyBinder recogBinder;
    private Context context;
    public int iTH_InitSmartVisionSDK=-1;
    public int[] regionPos=new int[4];
    public int error=-1;
    private boolean isRecognazing = false;
    String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";

    //这里存放识别结果的图片
    public   SparseArray sparseArray = new SparseArray<>(2);

    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (RecogService.MyBinder) service;
            iTH_InitSmartVisionSDK = recogBinder.getInitSmartVisionOcrSDK();
            if (iTH_InitSmartVisionSDK == 0) {
                //初始化成功
                recogBinder.AddTemplateFile();
            } else {
                System.out.println("核心初始化失败，错误码：" + iTH_InitSmartVisionSDK);
                Toast.makeText(context, "核心初始化失败，错误码：" + iTH_InitSmartVisionSDK, Toast.LENGTH_LONG).show();
            }
        }
    };

    public OcrRecogServer(Context context){
        this.context=context;
    }

    public OcrRecogServer OcrRecogServer(Context context){
        this.context=context;
        return this;
    }

    /**
     * 初始化核心
     */
    public  OcrRecogServer initOcr(){
        ThreadManager.getInstance().execute(initOcrRunable);
        return  this;
    }

    Runnable initOcrRunable = new Runnable() {
        @Override
        public void run() {
            // 将一些资源文件写入本地
            com.kernal.smartvisionocr.utils.Utills.copyFile(context);
            Intent authIntent = new Intent(context, RecogService.class);
            context.bindService(authIntent, recogConn, Service.BIND_AUTO_CREATE);
        }
    };


    /**
     * 设置屏幕方向，在横竖屏切换后需要重置敏感区域
     * @param oritation
     */
    public void setScreenOritation(int oritation){
        this.screenOritation = oritation;
        isResetArea = true;
    }

    /**
     * 设置识别的类型，vin 还是手机号，设置之后需要重置敏感区域
     * @param type
     */
    public void setOcr_type(int type){
        this.ocr_type = type;
        isResetArea = true;
    }

    /**
     * 设置敏感区域
     * @return
     */
    public boolean setRecogArea(){
        boolean isSet = false;
        if (recogBinder == null){
             isSet = false;
        }else {
            ocrTypeHelper = new OcrTypeHelper(ocr_type,screenOritation).getOcr();
            regionPos=setRegion(screenOritation ,cameraSize,ocrTypeHelper);
            // 绑定模板
            recogBinder.SetCurrentTemplate(ocrTypeHelper.ocrId);
            recogBinder.SetROI(regionPos, cameraSize.width, cameraSize.height);
            isSet = true;
        }
        return isSet;
    }


    /**
     * 拍照
     */
    public void setTakePicture(){
        //isTakePicFinish 防止连点拍照按钮多次拍照
        isTakePicture = isTakePicFinish?true:false;
    }

    /**
     * 识别函数，拍照识别与视频流识别都在这里
     * @param size                  相机尺寸
     * @param cameraData            数据
     * @param cameraRotate          相机旋转角度
     * @return  返回的识别结果，(vin码 或者 手机号码)
     */
    public String startRecognize(Camera.Size size,byte[] cameraData,int cameraRotate){
        if (isRecognazing){
            return null;
        }
        isRecognazing = true;
        cameraSize = size;
        data = cameraData;
        rotation = cameraRotate;
        if (isResetArea ){
            isResetArea = false;
            if (!setRecogArea()){
                isRecognazing = false;
                return null;
            }
        }
        String Imagepath,SavePicPath;
        int[] nCharCount=new int[1];
        int returnResult=-1;
        if(recogBinder==null || ocrTypeHelper == null){
            isRecognazing = false;
            return null;
        }
        //测试一直保存图片
        //com.kernal.smartvision.utils.Utills.savePicture(data,0,cameraSize,cameraRotate);

        //识别结果
        String recogResultString = "";
        // 点击拍照按钮 保存图片识别，强制跳过未自动识别条目
        if (isTakePicture ) {
            isTakePicture = false;
            isTakePicFinish = false;
            Imagepath = com.kernal.smartvision.utils.Utills.savePicture(data,1,cameraSize,rotation);
            if (Imagepath != null && !"".equals(Imagepath)) {
                // 根据图片路径 加载图片
                recogBinder.LoadImageFile(Imagepath,0);
                recogBinder.Recognize(Devcode.devcode, ocrTypeHelper.importTemplateID);
                 recogResultString = recogBinder.GetResults(nCharCount);
                if ((recogResultString != null && !recogResultString.equals(""))) {
                    // 有识别结果时，保存识别核心裁切到的图像
                    String name = "smartVisition" +  Utills.pictureName();
                    SavePicPath = saveROIPicture(PATH, name,false);
                    sparseArray.put(0,SavePicPath);
                    sparseArray.put(1,SavePicPath);
                } else {
                    // 未识别到结果，此时保存识别区域ROI内的图像
                    recogResultString = " ";
                    String name = "smartVisition" +  Utills.pictureName();
                    SavePicPath = saveROIPicture(PATH,name, true);
                    sparseArray.put(0,SavePicPath);
                    sparseArray.put(1,SavePicPath);
                }
                if (SavePicPath != null && !"".equals(SavePicPath)) {
                    if (SharedPreferencesHelper.getBoolean(context.getApplicationContext(), "upload", false)) {
                        String[] httpContent =new String[]{SavePicPath,""};
                        new com.kernal.smartvision.utils.WriteToPCTask(context).execute(httpContent);
                    }
                }
                File orignPicFile=new File(Imagepath);
                if(orignPicFile.exists()){
                    orignPicFile.delete();
                }
            }
            isRecognazing = false;
            isTakePicFinish = true;
            return recogResultString;
        } else {
            // 加载视频流数据源
            if (rotation == 90) {
                recogBinder.LoadStreamNV21(data, cameraSize.width, cameraSize.height, 1);
            } else if (rotation == 0) {
                recogBinder.LoadStreamNV21(data, cameraSize.width, cameraSize.height, 0);
            } else if (rotation == 180) {
                recogBinder.LoadStreamNV21(data,cameraSize.width, cameraSize.height, 2);
            } else {
                recogBinder.LoadStreamNV21(data,cameraSize.width, cameraSize.height, 3);
            }
        }
        if(recogBinder==null || ocrTypeHelper == null){
            isRecognazing = false;
            return null;
        }
        // 开始识别
        returnResult = recogBinder.Recognize(Devcode.devcode, ocrTypeHelper.ocrId);
        if (returnResult == 0) {
            if (recogBinder == null){
                isRecognazing = false;
                return null;
            }
            // 获取识别结果
            recogResultString = recogBinder.GetResults(nCharCount);

            /**
             * 保存图片的操作，不需要保存图片把下面的代码注释掉
             * SavePicPath 默认为裁切图片的路径，如需要返回全图，调用Utills.savePicture(data,0,cameraSize,rotation) 即可;
             */
            if (recogResultString != null && !recogResultString.equals("") && nCharCount[0] > 0&& RecogService.response==0) {
                if ((recogResultString != null && !recogResultString.equals(""))) {
                    // 有识别结果时，保存识别核心裁切到的图像
                    String name = "smartVisition" +  Utills.pictureName();
                    SavePicPath = saveROIPicture(PATH,name ,false);
                    sparseArray.put(0,SavePicPath);
                     name = "sensitive" +  Utills.pictureName();
                    String rectPicPath = saveROIPicture(PATH,name ,true);
                    com.kernal.smartvision.utils.Utills.rotatePic(rectPicPath,rotation,context.getApplicationContext());
                    sparseArray.put(1,rectPicPath);
                } else {
                    // 未识别到结果，此时保存识别区域ROI内的图像
                    recogResultString = " ";
                    String name = "smartVisition" +  Utills.pictureName();
                    SavePicPath = saveROIPicture(PATH,name ,true);
                    sparseArray.put(0,SavePicPath);
                    sparseArray.put(1,SavePicPath);
                }
                if (SavePicPath != null && !"".equals(SavePicPath)) {
                    if (SharedPreferencesHelper.getBoolean(context.getApplicationContext(), "upload", false)) {
                        String[] httpContent =new String[]{SavePicPath,""};
                        new com.kernal.smartvision.utils.WriteToPCTask(context).execute(httpContent);
                    }
                }
            }

            isRecognazing = false;
            return recogResultString;
        } else {
            //授权错误
            System.out.println("识别错误，错误码: " + returnResult);
            error=returnResult;
            isRecognazing = false;
            return null;
        }
    }

    /**
     * @Title: setRegion 计算识别区域
     */
    public int[] setRegion(int screenOratation, Camera.Size size,OcrTypeHelper ocrTypeHelper) {
        int[] regionPosTemp = new int[4];
        if (1 == screenOratation) {
            //竖屏
            regionPosTemp[0] = (int) (ocrTypeHelper.leftPointXPercent * size.height);
            regionPosTemp[1] = (int) (ocrTypeHelper.leftPointYPercent * size.width);
            regionPosTemp[2] = (int) ((ocrTypeHelper.leftPointXPercent + ocrTypeHelper.widthPercent) * size.height);
            regionPosTemp[3] = (int) ((ocrTypeHelper.leftPointYPercent + ocrTypeHelper.heightPercent) * size.width);
        } else {
            //横屏
            regionPosTemp[0] = (int) (ocrTypeHelper.leftPointXPercent * size.width);
            regionPosTemp[1] = (int) (ocrTypeHelper.leftPointYPercent * size.height);
            regionPosTemp[2] = (int) ((ocrTypeHelper.leftPointXPercent + ocrTypeHelper.widthPercent) * size.width);
            regionPosTemp[3] = (int) ((ocrTypeHelper.leftPointYPercent + ocrTypeHelper.heightPercent) * size.height);
        }
        return  regionPosTemp;
    }


    /**
     * @param path，需要保存的图片的路径
     * @param name，图片的名字
     * @param bol ，true:保存敏感区域图片，false:裁切图片
     * @return
     */
    public String saveROIPicture(String path,String name,boolean bol) {
        String picPathString = "";
        //路径合法判断
        if (null == path || "".equals(path) || null == name || "".equals(name) ){
            return  "";
        }
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            picPathString = path + name + ".jpg";
            if (bol) {
                // 识别区域内图片
                recogBinder.svSaveImage(picPathString);
            } else {
                // 识别成功剪切的图片
                recogBinder.svSaveImageResLine(picPathString);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return picPathString;
    }

    /**
     * 释放资源
     * @param context
     */
    public void freeKernalOpera(Context context){
        if (recogBinder != null ) {
            context.unbindService(recogConn);
            recogBinder = null;
        }
    }

}
