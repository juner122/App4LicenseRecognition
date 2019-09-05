package com.eb.geaiche.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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


public class ConfirmDialogEt extends Dialog {

    private Context context;

    private ClickListenerInterface clickListenerInterface;

    EditText tv_text;

    TextView e_title;
    String title, hint;

    public interface ClickListenerInterface {

        void doConfirm(String str);
    }

    public ConfirmDialogEt(Context context) {
        super(context, R.style.my_dialog);
        this.context = context;
        title = "提问/反馈";
        hint = "亲，有什么建议或问题请尽情提出来吧，我们会尽快给您回复。";
    }

    public ConfirmDialogEt(Context context, String title, String hint) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.title = title;
        this.hint = hint;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_et, null);
        setContentView(view);

        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        tv_text = view.findViewById(R.id.tv_text);

        tv_text.setHint(hint);
        e_title = view.findViewById(R.id.title);
        e_title.setText(title);


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
            int id = v.getId();
            switch (id) {
                case R.id.tv_confirm:
                    if (TextUtils.isEmpty(tv_text.getText())) {
                        ToastUtils.showToast("内容不能为空！");
                        return;
                    }

                    clickListenerInterface.doConfirm(tv_text.getText().toString());
                    break;
                case R.id.tv_cancel:
                    dismiss();
                    break;
            }
        }

    }
}