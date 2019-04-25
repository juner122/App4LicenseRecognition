package com.eb.geaiche.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.View;
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
        helper.setText(R.id.tv_info, item.getDescribe());

        ImageView s = helper.getView(R.id.iv);
        TextView tv_is = helper.getView(R.id.tv_is);

        if (item.getSelected() == 1) {
            tv_is.setText("正常");
            s.setImageResource(R.mipmap.icon_car_chick2);
            tv_is.setTextColor(Color.parseColor("#333333"));
        } else {
            s.setImageResource(R.mipmap.icon_car_chick1);
            tv_is.setText("异常");
            tv_is.setTextColor(Color.parseColor("#FF3900"));
        }
        if (is) {
            s.setVisibility(View.VISIBLE);
            tv_is.setVisibility(View.INVISIBLE);
        } else {
            s.setVisibility(View.INVISIBLE);
            tv_is.setVisibility(View.VISIBLE);
        }

    }

}
