package com.eb.geaiche.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.AppMenuAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.google.gson.Gson;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.Banner;

import net.grandcentrix.tray.AppPreferences;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 主页页面：工作台
 */

public class MainFragment1New extends BaseFragment {


    @BindView(R.id.iv)
    ImageView iv;
    List<AppMenu> list;


    @BindView(R.id.rv)
    RecyclerView rv;


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment1_main_new;
    }

    @Override
    protected void setUpView() {

        rv.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });

        AppMenu[] array = new Gson().fromJson(new AppPreferences(getContext()).getString("AppMenu", ""), AppMenu[].class);

        if (null != array) {
            list = Arrays.asList(array);
            rv.setAdapter(new AppMenuAdapter(list, getActivity()));
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
