package com.eb.geaiche.stockControl.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.OrderListActivity;
import com.eb.geaiche.stockControl.adapter.StockOutListAdapter;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.UserEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StockOutActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.order)
    TextView order;

    @BindView(R.id.et_remarks)
    EditText et_remarks;

    @BindView(R.id.tv_name)
    TextView tv_name;
    StockOutListAdapter adapter;

    int orderId;//订单id
    String orderSn;//订单sn
    UserEntity ue;
    Shop shop;//当前登录的门店信息

    @OnClick({R.id.ll_pick_order, R.id.enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_pick_order:
                toActivity(OrderListActivity.class, "view_intent", 1);
                break;

            case R.id.enter://确认出库

                stockOutEnter();
                break;
        }
    }

    @Override
    protected void init() {

        tv_title.setText("领料出库");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderId = intent.getIntExtra(Configure.ORDERINFOID, -1);

        if (orderId == -1) {
            order.setText("非订单出库");
        } else {
            orderSn = getIntent().getStringExtra(Configure.ORDERINFOSN);
            order.setText(orderSn);
            matchOrder(orderId);
        }
    }

    @Override
    protected void setUpView() {

        adapter = new StockOutListAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }

    @Override
    protected void setUpData() {


        //获取当前登录员工
        Api().getInfo().subscribe(new com.eb.geaiche.api.RxSubscribe<UserEntity>(this, true) {
            @Override
            protected void _onNext(UserEntity u) {
                ue = u;
                tv_name.setText(ue.getMobile());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("员工信息获取失败！" + message);
            }
        });


        //当前门店
        Api().shopInfo().subscribe(new com.eb.geaiche.api.RxSubscribe<Shop>(this, true) {
            @Override
            protected void _onNext(Shop s) {
                shop = s;
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("门店信息获取失败！" + message);
            }
        });


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_out;
    }


    //根据订单id查找出库商品
    private void matchOrder(int id) {
        Api().matchOrder(id).subscribe(new RxSubscribe<List<StockGoods>>(this, true) {
            @Override
            protected void _onNext(List<StockGoods> stockGoods) {
                adapter.setNewData(stockGoods);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("订单信息获取失败！" + message);
            }
        });

    }

    //确认出库
    private void stockOutEnter() {
        if (TextUtils.isEmpty(order.getText())) {
            ToastUtils.showToast("订单号为空！");
            return;
        }

        if (adapter.getData().size() == 0) {
            ToastUtils.showToast("出库商品列表为空！");
            return;
        }

        Api().inOrOut(getStock()).subscribe(new com.eb.geaiche.api.RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                finish();
                toActivity(StockInOrOutInfoActivity.class, "stockType", Configure.STOCK_IN);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("出库失败！" + message);
            }
        });
    }

    private StockInOrOut getStock() {
        StockInOrOut stock = new StockInOrOut();

        stock.setType("1");////1出库2入库
        stock.setOrderSn(order.getText().toString());
        stock.setShopId(shop.getShop().getId());
        stock.setOrderId(String.valueOf(orderId));
        stock.setUserId(String.valueOf(ue.getUserId()));
        stock.setRemarks(et_remarks.getText().toString());
        stock.setStockGoodsList(adapter.getData());

        return stock;
    }

}
