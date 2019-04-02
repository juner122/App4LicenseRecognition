package com.eb.geaiche.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallTypeBrandListAdapter;
import com.eb.geaiche.adapter.MallTypeListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.GoodsCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//商城商品分类列表
public class MallTypeActivity extends BaseActivity {

    public static final String goodsBrandId = "goodsBrandId";

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_type;
    }

    @BindView(R.id.rv1)
    RecyclerView rv1;//商品分类
    @BindView(R.id.rv2)
    RecyclerView rv2;//商品品牌

    @BindView(R.id.ll_rv2)
    View ll_rv2;

    MallTypeListAdapter typeListAdapter;//商品分类
    MallTypeBrandListAdapter brandListAdapter;//商品品牌


    @OnClick({R.id.back2})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back2:
                ll_rv2.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void init() {
        tv_title.setText("分类列表");
    }

    @Override
    protected void setUpView() {

        typeListAdapter = new MallTypeListAdapter(null, this);
        brandListAdapter = new MallTypeBrandListAdapter(null, this);

        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(typeListAdapter);

        rv2.setLayoutManager(new GridLayoutManager(this, 3));
        rv2.setAdapter(brandListAdapter);


        typeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                getBrandList(typeListAdapter.getData().get(position).getCategoryId());
            }
        });

        brandListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                Intent intent = new Intent(MallTypeActivity.this, MallGoodsActivity.class);
                intent.putExtra(goodsBrandId, brandListAdapter.getData().get(position).getBrandId());
                intent.putExtra(MallActivity.categoryId, brandListAdapter.getData().get(position).getCategoryId());
                startActivity(intent);

            }
        });


    }

    @Override
    protected void setUpData() {
        queryAll();//获取所有分类
    }

    private void queryAll() {

        Api().queryShopcategoryAll(null).subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> goodsCategories) {


                typeListAdapter.setNewData(goodsCategories);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    //获取品牌列表
    public void getBrandList(String id) {

        Api().shopcategoryInfo(id).subscribe(new RxSubscribe<List<GoodsBrand>>(this, true) {
            @Override
            protected void _onNext(List<GoodsBrand> goodsBrands) {

                int size = goodsBrands.size();
                if (size == 0) {
                    ToastUtils.showToast("该分类暂无商品！");
                    ll_rv2.setVisibility(View.GONE);
                    return;
                }

                ll_rv2.setVisibility(View.VISIBLE);
                brandListAdapter.setNewData(goodsBrands);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });


    }

}
