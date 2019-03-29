package com.eb.geaiche.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallTypeGoodsListAdapter;
import com.juner.mvp.bean.Goods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//商城商品列表
public class MallGoodsActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;
    MallTypeGoodsListAdapter adapter;


    @Override
    protected void init() {
        tv_title.setText("商品列表");
    }

    @Override
    protected void setUpView() {
        adapter = new MallTypeGoodsListAdapter(null, this);

        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter);

    }

    @Override
    protected void setUpData() {
        List<Goods> data = new ArrayList<>();



    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_goods;
    }
}
