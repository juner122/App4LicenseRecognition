package com.kernal.smartvision.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kernal.smartvision.R;
import com.kernal.smartvision.fragment.BasicFragment;
import com.kernal.smartvision.ocr.Devcode;
import com.kernal.smartvision.utils.PermissionUtils;
import com.kernal.smartvision.view.VinCameraPreView;
import com.kernal.smartvisionocr.RecogService;
import com.kernal.smartvisionocr.utils.Utills;

import java.io.File;
import java.lang.ref.WeakReference;

/**导入识别
 * Created by user on 2018/3/28.
 */
public class PictureRecogActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,BasicFragment.FragmentSelectPicture{
    public RecogService.MyBinder recogBinder;
    private int iTH_InitSmartVisionSDK = -1;
    private String Imagepath;
    private int[] nCharCount = new int[2];// 返回结果中的字符数
    public    ProgressDialog mypDialog;
    private String uploadPicPath;
    private final int SELECT_RESULT_CODE = 3;
    private static final int REQUEST_PICK_IMAGE = 4;


    private BasicFragment fragment;
    private boolean useCrop = false;
    private boolean isRecognazing = false;

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
                recogBinder.AddTemplateFile();// 添加识别模板

                /**
                 * 模板分为三种：
                 * vin 码扫描识别模板id    : SV_ID_VIN_CARWINDOW(已经在 OcrVin.java 中配置好)
                 * 手机号码扫描识别模板id  : SV_ID_YYZZ_MOBILEPHONE(已经在 OcrPhoneNumber 中配置好)
                 * vin 码导入识别模板id    :SV_ID_VIN_MOBILE(导入识别只支持vin码识别，在这里配置)
                 */
                recogBinder.SetCurrentTemplate("SV_ID_VIN_MOBILE");// 设置当前识别模板ID为 vin 码扫描识别的 id
                recogBinder.LoadImageFile(Imagepath,0);
               int returnRecogize=  recogBinder.Recognize(Devcode.devcode, "SV_ID_VIN_MOBILE");
                String recogResultString = recogBinder.GetResults(nCharCount);
                if (recogResultString==null||"".equals(recogResultString)) {
                    if(returnRecogize!=0){
                        recogResultString = "识别失败，错误代码为:"+returnRecogize;
                    }else {
                        recogResultString = "识别失败";
                    }
                }else{
                    //识别成功的
                    Imagepath = savePicture();
                }
                Intent intent = new Intent();
                intent.putExtra("RecogResult", recogResultString);
                //vin
                intent.putExtra("ocrType",0);
                intent.putExtra("resultPic",Imagepath);
                intent.putExtra("uploadPicPath",uploadPicPath);
                setResult(Activity.RESULT_OK,intent);
                finish();
            } else {
                Toast.makeText(PictureRecogActivity.this, "核心初始化失败，错误码：" + iTH_InitSmartVisionSDK, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    };

    public  static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE, // 读取权限
            Manifest.permission.CAMERA,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pic_recog);

        //判断是否需要动态授权
        if (Build.VERSION.SDK_INT >= 23) {
            //先进行权限申请
            permission();
        } else {
            //不需要动态授权
            setlectPicAndRecog();
        }
    }

    /**
     * 选择图片进行识别
     */
    private void setlectPicAndRecog(){
        // 将资源文件写入本地
        Utills.copyFile(this);
        Intent selectIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent wrapperIntent = Intent.createChooser(selectIntent, "请选择一张图片");
        try {
            startActivityForResult(wrapperIntent, SELECT_RESULT_CODE);
        } catch (Exception e) {
            Toast.makeText(PictureRecogActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public  void CreateDialog(Context context){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mypDialog=new ProgressDialog(context);
        mypDialog.setProgressStyle(context.getResources().getIdentifier("dialog","styles",context.getPackageName()));
        mypDialog.setMessage("识别中.....");
        mypDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( recogBinder != null) {
            unbindService(recogConn);
        }
        if (mypDialog != null){
            mypDialog.dismiss();
        }
    }

    /**
     * 保存图片
     * @return
     */
    public String savePicture() {
        String picPathString1 = "";
        String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name =  Utills.pictureName();
        picPathString1 = PATH + "smartVisition" + name + ".jpg";
        // 识别区域内图片
        recogBinder.svSaveImageResLine(picPathString1);
        File temp = new File(picPathString1);
        if (temp.length() <= 0){
            picPathString1 = uploadPicPath;
        }
        return picPathString1;
    }

    /**
     * 权限申请
     */
    private void permission(){
        boolean isgranted = true;
        for (int i = 0; i < PERMISSION.length; i++) {
            if (ContextCompat.checkSelfPermission(this, PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                isgranted = false;
                break;
            }
        }
        if(!isgranted){
            //没有授权
            PermissionUtils.requestMultiPermissions(this,mPermissionGrant);
        }else{
           setlectPicAndRecog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == SELECT_RESULT_CODE || requestCode == REQUEST_PICK_IMAGE ) && resultCode == Activity.RESULT_OK) {
            String picPathString = null;
            Uri uri = data.getData();
            picPathString = com.kernal.smartvision.utils.Utills.getPath(getApplicationContext(), uri);
            if (picPathString != null && !picPathString.equals("") && !picPathString.equals(" ") && !picPathString.equals("null")) {
                if (picPathString.endsWith(".jpg") || picPathString.endsWith(".JPG") || picPathString.endsWith(".png") || picPathString.endsWith(".PNG") ||  picPathString.endsWith(".jpeg")) {
                    String tempPath = picPathString;
                    /**
                     * 可以直接用从图库选择的图片进行识别，也可以对图片进行裁剪等操作后进行识别,如果不想裁切图片，把下面代码注释即可
                     */

                    Bundle bundle = new Bundle();
                    bundle.putString("DATA",uri.toString());
                    if (fragment == null){
                        useCrop = true;
                        fragment = BasicFragment.newInstance();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.pic_container, fragment).commit();
                    }else {
                        useCrop = true;
                        fragment = BasicFragment.newInstance();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.pic_container, fragment).commit();
                    }

                    /**
                     * 开始识别
                     */
                  if (!useCrop){
                      recogPic(tempPath);
                  }

                } else {
                    Toast.makeText(this, "请选择一张正确的图片", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            backEvent();
        }
    }

    /**
     * 开始识别
     * @param selectPicPath 选中的图片的路径
     */
    private void recogPic(String selectPicPath){
       // Log.i("ImagePathDemo", "recogPic: " + selectPicPath);
        Imagepath = selectPicPath;
        uploadPicPath = selectPicPath;
        CreateDialog(this);
        if (recogBinder == null) {
            Intent authIntent = new Intent(this, RecogService.class);
            bindService(authIntent, recogConn, Service.BIND_AUTO_CREATE);
        }
    }

    /**
     * 裁切图片回调函数
     * @param path 裁切好的图片的路径
     */
    @Override
    public void startRecog(String path) {
        if (isRecognazing){
          return;
        }
        isRecognazing = true;
        Imagepath = path;
        uploadPicPath = path;
         CreateDialog(this);
        if (recogBinder == null) {
            Intent authIntent = new Intent(this, RecogService.class);
            bindService(authIntent, recogConn, Service.BIND_AUTO_CREATE);
        }

    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_MULTI_PERMISSION:
                    setlectPicAndRecog();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backEvent();
            return true;
        }
        return true;
    }
    private void backEvent(){
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > 18) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    |    View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(option);
        }

    }


}
