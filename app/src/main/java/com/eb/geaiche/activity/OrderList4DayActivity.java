package com.eb.geaiche.activity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.List;

import butterknife.BindView;

public class OrderList4DayActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    OrderListAdapter ola;
    int type;
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    int page = 1;//第一页

    @Override
    protected void init() {
        type = getIntent().getIntExtra("type", -1);//type = 0 今日，=1本月
        ola = new OrderListAdapter(R.layout.item_fragment2_main, null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view, rv);

        if (type == 0) {
            tv_title.setText("今日订单");
        } else {
            tv_title.setText("本月订单");
        }


        getData();
    }


    private void getData() {

        Log.e("订单列表++++", "getData()");

        Api().orderList4DayOfMoon( 1, type).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                easylayout.refreshComplete();
                ola.setNewData(basePage.getList());
                if (basePage.getList().size() < Configure.limit_page)
                    easylayout.setLoadMoreModel(LoadModel.NONE);


            }

            @Override
            protected void _onError(String message) {
                easylayout.refreshComplete();
            }
        });


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

        ola.setOnItemClickListener((adapter, view, position) -> {
            if (((OrderInfoEntity) adapter.getData().get(position)).isSelected()) {
                ((OrderInfoEntity) adapter.getData().get(position)).setSelected(false);

            } else {
                for (OrderInfoEntity o : (List<OrderInfoEntity>) adapter.getData()) {
                    o.setSelected(false);
                }
                ((OrderInfoEntity) adapter.getData().get(position)).setSelected(true);
            }
            adapter.notifyDataSetChanged();
        });


        ola.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

                switch (view.getId()) {
                    case R.id.button_show_details://查看订单

                        toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, ((OrderInfoEntity) adapter.getData().get(position)).getId());

                        break;
                    case R.id.button_action://动作按钮

                        orderDetail(((OrderInfoEntity) adapter.getData().get(position)).getId());

                        break;
                }
            }
        });
    }
    private void loadMoreData() {
        Api().orderList4DayOfMoon(page, type).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                easylayout.loadMoreComplete();
                if (basePage.getList().size() == 0) {
                    ToastUtils.showToast("没有更多了！");
                    easylayout.setLoadMoreModel(LoadModel.NONE);
                    return;
                }

                ola.addData(basePage.getList());
                ola.notifyDataSetChanged();
            }

            @Override
            protected void _onError(String message) {
                easylayout.loadMoreComplete();
            }
        });
    }

    //查询订单
    private void orderDetail(int id) {

        Api().orderDetail(id).subscribe(new RxSubscribe<OrderInfo>(this, true) {
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
                ToastUtils.showToast("查找订单失败");
            }
        });

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_list4_day;
    }
}
