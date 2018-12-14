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
import com.frank.plate.activity.ProductListActivity;
import com.frank.plate.activity.StaffManagementActivity;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.WorkIndex;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：工作台
 */

public class MainFragment1 extends BaseFragment {

    @BindView(R.id.price1)
    TextView price1;

    @BindView(R.id.price2)
    TextView price2;

    @BindView(R.id.number1)
    TextView number1;

    @BindView(R.id.number2)
    TextView number2;

    @BindView(R.id.number3)
    TextView number3;

    @BindView(R.id.number4)
    TextView number4;


    @BindView(R.id.iv)
    ImageView iv;


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment1_main;
    }

    @Override
    protected void setUpView() {

    }

    @Override
    public void onResume() {
        super.onResume();

        Api().workIndex().subscribe(new RxSubscribe<WorkIndex>(getContext(), true) {
            @Override
            protected void _onNext(WorkIndex workIndex) {
                price1.setText(String.valueOf(workIndex.getMonthIn()));
                price2.setText(String.valueOf(workIndex.getDayIn()));

                number1.setText(String.valueOf(workIndex.getMonthOrder()));
                number2.setText(String.valueOf(workIndex.getDayOrder()));
                number3.setText(String.valueOf(workIndex.getMonthMember()));
                number4.setText(String.valueOf(workIndex.getDayMember()));

            }

            @Override
            protected void _onError(String message) {
                Log.d(getTag(), message);
            }
        });

        Glide.with(this)
                .load(getResources().getDrawable(R.mipmap.banner1))
                .into(iv);

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
                toActivity(ProductListActivity.class, Configure.isShow, 0);
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
