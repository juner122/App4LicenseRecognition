package com.eb.geaiche.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.List;

public class OrderList2Adapter extends BaseQuickAdapter<OrderInfoEntity, BaseViewHolder> {


    public OrderList2Adapter( @Nullable List<OrderInfoEntity> data) {
        super(R.layout.activity_member_management_member_info_order_history_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfoEntity item) {



        TextView tv_order_state = helper.getView(R.id.tv_order_state);

        helper.setText(R.id.tv_plate_number, item.getCar_no());
        helper.setText(R.id.tv_order_number, String.format("订单号:%s", item.getOrder_sn()));
        helper.setText(R.id.tv_date, item.getAdd_time());
        helper.setText(R.id.tv_order_state, item.getOrder_status_text());
        helper.setText(R.id.tv_money, String.format("￥%s", MathUtil.twoDecimal(item.getOrder_price())));


        switch (item.getOrder_status()) {
            case 0://待服务
                if (item.getPay_status() == 2) {
                    tv_order_state.setTextColor(Color.parseColor("#F23325"));

                } else {
                    tv_order_state.setTextColor(Color.BLACK);

                }
                break;
            case 1://服务中

                tv_order_state.setTextColor(Color.BLACK);

                break;
            case 2://完成
                tv_order_state.setTextColor(Color.parseColor("#999999"));

                break;

        }

    }



}
