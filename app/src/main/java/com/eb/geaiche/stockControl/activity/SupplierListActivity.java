package com.eb.geaiche.stockControl.activity;

import android.content.Intent;
import android.net.Uri;
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
import com.umeng.commonsdk.debug.I;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SupplierListActivity extends BaseActivity {

    SupplierListAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.add)
    View add;

    boolean fix;//是否编辑

    @OnClick({R.id.add, R.id.tv_title_r, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                //新增供应商
                toActivity(SupplierAddOrFixActivity.class);
                break;

            case R.id.tv_title_r:
                //编辑供应商
                fix = true;
                ToastUtils.showToast("请选择要编辑的供应商！");
                add.setVisibility(View.INVISIBLE);
                break;

            case R.id.tv_back:
                //返回键
                if (fix) {
                    fix = false;
                    add.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }

                break;

        }

    }


    @Override
    protected void init() {
        tv_title.setText("供应商管理");
        setRTitle("编辑");
    }

    @Override
    protected void setUpView() {
        adapter = new SupplierListAdapter(null, this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {

            if (fix) {
                toActivity(SupplierAddOrFixActivity.class, "id", adapter.getData().get(position).getId());
            } else {
                Intent intent = new Intent(this, StockAddStandardsActivity.class);
                intent.putExtra("pick_type", 1);
                intent.putExtra("supplier", adapter.getData().get(position));
                startActivity(intent);
            }
        });

        adapter.setOnItemChildClickListener((a, view, position) -> {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + adapter.getData().get(position).getMobile()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
