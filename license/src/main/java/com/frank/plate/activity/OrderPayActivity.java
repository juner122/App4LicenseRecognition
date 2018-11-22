package com.frank.plate.activity;


import android.view.View;

import com.frank.plate.R;

import butterknife.OnClick;

public class OrderPayActivity extends BaseActivity {

    @Override
    protected void init() {
        tv_title.setText("订单收款");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_pay;
    }



    @OnClick({R.id.tv_enter_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_enter_pay:
                toActivity(OrderDoneActivity.class);
                break;

        }

    }

}
