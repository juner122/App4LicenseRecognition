package com.frank.plate.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.bean.GoodsSpec;

import java.util.List;


public class SpecConfirmDialog extends Dialog {

    private Context context;
    private String product_name;
    private ClickListenerInterface clickListenerInterface;
    List<GoodsSpec> goodsSpecs;


    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public SpecConfirmDialog(Context context, String product_name, List<GoodsSpec> goodsSpecs) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.product_name = product_name;
        this.goodsSpecs = goodsSpecs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_pick_spec, null);
        setContentView(view);

        TextView tv_product_name = view.findViewById(R.id.product_name);
//        RecyclerView recyclerView = view.findViewById(R.id.rv);
        RadioGroup rg = view.findViewById(R.id.rg);


//        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));














        tv_product_name.setText(product_name);
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
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }
}