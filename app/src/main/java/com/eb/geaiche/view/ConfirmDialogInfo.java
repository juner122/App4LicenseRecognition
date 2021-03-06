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
import android.widget.Toast;

import com.eb.geaiche.R;

//修改备注弹框
public class ConfirmDialogInfo extends Dialog {

    private Context context;

    private ClickListenerInterface clickListenerInterface;
    EditText et_code;

    String info;//修改前的备注

    public interface ClickListenerInterface {

        public void doConfirm(String info);

    }

    public ConfirmDialogInfo(Context context,String info) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.info = info;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_info, null);
        setContentView(view);

        et_code = view.findViewById(R.id.et_car_code);
        et_code.setText(info);

        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);


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



                    clickListenerInterface.doConfirm(et_code.getText().toString());
                    break;
                case R.id.tv_cancel:
                    dismiss();
                    break;
            }
        }

    }
}