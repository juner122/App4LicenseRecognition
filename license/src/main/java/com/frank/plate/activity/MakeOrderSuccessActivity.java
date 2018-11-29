package com.frank.plate.activity;

import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.util.DateUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MakeOrderSuccessActivity extends BaseActivity {

    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;
    @BindView(R.id.tv_car_no)
    TextView tv_car_no;
    @BindView(R.id.tv_make_date)
    TextView tv_make_date;
    @BindView(R.id.tv_expect_date)
    TextView tv_expect_date;

    @BindView(R.id.tv_remarks)
    TextView tv_remarks;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;
    OrderInfo infoEntity;

    @Override
    protected void init() {
        tv_title.setText("下单成功");
        infoEntity = getIntent().getParcelableExtra("orderInfo");

        tv_order_sn.append(infoEntity.getOrderInfo().getOrder_sn());
        tv_car_no.append(infoEntity.getOrderInfo().getCar_no());
        tv_make_date.append(infoEntity.getOrderInfo().getAdd_time());
        tv_expect_date.append("");
        tv_remarks.append("");
        tv_shopName.append(infoEntity.getShop().getShopName());
        tv_name.append(infoEntity.getShop().getName());
        tv_phone.append(infoEntity.getShop().getPhone());
        tv_address.append(infoEntity.getShop().getAddress());

    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_make_order_success;
    }

    @OnClick({R.id.tv_now_pay, R.id.tv_start_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_now_pay:
                toActivity(OrderPayActivity.class);
                break;
            case R.id.tv_start_service:
                finish();
                break;


        }

    }


}
