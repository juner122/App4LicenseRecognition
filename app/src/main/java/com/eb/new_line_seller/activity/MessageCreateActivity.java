package com.eb.new_line_seller.activity;


import com.eb.new_line_seller.R;

import butterknife.OnClick;

public class MessageCreateActivity extends BaseActivity {

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_message_create;
    }

    @OnClick()


    @Override
    protected void init() {

        tv_title.setText("短信模板新建");

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }
}
