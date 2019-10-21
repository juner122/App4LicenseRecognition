package com.eb.geaiche.stockControl.activity;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.NullDataEntity;
import com.umeng.commonsdk.debug.E;

import butterknife.BindView;
import butterknife.OnClick;

public class SupplierAddOrFixActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    EditText tv_name;
    @BindView(R.id.tv_contact)
    EditText tv_contact;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.remarks)
    EditText remarks;


    String id;//供应商id;

    @OnClick({R.id.enter})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.enter:
                if (null == id)
                    addSupplier();
                else
                    fixSupplier();
                break;

        }
    }


    @Override
    protected void init() {


        id = getIntent().getStringExtra("id");
        if (null == id) {
            tv_title.setText("经销商添加");
        } else {
            tv_title.setText("经销商修改");
            getInfo();
        }
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    private Supplier getSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName(tv_name.getText().toString());
        supplier.setAddress(address.getText().toString());
        supplier.setLinkman(tv_contact.getText().toString());
        supplier.setAddTime(String.valueOf(System.currentTimeMillis()));
        supplier.setMobile(phone.getText().toString());
        supplier.setOperation(remarks.getText().toString());
        if (null != id)
            supplier.setId(id);

        return supplier;

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_supplier_add_or_fix;
    }


    //新增
    private void addSupplier() {

        Api().addSupplier(getSupplier()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("添加成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("添加失败！" + message);
            }
        });
    }

    //修改
    private void fixSupplier() {

        Api().fixSupplier(getSupplier()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("修改成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败！" + message);
            }
        });
    }

    //供应商详情
    private void getInfo() {
        Api().infoSupplier(id).subscribe(new RxSubscribe<Supplier>(this, true) {
            @Override
            protected void _onNext(Supplier supplier) {
                tv_name.setText(supplier.getName());
                address.setText(supplier.getAddress());
                tv_contact.setText(supplier.getLinkman());
                phone.setText(supplier.getMobile());
                remarks.setText(supplier.getOperation());

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("供应商详情查找失败！" + message);
            }
        });
    }
}
