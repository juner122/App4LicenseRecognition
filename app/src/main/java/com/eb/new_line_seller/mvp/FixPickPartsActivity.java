package com.eb.new_line_seller.mvp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.FixPickPartsContacts;
import com.eb.new_line_seller.mvp.presenter.FixPickPartsPtr;
import com.juner.mvp.bean.FixParts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE;
import static com.eb.new_line_seller.mvp.presenter.FixInfoPtr.TYPE_Parts;

public class FixPickPartsActivity extends BaseActivity<FixPickPartsContacts.FixPickPartsPtr> implements FixPickPartsContacts.FixPickPartsUI {

    @BindView(R.id.rg_type)
    RadioGroup rg_type;//一级

    @BindView(R.id.rv0)
    RecyclerView rv0;//二级

    @BindView(R.id.rv1)
    RecyclerView rv1;//三级

    @BindView(R.id.price)
    TextView price;//总价格


    @BindView(R.id.et_key)
    EditText et_key;//

    @OnClick({R.id.tv_confirm, R.id.iv_search, R.id.tv_title_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                //确认选择
                getPresenter().confirm();
                break;

            case R.id.iv_search:
                //搜索
                getPresenter().seekPartsforKey(et_key.getText().toString());
                break;
            case R.id.tv_title_r:
                //自定义配件
                toActivity(CustomPartsActivity.class, "type", 1);

                break;


        }


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_pick_service;
    }

    @Override
    protected void init() {
        tv_title.setText("请选择配件");
        setRTitle("自定义配件");
        getPresenter().initRecyclerView(rv0, rv1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onGetData(rg_type);
    }

    @Override
    public void showPartsList() {
        rv0.setVisibility(View.GONE);
        rv1.setVisibility(View.VISIBLE);
    }

    @Override
    public void showParts2List() {
        rv0.setVisibility(View.VISIBLE);
        rv1.setVisibility(View.GONE);
    }

    @Override
    public void setPickAllPrice(String prices) {
        price.setText(prices);
    }

    @Override
    public void onConfirm(List<FixParts> fixPartsList) {


        Intent intent = new Intent(this, FixInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TYPE_Parts, (ArrayList) fixPartsList);
        bundle.putString(TYPE, TYPE_Parts);
        bundle.putInt("from", 1);
        intent.putExtras(bundle);
        startActivity(intent);


    }


    @Override
    public FixPickPartsContacts.FixPickPartsPtr onBindPresenter() {
        return new FixPickPartsPtr(this);
    }
}
