package com.eb.geaiche.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.juner.mvp.bean.Coupon;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponWriteSuccessActivity extends BaseActivity {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.prepayid)
    TextView prepayid;//劵号
    @BindView(R.id.timestamp)
    TextView timestamp;//时间

    @OnClick({R.id.back_home})
    public void onClick(View v) {

        toMain(0);
    }

    @Override
    protected void init() {

        tv_title.setText("核销成功");


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

        Coupon coupon = getIntent().getParcelableExtra("coupon");

        name.setText(coupon.getName());
        prepayid.setText(String.format("核销券号：%s", coupon.getCoupon_number()));
        timestamp.setText(String.format("核销时间：%s", coupon.getUsed_time()));

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_write_success;
    }
}
