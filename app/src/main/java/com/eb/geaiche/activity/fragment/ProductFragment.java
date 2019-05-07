package com.eb.geaiche.activity.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;


import com.eb.geaiche.activity.ProductMealListActivity;
import com.eb.geaiche.adapter.ProductListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.view.MyRadioButton;
import com.eb.geaiche.view.ProductListDialog;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;
import com.eb.geaiche.util.ToastUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductFragment extends BaseFragment {
    public static final int type = 4;

    @BindView(R.id.rg_type)
    RadioGroup radioGroup;
    @BindView(R.id.et_key)
    EditText et_key;
    @BindView(R.id.rv1)
    RecyclerView rv1;//商品列表
    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;


    ProductListAdapter productListAdapter;

    String categoryId;//当前选的大分类索引id
    int page = 1;//第一页
    int isShow = 0;

    public static ProductFragment getInstance(int show) {
        ProductFragment sf = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("isShow", show);

        //fragment保存参数，传入一个Bundle对象
        sf.setArguments(bundle);
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //取出保存的值
            isShow = getArguments().getInt("isShow", -1);
        }

    }

    @Override
    protected void onVisible() {
        super.onVisible();

        //获取分类
        Api().queryShopcategoryAll(String.valueOf(Configure.Goods_TYPE_4)).subscribe(new RxSubscribe<List<GoodsCategory>>(getContext(), true) {
            @Override
            protected void _onNext(List<GoodsCategory> list) {
                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    radioGroup.setVisibility(View.GONE);
                    return;
                }

                radioGroup.setVisibility(View.VISIBLE);
                init0Data(radioGroup, list);//根据第一级类别数量 创建RadioButton
            }

            @Override
            protected void _onError(String message) {

            }
        });


        //新接口
        xgxshopgoodsList(null, null);
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_product_list_fr;
    }


    @Override
    protected void setUpView() {

        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                xgxshopgoodsList(null, null);
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                xgxshopgoodsList(null, null);

            }
        });

        rv1.setLayoutManager(new LinearLayoutManager(getContext()));
        productListAdapter = new ProductListAdapter(this, null, isShow);
        rv1.setAdapter(productListAdapter);
        productListAdapter.setEmptyView(R.layout.order_list_empty_view_p, rv1);

        productListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            TextView tv_number = (TextView) adapter.getViewByPosition(rv1, position, R.id.tv_number);
            TextView view_value = (TextView) adapter.getViewByPosition(rv1, position, R.id.tv_product_value);
            View ib_reduce = adapter.getViewByPosition(rv1, position, R.id.ib_reduce);//减号


            int number = productListAdapter.getData().get(position).getNum();//获取
            switch (view.getId()) {
                case R.id.ib_plus:
                    if (null == productListAdapter.getData().get(position).getGoodsStandard() || productListAdapter.getData().get(position).getGoodsStandard().getId() == 0) {
                        ToastUtils.showToast("请选择规格");
                        return;
                    }
                    number++;
                    if (number > 0) {
                        ib_reduce.setVisibility(View.VISIBLE);//显示减号
                        tv_number.setVisibility(View.VISIBLE);
                    }

                    MyApplication.cartUtils.addData(toGoodsEntity(productListAdapter.getData().get(position)));
                    tv_number.setText(String.valueOf(number));


                    productListAdapter.getData().get(position).setNum(number);//设置
                    upDatePrice();


                    break;

                case R.id.ib_reduce:
                    number--;
                    if (number == 0) {
                        ib_reduce.setVisibility(View.INVISIBLE);//隐藏减号
                        tv_number.setVisibility(View.INVISIBLE);
                    }

                    MyApplication.cartUtils.reduceData(toGoodsEntity(productListAdapter.getData().get(position)));
                    tv_number.setText(String.valueOf(number));


                    productListAdapter.getData().get(position).setNum(number);//设置
                    upDatePrice();


                    break;

                case R.id.tv_product_value:

                    getProductValue(view_value, productListAdapter.getData().get(position).getXgxGoodsStandardPojoList(), position, productListAdapter.getData().get(position));
                    break;
            }
        });
    }

    ProductListDialog confirmDialog;

    //获取规格列表
    private void getProductValue(final TextView view, final List<Goods.GoodsStandard> goodsStandards, final int positions, Goods goods) {


        confirmDialog = new ProductListDialog(getContext(), goodsStandards, goods);
        confirmDialog.show();
        confirmDialog.setClicklistener(new ProductListDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(Goods.GoodsStandard pick_value) {
                confirmDialog.dismiss();
                view.setText(pick_value.getGoodsStandardTitle() + "x" + pick_value.getNum());

                productListAdapter.getData().get(positions).setGoodsStandard(pick_value);
                productListAdapter.getData().get(positions).setNum(pick_value.getNum());
                productListAdapter.notifyDataSetChanged();

                MyApplication.cartUtils.commit();//点确认才保存
                upDatePrice();


            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
    }


    public void init0Data(RadioGroup rg, final List<GoodsCategory> list) {
        rg.removeAllViews();//清除所有
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getActivity(), list.get(i).getName(), i);
            final int finalI = i;
            radioButton.setOnClickListener(view -> {

                categoryId = list.get(finalI).getCategoryId();//一级分类id

                xgxshopgoodsList(null, categoryId);
            });

            rg.addView(radioButton);
        }


    }

    /**
     * 新商品接口
     *
     * @param key 搜索关键字
     */

    private void xgxshopgoodsList(String key, String categoryId) {
        Api().xgxshopgoodsList(key, null, categoryId, 1, type, 50).subscribe(new RxSubscribe<GoodsList>(getContext(), true) {
            @Override
            protected void _onNext(GoodsList goods) {


                List<GoodsEntity> car_list = MyApplication.cartUtils.getProductList();//购物车里的type=4的商品
                for (GoodsEntity ge : car_list) {
                    for (int i = 0; i < goods.getList().size(); i++) {
                        int ge_id = ge.getGoods_id();
                        int goods_id = goods.getList().get(i).getId();
                        if (ge_id == goods_id) {
                            goods.getList().get(i).setNum(ge.getNumber());//设置数量
                            goods.getList().get(i).setGoodsStandard(ge.getGoodsStandard());//设置规格
                        }
                    }
                }


                if (page == 1) {//不等于1 显示更多
                    easylayout.refreshComplete();
                    productListAdapter.setNewData(goods.getList());
                    if (goods.getList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {

                    easylayout.loadMoreComplete();
                    if (goods.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    productListAdapter.setNewData(goods.getList());
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    @OnClick({R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText())) {
                    xgxshopgoodsList(et_key.getText().toString(), null);
                    SoftInputUtil.hideSoftInput(getContext(), et_key);
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;

        }


    }

    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

    private GoodsEntity toGoodsEntity(Goods goods) {
        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setGoodsId(goods.getId());
        goodsEntity.setId(goods.getId());
        goodsEntity.setGoods_id(goods.getId());

        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setGoodsSn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(goods.getGoodsStandard().getId());
        goodsEntity.setNumber(goods.getNum());
        goodsEntity.setMarket_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetail_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setMarketPrice(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetailPrice(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setGoods_specifition_name_value(goods.getGoodsStandard().getGoodsStandardTitle());
        goodsEntity.setFirstCategoryId(goods.getFirstCategoryId());
        return goodsEntity;
    }


    //更新合计金额
    private void upDatePrice() {
        ((ProductMealListActivity) Objects.requireNonNull(getActivity())).onPulsTotalPrice(MyApplication.cartUtils.getProductPrice());

    }
}
