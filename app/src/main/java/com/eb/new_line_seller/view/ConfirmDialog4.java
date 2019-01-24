package com.eb.new_line_seller.view;


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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.util.ToastUtils;

import java.math.BigDecimal;


public class ConfirmDialog4 extends Dialog implements View.OnClickListener {

    private Context context;

    private ClickListenerInterface clickListenerInterface;
    EditText et1;//调整价
    EditText et2;//折扣价
    TextView tv_num;

    ImageView iv_reduce;
    ImageView iv_plus;

    String price;//原价
    int num;//数量
    boolean isChange;//是否能改变数量

    @Override
    public void onClick(View view) {

        if (!isChange) {
            ToastUtils.showToast("工时服务不能修改数量");
            return;
        }
        switch (view.getId()) {

            case R.id.iv_reduce:
                if (num == 1) return;
                tv_num.setText(String.valueOf(--num));
                break;

            case R.id.iv_plus:
                tv_num.setText(String.valueOf(++num));
                break;


        }


    }

    public interface ClickListenerInterface {

        public void doConfirm(String price, int num);

        public void doCancel();
    }

    public ConfirmDialog4(Context context, String price, int num, boolean isChange) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.price = price;
        this.num = num;
        this.isChange = isChange;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog4, null);
        setContentView(view);

        et1 = view.findViewById(R.id.et1);
        et2 = view.findViewById(R.id.et2);

        et1.addTextChangedListener(new DecimalInputTextWatcher(et1, 7, 2));//限制输入位数：整数3位，小数点后两位


        iv_reduce = view.findViewById(R.id.iv_reduce);
        iv_plus = view.findViewById(R.id.iv_plus);
        iv_reduce.setOnClickListener(this);
        iv_plus.setOnClickListener(this);

        tv_num = view.findViewById(R.id.tv_num);
        tv_num.setText(String.valueOf(num));

        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);

        TextView p = view.findViewById(R.id.price);//原价
        p.setText("￥" + price);


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
                    String chengeprice = et1.getText().toString();



                    if (TextUtils.isEmpty(et1.getText())) {
                        chengeprice = price;
                    }


                    clickListenerInterface.doConfirm(chengeprice, num);
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }
}