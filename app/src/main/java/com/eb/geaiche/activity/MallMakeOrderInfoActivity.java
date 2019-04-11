package com.eb.geaiche.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.eb.geaiche.R;

import butterknife.BindView;

public class MallMakeOrderInfoActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.pay_price)
    TextView pay_price;//实付金额
    @BindView(R.id.reduce_price)
    TextView reduce_price;//优惠金额
    @BindView(R.id.order_price)
    TextView order_price;//订单金额

    @BindView(R.id.pay_status)
    TextView pay_status;//支付状态

    @BindView(R.id.order_sn)
    TextView order_sn;//订单编号

    @BindView(R.id.order_time)
    TextView order_time;//下单时间

    @Override
    protected void init() {

        tv_title.setText("订单详情");
        tv_title_r.setText("待支付");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_make_order_info;
    }
}
