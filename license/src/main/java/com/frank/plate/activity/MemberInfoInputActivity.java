package com.frank.plate.activity;


import android.view.View;

import com.frank.plate.R;

import butterknife.OnClick;

public class MemberInfoInputActivity extends BaseActivity {

    @Override
    protected void init() {
        tv_title.setText("会员信息录入");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_info_input;
    }


    @OnClick({R.id.tv_add_car, R.id.tv_to_car_info, R.id.tv_to_make_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_car:
                toActivity(CarInfoInputActivity.class);
                break;
            case R.id.tv_to_car_info:
                toActivity(CarInfoInputActivity.class);
                break;
            case R.id.tv_to_make_order:
                toActivity(MakeOrderActivity.class);
                break;


        }

    }

}
