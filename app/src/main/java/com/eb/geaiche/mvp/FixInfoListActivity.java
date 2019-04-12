package com.eb.geaiche.mvp;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.OrderForFixActivity;
import com.eb.geaiche.activity.OrderList4DayActivity;
import com.eb.geaiche.activity.OrderSearch;
import com.eb.geaiche.activity.fragment.FixInfoListFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class FixInfoListActivity extends BaseActivity {
    private String[] title = {"全部", "待报价", "待确认", "已确认", "已出单"};

    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;

    ArrayList<Fragment> fragments = new ArrayList<>();


    @OnClick({R.id.tv_iv_r})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_iv_r://搜索
                toActivity(OrderForFixActivity.class);
                break;
        }
    }

    @Override
    protected void init() {
        tv_title.setText("汽车检修单");
    }

    @Override
    protected void setUpView() {
        fragments.add(FixInfoListFragment.newInstance(-1));
        fragments.add(FixInfoListFragment.newInstance(0));
        fragments.add(FixInfoListFragment.newInstance(2));
        fragments.add(FixInfoListFragment.newInstance(3));
        fragments.add(FixInfoListFragment.newInstance(4));


        stl.setViewPager(vp, title, this, fragments);
        showIVR();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_list;
    }
}
