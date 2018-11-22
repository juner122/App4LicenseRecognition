package com.frank.plate.thread;

import org.opencv.core.Mat;
import java.util.LinkedList;
import com.frank.plate.PlateRecognition;

/**
 * 车牌识别线程
 * Created by frank on 2018/1/7.
 */

public class RecognizeThread extends Thread {
    private PlateRecognition plateRecognition;
    private LinkedList<Mat> matQueue;
    private final static Object lock = new Object();
    private boolean isRunning;

    public RecognizeThread(PlateRecognition plateRecognition){
        this.plateRecognition = plateRecognition;
        matQueue = new LinkedList<>();
        isRunning = true;
    }

    public void addMat(Mat mat){
        synchronized (lock){
            if(matQueue != null){
                matQueue.add(mat);
            }
        }
    }

    public void setRunning(boolean running){
        this.isRunning = running;
    }

    @Override
    public void run() {
        while (isRunning){
            Mat mat = null;
            synchronized (lock){
                if(matQueue != null && matQueue.size() > 0){
                    mat = matQueue.poll();//poll a mat from the queue
                }
            }
            if(mat != null && plateRecognition != null){
                plateRecognition.doPlateRecognize(mat);
            }
        }
    }

}
