package com.eb.new_line_seller.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.MakeOrderSuccessActivity;
import com.eb.new_line_seller.activity.OrderDoneActivity;
import com.eb.new_line_seller.activity.OrderInfoActivity;
import com.eb.new_line_seller.activity.OrderPayActivity;
import com.eb.new_line_seller.adapter.OrderListAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;
import com.eb.new_line_seller.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderListFragment extends BaseFragment {

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    List<OrderInfoEntity> list = new ArrayList<>();
    OrderListAdapter ola;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    int position;

    public static OrderListFragment newInstance(int position) {
        OrderListFragment fragmentOne = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            position = getArguments().getInt("position");
        }

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_orderlist;
    }

    @Override
    protected void setUpView() {


        initData();

    }

    @Override
    protected void onVisible() {
        super.onVisible();
        getData();

    }

    private void initData() {
        ola = new OrderListAdapter(R.layout.item_fragment2_main, list, getContext());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);


        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                loadMoreData();


            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getData();

            }
        });

        ola.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.get(position).isSelected()) {
                    list.get(position).setSelected(false);

                } else {
                    for (OrderInfoEntity o : list) {
                        o.setSelected(false);
                    }
                    list.get(position).setSelected(true);
                }
                adapter.notifyDataSetChanged();
            }
        });


        ola.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

                switch (view.getId()) {
                    case R.id.button_show_details://查看订单

                        toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, list.get(position).getId());

                        break;
                    case R.id.button_action://动作按钮

                        orderDetail(list.get(position).getId());

                        break;
                }
            }
        });


    }

    private void getData() {

        Log.e("订单列表++++", "getData()");

        Api().orderList(position, 1).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {


                easylayout.refreshComplete();
                list.clear();
                list = basePage.getList();
                ola.setNewData(list);

                if (list.size() < Configure.limit_page)
                    easylayout.setLoadMoreModel(LoadModel.NONE);


            }

            @Override
            protected void _onError(String message) {
                easylayout.refreshComplete();
            }
        });
    }

    int page = 1;//第一页

    private void loadMoreData() {
        Api().orderList(position, page).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(mContext, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {

                easylayout.loadMoreComplete();

                if (basePage.getList().size() == 0) {
                    ToastUtils.showToast("没有更多了！");

                    easylayout.setLoadMoreModel(LoadModel.NONE);
                    return;
                }

                list.addAll(basePage.getList());
                ola.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                easylayout.loadMoreComplete();
            }
        });
    }


    //查询订单
    private void orderDetail(int id) {

        Api().orderDetail(id).subscribe(new RxSubscribe<OrderInfo>(getContext(), true) {
            @Override
            protected void _onNext(OrderInfo orderInfo) {
                int order_staus = orderInfo.getOrderInfo().getOrder_status();
                int pay_staus = orderInfo.getOrderInfo().getPay_status();

                if (order_staus == 0)//未服务
                    if (pay_staus == 2)
                        sendOrderInfo(MakeOrderSuccessActivity.class, orderInfo);
                    else
                        toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, orderInfo.getOrderInfo().getId());
                else if (order_staus == 1) {//服务中
                    if (pay_staus == 2)
                        toActivity(OrderDoneActivity.class, Configure.ORDERINFOID, orderInfo.getOrderInfo().getId());
                    else
                        sendOrderInfo(OrderPayActivity.class, orderInfo);
                } else

                    ToastUtils.showToast("订单已完成");

            }

            @Override
            protected void _onError(String message) {
                Log.d(getTag(), message);
                ToastUtils.showToast("查找订单失败");
            }
        });

    }


    @Override
    protected String setTAG() {
        return "OrderListFragment";
    }
}
