package com.eb.new_line_seller.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.MealInfoListAdapter;
import com.eb.new_line_seller.bean.MealEntity;

import java.util.List;

import butterknife.BindView;

//套卡录入确认

public class CardInputConfirmDialog extends Dialog {

    private Context context;

    private ClickListenerInterface clickListenerInterface;
    EditText et_code;//卡号

    MealInfoListAdapter mealInfoListAdapter;//套卡商品列表

    RecyclerView rv_meal;
    String mealName;//卡名
    String card_sn;//卡号

    public interface ClickListenerInterface {

        public void doConfirm(String card_sn);

        public void doCancel();
    }

    public CardInputConfirmDialog(Context context, List<MealEntity> mealInfoList, String mealName, String card_sn) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.mealName = mealName;
        this.card_sn = card_sn;

        mealInfoListAdapter = new MealInfoListAdapter(mealInfoList);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_card_input, null);
        setContentView(view);

        et_code = view.findViewById(R.id.et_car_code);
        TextView title = view.findViewById(R.id.title);
        rv_meal = view.findViewById(R.id.rv);


        rv_meal.setLayoutManager(new LinearLayoutManager(context));
        rv_meal.setAdapter(mealInfoListAdapter);


        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);

        title.setText(mealName);
        et_code.setText(card_sn);

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
                    if (TextUtils.isEmpty(et_code.getText())) {
                        Toast.makeText(context, "卡号不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    clickListenerInterface.doConfirm(et_code.getText().toString());
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }
}