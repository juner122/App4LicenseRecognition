package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.OrderListItemEntity;

import java.util.List;

public class OrderListAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseViewHolder> {


    public OrderListAdapter(int layoutResId, @Nullable List<OrderInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfoEntity item) {
        helper.setText(R.id.tv_plate_number, item.getCar_no());
        helper.setText(R.id.tv_order_number, item.getOrder_sn());
        helper.setText(R.id.tv_date, item.getAdd_time());
        helper.setText(R.id.tv_order_state, item.getOrder_status_text());
        helper.setText(R.id.tv_money, String.valueOf(item.getOrder_price()));


    }
}
