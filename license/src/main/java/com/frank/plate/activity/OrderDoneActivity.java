package com.frank.plate.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.util.DateUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDoneActivity extends BaseActivity {

    OrderInfo infoEntity;

    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;
    @BindView(R.id.tv_car_no)
    TextView tv_car_no;
    @BindView(R.id.tv_make_date)
    TextView tv_make_date;
    @BindView(R.id.tv_expect_date)
    TextView tv_expect_date;
    @BindView(R.id.tv_order_price)
    TextView tv_order_price;


    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;

    @Override
    protected void init() {
        hideReturnView();
        tv_title.setText("完成订单");


        infoEntity = getIntent().getParcelableExtra("orderInfo");
        tv_order_sn.append(infoEntity.getOrderInfo().getOrder_sn());
        tv_car_no.append(infoEntity.getOrderInfo().getCar_no());
        tv_make_date.append(infoEntity.getOrderInfo().getAdd_time());
        tv_order_price.append(String.format("￥%s", infoEntity.getOrderInfo().getOrder_price()));

        tv_expect_date.append(DateUtil.getFormatedDateTime(infoEntity.getOrderInfo().getPlanfinishi_time()));

        tv_shopName.append(null == infoEntity.getShop().getShopName() ? "-" : infoEntity.getShop().getShopName());
        tv_name.append(null == infoEntity.getShop().getName() ? "-" : infoEntity.getShop().getName());
        tv_phone.append(null == infoEntity.getShop().getPhone() ? "-" : infoEntity.getShop().getPhone());
        tv_address.append(null == infoEntity.getShop().getAddress() ? "-" : infoEntity.getShop().getAddress());


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_done;
    }


    @OnClick({R.id.tv_done})
    public void onClick(View v) {

        Intent intent = new Intent(OrderDoneActivity.this, MainActivity.class);
        intent.putExtra(Configure.show_fragment, 1);
        toActivity(intent);


    }
}
