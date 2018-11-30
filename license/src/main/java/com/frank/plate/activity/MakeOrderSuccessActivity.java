package com.frank.plate.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.Technician;
import com.frank.plate.util.DateUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

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
        hideReturnView();
        showRView("打印凭证");
        tv_title.setText("下单成功");
        infoEntity = getIntent().getParcelableExtra("orderInfo");

        tv_order_sn.append(infoEntity.getOrderInfo().getOrder_sn());
        tv_car_no.append(infoEntity.getOrderInfo().getCar_no());
        tv_make_date.append(infoEntity.getOrderInfo().getAdd_time());
        tv_expect_date.append(DateUtil.getFormatedDateTime(infoEntity.getOrderInfo().getPlanfinishi_time()));
        tv_remarks.setText(infoEntity.getOrderInfo().getPostscript());


//        tv_shopName.append(null == infoEntity.getShop().getShopName() ? "-" : infoEntity.getShop().getShopName());
//        tv_name.append(null == infoEntity.getShop().getName() ? "-" : infoEntity.getShop().getName());
//        tv_phone.append(null == infoEntity.getShop().getPhone() ? "-" : infoEntity.getShop().getPhone());
//        tv_address.append(null == infoEntity.getShop().getAddress() ? "-" : infoEntity.getShop().getAddress());

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
                Intent intent = new Intent(MakeOrderSuccessActivity.this, MainActivity.class);
                intent.putExtra(Configure.show_fragment, 1);
                toActivity(intent);

                break;


        }

    }


}
