package com.frank.plate.api;

import android.content.Context;
import android.util.Log;

import com.tamic.novate.BaseSubscriber;
import com.tamic.novate.Throwable;


public class MySubscriber<T> extends BaseSubscriber implements ProgressCancelListener {


    private SubscribeOnNextListener mOnNextListener;
    private Context mContext;
    private ProgressHandler mProgressHandler;//加载进度条
    private boolean mUnsubscribable = false;//dialog被cancel掉的时候，是否取消掉事件监听


    public MySubscriber(Context context, SubscribeOnNextListener<T> listener) {
        mOnNextListener = listener;
        mContext = context;
        mProgressHandler = new ProgressHandler(mContext, this, false, "");
    }

    public MySubscriber(Context context, String text, SubscribeOnNextListener<T> listener) {
        mOnNextListener = listener;
        mContext = context;
        mProgressHandler = new ProgressHandler(mContext, this, false, text);
    }

    public MySubscriber(Context context, boolean cancelable, String text, SubscribeOnNextListener<T> listener) {
        mOnNextListener = listener;
        mContext = context;
        mProgressHandler = new ProgressHandler(mContext, this, cancelable, text);
    }

    public MySubscriber(Context context, boolean cancelable, boolean unsubscribable, String text, SubscribeOnNextListener<T> listener) {
        mOnNextListener = listener;
        mContext = context;
        mUnsubscribable = unsubscribable;
        mProgressHandler = new ProgressHandler(mContext, this, cancelable, text);

    }

    private void showProgressDialog() {
        mProgressHandler.initProgressDialog();
    }

    private void dismissProgressDialog() {
        mProgressHandler.dismissProgressDialog();
    }


    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Log.d("MySubscriber", "onError----->" + e.getMessage());
        onCompleted();
        mOnNextListener.onError(e);


    }

    @Override
    public void onNext(Object o) {
        if (mOnNextListener != null) {
            Log.d("MySubscriber", "onNext------>" + o.toString());
            mOnNextListener.onNext(o);
        }
    }

    @Override
    public void onProgressCanceled() {//取消请求
        if (mUnsubscribable && !isUnsubscribed()) {
            unsubscribe();
        }

    }
}
