package com.frank.plate.activity.fragment;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.activity.ActivityPackageListActivity;
import com.frank.plate.activity.BillListActivity;
import com.frank.plate.activity.MainActivity;
import com.frank.plate.activity.MemberManagementActivity;
import com.frank.plate.activity.PreviewActivity;
import com.frank.plate.activity.ProductListActivity;
import com.frank.plate.activity.StaffManagementActivity;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.WorkIndex;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：扫描接单
 */

public class MainFragmentPlate extends BaseFragment {
    public static final String TAG = "MainFragmentPlate";

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_p_main;
    }

    @Override
    protected void setUpView() {


    }


    @Override
    protected void onVisible() {
        super.onVisible();
    }

    @Override
    protected String setTAG() {
        return null;
    }
}
