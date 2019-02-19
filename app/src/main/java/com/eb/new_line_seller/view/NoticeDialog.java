package com.eb.new_line_seller.view;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.util.ToastUtils;


//通知客户
public class NoticeDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private ClickListenerInterface clickListenerInterface;

    TextView tv_phone;
    String phone;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_button1://短信

                ToastUtils.showToast("开发中");

                break;
            case R.id.tv_button2://呼叫
                callPhone(phone);
                break;
        }
    }


    public interface ClickListenerInterface {
        public void doCancel();
    }

    public NoticeDialog(Context context, String phone) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.phone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_notice, null);
        setContentView(view);

        TextView tv_cancel = view.findViewById(R.id.tv_cancel);

        tv_phone = view.findViewById(R.id.tv_button2);
        tv_phone.setText("呼叫:" + phone);

        tv_phone.setOnClickListener(this);
        view.findViewById(R.id.tv_button1).setOnClickListener(this);

        tv_cancel.setOnClickListener(new clickListener());


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }
    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}