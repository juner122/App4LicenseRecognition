package com.eb.geaiche.stockControl.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.NullDataEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class StockAddGoodsActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_type)
    TextView tv_type;//分类
    @BindView(R.id.tv_brand)
    TextView tv_brand;//品牌
    @BindView(R.id.sw)
    Switch sw;//是否推广
    @BindView(R.id.num)
    TextView num;//
    @BindView(R.id.et_remarks)
    EditText et_remarks;//

    GoodsBrand brand;//商品品牌
    int firstCategoryId;//分类id
    String firstCategoryTitle;//分类名


    @OnClick({R.id.enter})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.enter://新增商品
                Api().addGoods(getGoods()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        ToastUtils.showToast("操作成功！");
                        finish();

                    }

                    @Override
                    protected void _onError(String message) {

                        ToastUtils.showToast("操作失败！" + message);

                    }
                });
                break;

        }
    }

    private Goods getGoods() {

        Goods goods = new Goods();

        goods.setGoodsTitle(name.getText().toString());
        goods.setGoodsBrandId(brand.getBrandId());
        goods.setGoodsBrandTitle(brand.getBrandTitle());
        goods.setGoodsBrandTitle(brand.getBrandTitle());
        goods.setFirstCategoryId(firstCategoryId);
        goods.setFirstCategoryTitle(firstCategoryTitle);
        goods.setType(4);//固定为 4 零件
        goods.setWxType(sw.isChecked() ? 1 : 2);//是否微信上架1是2否
        goods.setRemindLimit(Integer.valueOf(num.getText().toString()));//库存提醒设置数字
        goods.setRemarks(et_remarks.getText().toString());//库存备注

        return goods;
    }


    @Override
    protected void init() {
        tv_title.setText("新增商品");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_add_goods;
    }
}
