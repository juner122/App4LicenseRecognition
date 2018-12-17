package com.frank.plate.activity.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.activity.MakeOrderSuccessActivity;
import com.frank.plate.activity.OrderDoneActivity;
import com.frank.plate.activity.OrderInfoActivity;
import com.frank.plate.activity.OrderPayActivity;
import com.frank.plate.adapter.OrderListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 主页页面：订单
 */
public class MainFragment2 extends BaseFragment {

    public static final String TAG = "MainFragment2";

    @BindView(R.id.top_num1)
    TextView top_num1;
    @BindView(R.id.top_num2)
    TextView top_num2;

    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;
    private String[] title = {"全部", "已预约", "待服务", "服务中", "已完成"};

    ArrayList<Fragment> fragments = new ArrayList<>();


    @Override
    protected void setUpView() {

        fragments.add(OrderListFragment.newInstance(""));
        fragments.add(OrderListFragment.newInstance("0"));
        fragments.add(OrderListFragment.newInstance("00"));
        fragments.add(OrderListFragment.newInstance("1"));
        fragments.add(OrderListFragment.newInstance("2"));


        stl.setViewPager(vp, title, getActivity(), fragments);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        Api().orderList().subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {

                top_num1.setText(String.valueOf(basePage.getDayTotal()));
                top_num2.setText(String.valueOf(basePage.getMonthTotal()));
            }


            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
            }
        });
    }


    @Override
    protected String setTAG() {
        return TAG;
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment2_main;
    }

}
