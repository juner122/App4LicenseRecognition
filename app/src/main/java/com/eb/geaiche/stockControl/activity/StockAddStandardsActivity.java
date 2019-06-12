package com.eb.geaiche.stockControl.activity;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
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
    TextView supplier;//供应商名


    String supplierId;

    int goodsId;
    String goodsTitle;

    @OnClick({R.id.enter, R.id.reset})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.enter:
                addStandard();//新增
                break;

            case R.id.reset://重置


                break;
        }
    }


    @Override
    protected void init() {


        goodsId = getIntent().getIntExtra("goodsId", 0);
        goodsTitle = getIntent().getStringExtra("goodsTitle");

    }

    @Override
    protected void setUpView() {
        tv_title.setText("新增规格");
        tv_name.setText(goodsTitle);
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


    }

    //新增规格
    private void addStandard() {

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

    private Goods.GoodsStandard getStandard() {

        Goods.GoodsStandard standard = new Goods.GoodsStandard();
        standard.setGoodsId(goodsId);
        standard.setGoodsStandardTitle(tv_standard.getText().toString());
        standard.setGoodsStandardPrice(tv_price.getText().toString());
        standard.setStockPrice(tv_price_in.getText().toString());
        standard.setStock("0");//库存数量
        standard.setSupplierName(supplier.getText().toString());//供应商
        standard.setSupplierId(supplierId);//供应商名

        return standard;


    }

}
