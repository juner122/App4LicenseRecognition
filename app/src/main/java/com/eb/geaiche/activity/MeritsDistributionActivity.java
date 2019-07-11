package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MeeitsAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.StaffPerformance;

import java.util.List;

import butterknife.BindView;

//绩效分配员工页面
public class MeritsDistributionActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    MeeitsAdapter adapter;


    @Override
    protected void init() {
        tv_title.setText("绩效分配");

    }

    @Override
    protected void setUpView() {
        adapter = new MeeitsAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


        Api().getOrderDeduction(getIntent().getIntExtra("orderId", 0)).subscribe(new RxSubscribe<List<StaffPerformance>>(this, true) {
            @Override
            protected void _onNext(List<StaffPerformance> s) {
                adapter.setNewData(s);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查看绩效分配失败" + message);
                finish();
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_merits_distribution;
    }
}
