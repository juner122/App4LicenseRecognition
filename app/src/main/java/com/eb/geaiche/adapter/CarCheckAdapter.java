package com.eb.geaiche.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.CarCheckResul;

import java.util.List;

public class CarCheckAdapter extends BaseQuickAdapter<CarCheckResul, BaseViewHolder> {


    public CarCheckAdapter(@Nullable List<CarCheckResul> data) {
        super(R.layout.activity_car_check_result_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarCheckResul item) {
        helper.setText(R.id.tv_sn, item.getResultSn());
        helper.setText(R.id.tv_car_no, item.getCarNo());
        helper.setText(R.id.tv_time, MathUtil.toDate(item.getAddTime()));
        helper.setText(R.id.tv_num, String.valueOf(item.getInfoNum()));
        TextView tv = helper.getView(R.id.rv_is_check);
        if (item.getType() == 0) {
            tv.setText("修改");
            tv.setTextColor(Color.parseColor("#4A9DE3"));
        } else {
            tv.setText("已生成");
            tv.setTextColor(Color.parseColor("#999999"));
        }
    }
}
