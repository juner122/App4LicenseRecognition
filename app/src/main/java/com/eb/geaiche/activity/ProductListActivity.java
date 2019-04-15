package com.eb.geaiche.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.view.MyRadioButton;
import com.juner.mvp.Configure;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.fragment.ProductListFragment;
import com.eb.geaiche.adapter.Brandadapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.Category;
import com.juner.mvp.bean.CategoryBrandList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;
import com.juner.mvp.bean.GoodsListEntity;
import com.juner.mvp.bean.SubCategoryEntity;

import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductListActivity extends BaseActivity {

    public int type;
    @BindView(R.id.rg_type)
    RadioGroup radioGroup;


    @BindView(R.id.et_key)
    EditText et_key;


    @BindView(R.id.tv_totalPrice)
    TextView tv_totalPrice;

    @BindView(R.id.ll)
    View ll;

    ProductListFragment fragment;
    CommonPopupWindow popupWindow;
    RecyclerView commonPopupRecyclerView;
    Brandadapter brandadapter;
    Integer checkedTag;
    List<Category> categories;

    int page = 1;//第一页

    private Map<String, List<GoodsEntity>> listMap = new HashMap<>();//所有商品Map

    public static int isShow;//是否显示选择数量和价格 0不显示  1显示

    public static int setProject;
    String categoryId;//当前选的大分类索引id

    @Override
    protected void init() {
        type = getIntent().getIntExtra(Configure.Goods_TYPE, 0);
        if (type == Configure.Goods_TYPE_3) {
            tv_title.setText("服务列表");
        } else if (type == Configure.Goods_TYPE_4) {
            tv_title.setText("配件列表");
        }


        isShow = getIntent().getIntExtra(Configure.isShow, 0);
        setProject = getIntent().getIntExtra(Configure.setProject, -1);

        if (isShow == 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }


        replaceFragment();
        getPartsData();

        //新接口
        xgxshopgoodsList(null);


    }

    /**
     * 新商品接口
     *
     * @param key 搜索关键字
     */

    private void xgxshopgoodsList(String key) {
        Api().xgxshopgoodsList(key, null, categoryId, 1, type, 50).subscribe(new RxSubscribe<GoodsList>(this, true) {
            @Override
            protected void _onNext(GoodsList goods) {
                fragment.switchData(goods.getList());
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

                finish();
                break;
            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText())) {
                    SoftInputUtil.hideSoftInput(ProductListActivity.this, v);
                    xgxshopgoodsList(et_key.getText().toString());
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;

        }


    }


    @Override
    protected void setUpView() {
        commonPopupRecyclerView = new RecyclerView(this);
        commonPopupRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        brandadapter = new Brandadapter(null);
        brandadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.v("BaseQuickAdapter", "第" + position + "项");

                //根据商品类型，品牌，查询商品
                popupWindow.dismiss();

                onQueryAnyGoods(categories.get(checkedTag).getId(), categories.get(checkedTag).getSubCategoryList().get(position).getId(), et_key.getText().toString());

            }

        });
        commonPopupRecyclerView.setAdapter(brandadapter);

        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(commonPopupRecyclerView)
                .create();
    }


    private void onQueryAnyGoods(final String category_id, final String brand_id, String name) {


        if (null != listMap && null != listMap.get(category_id + brand_id)) {
            fragment.switchData(category_id, brand_id, listMap.get(category_id + brand_id));


        } else {
            Api().queryAnyGoods(category_id, brand_id, name).subscribe(new RxSubscribe<GoodsListEntity>(this, true) {
                @Override
                protected void _onNext(GoodsListEntity goodsListEntity) {

                    List<GoodsEntity> brand_all = goodsListEntity.getGoodsList();//同brand品牌商品

                    listMap.put(category_id + brand_id, brand_all);
                    fragment.switchData(category_id, brand_id, brand_all);


                }

                @Override
                protected void _onError(String message) {
                    Log.v("BaseQuickAdapter", message);
                }
            });
        }

    }


    @Override
    protected void setUpData() {

    }

    private void replaceFragment() {
        fragment = ProductListFragment.getInstance(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        transaction.commit();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list;
    }


    //获取分类数据
    private void getPartsData() {

        Api().queryShopcategoryAll(String.valueOf(Configure.Goods_TYPE_4)).subscribe(new RxSubscribe<List<GoodsCategory>>(this, true) {
            @Override
            protected void _onNext(List<GoodsCategory> categories) {
                if (null == categories || categories.size() == 0) {
                    ToastUtils.showToast("暂无分类！");
                    radioGroup.setVisibility(View.GONE);
                    return;
                }

                radioGroup.setVisibility(View.VISIBLE);
                init0Data(categories);//根据第一级类别数量 创建RadioButton
            }

            @Override
            protected void _onError(String message) {

            }
        });

    }

    public void init0Data(final List<GoodsCategory> list) {
        radioGroup.removeAllViews();//清除所有
        for (int i = 0; i < list.size(); i++) {
            MyRadioButton radioButton = new MyRadioButton(ProductListActivity.this, list.get(i).getName(), i);
            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    categoryId = list.get(finalI).getCategoryId();//一级分类id
                    page = 1;
                    xgxshopgoodsList(null);
                }
            });


            radioGroup.addView(radioButton);
        }
    }
}
