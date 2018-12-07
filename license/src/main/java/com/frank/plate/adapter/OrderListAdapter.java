package com.frank.plate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.OrderInfoEntity;

import java.util.List;

public class OrderListAdapter extends BaseQuickAdapter<OrderInfoEntity, BaseViewHolder> {

    Context context;


    public OrderListAdapter(int layoutResId, @Nullable List<OrderInfoEntity> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfoEntity item) {


        TextView button_action = helper.getView(R.id.button_action);
        TextView tv_order_state = helper.getView(R.id.tv_order_state);

        helper.setText(R.id.tv_plate_number, item.getCar_no());
        helper.setText(R.id.tv_order_number, String.format("订单号:%s", item.getOrder_sn()));
        helper.setText(R.id.tv_date, item.getAdd_time());
        helper.setText(R.id.tv_order_state, item.getOrder_status_text());
        helper.setText(R.id.tv_money, String.format("￥%s", item.getOrder_price()));
        helper.addOnClickListener(R.id.button_show_details);
        helper.addOnClickListener(R.id.button_action);


        switch (item.getOrder_status()) {
            case 0://待服务

                if (item.getPay_status() == 2) {
                    tv_order_state.setTextColor(Color.parseColor("#F23325"));
                    button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_x));
                    button_action.setText("立即下单");
                    button_action.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    tv_order_state.setTextColor(Color.BLACK);

                    button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_f));
                    button_action.setText("前往下单");
                    button_action.setTextColor(Color.parseColor("#000000"));
                }
                break;
            case 1://服务中

                tv_order_state.setTextColor(Color.BLACK);
                button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_f));
                button_action.setText("完成订单");
                button_action.setTextColor(Color.parseColor("#000000"));
                break;
            case 2://完成
                tv_order_state.setTextColor(Color.parseColor("#999999"));
                button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_o));
                button_action.setText("已完成");
                button_action.setTextColor(Color.parseColor("#666666"));
                break;

        }


        if (item.isSelected()) {
            helper.setGone(R.id.ll_button_view, true);
            ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.icon_down);
        } else {
            helper.setGone(R.id.ll_button_view, false);
            ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.icon_up);
        }

    }


}
