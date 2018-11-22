package com.frank.plate.view;

import android.content.Context;
import android.util.AttributeSet;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;
import com.frank.plate.listener.OnNewFrameListener;


/**
 * 自定义view：预览数据回调进程车牌识别
 * Created by frank on 2018/1/7.
 */

public class PlateRecognizerView extends JavaCameraView implements CameraBridgeViewBase.CvCameraViewListener2{

    private long lastRecognizeTime;
    private OnNewFrameListener onNewFrameListener;

    public PlateRecognizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCvCameraViewListener(this);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {
        disableView();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        //每次进行车牌识别间隔3s
        long currentTime = System.currentTimeMillis();
        if((currentTime - lastRecognizeTime) > 3000){
            lastRecognizeTime = currentTime;
            //回调给识车牌别线程处理
            if(onNewFrameListener != null){
                onNewFrameListener.onNewFrame(inputFrame.rgba());
            }
        }
        return inputFrame.rgba();
    }

    public void setOnNewFrameListener(OnNewFrameListener onNewFrameListener){
        this.onNewFrameListener = onNewFrameListener;
    }

}
