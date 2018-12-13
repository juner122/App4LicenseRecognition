package com.frank.plate.activity;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.frank.plate.Configure;
import com.frank.plate.PlateRecognition;


import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.util.ToastUtils;
import com.frank.plate.view.PlateRecognizerView;
import com.frank.plate.R;
import com.frank.plate.listener.OnNewFrameListener;
import com.frank.plate.thread.RecognizeThread;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

import static com.frank.plate.activity.MemberInfoInputActivity.car_number;

public class PreviewActivity extends BaseActivity implements OnNewFrameListener {

    private static final String TAG = PreviewActivity.class.getSimpleName();
    private PlateRecognition plateRecognition;
    private RecognizeThread recognizeThread;
    private Mat dstMat;

    private PlateRecognizerView recognizerView;

    @BindView(R.id.e1)
    EditText et1;
    @BindView(R.id.e2)
    EditText et2;
    @BindView(R.id.e3)
    EditText et3;
    @BindView(R.id.e4)
    EditText et4;
    @BindView(R.id.e5)
    EditText et5;
    @BindView(R.id.e6)
    EditText et6;
    @BindView(R.id.e7)
    EditText et7;

    @Override
    protected void init() {

        tv_title.setText("扫描接单");
        initOpenCV();
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_preview;
    }

    @OnClick(R.id.but_next)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_next:
                onQueryByCar();
                break;
        }


    }

    private void onQueryByCar() {

        new AppPreferences(this).put(Configure.car_no, car_number);
        Api().queryByCar(car_number).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(QueryByCarEntity entity) {

                Log.d(TAG, "QueryByCarEntity信息：" + entity.toString());

                if (entity.getMember() != null) {

                    toActivity(MemberManagementInfoActivity.class, Configure.user_id, entity.getMember().getUserId());
                } else {
                    toActivity(MemberInfoInputActivity.class);
                }
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);

                ToastUtils.showToast(message);

            }
        });
    }


    private void initOpenCV() {

        plateRecognition = new PlateRecognition(this, mHandler);
        //init plate recognizer
        new Thread(new Runnable() {
            @Override
            public void run() {
                plateRecognition.initRecognizer("pr");
            }
        }).start();

        recognizerView = findViewById(R.id.surface_view);
        recognizerView.setOnNewFrameListener(this);
        recognizeThread = new RecognizeThread(plateRecognition);
        recognizeThread.start();

        boolean result = OpenCVLoader.initDebug();
        if (result) {
            Log.i(TAG, "initOpenCV success...");
            recognizerView.enableView();
        } else {
            Log.e(TAG, "initOpenCV fail...");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recognizerView != null)
            recognizerView.disableView();
        if (plateRecognition != null) {
            //release plate recognizer
            plateRecognition.releaseRecognizer();
        }
        if (recognizeThread != null) {
            recognizeThread.setRunning(false);
            recognizeThread.interrupt();
            recognizeThread = null;
        }
    }

    @Override
    public void onNewFrame(Mat newFrame) {
        if (dstMat == null) {
            dstMat = new Mat(newFrame.rows(), newFrame.cols(), CvType.CV_8UC4);
        }
        newFrame.copyTo(dstMat);


        //竖屏识别 帧数据旋转
        Core.transpose(dstMat, dstMat);
        Core.flip(dstMat, dstMat, 1);
        if (recognizeThread != null) {
            recognizeThread.addMat(dstMat);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PlateRecognition.MSG_RESULT://recognize finish
                    String result = (String) msg.obj;

                    ToastUtils.showToast("车牌号=" + result);
                    car_number = result;
                    try {
                        et1.setText(String.valueOf(result.charAt(0)));
                        et2.setText(String.valueOf(result.charAt(1)));
                        et3.setText(String.valueOf(result.charAt(2)));
                        et4.setText(String.valueOf(result.charAt(3)));
                        et5.setText(String.valueOf(result.charAt(4)));
                        et6.setText(String.valueOf(result.charAt(5)));
                        et7.setText(String.valueOf(result.charAt(6)));

                    } catch (Exception e) {

                        ToastUtils.showToast(e.getMessage());

                    }


                    break;
            }
        }
    };
}
