package com.eb.new_line_seller.mvp;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.BaseActivity;
import com.eb.new_line_seller.activity.fragment.FixInfoListFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class FixInfoListActivity extends BaseActivity {
    private String[] title = {"全部", "未确认", "已确认"};

    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;

    ArrayList<Fragment> fragments = new ArrayList<>();


    @Override
    protected void init() {
        tv_title.setText("汽车检修单");
    }

    @Override
    protected void setUpView() {
        fragments.add(FixInfoListFragment.newInstance(0));
        fragments.add(FixInfoListFragment.newInstance(1));
        fragments.add(FixInfoListFragment.newInstance(2));


        stl.setViewPager(vp, title, this, fragments);

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_list;
    }
}
