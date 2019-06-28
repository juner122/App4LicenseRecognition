package com.eb.geaiche.stockControl.activity;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.stockControl.adapter.SupplierListAdapter;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SupplierListActivity extends BaseActivity {

    SupplierListAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView rv;


    @OnClick({R.id.add})
    public void onClick(View view) {
        //新增供应商
        toActivity(SupplierAddOrFixActivity.class);
    }


    @Override
    protected void init() {
        tv_title.setText("供应商管理");
    }

    @Override
    protected void setUpView() {
        adapter = new SupplierListAdapter(null, this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {

            Intent intent = new Intent(this, StockAddStandardsActivity.class);
            intent.putExtra("pick_type", 1);
            intent.putExtra("supplier", adapter.getData().get(position));

            startActivity(intent);
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Api().listSupplier().subscribe(new RxSubscribe<List<Supplier>>(this, true) {
            @Override
            protected void _onNext(List<Supplier> suppliers) {

                adapter.setNewData(suppliers);

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("查询失败" + message);
            }
        });

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_supplier_list;
    }
}
