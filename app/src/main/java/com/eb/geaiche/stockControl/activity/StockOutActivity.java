package com.eb.geaiche.stockControl.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.stockControl.adapter.StockOutListAdapter;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;

import java.util.List;

import butterknife.BindView;

public class StockOutActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    StockOutListAdapter adapter;

    @Override
    protected void init() {

        tv_title.setText("领料出库");

    }

    @Override
    protected void setUpView() {

        adapter = new StockOutListAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }

    @Override
    protected void setUpData() {

        Api().matchOrder(getIntent().getIntExtra("order_id", -1)).subscribe(new RxSubscribe<List<StockGoods>>(this, true) {
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

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_out;
    }
}
