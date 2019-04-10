package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;


import com.eb.geaiche.adapter.ServeListAdapter;
import com.eb.geaiche.api.RxSubscribe;


import com.eb.geaiche.util.SoftInputUtil;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;
import com.juner.mvp.bean.Server;
import com.juner.mvp.bean.ServerList;

import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ServeListActivity extends BaseActivity {


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

    ServeListAdapter serveListAdapter;
    List<Goods> servers = new ArrayList<>();//所有数据


    @Override
    protected void init() {


        //计算总价
        count();

        tv_title.setText("服务工时列表");

        serveListAdapter = new ServeListAdapter(servers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serveListAdapter);

        serveListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (servers.get(position).isSelected()) {
                    servers.get(position).setSelected(false);
                    MyApplication.cartUtils.reduceData(toGoodsEntity(servers.get(position)));
                } else {
                    servers.get(position).setSelected(true);
                    MyApplication.cartUtils.addData(toGoodsEntity(servers.get(position)));
                }

                //计算总价
                count();
                adapter.notifyDataSetChanged();
            }
        });


        xgxshopgoodsList("");

    }

    private void xgxshopgoodsList(String key) {

        Api().xgxshopgoodsList(key, null, null, 1, 3).subscribe(new RxSubscribe<GoodsList>(this, true) {
            @Override
            protected void _onNext(GoodsList goodsList) {
                servers = goodsList.getList();
                for (GoodsEntity ps : MyApplication.cartUtils.getServerList()) {
                    for (int i = 0; i < servers.size(); i++) {
                        if (ps.getGoods_id() == servers.get(i).getId()) {
                            servers.get(i).setSelected(true);
                        }
                    }
                }

                serveListAdapter.setNewData(servers);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }


    @OnClick({R.id.but_enter_order, R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_enter_order:

                Log.i("TAG", "选择的服务有" + MyApplication.cartServerUtils.getServerList().size() + "个  " + MyApplication.cartServerUtils.getServerList().toString());

                finish();


                break;
            case R.id.iv_search:

                SoftInputUtil.hideSoftInput(this, v);

                if (!TextUtils.isEmpty(et_key.getText()))
                    xgxshopgoodsList(et_key.getText().toString());
                else

                    ToastUtils.showToast("请输入搜索关键字！");

                break;
        }

    }


    private void count() {
        tv_totalPrice.setText(String.format("合计：￥%s", MathUtil.twoDecimal(MyApplication.cartServerUtils.getServerPrice())));    //计算总价格

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

    private GoodsEntity toGoodsEntity(Goods goods) {
        GoodsEntity goodsEntity = new GoodsEntity();

        //暂时用第一个规格
        goods.setGoodsStandard(goods.getXgxGoodsStandardPojoList().get(0));

        goodsEntity.setGoodsId(goods.getId());
        goodsEntity.setGoods_id(goods.getId());
        goodsEntity.setId(goods.getId());
        goodsEntity.setName(goods.getGoodsTitle());
        goodsEntity.setGoods_name(goods.getGoodsTitle());
        goodsEntity.setGoodsName(goods.getGoodsTitle());
        goodsEntity.setGoods_sn(goods.getGoodsCode());
        goodsEntity.setType(goods.getType());
        goodsEntity.setProduct_id(goods.getGoodsStandard().getId());
        goodsEntity.setNumber(goods.getNum());
        goodsEntity.setMarket_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setRetail_price(goods.getGoodsStandard().getGoodsStandardPrice());
        goodsEntity.setGoods_specifition_name_value(goods.getGoodsStandard().getGoodsStandardTitle());
        goodsEntity.setFirstCategoryId(goods.getFirstCategoryId());
        return goodsEntity;
    }
}
