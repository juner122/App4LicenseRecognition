package com.frank.plate.listener;

import org.opencv.core.Mat;

/**
 * 预览帧数据回调监听
 * Created by frank on 2018/1/8.
 */

public interface OnNewFrameListener {
    void onNewFrame(Mat newFrame);
}
