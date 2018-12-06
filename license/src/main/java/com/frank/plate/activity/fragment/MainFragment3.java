package com.frank.plate.activity.fragment;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.activity.MemberInfoInputActivity;
import com.frank.plate.PlateRecognition;
import com.frank.plate.activity.OrderInfoActivity;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.listener.OnNewFrameListener;
import com.frank.plate.thread.RecognizeThread;
import com.frank.plate.view.PlateRecognizerView;

import net.grandcentrix.tray.AppPreferences;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：扫描接单
 */
public class MainFragment3 extends BaseFragment implements OnNewFrameListener {


    public static final String TAG = "MainFragment3";


    private PlateRecognition plateRecognition;
    private RecognizeThread recognizeThread;
    private Mat dstMat;

    @BindView(R.id.surface_view)
    PlateRecognizerView recognizerView;

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

    String car_number = "粤B88888";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PlateRecognition.MSG_RESULT://recognize finish
                    String result = (String) msg.obj;
                    Toast.makeText(getContext(), "车牌号=" + result, Toast.LENGTH_SHORT).show();
                    car_number = result;


//                    new AppPreferences(getContext()).put(Configure.car_no, car_number);
                    try {
                        et1.setText(String.valueOf(result.charAt(0)));
                        et2.setText(String.valueOf(result.charAt(1)));
                        et3.setText(String.valueOf(result.charAt(2)));
                        et4.setText(String.valueOf(result.charAt(3)));
                        et5.setText(String.valueOf(result.charAt(4)));
                        et6.setText(String.valueOf(result.charAt(5)));
                        et7.setText(String.valueOf(result.charAt(6)));

                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }


                    break;
            }
        }
    };

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment3_main;
    }


    @Override
    protected void onVisible() {
        super.onVisible();
//        init();
    }


    public void init() {
        plateRecognition = new PlateRecognition(getActivity(), mHandler);
        //init plate recognizer
        new Thread(new Runnable() {
            @Override
            public void run() {
                plateRecognition.initRecognizer("pr");
            }
        }).start();


        recognizerView.setOnNewFrameListener(this);
        recognizeThread = new RecognizeThread(plateRecognition);
        recognizeThread.start();
        initOpenCV();


    }

    private void initOpenCV() {
        boolean result = OpenCVLoader.initDebug();
        if (result) {
            Log.i("MainFragment3", "主页页面：扫描接单  initOpenCV success...");
            recognizerView.enableView();
        } else {
            Log.e("MainFragment3", "主页页面：扫描接单  initOpenCV fail...");
        }
    }
    @Override
    protected void onHidden() {
        super.onHidden();
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
    protected void setUpView() {

    }


    @OnClick(R.id.but_next)
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.but_next:


                onQueryByCar();
                break;
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

    @Override
    protected String setTAG() {
        return TAG;
    }


    private void onQueryByCar() {

        new AppPreferences(getActivity()).put(Configure.car_no, car_number);
        Api().queryByCar(car_number).subscribe(new RxSubscribe<QueryByCarEntity>(getActivity(), true) {
            @Override
            protected void _onNext(QueryByCarEntity entity) {


                if (entity.getOrderInfo() != null) {//有订单 跳到订单详情
                    toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, entity.getOrderInfo().getId());
                } else if (entity.getUser() != null) {//没订单 有车况信息
                    toActivity(MemberInfoInputActivity.class, entity, Configure.QUERYBYCARINFO);
                }
            }

            @Override
            protected void _onError(String message) {

                Log.d(TAG, message);
                //没订单 没车况信息
                toActivity(MemberInfoInputActivity.class);

            }
        });
    }


}
