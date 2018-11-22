package com.frank.plate.activity;

import android.support.v4.app.Fragment;

import com.frank.plate.R;
import com.frank.plate.activity.fragment.MainFragment1;
import com.frank.plate.activity.fragment.MainFragment2;
import com.frank.plate.activity.fragment.MainFragment3;
import com.frank.plate.activity.fragment.MainFragment4;
import com.frank.plate.activity.fragment.MainFragment5;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"工作台", "订单", "接单", "学院", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.icon_bottom_button1_unselect, R.mipmap.icon_bottom_button2_unselect,
            R.mipmap.icon_bottom_button3_unselect, R.mipmap.icon_bottom_button4_unselect, R.mipmap.icon_bottom_button5_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.icon_bottom_button1_select, R.mipmap.icon_bottom_button2_select,
            R.mipmap.icon_bottom_button3_select, R.mipmap.icon_bottom_button4_select, R.mipmap.icon_bottom_button5_select};


    @Override
    protected void init() {
        hideHeadView();

    }

    @Override
    public void setUpView() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new MainFragment1());
        mFragments.add(new MainFragment2());
        mFragments.add(new MainFragment3());
        mFragments.add(new MainFragment4());
        mFragments.add(new MainFragment5());


        commonTabLayout.setTabData(mTabEntities, this, R.id.fragment, mFragments);
        setCurrentTab(0);

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_main;
    }


    class TabEntity implements CustomTabEntity {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }

    public void setCurrentTab(int i) {
        commonTabLayout.setCurrentTab(i);
    }

}
