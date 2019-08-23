package com.eb.geaiche.mall.fragment;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.MallActivity;
import com.eb.geaiche.activity.MallGoodsActivity;
import com.eb.geaiche.activity.MallGoodsInfoActivity;
import com.eb.geaiche.activity.MallGoodsVinScanActivity;
import com.eb.geaiche.activity.MallTypeActivity;
import com.eb.geaiche.activity.fragment.BaseFragment;
import com.eb.geaiche.adapter.AppMenuAdapter;
import com.eb.geaiche.adapter.MallMuneButAdapter;
import com.eb.geaiche.adapter.MallTypeGoodsListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.ShoppingCartActivity;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.ToastUtils;
import com.google.gson.Gson;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.Banner;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import net.grandcentrix.tray.AppPreferences;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 汽配件
 */

public class MallMainFragment extends BaseFragment {
    public static final String categoryId = "categoryId";
    public static final String goodsTitle = "goodsTitle";
    public static final String VIN = "Vin";

    @BindView(R.id.rv1)
    RecyclerView rv1;

    @BindView(R.id.rv2)
    RecyclerView rv2;


    @BindView(R.id.et_key)
    EditText et_key;

    @BindView(R.id.iv_banner)
    ImageView iv_banner;
    @BindView(R.id.vin)
    TextView vin;//当前查询的车架号


    int page = 1;


    MallMuneButAdapter muneButAdapter;//分类
    MallTypeGoodsListAdapter mallTypeGoodsListAdapter;//推荐商品

    @OnClick({R.id.iv_search, R.id.iv_scan, R.id.ll_more, R.id.iv_cart})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_scan:
                toActivity(MallGoodsVinScanActivity.class);
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

            case R.id.iv_cart:
                //查看见购物车

                toActivity(ShoppingCartActivity.class);
                break;
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (!MyAppPreferences.getString(VIN).equals("")) {
            vin.setText(String.format("当前车架号：%s", MyAppPreferences.getString(VIN)));
            vin.setVisibility(View.VISIBLE);
        } else {
            vin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void setUpView() {
        et_key.setHint("搜索商品");

        Glide.with(this)
                .load(R.mipmap.icon_mall_banner)
                .into(iv_banner);

        //主页分类
        Api().queryShopcategoryAll("1").subscribe(new RxSubscribe<List<GoodsCategory>>(getActivity(), true) {
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

        muneButAdapter = new MallMuneButAdapter(null, getActivity());
        rv1.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rv1.setAdapter(muneButAdapter);

        muneButAdapter.setOnItemClickListener((adapter, view, position) -> toActivity(MallGoodsActivity.class, categoryId, muneButAdapter.getData().get(position).getCategoryId()));


        mallTypeGoodsListAdapter = new MallTypeGoodsListAdapter(null, getActivity(), R.layout.activity_mall_goods_item2);


        rv2.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv2.setAdapter(mallTypeGoodsListAdapter);

        mallTypeGoodsListAdapter.setOnItemClickListener((adapter, view, position) -> toActivity(MallGoodsInfoActivity.class, MallGoodsActivity.goodsId, mallTypeGoodsListAdapter.getData().get(position).getId()));

        mallTypeGoodsListAdapter.setOnItemChildClickListener((adapter, view, position) -> addToShopCart(mallTypeGoodsListAdapter.getData().get(position).getId(), mallTypeGoodsListAdapter.getData().get(position).getXgxGoodsStandardPojoList().get(0).getGoodsStandardId()));


        mallTypeGoodsListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);

    }

    private void getGoodsList(final int stu) {
        if (stu == 0)
            page = 1;
        else
            page++;

        //查询商品
        Api().xgxshopgoodsList(null, null, null, page, Configure.Goods_TYPE_1).subscribe(new RxSubscribe<GoodsList>(getActivity(), true) {
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


    //添加商品到购物车
    private void addToShopCart(int goodsId, int productId) {

        Api().addToShoppingCart(goodsId, productId).subscribe(new RxSubscribe<CartList>(getActivity(), true) {
            @Override
            protected void _onNext(CartList cartList) {
                if (null == cartList.getCartList() || cartList.getCartList().size() == 0) {
                    ToastUtils.showToast("添加失败！");
                } else {
                    ToastUtils.showToast("添加成功！");
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("添加失败！" + message);

            }
        });

    }

    @Override
    protected String setTAG() {
        return "MallMainFragment";
    }


}
