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

        if (null != item.getOs_type()) {
            helper.setText(R.id.tv_os_type, item.getOs_type() == 0 ? "销售订单" : "服务订单");
            helper.setGone(R.id.tv_os_type, true);
        } else
            helper.setGone(R.id.tv_os_type, false);
    }


}
