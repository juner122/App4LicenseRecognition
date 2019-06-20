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


    @OnClick({R.id.enter})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.enter:

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
                break;

        }
    }


    @Override
    protected void init() {
        tv_title.setText("经销商添加/修改");
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

        return supplier;

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_supplier_add_or_fix;
    }
}
