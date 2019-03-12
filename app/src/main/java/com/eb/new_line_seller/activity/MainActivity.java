package com.eb.new_line_seller.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.eb.new_line_seller.activity.fragment.MainFragment1New;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.fragment.MainFragment1;
import com.eb.new_line_seller.activity.fragment.MainFragment2;

import com.eb.new_line_seller.activity.fragment.MainFragment4;
import com.eb.new_line_seller.activity.fragment.MainFragment5;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.eb.new_line_seller.activity.fragment.MainFragmentPlate;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.VersionInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"工作台", "", "我的"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mIconUnselectIds = {
            R.mipmap.icon_bottom_button1_unselect,
            R.color.fff, R.mipmap.icon_bottom_button5_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.icon_bottom_button1_select,
            R.color.fff, R.mipmap.icon_bottom_button5_select};

    @OnClick({R.id.ll, R.id.ll2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll:
                toActivity(PreviewActivity2.class);
                break;

        }
    }


    @Override
    protected void init() {
        hideHeadView();
    }

    @Override
    public void setUpView() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new MainFragment1New());
//        mFragments.add(new MainFragment2());
//        mFragments.add(new MainFragment3());
        mFragments.add(new MainFragmentPlate());
//        mFragments.add(new MainFragment4());
        mFragments.add(new MainFragment5());
        commonTabLayout.setTabData(mTabEntities, this, R.id.fragment, mFragments);
        setCurrentTab(0);


    }


    @Override
    protected void setUpData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        int fragment = intent.getIntExtra(Configure.show_fragment, 0);//显示哪个fragment

        setCurrentTab(fragment);
    }


    //检查版本更新
    private void checkVersionUpDate() {

        Api().checkVersionUpDate().subscribe(new RxSubscribe<VersionInfo>(this, false) {
            @Override
            protected void _onNext(VersionInfo versionInfo) {


            }

            @Override
            protected void _onError(String message) {

            }
        });

    }

}
