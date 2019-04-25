package com.eb.geaiche.activity;


import android.graphics.Paint;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.GoodsPicListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.ShoppingCartActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.GlideImageLoader;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.Goods;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MallGoodsInfoActivity extends BaseActivity {

    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.price1)
    TextView price1;
    @BindView(R.id.price2)
    TextView price2;

    @BindView(R.id.number)
    TextView number;//购物车商品数量

    GoodsPicListAdapter goodsPicListAdapter;//商品详情图片列表

    @BindView(R.id.rv)
    RecyclerView rv;

    int productId;//默认第一个规格id

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_goods_info;
    }

    @OnClick({R.id.tv_collection, R.id.ll_cart, R.id.tv_add_cart, R.id.tv_buy})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_collection://收藏

                break;

            case R.id.ll_cart://进入购物车
                toActivity(ShoppingCartActivity.class);
                break;

            case R.id.tv_add_cart://加入购物车
                addToShopCart(getIntent().getIntExtra(MallGoodsActivity.goodsId, -1), productId);
                break;

            case R.id.tv_buy://购买
//                List<CartItem> cartItems = new ArrayList<>();
//
//                toActivity(MallGoodsInfoActivity.class, cartItems, "cart_goods");

                break;


        }

    }


    @Override
    protected void init() {

        tv_title.setText("商品详情");


    }


    @Override
    protected void setUpView() {

        goodsPicListAdapter = new GoodsPicListAdapter(null, this);
        rv.setAdapter(goodsPicListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);

    }

    @Override
    protected void setUpData() {
        getGoodsInfo();
        getShoppingCartInfo();
    }


    private void getGoodsInfo() {
        //查询商品
        Api().xgxshopgoodsInfo(getIntent().getIntExtra(MallGoodsActivity.goodsId, -1)).subscribe(new RxSubscribe<Goods>(this, true) {
            @Override
            protected void _onNext(Goods goods) {

                try {
                    List<String> pic_url = new ArrayList<>();
                    List<String> pic_url_info = new ArrayList<>();

                    for (Goods.GoodsPic goodsPic : goods.getGoodsDetailsPojoList()) {
                        pic_url.add(goodsPic.getImage());
                    }

                    for (Goods.GoodsInfoPic goodsInfoPic : goods.getGoodsInfoPicList()) {
                        pic_url_info.add(goodsInfoPic.getImage());
                    }
                    title.setText(goods.getGoodsTitle());

                    //添加删除线
                    price2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    price1.setText("￥" + goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice());

                    goodsPicListAdapter.setNewData(pic_url_info);

                    if (pic_url.size() > 0) {
                        //设置图片集合
                        banner.setImages(pic_url);
                        //banner设置方法全部调用完毕时最后调用
                        banner.start();
                    } else {
                        ToastUtils.showToast("暂无商品图片！");

                    }


                    productId = goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardId();
                } catch (Exception e) {
                    ToastUtils.showToast("获取商品数据失败,请重试！");
                    finish();
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    //添加商品到购物车
    private void addToShopCart(int goodsId, int productId) {

        Api().addToShoppingCart(goodsId, productId).subscribe(new RxSubscribe<CartList>(this, true) {
            @Override
            protected void _onNext(CartList cartList) {
                if (null == cartList.getCartList() || cartList.getCartList().size() == 0) {
                    ToastUtils.showToast("添加失败！");
                } else {
                    ToastUtils.showToast("添加成功！");
                    getShoppingCartInfo();
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("添加失败！" + message);

            }
        });
    }

    /**
     * 获取购物车信息
     */
    private void getShoppingCartInfo() {
        Api().getShoppingCart().subscribe(new RxSubscribe<CartList>(this, true) {
            @Override
            protected void _onNext(CartList cartList) {

                int num = 0;
                if (null != cartList.getCartList() && cartList.getCartList().size() > 0) {
                    num = cartList.getCartList().size();
                }
                number.setText(String.valueOf(num));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取购物车信息失败！" + message);
            }
        });
    }
}
