package com.eb.geaiche.stockControl.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.CarInfoInputActivity;
import com.eb.geaiche.adapter.GridImageAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.stockControl.adapter.PickBrandListAdapter;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.NullDataEntity;

import java.util.List;

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

    @BindView(R.id.rv)
    RecyclerView rv;//
    @BindView(R.id.rv2)
    RecyclerView rv2;//


    @BindView(R.id.cv)
    CardView cv;//

//    @BindView(R.id.rv_brand)
//    RecyclerView rv_brand;//品牌列表

    @BindView(R.id.rv_category)
    RecyclerView rv_category;//分类列表

    GridImageAdapter headAdapter;//商品头部图片
    GridImageAdapter infoAdapter;//商品详情图片


    PickBrandListAdapter aba;//品牌列表


    @OnClick({R.id.enter, R.id.ll_pick_brand, R.id.ll_pick_category})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter://新增商品

                addGood();
                break;
            case R.id.ll_pick_brand://选择品牌

//                addGood();
                break;
            case R.id.ll_pick_category://选择分类

                showRvCategory();
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

    private void addGood() {
        if (TextUtils.isEmpty(name.getText())) {
            ToastUtils.showToast("商品名称不能为空！");
            return;
        }
        if (null == brand || null == brand.getBrandId()) {
            ToastUtils.showToast("商品品牌不能为空！");
            return;
        }
        if (firstCategoryId == 0) {
            ToastUtils.showToast("商品分类不能为空！");
            return;
        }

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

    }


    //显示分类列表
    private void showRvCategory() {
        cv.setVisibility(View.VISIBLE);


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
        aba = new PickBrandListAdapter(null, this);
        rv_category.setAdapter(aba);
        rv_category.setLayoutManager(new LinearLayoutManager(this));
        //获取分类
        Api().queryShopcategoryAll(String.valueOf(Configure.Goods_TYPE_4)).subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> list) {
                if (null == list || list.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    return;
                }
                aba.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取分类失败" + message);
            }
        });

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_stock_add_goods;
    }
}
