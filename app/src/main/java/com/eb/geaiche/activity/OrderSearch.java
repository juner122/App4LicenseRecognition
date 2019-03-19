package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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
import butterknife.OnClick;

public class OrderSearch extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.et_key)
    EditText et;

    OrderListAdapter ola;

    @Override
    protected void init() {
        ola = new OrderListAdapter(R.layout.item_fragment2_main, null, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ola);
        ola.setEmptyView(R.layout.order_list_empty_view, recyclerView);


        ola.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (((OrderInfoEntity) adapter.getData().get(position)).isSelected()) {
                    ((OrderInfoEntity) adapter.getData().get(position)).setSelected(false);

                } else {
                    for (OrderInfoEntity o : (List<OrderInfoEntity>) adapter.getData()) {
                        o.setSelected(false);
                    }
                    ((OrderInfoEntity) adapter.getData().get(position)).setSelected(true);
                }
                ola.notifyDataSetChanged();
            }
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

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_search;
    }


    @OnClick({R.id.iv_search, R.id.back})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                searchData();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void searchData() {
        if (TextUtils.isEmpty(et.getText())) {

            ToastUtils.showToast("请输入搜索内容！");
            return;
        }


        Api().orderList(et.getText().toString()).subscribe(new RxSubscribe<BasePage<OrderInfoEntity>>(this, true) {
            @Override
            protected void _onNext(BasePage<OrderInfoEntity> basePage) {
                ola.setNewData(basePage.getList());
            }

            @Override
            protected void _onError(String message) {
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
}
