package com.eb.geaiche.adapter;


import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.XgxPurchaseOrderPojo;


import java.util.List;

//报价单列表
public class MallOrderListAdapter extends BaseQuickAdapter<XgxPurchaseOrderPojo, BaseViewHolder> {

    Context context;
    MallOrderGoodsListAdapter2 adapter;

    public MallOrderListAdapter(@Nullable List<XgxPurchaseOrderPojo> data, Context c) {
        super(R.layout.activity_mall_order_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, XgxPurchaseOrderPojo item) {
        helper.setText(R.id.order_sn, String.format("订单编号：%s", item.getOrderSn()));
        helper.setText(R.id.tv_pay_status, item.getPayStatus());
        helper.setText(R.id.tv_price, String.format("合计￥：%s", MathUtil.twoDecimal(item.getOrderPrice())));



        String payStatus = item.getPayStatus();//支付状态
        String shipStatus = item.getShipStatus();//发货状态
        TextView tv_pay_status = helper.getView(R.id.tv_pay_status);//订单状态
        TextView tv_pay_button = helper.getView(R.id.tv_pay_button);//订单状态

        adapter = new MallOrderGoodsListAdapter2(item.getXgxPurchaseOrderGoodsPojoList(), context);
        if (payStatus.equals("1")) {
            tv_pay_status.setText("待付款");
            tv_pay_status.setTextColor(Color.parseColor("#4A9DE3"));
            tv_pay_button.setText("在线支付");
            tv_pay_button.setTextColor(Color.parseColor("#ffffff"));
            tv_pay_button.setBackground(context.getResources().getDrawable(R.drawable.button_background_cccc));
            helper.addOnClickListener(R.id.tv_pay_button);
        } else if (payStatus.equals("2") && shipStatus.equals("1")) {
            tv_pay_status.setText("待发货");
            tv_pay_status.setTextColor(Color.parseColor("#FF3900"));
            tv_pay_button.setText("已付款");
            tv_pay_button.setTextColor(Color.parseColor("#999999"));
            tv_pay_button.setBackground(context.getResources().getDrawable(R.drawable.button_background_cccc1));
        } else if (payStatus.equals("2") && shipStatus.equals("2")) {
            tv_pay_status.setText("待收货");
            tv_pay_status.setTextColor(Color.parseColor("#FFBA00"));
            tv_pay_button.setText("确认收货");
            tv_pay_button.setTextColor(Color.parseColor("#ffffff"));
            tv_pay_button.setBackground(context.getResources().getDrawable(R.drawable.button_background_cccc));
            helper.addOnClickListener(R.id.tv_pay_button);
        } else if (shipStatus.equals("3")) {
            tv_pay_status.setText("已收货");
            tv_pay_status.setTextColor(Color.parseColor("#999999"));
            tv_pay_button.setText("已收货");
            tv_pay_button.setTextColor(Color.parseColor("#999999"));
            tv_pay_button.setBackground(context.getResources().getDrawable(R.drawable.button_background_cccc1));
        }

        RecyclerView rv = helper.getView(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);


    }

}
