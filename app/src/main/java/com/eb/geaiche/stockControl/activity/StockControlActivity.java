package com.eb.geaiche.stockControl.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.MallGoodsActivity;
import com.eb.geaiche.activity.MallGoodsInfoActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.Meal2;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.stockControl.adapter.StockControlListAdapter;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.util.StockCartUtils;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StockControlActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.et_key)
    EditText et_key;

    @BindView(R.id.ll_button_view)
    View ll_button_view;

    int view_type = 1;//当前Activity显示类型 1 库存管理，2 采购入库

    StockControlListAdapter adapter;

    public static StockCartUtils stockCartUtils;

    @OnClick({R.id.stock_in, R.id.stock_out, R.id.iv_search, R.id.tv_back, R.id.tv_title_r})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_title_r:


                if (view_type == 2) {//进入入库单页面
                    toActivity(StockInActivity.class);
                } else {//新增商品
                    toActivity(StockAddGoodsActivity.class);
                }

                break;

            case R.id.stock_in:
                //显示采购入库页面
                showStockIn();
                break;
            case R.id.stock_out:
                toActivity(StockOutActivity.class);
                break;
            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText())) {
                    SoftInputUtil.hideSoftInput(StockControlActivity.this, v);
                    getData(et_key.getText().toString());
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;
            case R.id.tv_back:

                if (view_type == 1) {
                    finish();
                } else {
                    showMain();
                }

                break;
        }
    }


    //显示采购入库页面
    private void showStockIn() {
        view_type = 2;
        ll_button_view.setVisibility(View.GONE);
        tv_title.setText("采购入库");
        setRTitle("入库单");
    }

    //显示库存管理页面
    private void showMain() {
        view_type = 1;
        ll_button_view.setVisibility(View.VISIBLE);
        tv_title.setText("库存管理");
        setRTitle("新增商品");
    }

    @Override
    protected void init() {

        et_key.setHint("请输入你要查询的商品");
        stockCartUtils = StockCartUtils.getInstance(this);
    }



    @Override
    protected void setUpView() {
        showMain();
        adapter = new StockControlListAdapter(null, this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    @Override
    protected void setUpData() {
        getData("");

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stoc_kcontrol;
    }

    /**
     * 新商品接口
     *
     * @param key 获取配件数据
     */

    private void getData(String key) {
        Api().xgxshopgoodsList(key, null, null, 1, Configure.Goods_TYPE_4).subscribe(new RxSubscribe<GoodsList>(this, true) {
            @Override
            protected void _onNext(GoodsList goods) {
                adapter.setNewData(generateData(goods.getList()));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("配件列表获取失败！" + message);
            }
        });
    }


    private List<MultiItemEntity> generateData(List<Goods> list) {
        List<MultiItemEntity> res = new ArrayList<>();
        if (null == list || list.size() == 0) {
            return res;
        }

        for (int i = 0; i < list.size(); i++) {
            Goods lv0 = list.get(i);

            if (null != list.get(i).getXgxGoodsStandardPojoList() && list.get(i).getXgxGoodsStandardPojoList().size() > 0) {
                for (Goods.GoodsStandard gs : list.get(i).getXgxGoodsStandardPojoList()) {
                    if (null != gs) {
                        lv0.addSubItem(gs);

                    }
                }
            }
            res.add(lv0);
        }
        return res;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stockCartUtils.deleteAllData();
    }
}
