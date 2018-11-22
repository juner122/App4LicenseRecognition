package com.frank.plate.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frank.plate.R;
import com.frank.plate.adapter.OrderListAdapter;
import com.frank.plate.bean.OrderListItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 主页页面：订单
 */
public class MainFragment2 extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<OrderListItemEntity> list;


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment2_main;
    }

    @Override
    protected void setUpView() {

        list = new ArrayList<>();
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));
        list.add(new OrderListItemEntity("粤A78397", "订单号：XGX235678", "2018-12-03 14:20", "服务中", "$200"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new OrderListAdapter(R.layout.item_fragment2_main, list));

    }

    public static final String TAG = "MainFragment2";
    @Override
    protected String setTAG() {
        return TAG;
    }

}
