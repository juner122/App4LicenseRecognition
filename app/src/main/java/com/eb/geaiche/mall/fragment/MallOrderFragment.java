package com.eb.geaiche.mall.fragment;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.eb.geaiche.R;

import com.eb.geaiche.activity.fragment.BaseFragment;
import com.eb.geaiche.activity.fragment.MallOrderListFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 采购订单列表
 */

public class MallOrderFragment extends BaseFragment {

    private String[] title = {"全部", "待付款", "待发货", "待收货", "已收货"};
    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;

    ArrayList<Fragment> fragments = new ArrayList<>();

    int potint = 0;//位置


    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tv_back)
    protected View tv_back;
    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_list;
    }

    @Override
    protected void setUpView() {
        tv_back.setVisibility(View.INVISIBLE);
        tv_title.setText("采购订单管理");

        fragments.add(MallOrderListFragment.newInstance(0));
        fragments.add(MallOrderListFragment.newInstance(1));
        fragments.add(MallOrderListFragment.newInstance(2));
        fragments.add(MallOrderListFragment.newInstance(3));
        fragments.add(MallOrderListFragment.newInstance(4));
        stl.setViewPager(vp, title, getActivity(), fragments);

        vp.setCurrentItem(potint);
    }

    @Override
    protected String setTAG() {
        return "MallOrderFragment";
    }
}
