package com.eb.geaiche.activity;

import androidx.fragment.app.Fragment;


import com.eb.geaiche.R;
import com.eb.geaiche.mall.fragment.MallCartFragment;
import com.eb.geaiche.mall.fragment.MallMainFragment;
import com.eb.geaiche.mall.fragment.MallOrderFragment;
import com.eb.geaiche.util.MyAppPreferences;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

//商城
public class MallActivity extends BaseActivity {

    public static final String categoryId = "categoryId";
    public static final String goodsTitle = "goodsTitle";
    public static final String VIN = "Vin";

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"工作台", "汽配件", "购物车", "采购单"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private int[] mIconUnselectIds = {
            R.mipmap.icon_mall_b1,
            R.mipmap.icon_mall_b2, R.mipmap.icon_mall_b3, R.mipmap.icon_mall_b4};
    private int[] mIconSelectIds = {
            R.mipmap.icon_mall_b1,
            R.mipmap.icon_mall_b2_on, R.mipmap.icon_mall_b3_on, R.mipmap.icon_mall_b4_on};


    @Override
    protected void init() {

    }

    @Override
    protected void setUpView() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new MallMainFragment());
        mFragments.add(new MallMainFragment());
        mFragments.add(new MallCartFragment());
        mFragments.add(new MallOrderFragment());
        commonTabLayout.setTabData(mTabEntities, this, R.id.fragment, mFragments);


        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    MyAppPreferences.remove(VIN);//退出商城页面时清空车架号
                    finish();

                }
            }

            @Override
            public void onTabReselect(int position) {

                if (position == 0) {
                    MyAppPreferences.remove(VIN);//退出商城页面时清空车架号
                    finish();
                }
            }
        });
        setCurrentTab(1);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_home;
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
