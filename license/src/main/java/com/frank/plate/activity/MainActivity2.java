package com.frank.plate.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import com.frank.plate.PlateRecognition;
import com.frank.plate.R;
import com.frank.plate.thread.RecognizeThread;
import com.frank.plate.util.ImageUtil;

public class MainActivity2 extends Activity implements View.OnClickListener{

    private static final String TAG = MainActivity2.class.getSimpleName();
    public TextView tv_result;
    public TextView tv_runtime;
    public ImageView img_plate;
    private static final int REQUEST_CODE_IMAGE = 1000;
    public long handle;
    private ImageUtil imageUtil;
    private PlateRecognition plateRecognition;
    private RecognizeThread recognizeThread;
    private Mat mat;

    //load openCV library
    static {
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "openCV load_success...");

        } else {
            Log.d(TAG, "openCV  load fail...");

        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PlateRecognition.MSG_RESULT://recognize finish
                    String result = (String) msg.obj;
                    int diff = msg.arg1;
                    result = getString(R.string.result) + result;
                    tv_result.setText(result);
                    String needTime = "识别耗时:" + diff+" ms";
                    tv_runtime.setText(needTime);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        requirePermission();
        initView();
        plateRecognition = new PlateRecognition(this, mHandler);
        //init plate recognizer
        new Thread(new Runnable() {
            @Override
            public void run() {
                plateRecognition.initRecognizer("pr");
            }
        }).start();

    }

    private void initView(){
        img_plate = (ImageView)findViewById(R.id.img_plate);
        tv_result = (TextView)findViewById(R.id.txt_result);
        tv_runtime = (TextView)findViewById(R.id.txt_runtime);
        Button btn_select = (Button)findViewById(R.id.btn_select);
        btn_select.setOnClickListener(this);
        Button btn_preview = (Button)findViewById(R.id.btn_preview);
        btn_preview.setOnClickListener(this);
    }

    //require permission if API is larger than 23
    private void requirePermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ActivityCompat.checkSelfPermission(MainActivity2.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity2.this, new String[]{permission},123);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albumIntent.addCategory(Intent.CATEGORY_OPENABLE);
                albumIntent.setType("image/jpeg");
                startActivityForResult(albumIntent, REQUEST_CODE_IMAGE);
                break;
            case R.id.btn_preview:
                startActivity(new Intent(MainActivity2.this, PreviewActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            if(imageUtil == null){
                imageUtil = new ImageUtil(this);
            }
            String file = imageUtil.getPath(mPath);
            Bitmap bitmap = imageUtil.decodeImage(file);
            if (bitmap == null) {
                Log.e(TAG, "error");
                return;
            }
            Log.i(TAG, "width=" + bitmap.getWidth() + ",height=" + bitmap.getHeight());
            img_plate.setImageBitmap(bitmap);
            //start plate recognizing
            if(plateRecognition != null){
//                plateRecognition.doPlateRecognize(bmp, sb.getProgress());
                if(recognizeThread == null){
                    recognizeThread = new RecognizeThread(plateRecognition);
                    recognizeThread.start();
                }
                if(mat == null){
                    mat = new Mat();
                }
                Utils.bitmapToMat(bitmap, mat);
                recognizeThread.addMat(mat);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

        if(plateRecognition != null){
            //release plate recognizer
            plateRecognition.releaseRecognizer();
        }

        if(recognizeThread != null){
            recognizeThread.setRunning(false);
            recognizeThread.interrupt();
            recognizeThread = null;
        }
    }

}

