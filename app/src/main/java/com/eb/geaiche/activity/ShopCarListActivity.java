package com.eb.geaiche.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.ShopCarListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.ShopCarBane;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopCarListActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.et_key)
    EditText et_key;


    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    ShopCarListAdapter listAdapter;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_shop_car_list;
    }

    int pick;//是否为选择车辆  1是，0否

    @OnClick({R.id.iv_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                if (TextUtils.isEmpty(et_key.getText())) {
                    ToastUtils.showToast("请输入搜索信息！");
                    return;
                }
                shopCarList(0, et_key.getText().toString());

                break;
        }

    }

    @Override
    protected void init() {

        tv_title.setText("车辆管理");

        pick = getIntent().getIntExtra("pick", 0);
    }

    @Override
    protected void setUpView() {
        listAdapter = new ShopCarListAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (pick == 1) {//返回生成车辆生成检查页面

                    Intent intent = new Intent(ShopCarListActivity.this, CarCheckResultActivity.class);
                    intent.putExtra(Configure.car_id, listAdapter.getData().get(position).getCar_id());
                    intent.putExtra(Configure.car_no, listAdapter.getData().get(position).getCar_no());
                    startActivity(intent);
                } else {//进入车况页面
                    toActivity(CarInfoInputActivity.class, Configure.CARID, listAdapter.getData().get(position).getCar_id());
                }
            }
        });

        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                shopCarList(1, "");
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                shopCarList(0, "");

            }
        });

    }

    @Override
    protected void setUpData() {
        shopCarList(0, "");

    }

    int page = 1;//第一页

    private void shopCarList(final int type, String key) {
        if (type == 0)
            page = 1;
        else
            page++;

        Api().shopCarList(key, page).subscribe(new RxSubscribe<ShopCarBane>(this, true) {
            @Override
            protected void _onNext(ShopCarBane shopCarBane) {
                int size = shopCarBane.getShopCarList().size();
                if (type == 0) {
                    easylayout.refreshComplete();
                    listAdapter.setNewData(shopCarBane.getShopCarList());
                    if (size < Configure.limit_page)//少于每页个数，不用加载更多
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (size == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    listAdapter.addData(shopCarBane.getShopCarList());
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });
    }

}
