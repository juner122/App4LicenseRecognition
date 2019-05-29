package com.eb.geaiche.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.util.ToastUtils;


public class ConfirmDialog extends Dialog {

    private Context context;
    private String name;
    private String mobile;
    private ClickListenerInterface clickListenerInterface;
    EditText tv_name;

    public interface ClickListenerInterface {

        public void doConfirm(String new_name);

        public void doCancel();
    }

    public ConfirmDialog(Context context, String name, String mobile) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.name = name;
        this.mobile = mobile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_check_user_name, null);
        setContentView(view);

        tv_name = (EditText) view.findViewById(R.id.tv_2);
        TextView tv_mobile = (TextView) view.findViewById(R.id.tv_1);

        tv_name.setText(name);
        tv_mobile.append(mobile);


        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);


        tv_confirm.setOnClickListener(new clickListener());
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
                case R.id.tv_confirm:
                    if (TextUtils.isEmpty(tv_name.getText())) {
                        ToastUtils.showToast("用户名不能为空");
                        return;
                    }
                    clickListenerInterface.doConfirm(tv_name.getText().toString());
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }
}