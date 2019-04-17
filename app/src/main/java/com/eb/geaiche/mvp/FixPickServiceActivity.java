package com.eb.geaiche.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.eb.geaiche.R;
import com.eb.geaiche.mvp.contacts.FixPickServiceContacts;
import com.eb.geaiche.mvp.presenter.FixPickPartsPtr;
import com.eb.geaiche.mvp.presenter.FixPickServicePtr;
import com.eb.geaiche.util.SoftInputUtil;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixServie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.mvp.presenter.FixInfoPtr.TYPE;
import static com.eb.geaiche.mvp.presenter.FixInfoPtr.TYPE_Service;


//选择服务工时
public class FixPickServiceActivity extends BaseActivity<FixPickServiceContacts.FixPickServicePtr> implements FixPickServiceContacts.FixPickServiceUI {


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
    @BindView(R.id.ll_bottom)
    View ll_bottom;//

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;

    @OnClick({R.id.tv_confirm, R.id.iv_search, R.id.tv_title_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                //确认选择
                getPresenter().confirm();
                break;

            case R.id.iv_search:
                //搜索
                SoftInputUtil.hideSoftInput(this, view);
                getPresenter().seekServerforKey(et_key.getText().toString());
                break;
            case R.id.tv_title_r:
                //自定义服务

                toActivity(CustomPartsActivity.class, "type", Configure.Goods_TYPE_3);
                break;

        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onGetData(rg_type);
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_pick_service;
    }

    @Override
    protected void init() {
        tv_title.setText("工时库");
//        setRTitle("自定义工时");
        getPresenter().initRecyclerView(rv0, rv1, easylayout);

        if (getIntent().getIntExtra(Configure.isShow, 0) == 1) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else
            ll_bottom.setVisibility(View.GONE);

    }


    @Override
    public FixPickServiceContacts.FixPickServicePtr onBindPresenter() {
        if (getIntent().getIntExtra(Configure.isShow, 0) == 1) {
            return new FixPickServicePtr(this, R.layout.activity_fix_info_item);
        } else
            return new FixPickServicePtr(this, R.layout.activity_fix_info_item_show);

    }


    @Override
    public void showServiceList() {

        rv0.setVisibility(View.GONE);
        rv1.setVisibility(View.VISIBLE);


    }

    @Override
    public void showService2List() {
        rv0.setVisibility(View.VISIBLE);
        rv1.setVisibility(View.GONE);
    }

    @Override
    public void setPickAllPrice(String prices) {
        price.setText(prices);
    }

    @Override
    public void onConfirm(List<FixServie> fixServies) {


        Intent intent = new Intent(this, FixInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TYPE_Service, (ArrayList) fixServies);
        bundle.putString(TYPE, TYPE_Service);
        bundle.putInt("from", 1);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public String getKey() {
        return et_key.getText().toString();
    }
}
