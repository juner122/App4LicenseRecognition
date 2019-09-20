package com.eb.geaiche.stockControl.activity;


import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.stockControl.adapter.StockControlInfoGoodAdapter;
import com.eb.geaiche.stockControl.adapter.StockOutListAdapter;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.bean.Goods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//入库出库详情
public class StockInOrOutInfoActivity extends BaseActivity {


    String stockType;//库操作类型


    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.title1)
    TextView title1;

    @BindView(R.id.title2)
    TextView title2;

    StockOutListAdapter adapter;

    @Override
    protected void init() {
        stockType = getIntent().getStringExtra("stockType");

        if (stockType.equals(Configure.STOCK_IN)) {
            tv_title.setText("入库详情");
            title1.setText("入库人：");
            title2.setText("入库时间：");
        } else {
            tv_title.setText("出库详情");
            title1.setText("出库人：");
            title2.setText("出库时间：");
        }

    }

    @Override
    protected void setUpView() {
        adapter = new StockOutListAdapter(null,this,true);


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        name.setText(getIntent().getStringExtra("phone"));
    }

    @Override
    protected void setUpData() {

        Api().stockInfo(getIntent().getStringExtra("id")).subscribe(new RxSubscribe<StockInOrOut>(this, true) {
            @Override
            protected void _onNext(StockInOrOut stockInOrOut) {

                time.setText(DateUtil.getFormatedDateTime(stockInOrOut.getAddTime()));
                adapter.setNewData(stockInOrOut.getStockGoodsList());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("详情查询失败！" + message);
                finish();
            }
        });

    }





    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_in_or_out_infoctivity;
    }
}
