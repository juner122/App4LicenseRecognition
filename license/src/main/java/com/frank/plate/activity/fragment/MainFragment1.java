package com.frank.plate.activity.fragment;


import android.view.View;

import com.frank.plate.R;
import com.frank.plate.activity.ActivityPackageListActivity;
import com.frank.plate.activity.BillListActivity;
import com.frank.plate.activity.MainActivity;
import com.frank.plate.activity.MemberManagementActivity;
import com.frank.plate.activity.ProductListActivity;
import com.frank.plate.activity.StaffManagementActivity;

import butterknife.OnClick;

/**
 * 主页页面：工作台
 */

public class MainFragment1 extends BaseFragment {

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment1_main;
    }

    @Override
    protected void setUpView() {




    }


    @OnClick({R.id.but_top1, R.id.but_top2, R.id.but_top3, R.id.but_top4, R.id.rv_button_bill, R.id.rv_order_count, R.id.rv_new_members})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_top1:
                toActivity(MemberManagementActivity.class);
                break;
            case R.id.but_top2:
                toActivity(StaffManagementActivity.class);
                break;
            case R.id.but_top3:
                toActivity(ProductListActivity.class);
                break;
            case R.id.but_top4:
                toActivity(ActivityPackageListActivity.class);
                break;
            case R.id.rv_button_bill:
                toActivity(BillListActivity.class);
                break;
            case R.id.rv_order_count:
                ((MainActivity) getActivity()).setCurrentTab(1);
                break;
            case R.id.rv_new_members:
                toActivity(MemberManagementActivity.class);
                break;

        }

    }

    public static final String TAG = "MainFragment1";
    @Override
    protected String setTAG() {
        return TAG;
    }

}
