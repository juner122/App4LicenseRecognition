package com.eb.geaiche.activity;

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

//商城商品分类列表
public class MallTypeActivity extends BaseActivity {


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_type;
    }

    @BindView(R.id.rv1)
    RecyclerView rv1;//商品分类
    @BindView(R.id.rv2)
    RecyclerView rv2;//商品品牌

    MallTypeListAdapter typeListAdapter;//商品分类
    MallTypeBrandListAdapter brandListAdapter;//商品品牌


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

                rv2.setVisibility(View.VISIBLE);

                List<GoodsBrand> data = new ArrayList<>();

                data.add(new GoodsBrand("米其林"));
                data.add(new GoodsBrand("马牌"));
                data.add(new GoodsBrand("米其林"));
                data.add(new GoodsBrand("马牌"));
                data.add(new GoodsBrand("米其林"));
                data.add(new GoodsBrand("马牌"));
                data.add(new GoodsBrand("米其林"));
                data.add(new GoodsBrand("马牌"));
                data.add(new GoodsBrand("米其林"));
                data.add(new GoodsBrand("马牌"));

                brandListAdapter.setNewData(data);

            }
        });


    }

    @Override
    protected void setUpData() {
        queryAll();//获取所有分类
    }

    private void queryAll() {

        Api().queryShopcategoryAll().subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
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


}
