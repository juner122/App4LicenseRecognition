package com.eb.geaiche.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallTypeGoodsListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.GoodsList;


import butterknife.BindView;

//商城商品列表
public class MallGoodsActivity extends BaseActivity {
    public static final String goodsId = "goodsId";


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_goods;
    }

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;
    @BindView(R.id.rv)
    RecyclerView rv;
    MallTypeGoodsListAdapter adapter;
    int page = 1;

    @Override
    protected void init() {
        tv_title.setText("商品列表");
    }

    @Override
    protected void setUpView() {
        adapter = new MallTypeGoodsListAdapter(null, this, R.layout.activity_mall_goods_item);

        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_list_empty_view_p, rv);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                toActivity(MallGoodsInfoActivity.class, MallGoodsActivity.goodsId, adapter.getData().get(position).getId());
            }
        });
        //一行代码开启动画 默认CUSTOM动画
        adapter.openLoadAnimation();
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                getGoodsList(1);
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getGoodsList(0);

            }
        });

        //添加购物车监听器
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter a, View view, int position) {
             int goodsId =   adapter.getData().get(position).getId();
//                addToShoppingCart(goodsId);
            }
        });

    }

    @Override
    protected void setUpData() {
        getGoodsList(0);

    }


    private void getGoodsList(final int type) {
        if (type == 0)
            page = 1;
        else
            page++;

        //查询商品
        Api().xgxshopgoodsList(getIntent().getStringExtra(MallActivity.goodsTitle), getIntent().getStringExtra(MallTypeActivity.goodsBrandId), getIntent().getStringExtra(MallActivity.categoryId), page).subscribe(new RxSubscribe<GoodsList>(this, type == 0) {
            @Override
            protected void _onNext(GoodsList goods) {

                if (type == 0) {
                    easylayout.refreshComplete();
                    adapter.setNewData(goods.getList());
                    if (goods.getList().size() < Configure.limit_page)//少于每页个数，不用加载更多
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (goods.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    adapter.addData(goods.getList());
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    private void addToShoppingCart() {
//        Api().addToShoppingCart().subscribe(new RxSubscribe<CartList>(this, true) {
//            @Override
//            protected void _onNext(CartList cartList) {
//
//            }
//
//            @Override
//            protected void _onError(String message) {
//
//            }
//        });
    }
}
