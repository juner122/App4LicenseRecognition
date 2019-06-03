package com.eb.geaiche.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.fragment.MallOrderListFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.juner.mvp.Configure;

import java.util.ArrayList;

import butterknife.BindView;

public class MallOrderAllListActivity extends BaseActivity {

    private String[] title = {"全部", "待付款", "待发货", "待收货", "已收货"};
    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;

    ArrayList<Fragment> fragments = new ArrayList<>();

    int potint = 0;//位置

    @Override
    protected void init() {

        tv_title.setText("采购订单管理");

        potint = getIntent().getIntExtra("potint",0);
    }

    @Override
    protected void setUpView() {
        fragments.add(MallOrderListFragment.newInstance(0));
        fragments.add(MallOrderListFragment.newInstance(1));
        fragments.add(MallOrderListFragment.newInstance(2));
        fragments.add(MallOrderListFragment.newInstance(3));
        fragments.add(MallOrderListFragment.newInstance(4));
        stl.setViewPager(vp, title, this, fragments);


    }

    @Override
    protected void setUpData() {

        vp.setCurrentItem(potint);


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_list;
    }
}
