package com.eb.geaiche.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.List;

public class OrderList3Adapter extends BaseQuickAdapter<OrderInfoEntity, BaseViewHolder> {

    Context context;


    public OrderList3Adapter(@Nullable List<OrderInfoEntity> data, Context context) {
        super(R.layout.item_fragment3_main, data);
        this.context = context;

    }


    @Override
    protected void convert(BaseViewHolder helper, OrderInfoEntity item) {


        TextView button_action = helper.getView(R.id.button_action);
        TextView tv_order_state = helper.getView(R.id.tv_order_state);

        helper.setText(R.id.tv_plate_number, item.getCar_no());
        helper.setText(R.id.tv_order_number, String.format("订单号:%s", item.getOrder_sn()));
        helper.setText(R.id.tv_date, item.getAdd_time());
//        helper.setText(R.id.tv_order_state, item.getOrder_status_text());

        if (item.getPay_status() == 0)
            helper.setText(R.id.tv_money, String.format("￥%s", MathUtil.twoDecimal(item.getOrder_price())));
        else
            helper.setText(R.id.tv_money, String.format("￥%s", MathUtil.twoDecimal(item.getActual_price())));

        helper.addOnClickListener(R.id.button_show_details);
        helper.addOnClickListener(R.id.button_action);//查看绩效分配


        if (null != item.getDeduction_status() && item.getDeduction_status().equals("1")) {
            button_action.setText("已分配");
            tv_order_state.setText("已分配");
            button_action.setTextColor(Color.parseColor("#ffffff"));
            button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_b));
        } else {

            button_action.setText("绩效分配");
            tv_order_state.setText("绩效分配");
            button_action.setTextColor(Color.parseColor("#4A9DE3"));
            button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_bbb));
        }


//        switch (item.getOrder_status()) {
//            case 0://待服务
//
//                if (item.getPay_status() == 2) {
//                    tv_order_state.setTextColor(Color.parseColor("#F23325"));
//                    button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
////                    button_action.setText("开始服务");
//                    button_action.setTextColor(Color.parseColor("#FF3900"));
//                } else {
//                    tv_order_state.setTextColor(Color.BLACK);
//
//                    button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
////                    button_action.setText("前往下单");
//                    button_action.setTextColor(Color.parseColor("#FF3900"));
//                }
//                break;
//            case 1://服务中
//
//                tv_order_state.setTextColor(Color.BLACK);
//                button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
////                button_action.setText("完成订单");
//                button_action.setTextColor(Color.parseColor("#FF3900"));
//                break;
//            case 2://完成
//                tv_order_state.setTextColor(Color.parseColor("#999999"));
//                button_action.setBackground(context.getResources().getDrawable(R.drawable.button_background_f));
////                button_action.setText("已完成");
//                button_action.setTextColor(Color.parseColor("#666666"));
//                break;
//
//        }

        ImageView imageView = (helper.getView(R.id.iv_icon));

        if (item.isSelected()) {
            helper.setGone(R.id.ll_button_view, true);

            imageView.setImageResource(R.mipmap.icon_up);
        } else {

            helper.setGone(R.id.ll_button_view, false);

            imageView.setImageResource(R.mipmap.icon_down);


        }

    }


}
