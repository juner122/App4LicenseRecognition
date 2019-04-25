package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.List;

public class OrderOfTchnicianAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseViewHolder> {


    public OrderOfTchnicianAdapter(@Nullable List<OrderInfoEntity> data) {
        super(R.layout.activity_technician_info_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfoEntity item) {


        helper.setText(R.id.tv_car_no, item.getCar_no());
        helper.setText(R.id.tv_price, String.format("%s元", MathUtil.twoDecimal(item.getActual_price())));
        helper.setText(R.id.tv_data, item.getConfirm_time());
//        helper.setText(R.id.tv_performance, item.getOrder_status_text());

    }


}
