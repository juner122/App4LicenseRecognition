package com.eb.new_line_seller.mvp;

import android.view.View;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MarketingToolsActivity extends BaseActivity {


    @OnClick({R.id.ll1, R.id.ll2})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll1:

                //短信营销
                break;
            case R.id.ll2:
                //分享营销
                break;

        }
    }


    @Override
    protected void init() {

        tv_title.setText("营销工具");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_marketing_tools;
    }
}
