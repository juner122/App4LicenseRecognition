package com.eb.new_line_seller.activity.fragment;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.OrderNewsListActivity;
import com.eb.new_line_seller.adapter.MuneButAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.util.SystemUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.google.gson.Gson;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.Banner;
import com.juner.mvp.bean.Shop;

import net.grandcentrix.tray.AppPreferences;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：工作台
 */

public class MainFragment1New extends BaseFragment {


    @BindView(R.id.iv)
    ImageView iv;
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


    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.ll5)
    LinearLayout ll5;

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


        muneButAdapter1 = new MuneButAdapter(null, getActivity());
        muneButAdapter2 = new MuneButAdapter(null, getActivity());
        muneButAdapter3 = new MuneButAdapter(null, getActivity());
        muneButAdapter4 = new MuneButAdapter(null, getActivity());
        muneButAdapter5 = new MuneButAdapter(null, getActivity());

        rv1.setAdapter(muneButAdapter1);
        rv2.setAdapter(muneButAdapter2);
        rv3.setAdapter(muneButAdapter3);
        rv4.setAdapter(muneButAdapter4);
        rv5.setAdapter(muneButAdapter5);

        rv1.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv2.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv3.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv4.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv5.setLayoutManager(new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });

        AppMenu[] array = new Gson().fromJson(new AppPreferences(getContext()).getString("AppMenu", ""), AppMenu[].class);


        if (null != array) {
            list = Arrays.asList(array);
            for (AppMenu appMenu : list) {
                switch (appMenu.getPerms()) {
                    case "shop":
                        if (null != appMenu.getList() && appMenu.getList().size() > 0) {
                            muneButAdapter1.setNewData(appMenu.getList());
                            ll1.setVisibility(View.VISIBLE);
                        }

                        break;
                    case "business":
                        if (null != appMenu.getList() && appMenu.getList().size() > 0) {
                            muneButAdapter2.setNewData(appMenu.getList());
                            ll2.setVisibility(View.VISIBLE);
                        }


                        break;
                    case "ad":
                        if (null != appMenu.getList() && appMenu.getList().size() > 0) {
                            muneButAdapter3.setNewData(appMenu.getList());
                            ll3.setVisibility(View.VISIBLE);
                        }

                        break;
                    case "school":
                        if (null != appMenu.getList() && appMenu.getList().size() > 0) {
                            muneButAdapter4.setNewData(appMenu.getList());
                            ll4.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "erp":
                        if (null != appMenu.getList() && appMenu.getList().size() > 0) {
                            muneButAdapter5.setNewData(appMenu.getList());
                            ll5.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }



    public static final String TAG = "MainFragment1New";

    @Override
    protected String setTAG() {
        return TAG;
    }

    private void getInfo() {


        //获取主页权限信息
        Api().getWorkHeaderAd().subscribe(new RxSubscribe<List<Banner>>(getContext(), false) {
            @Override
            protected void _onNext(List<Banner> banners) {
                if (null != banners && banners.size() > 0) {
                    Glide.with(getActivity())
                            .load(banners.get(0).getLink())
                            .into(iv);
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }



}
