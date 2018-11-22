package com.frank.plate.activity.fragment;


import android.view.View;

import com.frank.plate.R;
import com.frank.plate.activity.MyBalanceActivity;

import butterknife.OnClick;

/**
 * 主页页面：扫描
 */
public class MainFragment5 extends BaseFragment {

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment5_main;
    }

    @Override
    protected void setUpView() {

    }


    @OnClick({R.id.tv_my_balance})
    public void onclick(View v) {


        switch (v.getId()) {

            case R.id.tv_my_balance:

                toActivity(MyBalanceActivity.class);

                break;


        }


    }

    public static final String TAG = "MainFragment5";
    @Override
    protected String setTAG() {
        return TAG;
    }
}
