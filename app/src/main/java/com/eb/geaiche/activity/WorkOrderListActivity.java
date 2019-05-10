package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderOfTchnicianAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class WorkOrderListActivity extends BaseActivity {

    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.rv2)
    RecyclerView rv2;


    OrderOfTchnicianAdapter adapter, adapter2;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_work_order_list;
    }

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"销售订单", "服务订单"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @Override
    protected void init() {
        tv_title.setText("工作订单列表");


        adapter = new OrderOfTchnicianAdapter(null);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        adapter.setEmptyView(R.layout.order_list_empty_view, rv1);
        rv1.setAdapter(adapter);


        adapter2 = new OrderOfTchnicianAdapter(null);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        adapter2.setEmptyView(R.layout.order_list_empty_view, rv2);
        rv2.setAdapter(adapter2);


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    rv1.setVisibility(View.VISIBLE);
                    rv2.setVisibility(View.GONE);
                } else {
                    rv1.setVisibility(View.GONE);
                    rv2.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        adapter.setOnItemClickListener((a, view, position) -> toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, adapter.getData().get(position).getId()));

        adapter2.setOnItemClickListener((a, view, position) -> toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, adapter2.getData().get(position).getId()));







    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        int id = getIntent().getIntExtra("id", -1);
//        int id = 61;
        Api().saleList(id).subscribe(new RxSubscribe<List<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(List<OrderInfoEntity> orderInfoEntities) {
                adapter.setNewData(orderInfoEntities);
            }

            @Override
            protected void _onError(String message) {

            }
        });

        Api().sysOrderList(id).subscribe(new RxSubscribe<List<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(List<OrderInfoEntity> orderInfoEntities) {
                adapter2.setNewData(orderInfoEntities);
            }

            @Override
            protected void _onError(String message) {

            }
        });


    }


}
