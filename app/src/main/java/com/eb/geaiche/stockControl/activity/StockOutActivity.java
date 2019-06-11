package com.eb.geaiche.stockControl.activity;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;

public class StockOutActivity extends BaseActivity {


    @Override
    protected void init() {

        tv_title.setText("领料出库");

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_out;
    }
}
