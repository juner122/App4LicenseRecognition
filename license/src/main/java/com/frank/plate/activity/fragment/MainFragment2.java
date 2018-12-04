package com.frank.plate.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.frank.plate.R;
import com.frank.plate.adapter.OrderListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.OrderListItemEntity;
import java.util.List;

import butterknife.BindView;


/**
 * 主页页面：订单
 */
public class MainFragment2 extends BaseFragment {

    public static final String TAG = "MainFragment2";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<OrderInfoEntity> list;
    OrderListAdapter ola;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment2_main;
    }

    @Override
    protected void setUpView() {
        ola = new OrderListAdapter(R.layout.item_fragment2_main, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view,recyclerView);

        Api().orderList().subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext,true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {

                ola.setNewData(basePage.getList());

            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG,message);
            }
        });






    }


    @Override
    protected String setTAG() {
        return TAG;
    }

}
