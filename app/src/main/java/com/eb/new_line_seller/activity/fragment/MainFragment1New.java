package com.eb.new_line_seller.activity.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.CollegeActivity;
import com.eb.new_line_seller.activity.MemberManagementActivity;
import com.eb.new_line_seller.activity.MyBalanceActivity;
import com.eb.new_line_seller.activity.OrderListActivity;
import com.eb.new_line_seller.activity.ProductListActivity;
import com.eb.new_line_seller.activity.StaffManagementActivity;
import com.eb.new_line_seller.adapter.MuneButAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.mvp.ActivateCardActivity;
import com.eb.new_line_seller.mvp.FixInfoListActivity;
import com.eb.new_line_seller.mvp.MarketingToolsActivity;
import com.eb.new_line_seller.mvp.MessageMarketingActivity;
import com.eb.new_line_seller.util.SystemUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.google.gson.Gson;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.Shop;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.juner.mvp.Configure.shop_address;
import static com.juner.mvp.Configure.shop_name;
import static com.juner.mvp.Configure.shop_phone;
import static com.juner.mvp.Configure.shop_user_name;

/**
 * 主页页面：工作台
 */

public class MainFragment1New extends BaseFragment {


    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    List<AppMenu> list;

    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.rv2)
    RecyclerView rv2;
    @BindView(R.id.rv3)
    RecyclerView rv3;
    @BindView(R.id.rv4)
    RecyclerView rv4;
    @BindView(R.id.rv5)
    RecyclerView rv5;

    MuneButAdapter muneButAdapter1;
    MuneButAdapter muneButAdapter2;
    MuneButAdapter muneButAdapter3;
    MuneButAdapter muneButAdapter4;
    MuneButAdapter muneButAdapter5;


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment1_main_new;
    }

    @Override
    protected void setUpView() {
        Glide.with(this)
                .load(getResources().getDrawable(R.mipmap.banner2))
                .into(iv);

        AppMenu[] array = new Gson().fromJson(new AppPreferences(getContext()).getString("AppMenu", ""), AppMenu[].class);
        list = Arrays.asList(array);


        muneButAdapter1 = new MuneButAdapter(list.get(0).getList(), getActivity());
        muneButAdapter2 = new MuneButAdapter(list.get(1).getList(), getActivity());
        muneButAdapter3 = new MuneButAdapter(list.get(2).getList(), getActivity());
        muneButAdapter4 = new MuneButAdapter(list.get(3).getList(), getActivity());
        muneButAdapter5 = new MuneButAdapter(list.get(4).getList(), getActivity());















        rv1.setLayoutManager(new GridLayoutManager(getContext(), 4){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv2.setLayoutManager(new GridLayoutManager(getContext(), 4){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv3.setLayoutManager(new GridLayoutManager(getContext(), 4){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv4.setLayoutManager(new GridLayoutManager(getContext(), 4){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv5.setLayoutManager(new GridLayoutManager(getContext(), 4){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });

        rv1.setAdapter(muneButAdapter1);
        rv2.setAdapter(muneButAdapter2);
        rv3.setAdapter(muneButAdapter3);
        rv4.setAdapter(muneButAdapter4);
        rv5.setAdapter(muneButAdapter5);

    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();

    }

    @OnClick({R.id.but_top1, R.id.but_top2, R.id.but_top3, R.id.but_top4, R.id.but_top5, R.id.but_top6, R.id.but_top7, R.id.but_top8, R.id.but_top9, R.id.but_top10, R.id.but_top11, R.id.but_top12, R.id.but_top13, R.id.but_top14, R.id.but_top15})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_top1:
                new AppPreferences(getContext()).put(Configure.car_no, "");

                toActivity(MemberManagementActivity.class);
                break;
            case R.id.but_top2:
                toActivity(StaffManagementActivity.class);
                break;
            case R.id.but_top3:
//                toActivity(ProductListActivity.class, Configure.isShow, 0);
                toActivity(ActivateCardActivity.class);
                break;
            case R.id.but_top4:
                ToastUtils.showToast("开发中");
//               服务管理
                break;

            case R.id.but_top5:
//                ToastUtils.showToast("开发中");
                //订单管理
                toActivity(OrderListActivity.class);
                break;
            case R.id.but_top6:
//                toActivity(ActivateCardActivity.class);
                toActivity(FixInfoListActivity.class);
                break;
            case R.id.but_top7:
//                ToastUtils.showToast("开发中");
                toActivity(MyBalanceActivity.class);

                break;
            case R.id.but_top8:
                toActivity(MarketingToolsActivity.class);

                //分享
                break;
            case R.id.but_top9:
                //短信营销
                toActivity(MessageMarketingActivity.class);
                break;
            case R.id.but_top10:
                ToastUtils.showToast("开发中");
                //活动管理
                break;
            case R.id.but_top11:
//                ToastUtils.showToast("开发中");
                toActivity(CollegeActivity.class);
                //在线课堂
                break;
            case R.id.but_top12:
                ToastUtils.showToast("开发中");
                //线下沙龙
                break;
            case R.id.but_top13:
                ToastUtils.showToast("开发中");
                //汽修招聘
                break;
            case R.id.but_top14:
//                ToastUtils.showToast("开发中");
                //云购商城
                toActivity(ProductListActivity.class, Configure.isShow, 0);

                break;
            case R.id.but_top15:
                ToastUtils.showToast("开发中");
                //库存管理
                break;


        }

    }

    public static final String TAG = "MainFragment1New";

    @Override
    protected String setTAG() {
        return TAG;
    }

    private void getInfo() {
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(getContext(), true) {
            @Override
            protected void _onNext(Shop shop) {
                tv_shopName.setText(shop.getShop().getShopName());
                new AppPreferences(getContext()).put(shop_name, shop.getShop().getShopName());
                new AppPreferences(getContext()).put(shop_address, shop.getShop().getAddress());
                new AppPreferences(getContext()).put(shop_phone, shop.getShop().getPhone());
                new AppPreferences(getContext()).put(shop_user_name, shop.getShop().getName());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, getActivity());
            }
        });
    }

}