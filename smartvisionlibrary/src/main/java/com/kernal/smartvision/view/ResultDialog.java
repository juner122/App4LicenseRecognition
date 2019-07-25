package com.kernal.smartvision.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kernal.smartvision.R;
import com.kernal.smartvision.utils.Utills;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by WenTong on 2018/12/21.
 */

/**
 * 自定义 dialog
 */
public class ResultDialog extends Dialog {

    private Button feedBack;//确定按钮
    private Button cancel;//取消按钮
    private ImageView imageView;
    private TextView titleTv;
    private EditText messageTv;
    private String titleStr;
    private String messageStr;
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
    private String picPath;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public ResultDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_dialog_view);
        setCanceledOnTouchOutside(true);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        Utills.initImageLoader(getContext().getApplicationContext());
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
            messageTv.setSelection(messageStr.length());
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            feedBack.setText(yesStr);
        }
        if (noStr != null) {
            cancel.setText(noStr);
        }
        if (picPath != null){
            ImageLoader.getInstance().displayImage(picPath,imageView);
        }
    }
    /**
     * 初始化界面控件
     */
    private void initView() {
        feedBack = (Button) findViewById(R.id.feedBack);
        cancel = (Button) findViewById(R.id.cancel);
        titleTv = (TextView) findViewById(R.id.title);
        messageTv = (EditText) findViewById(R.id.message);
        imageView = (ImageView) findViewById(R.id.dialog_image);
    }

    /**
     * 从外界Activity为Dialog设置标题
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     * @param message
     */
    public void setResult(String message) {
        messageStr = message;
    }

    public void setImage(String path){
         picPath = "file:/"+ path;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
}
