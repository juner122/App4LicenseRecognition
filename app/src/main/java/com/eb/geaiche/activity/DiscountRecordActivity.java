package com.eb.geaiche.activity;


import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.DisRecordAdapter;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.bean.DisRecordList;


import butterknife.BindView;

public class DiscountRecordActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;

    DisRecordAdapter adapter;

    @Override
    protected void init() {
        tv_title.setText("提现记录");
    }

    @Override
    protected void setUpView() {
        adapter = new DisRecordAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    protected void setUpData() {
        Api().disRecode().subscribe(new RxSubscribe<DisRecordList>(this, true) {
            @Override
            protected void _onNext(DisRecordList disRecordList) {

                tv1.setText(String.format("月总计:%s", null == disRecordList.getMonthMoney() ? 0 : disRecordList.getMonthMoney()));
                tv2.setText(String.format("历史总计:%s", null == disRecordList.getSumMoney() ? 0 : disRecordList.getSumMoney()));


                if (null == disRecordList.getAuthList() || disRecordList.getAuthList().size() == 0) {
                    ToastUtils.showToast("记录为空！");
                    return;
                }


                adapter.setNewData(disRecordList.getAuthList());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取记录失败！" + message);
            }
        });

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_discount_record;
    }
}
