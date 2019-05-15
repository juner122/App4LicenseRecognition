package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;


import com.eb.geaiche.adapter.ServeListAdapter;
import com.eb.geaiche.api.RxSubscribe;


import com.eb.geaiche.mvp.CustomPartsActivity;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.view.MyRadioButton;
import com.eb.geaiche.view.ProductListDialog;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;

import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ServeListActivity extends BaseActivity {
    @BindView(R.id.rg_type)
    RadioGroup rg_type;//一级

    @BindView(R.id.et_key)
    EditText et_key;


    @BindView(R.id.tv_totalPrice)
    TextView tv_totalPrice;

    @BindView(R.id.ll)
    View ll;
    @BindView(R.id.ll_search)
    View ll_search;
    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.easylayout)
    EasyRefreshLayout easylayout;


    ServeListAdapter serveListAdapter;
    List<Goods> servers = new ArrayList<>();//所有数据

    int page = 1;//第一页

    String categoryId;//当前选的大分类索引id


    @Override
    protected void init() {


        //计算总价
        count();

        tv_title.setText("服务工时列表");
        if (!MyAppPreferences.getShopType())
            setRTitle("自定义工时");
        serveListAdapter = new ServeListAdapter(servers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serveListAdapter);

        easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
        easylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                xgxshopgoodsList(null);
            }

            @Override
            public void onRefreshing() {

                page = 1;
                easylayout.setLoadMoreModel(LoadModel.COMMON_MODEL);
                xgxshopgoodsList(null);

            }
        });

//
//        serveListAdapter.setOnItemClickListener((adapter, view, position) -> {
//
//
//            if (servers.get(position).isSelected()) {
//                servers.get(position).setSelected(false);
//                MyApplication.cartUtils.reduceData(toGoodsEntity(servers.get(position)));
//            } else {
//                servers.get(position).setSelected(true);
//                MyApplication.cartUtils.addData(toGoodsEntity(servers.get(position)));
//            }
//
//            //计算总价
//            count();
//            adapter.notifyDataSetChanged();
//        });


        serveListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            TextView view_value = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_product_value);

            getProductValue(view_value, serveListAdapter.getData().get(position).getXgxGoodsStandardPojoList(), position, serveListAdapter.getData().get(position));
        });
    }

    ProductListDialog confirmDialog;

    //获取规格列表
    private void getProductValue(final TextView view, final List<Goods.GoodsStandard> goodsStandards, final int positions, Goods goods) {


        confirmDialog = new ProductListDialog(this, goodsStandards, goods);
        confirmDialog.show();
        confirmDialog.setClicklistener(new ProductListDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(Goods.GoodsStandard pick_value) {
                confirmDialog.dismiss();
                view.setText(String.format("%sx%s", pick_value.getGoodsStandardTitle(), pick_value.getNum()));

                serveListAdapter.getData().get(positions).setGoodsStandard(pick_value);
                serveListAdapter.getData().get(positions).setNum(pick_value.getNum());
                serveListAdapter.notifyDataSetChanged();

                MyApplication.cartUtils.commit();//点确认才保存


                //计算总价
                count();

            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取商品类别
        queryShopcategoryAll();

        //获取商品不分类
        xgxshopgoodsList(null);
    }

    //获取服务
    private void xgxshopgoodsList(String key) {


        Api().xgxshopgoodsList(key, null, categoryId, page, Configure.Goods_TYPE_3).subscribe(new RxSubscribe<GoodsList>(this, true) {
            @Override
            protected void _onNext(GoodsList goodsList) {
                servers = goodsList.getList();
                for (GoodsEntity ps : MyApplication.cartUtils.getServerList()) {
                    for (int i = 0; i < servers.size(); i++) {
                        if (ps.getGoods_id() == servers.get(i).getId()) {
                            servers.get(i).setSelected(true);
                            servers.get(i).setNum(ps.getNumber());
                            servers.get(i).setGoodsStandard(ps.getGoodsStandard());//设置规格

                        }
                    }
                }

                if (page == 1) {//不等于1 显示更多
                    easylayout.refreshComplete();
                    serveListAdapter.setNewData(servers);
                    if (servers.size() < Configure.limit_page)
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                } else {

                    easylayout.loadMoreComplete();
                    if (servers.size() == 0) {
                        ToastUtils.showToast("没有更多了！");
                        easylayout.setLoadMoreModel(LoadModel.NONE);
                        return;
                    }
                    serveListAdapter.addData(servers);
                }


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    //获取商品分类
    private void queryShopcategoryAll() {

        Api().queryShopcategoryAll(String.valueOf(Configure.Goods_TYPE_3)).subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> goodsCategories) {


                initRadioGroup(goodsCategories);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    @OnClick({R.id.but_enter_order, R.id.iv_search, R.id.tv_title_r})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_enter_order:

                finish();


                break;
            case R.id.iv_search:

                SoftInputUtil.hideSoftInput(this, v);

                if (!TextUtils.isEmpty(et_key.getText())) {
                    categoryId = null;
                    xgxshopgoodsList(et_key.getText().toString());
                } else

                    ToastUtils.showToast("请输入搜索关键字！");

                break;

            case R.id.tv_title_r:
                //自定义服务

                toActivity(CustomPartsActivity.class, "type", Configure.Goods_TYPE_3);
                break;
        }

    }


    private void count() {
        tv_totalPrice.setText(String.format("合计：￥%s", MathUtil.twoDecimal(MyApplication.cartUtils.getServerPrice())));    //计算总价格

    }

    @Override
    protected void setUpView() {
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_server_list;
    }



    public void initRadioGroup(final List<GoodsCategory> list) {
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(this, list.get(i).getName(), i);


            final int finalI = i;
            radioButton.setOnClickListener(view -> {
                categoryId = list.get(finalI).getCategoryId();
                page = 1;
                xgxshopgoodsList(null);

            });

            rg_type.addView(radioButton);
        }


    }

}
