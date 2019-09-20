package com.eb.geaiche.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.FixInfoPartsItemAdapter;

import com.eb.geaiche.adapter.FixPickParts2ItemAdapter;
import com.eb.geaiche.adapter.ProductListAdapter;
import com.eb.geaiche.mvp.contacts.FixPickPartsContacts;

import com.eb.geaiche.mvp.model.FixPickPartsMdl;

import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.MyRadioButton;
import com.eb.geaiche.view.ProductListDialog;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsSeek;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class FixPickPartsPtr extends BasePresenter<FixPickPartsContacts.FixPickPartsUI> implements FixPickPartsContacts.FixPickPartsPtr {


    FixPickPartsContacts.FixPickPartsMdl mdl;


    FixPickParts2ItemAdapter adapter_s2;//2级分类
    ProductListAdapter productListAdapter; //配件列表 新

    HashSet<FixParts> pick_partsList;//点击配件

    int id;//当前选择分类id
    int page = 1;//第一页
    EasyRefreshLayout easylayout;
    String categoryId;//当前选的大分类索引id


    public FixPickPartsPtr(@NonNull FixPickPartsContacts.FixPickPartsUI view, int layout) {
        super(view);
        mdl = new FixPickPartsMdl(view.getSelfActivity());
        pick_partsList = new HashSet<>();
        adapter_s2 = new FixPickParts2ItemAdapter(null);


        productListAdapter = new ProductListAdapter(getView().getSelfActivity(), null, 1);

        adapter_s2.setOnItemClickListener((adapter, view1, position) -> {
            //查找配件 根据id
            id = ((FixParts2item) (adapter.getData().get(position))).getId();

            getView().showPartsList();
        });



    }


    @Override
    public void onGetData(final RadioGroup rg) {
        rg.removeAllViews();
        mdl.getPartsData(new RxSubscribe<List<GoodsCategory>>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(List<GoodsCategory> list) {


                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    rg.setVisibility(View.GONE);
                    return;
                }

                rg.setVisibility(View.VISIBLE);
                init0Data(rg, list);//根据第一级类别数量 创建RadioButton
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

        getGoodList(null, page, "");
    }

    public void init0Data(RadioGroup rg, final List<GoodsCategory> list) {
        rg.removeAllViews();//清除所有
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(getView().getSelfActivity(), list.get(i).getName(), i);
            final int finalI = i;
            radioButton.setOnClickListener(view -> {

                categoryId = list.get(finalI).getCategoryId();//一级分类id
                page = 1;
                getGoodList(null, page, categoryId);
            });


            rg.addView(radioButton);
        }
    }


    @Override
    public void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_Parts, EasyRefreshLayout e) {
        rv_2item.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));
        rv_Parts.setLayoutManager(new LinearLayoutManager(getView().getSelfActivity()));

        rv_2item.setAdapter(adapter_s2);
//        rv_Parts.setAdapter(adapter_item);
        rv_Parts.setAdapter(productListAdapter);
//        adapter_item.setEmptyView(R.layout.order_list_empty_view_p, rv_Parts);
        adapter_s2.setEmptyView(R.layout.order_list_empty_view_p, rv_2item);
        easylayout = e;
        // 普通加载
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }

            @Override
            public void onRefreshing() {

                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                getData();

            }
        });

        productListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            TextView tv_number = (TextView) adapter.getViewByPosition(rv_Parts, position, R.id.tv_number);
            TextView view_value = (TextView) adapter.getViewByPosition(rv_Parts, position, R.id.tv_product_value);
            View ib_reduce = adapter.getViewByPosition(rv_Parts, position, R.id.ib_reduce);//减号

            List<Goods> goodsList = productListAdapter.getData();

            int number = goodsList.get(position).getNum();//获取
            switch (view.getId()) {
                case R.id.ib_plus:
                    if (null == goodsList.get(position).getGoodsStandard() || goodsList.get(position).getGoodsStandard().getId() == 0) {
                        ToastUtils.showToast("请选择规格");
                        return;
                    }
                    number++;
                    if (number > 0) {
                        ib_reduce.setVisibility(View.VISIBLE);//显示减号
                        tv_number.setVisibility(View.VISIBLE);
                    }

                    MyApplication.cartUtils.addData(toGoodsEntity(goodsList.get(position)));
                    tv_number.setText(String.valueOf(number));


                    goodsList.get(position).setNum(number);//设置
                    countPrice2();


                    break;

                case R.id.ib_reduce:
                    number--;
                    if (number == 0) {
                        ib_reduce.setVisibility(View.INVISIBLE);//隐藏减号
                        tv_number.setVisibility(View.INVISIBLE);
                    }

                    MyApplication.cartUtils.reduceData(toGoodsEntity(goodsList.get(position)));
                    tv_number.setText(String.valueOf(number));


                    goodsList.get(position).setNum(number);//设置
                    countPrice2();


                    break;

                case R.id.tv_product_value:

                    getProductValue(view_value, productListAdapter.getData().get(position).getXgxGoodsStandardPojoList(), position, productListAdapter.getData().get(position));
                    break;
            }


        });


    }

    //获取规格列表
    private void getProductValue(final TextView view, final List<Goods.GoodsStandard> goodsStandards, final int positions, Goods goods) {


        final ProductListDialog confirmDialog = new ProductListDialog(getView().getSelfActivity(), goodsStandards, goods);
        confirmDialog.show();
        confirmDialog.setClicklistener(new ProductListDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(Goods.GoodsStandard pick_value) {
                confirmDialog.dismiss();
                view.setText(pick_value.getGoodsStandardTitle());

                productListAdapter.getData().get(positions).setGoodsStandard(pick_value);
                productListAdapter.getData().get(positions).setNum(pick_value.getNum());
                productListAdapter.notifyDataSetChanged();
                //按确认才保存
                MyApplication.cartUtils.commit();

                countPrice2();


            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
    }

    @Override
    public void confirm() {

        if (MyApplication.cartUtils.getProductList().size() == 0) {
            ToastUtils.showToast("请最少选择一个项目！");
            return;
        }

        getView().onConfirm(null);


    }



    //搜索
    @Override
    public void seekPartsforKey(String key) {
        if (key.equals("")) {
            ToastUtils.showToast("请输入内容！");
            return;
        }

        page = 1;
        getGoodList(key, page, null);
    }

    @Override
    public void loadMoreData() {
        page++;
        getGoodList(getView().getKey(), page, categoryId);
    }

    @Override
    public void getData() {
        page = 1;
        getGoodList(getView().getKey(), page, categoryId);

    }


    //计算选择的总价格
    private void countPrice2() {


        getView().setPickAllPrice("已选择：￥" + MathUtil.twoDecimal(MyApplication.cartUtils.getProductPrice()));

    }

    private void getGoodList(String key, final int page, String categoryId) {
        mdl.getGoodList(new RxSubscribe<GoodsList>(getView().getSelfActivity(), page == 1) {
            @Override
            protected void _onNext(GoodsList goodsList) {
                if (page == 1) {//不等于1 显示更多
                    easylayout.refreshComplete();
                    productListAdapter.setNewData(goodsList.getList());
                    if (goodsList.getList().size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {
                    easylayout.loadMoreComplete();
                    if (goodsList.getList().size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    productListAdapter.addData(goodsList.getList());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        }, key, page, categoryId);
    }


    private GoodsEntity toGoodsEntity(Goods goods) {
        GoodsEntity goodsEntity = new GoodsEntity();

        goodsEntity.setGoodsId(goods.getId());

        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setGoodsSn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(goods.getGoodsStandard().getId());
        goodsEntity.setGoods_specifition_ids(goods.getGoodsStandard().getId());
        goodsEntity.setGoodsSpecifitionIds(goods.getGoodsStandard().getId());
        goodsEntity.setNumber(goods.getNum());
        goodsEntity.setMarket_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetail_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setMarketPrice(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetailPrice(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setGoods_specifition_name_value(goods.getGoodsStandard().getGoodsStandardTitle());
        goodsEntity.setGoodsSpecifitionNameValue(goods.getGoodsStandard().getGoodsStandardTitle());
        goodsEntity.setFirstCategoryId(goods.getFirstCategoryId());
        return goodsEntity;
    }
}
