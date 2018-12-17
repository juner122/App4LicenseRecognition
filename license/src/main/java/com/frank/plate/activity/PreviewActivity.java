package com.frank.plate.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.frank.plate.Configure;
import com.frank.plate.PlateRecognition;


import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.util.A2bigA;
import com.frank.plate.util.String2Utils;
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


        initOpenCV();
    }

    @Override
    protected void setUpView() {
        tv_title.setText("扫描接单");

        et1.setTransformationMethod(new A2bigA());
        et2.setTransformationMethod(new A2bigA());
        et3.setTransformationMethod(new A2bigA());
        et4.setTransformationMethod(new A2bigA());
        et5.setTransformationMethod(new A2bigA());
        et6.setTransformationMethod(new A2bigA());
        et7.setTransformationMethod(new A2bigA());


        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && TextUtils.isEmpty(et2.getText())) {
                    et2.requestFocus();
                }
            }
        });
        et1.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et1.setText("");
                } else {
                    // 失去焦点
                }
            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && TextUtils.isEmpty(et3.getText())) {
                    et3.requestFocus();
                }
            }
        });
        et2.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et2.setText("");
                } else {
                    // 失去焦点
                }
            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && TextUtils.isEmpty(et4.getText())) {
                    et4.requestFocus();
                }
            }
        });
        et3.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et3.setText("");
                } else {
                    // 失去焦点
                }
            }
        });
        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && TextUtils.isEmpty(et5.getText())) {
                    et5.requestFocus();
                }
            }
        });
        et4.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et4.setText("");
                } else {
                    // 失去焦点
                }
            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && TextUtils.isEmpty(et6.getText())) {
                    et6.requestFocus();
                }
            }
        });
        et5.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et5.setText("");
                } else {
                    // 失去焦点
                }
            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && TextUtils.isEmpty(et7.getText())) {
                    et7.requestFocus();
                }
            }
        });
        et6.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et6.setText("");
                } else {
                    // 失去焦点
                }
            }
        });
        et7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!TextUtils.isEmpty(editable)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et7.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
        et7.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 获得焦点
                    et7.setText("");
                } else {
                    // 失去焦点
                }
            }
        });

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

                if (String2Utils.isNullCarNumber(et1, et2, et3, et4, et5, et6, et7))
                    onQueryByCar();
                else
                    ToastUtils.showToast("请输入正确车牌号码");
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

        try {
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
        } catch (Exception e) {
            Log.e("车牌扫描功能异常", e.toString());
            e.printStackTrace();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
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
        } catch (Exception e) {
            Log.e("车牌扫描功能异常", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onNewFrame(Mat newFrame) {
        try {
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
        } catch (Exception e) {
            Log.e("车牌扫描功能异常", e.toString());
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PlateRecognition.MSG_RESULT://recognize finish
                    String result = (String) msg.obj;
                    car_number = "";


                    try {
                        et1.setText(String.valueOf(result.charAt(0)));
                        et2.setText(String.valueOf(result.charAt(1)));
                        et3.setText(String.valueOf(result.charAt(2)));
                        et4.setText(String.valueOf(result.charAt(3)));
                        et5.setText(String.valueOf(result.charAt(4)));
                        et6.setText(String.valueOf(result.charAt(5)));
                        et7.setText(String.valueOf(result.charAt(6)));

                        car_number = result;

                        ToastUtils.showToast("车牌号=" + car_number);

                    } catch (Exception e) {
                        ToastUtils.showToast(e.getMessage());
                    }
                    break;
            }
        }
    };
}
