package com.eb.geaiche.stockControl.activity;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.NullDataEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class StockAddStandardsActivity extends BaseActivity {


    @BindView(R.id.tv_name)
    TextView tv_name;//商品名
    @BindView(R.id.tv_standard)
    EditText tv_standard;
    @BindView(R.id.tv_price)
    EditText tv_price;//销售价
    @BindView(R.id.tv_price_in)
    EditText tv_price_in;//成本入库价
    @BindView(R.id.supplier)
    TextView supplier_name;//供应商名


    Integer goodsId;
    int goodsStandardId;//规格id 修改时用

    Goods.GoodsStandard goodsStandard;//查询到的规格对象

    String goodsTitle;

    Supplier supplier_pick;//选择了的供应商

    @OnClick({R.id.enter, R.id.reset, R.id.pick_supplier})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.enter:
                if (goodsStandardId == 0)
                    addStandard();//新增
                else
                    fixStandard();
                break;

            case R.id.reset://重置
                break;

            case R.id.pick_supplier:
                toActivity(SupplierListActivity.class);

                break;
        }
    }


    @Override
    protected void init() {
        goodsStandardId = getIntent().getIntExtra("goodsStandardId", 0);
        goodsTitle = getIntent().getStringExtra("goodsTitle");
        tv_name.setText(goodsTitle);
        if (goodsStandardId == 0) {//新增页面
            goodsId = getIntent().getIntExtra("goodsId", 0);
            tv_title.setText("新增规格");
        } else {
            tv_title.setText("规格信息");
            getInfo();

        }


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_add_standards;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getIntExtra("pick_type", -1) == 1) {//在供应商列表选择供应商后返回
            supplier_pick = intent.getParcelableExtra("supplier");
            supplier_name.setText(supplier_pick.getName());
        }


    }

    //新增规格
    private void addStandard() {
        if (null == supplier_pick) {
            ToastUtils.showToast("请选择供应商！");
            return;
        }

        Api().addStandard(getStandard()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("新增成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("新增失败！" + message);
            }
        });
    }

    //修改规格
    private void fixStandard() {

        Api().updateStandard(getStandard()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("修改成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败！" + message);
            }
        });
    }


    private Goods.GoodsStandard getStandard() {

        Goods.GoodsStandard standard = new Goods.GoodsStandard();

        if (null != goodsId)
            standard.setGoodsId(goodsId);

        standard.setGoodsTitle(tv_name.getText().toString());
        standard.setGoodsStandardTitle(tv_standard.getText().toString());
        standard.setGoodsStandardPrice(tv_price.getText().toString());
        standard.setStockPrice(tv_price_in.getText().toString());
        standard.setStock("0");//库存数量
        standard.setSupplierName(supplier_name.getText().toString());//供应商
        if (null != supplier_pick)
            standard.setSupplierId(supplier_pick.getId());//供应商名

        if (null != goodsStandard) {
            standard.setGoodsId(goodsStandard.getGoodsId());
            standard.setId(goodsStandard.getId());
            standard.setGoodsStandardId(goodsStandard.getGoodsStandardId());
            if (null == supplier_pick) {
                standard.setSupplierId(goodsStandard.getSupplierId());//供应商名
            } else {
                standard.setSupplierId(supplier_pick.getId());//供应商名

            }
        }
        return standard;

    }


    //查询规格信息
    private void getInfo() {
        Api().standardInfo(goodsStandardId).subscribe(new RxSubscribe<Goods.GoodsStandard>(this, true) {
            @Override
            protected void _onNext(Goods.GoodsStandard gs) {
                goodsStandard = gs;
                if (null != gs.getGoodsTitle())
                    tv_name.setText(gs.getGoodsTitle());

                tv_standard.setText(gs.getGoodsStandardTitle());
                tv_price.setText(gs.getGoodsStandardPrice());
                tv_price_in.setText(gs.getStockPrice());
                supplier_name.setText(gs.getSupplierName());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查询规格信息失败！" + message);
            }
        });
    }

}
