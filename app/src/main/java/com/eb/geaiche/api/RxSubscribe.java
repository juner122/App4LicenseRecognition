package com.eb.geaiche.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.eb.geaiche.R;
import com.eb.geaiche.util.SystemUtil;

import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

//实现Observer类，把里面用不到的方法这里全处理掉 我们只处理结果 成功/失败
public abstract class RxSubscribe<T> implements Observer<T> {
    private Context mContext;
    private ProgressDialog dialog;
    private String msg;
    private boolean isShow = false;

    public RxSubscribe(Context context) {
        this.mContext = context;
    }

    public RxSubscribe(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
        msg = mContext.getString(R.string.dialog_message_loading);

    }

    public RxSubscribe(Context context, boolean isShow, String text) {
        this.mContext = context;
        this.isShow = isShow;
        msg = text;
    }


    public RxSubscribe(Context context, int msgResId) {
        this.mContext = context;
        isShow = true;
        msg = mContext.getString(msgResId);
    }


    @Override
    public void onComplete() {
        if (isShow) {
            if (dialog != null)
                dialog.dismiss();
        }
    }

    @Override
    public void onSubscribe(@NonNull final Disposable d) {
        if (isShow) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(msg);
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(dialogInterface -> {
                // 对话框取消 直接停止执行请求
                if (!d.isDisposed()) {
                    d.dispose();
                }
            });
            dialog.show();

        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {


        //把底层的一些错误翻译一下用户看不来
        if (e.getMessage() == null || e.getMessage().isEmpty()) {//未知错误
            _onError(mContext.getString(R.string.error_unknown));
        } else if (e.getMessage().contains("Failed to connect to") ||
                e.getMessage().contains("failed to connect to")) {//连接服务器失败
            if (!SystemUtil.isNetworkAvailable(mContext)) {//没网络
                _onError(mContext.getString(R.string.error_bad_network));
            } else {//真的没连接上服务器
                _onError(mContext.getString(R.string.error_connect_server));
            }
        } else if (e.getMessage().contains("timed out") || e.getMessage().contains("timeout")) {
            //超时了
            _onError(mContext.getString(R.string.error_connect_server_timeout));
        } else if (e.getMessage().contains("500")) {
            //服务器内部错误
            _onError(mContext.getString(R.string.error_connect_server_timeout));
        } else {
            if (e instanceof UnknownHostException) {
                _onError("服务器超时，请检查手机网络！");
            } else if (e instanceof NullPointerException) {
                _onError("服务器返回数据有误！");
            }
        }
        _onError(e.getMessage());
        if (isShow) {
            if (dialog != null)
                dialog.dismiss();
        }
    }

    //成功
    protected abstract void _onNext(T t);

    //失败
    protected abstract void _onError(String message);
}