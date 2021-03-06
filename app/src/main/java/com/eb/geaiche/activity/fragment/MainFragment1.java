package com.eb.geaiche.activity.fragment;


import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.activity.OrderList4DayActivity;
import com.eb.geaiche.mvp.ActivateCardActivity;
import com.eb.geaiche.mvp.FixInfoListActivity;
import com.eb.geaiche.mvp.MarketingToolsActivity;
import com.eb.geaiche.util.SystemUtil;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BillListActivity;
import com.eb.geaiche.activity.MainActivity;
import com.eb.geaiche.activity.MemberManagementActivity;
import com.eb.geaiche.activity.ProductListActivity;
import com.eb.geaiche.activity.StaffManagementActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.WorkIndex;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

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
        Glide.with(this)
                .load(getResources().getDrawable(R.mipmap.banner3))
                .into(iv);
        new AppPreferences(getContext()).put(Configure.car_no, "");
    }

    @Override
    public void onResume() {
        super.onResume();

        Api().workIndex().subscribe(new RxSubscribe<WorkIndex>(getContext(), true) {
            @Override
            protected void _onNext(WorkIndex workIndex) {
                price1.setText(MathUtil.twoDecimal(workIndex.getMonthIn()));
                price2.setText(MathUtil.twoDecimal(workIndex.getDayIn()));

                number1.setText(String.valueOf(workIndex.getMonthOrder()));
                number2.setText(String.valueOf(workIndex.getDayOrder()));
                number3.setText(String.valueOf(workIndex.getMonthMember()));
                number4.setText(String.valueOf(workIndex.getDayMember()));

            }

            @Override
            protected void _onError(String message) {
                Log.d(getTag(), message);
                ToastUtils.showToast(message);

                //判断是否是401 token失效
                SystemUtil.isReLogin(message,getActivity());

            }
        });



    }

    @OnClick({R.id.but_top1, R.id.but_top2, R.id.but_top3, R.id.but_top4, R.id.but_top5, R.id.but_top6, R.id.but_top7, R.id.but_top8, R.id.rv_button_bill, R.id.rv_order_count, R.id.rv_new_members, R.id.ll_moon, R.id.ll_day})
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
                ToastUtils.showToast("开发中");
//                toActivity(ActivityPackageListActivity.class);
                break;

            case R.id.but_top5:
//                ToastUtils.showToast("开发中");
                toActivity(FixInfoListActivity.class);
                break;
            case R.id.but_top6:
                toActivity(ActivateCardActivity.class);
                break;
            case R.id.but_top7:
//                ToastUtils.showToast("开发中");
                toActivity(MarketingToolsActivity.class);

                break;
            case R.id.but_top8:
                ToastUtils.showToast("开发中");
                break;

            case R.id.rv_button_bill:
                toActivity(BillListActivity.class, Configure.isShow, 0);
                break;
            case R.id.rv_order_count:
                ((MainActivity) getActivity()).setCurrentTab(1);


                break;
            case R.id.rv_new_members:
                toActivity(MemberManagementActivity.class);
                break;

            case R.id.ll_day:
                toActivity(OrderList4DayActivity.class, "type", 0);
                break;

            case R.id.ll_moon:
                toActivity(OrderList4DayActivity.class, "type", 1);
                break;

        }

    }

    public static final String TAG = "MainFragment1";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
