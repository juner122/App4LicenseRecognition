package com.eb.geaiche.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.ShopCarListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.ShopCarBane;

import butterknife.BindView;

public class ShopCarListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    ShopCarListAdapter listAdapter;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_shop_car_list;
    }

    @Override
    protected void init() {

        tv_title.setText("车辆管理");
    }

    @Override
    protected void setUpView() {
        listAdapter = new ShopCarListAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);
    }

    @Override
    protected void setUpData() {
        Api().shopCarList().subscribe(new RxSubscribe<ShopCarBane>(this, true) {
            @Override
            protected void _onNext(ShopCarBane shopCarBane) {

                listAdapter.setNewData(shopCarBane.getShopCarList());

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });

    }


}
