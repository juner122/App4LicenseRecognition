package com.frank.plate.api;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.frank.plate.view.CommonLoadingDialog;

public class ProgressHandler extends Handler {
    public static final int SHOW_PROGRESS = 0;
    public static final int DISMISS_PROGRESS = 1;
    private CommonLoadingDialog mDialog;
    private Context mContext;
    private ProgressCancelListener mProgressCancelListener;
    private boolean cancelable;
    private String mText;

    public ProgressHandler(Context context, ProgressCancelListener listener, boolean cancelable, String text){
        this.mContext = context;
        mProgressCancelListener = listener;
        this.cancelable = cancelable;
        this.mText = text;
    }

    public void initProgressDialog(){
        if(mDialog == null){
            mDialog = new CommonLoadingDialog(mContext);
            mDialog.setCancelable(cancelable);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(mText);
            if(cancelable){
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressCancelListener.onProgressCanceled();
                    }
                });
            }
            if(!mDialog.isShowing()){
                mDialog.show();//显示进度条
            }
        }
    }

    public void dismissProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();//取消进度条
            mDialog = null;
        }
    }

}
