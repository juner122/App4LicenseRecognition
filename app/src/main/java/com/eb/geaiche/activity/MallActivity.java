package com.eb.geaiche.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallMuneButAdapter;
import com.eb.geaiche.adapter.MallTypeGoodsListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//商城
public class MallActivity extends BaseActivity {
    public static final String categoryId = "categoryId";
    public static final String goodsTitle = "goodsTitle";
    public static final int type = 1;
    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall;
    }



    @BindView(R.id.rv1)
    RecyclerView rv1;

    @BindView(R.id.rv2)
    RecyclerView rv2;


    @BindView(R.id.et_key)
    EditText et_key;

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    //    @BindView(R.id.easylayout)
//    EasyRefreshLayout easylayout;
    int page = 1;


    MallMuneButAdapter muneButAdapter;//分类
    MallTypeGoodsListAdapter mallTypeGoodsListAdapter;//推荐商品

    @OnClick({R.id.tv_back2, R.id.iv_search, R.id.ll_more})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back2:
                finish();
                break;
            case R.id.ll_more:
                toActivity(MallTypeActivity.class);
                break;
            case R.id.iv_search:

                if (TextUtils.isEmpty(et_key.getText())) {
                    ToastUtils.showToast("搜索内容不能为空！");
                    return;
                }
                toActivity(MallGoodsActivity.class, goodsTitle, et_key.getText().toString());


                break;
        }
    }


    @Override
    protected void init() {
        et_key.setHint("搜索商品");

        Glide.with(this)
                .load(R.mipmap.icon_mall_banner)
                .into(iv_banner);

    }

    @Override
    protected void setUpView() {

        muneButAdapter = new MallMuneButAdapter(null, this);
        rv1.setLayoutManager(new GridLayoutManager(this, 4));
        rv1.setAdapter(muneButAdapter);

        muneButAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toActivity(MallGoodsActivity.class, categoryId, muneButAdapter.getData().get(position).getCategoryId());
            }
        });


        mallTypeGoodsListAdapter = new MallTypeGoodsListAdapter(null, this, R.layout.activity_mall_goods_item2);


        rv2.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv2.setAdapter(mallTypeGoodsListAdapter);

        mallTypeGoodsListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toActivity(MallGoodsInfoActivity.class, MallGoodsActivity.goodsId, mallTypeGoodsListAdapter.getData().get(position).getId());
            }
        });

        mallTypeGoodsListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);


    }

    @Override
    protected void setUpData() {

        //主页分类
        Api().queryShopcategoryAll("1").subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> categories) {
                muneButAdapter.setNewData(categories);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
        getGoodsList(0);
    }

    private void getGoodsList(final int stu) {
        if (stu == 0)
            page = 1;
        else
            page++;

        //查询商品
        Api().xgxshopgoodsList(null, null, null, page, type).subscribe(new RxSubscribe<GoodsList>(this, stu == 0) {
            @Override
            protected void _onNext(GoodsList goods) {


                mallTypeGoodsListAdapter.setNewData(goods.getList());


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }
}
