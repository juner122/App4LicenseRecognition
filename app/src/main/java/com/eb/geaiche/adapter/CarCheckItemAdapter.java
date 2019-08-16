package com.eb.geaiche.adapter;

import android.graphics.Color;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.CheckOptions;

import java.util.List;


//每一项检查细节
public class CarCheckItemAdapter extends BaseQuickAdapter<CheckOptions, BaseViewHolder> {

    boolean is;


    public CarCheckItemAdapter(@Nullable List<CheckOptions> data, boolean isFix) {
        super(R.layout.activity_car_check_result_item, data);
        is = isFix;
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckOptions item) {


        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_info, item.getDescribe());


        ImageView s = helper.getView(R.id.iv);
        TextView tv_is = helper.getView(R.id.tv_is);

        EditText tv_info = helper.getView(R.id.tv_info);//可编辑的文字


        if (item.getSelected() == 1) {
            tv_is.setText("正常");
            s.setImageResource(R.mipmap.icon_car_chick2);
            tv_is.setTextColor(Color.parseColor("#333333"));
            tv_info.setFocusableInTouchMode(false);
            tv_info.setFocusable(false);

        } else {
            s.setImageResource(R.mipmap.icon_car_chick1);
            tv_is.setText("异常");
            tv_is.setTextColor(Color.parseColor("#FF3900"));
            tv_info.setFocusableInTouchMode(true);
            tv_info.setFocusable(true);

        }
        if (is) {
            s.setVisibility(View.VISIBLE);
            tv_is.setVisibility(View.INVISIBLE);
        } else {
            s.setVisibility(View.INVISIBLE);
            tv_is.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        EditText tv_info = holder.getView(R.id.tv_info);//可编辑的文字


        tv_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getData().get(position).setDescribe(s.toString());
            }
        });

    }
}
