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
import com.juner.mvp.Configure;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.fragment.ProductListFragment;
import com.eb.geaiche.adapter.Brandadapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.Category;
import com.juner.mvp.bean.CategoryBrandList;
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

    public static final int type = 4;
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

    private double TotalPrice;//总价格


    private Map<String, List<GoodsEntity>> listMap = new HashMap<>();//所有商品Map

    public static int isShow;//是否显示选择数量和价格 0不显示  1显示

    public static int setProject;

    @Override
    protected void init() {


        isShow = getIntent().getIntExtra(Configure.isShow, 0);
        setProject = getIntent().getIntExtra(Configure.setProject, -1);

        if (isShow == 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }

        tv_title.setText("商品列表");
        tv_totalPrice.append(String.valueOf(TotalPrice));


        replaceFragment();

        //旧接口
        Api().categoryBrandList().subscribe(new RxSubscribe<CategoryBrandList>(this, true) {
            @Override
            protected void _onNext(CategoryBrandList o) {
                listMap.put(o.getCategoryList().get(0).getId() + "", o.getGoodList());//保存初始List<GoodsEntity> key为种类id+品牌id

                fragment.switchData(o.getCategoryList().get(0).getId(), "", o.getGoodList());
                categories = o.getCategoryList();
                checkedTag = 0;
                for (int i = 0; i < categories.size(); i++) {
                    RadioButton radioButton = new RadioButton(ProductListActivity.this);
                    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 1, 0, 0);
                    radioButton.setPadding(0, 36, 0, 36);
                    radioButton.setText(categories.get(i).getName());
                    radioButton.setBackground(getResources().getDrawable(R.drawable.radiobutton_background_a));
                    radioButton.setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
                    radioButton.setGravity(Gravity.CENTER);
                    radioButton.setLayoutParams(layoutParams);
                    radioButton.setTag(i);
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showPopupWindow(view);
                        }
                    });

                    radioGroup.addView(radioButton);
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });

        //新接口
        xgxshopgoodsList(null);


    }

    /**
     * 新商品接口
     *
     * @param key 搜索关键字
     */

    private void xgxshopgoodsList(String key) {
        Api().xgxshopgoodsList(key, null, null, 1, type,50).subscribe(new RxSubscribe<GoodsList>(this, true) {
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
//                    onQueryAnyGoods4Key(et_key.getText().toString());
                    xgxshopgoodsList(et_key.getText().toString());
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;

        }


    }

    private void showPopupWindow(View v) {

        et_key.setText("");//清空搜索栏


        if (v.getTag() == checkedTag) {//判断当前选择的View Tag是否为已选中的View
            Log.d("radioGroup", "showPopupWindow+++当前选中的id为：" + v.getTag().toString());
            List<SubCategoryEntity> list = categories.get(checkedTag).getSubCategoryList();
            if (list.size() > 0) {
                //PopupWindow在rb向右弹弹出
                brandadapter.setNewData(list);
                popupWindow.showAsDropDown(v, v.getWidth(), -v.getHeight());
            }
        } else {

            checkedTag = (Integer) v.getTag();
            onQueryAnyGoods(categories.get(checkedTag).getId(), "", et_key.getText().toString());
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

    private void onQueryAnyGoods4Key(String name) {


        Api().queryAnyGoods(name).subscribe(new RxSubscribe<GoodsListEntity>(this, true) {
            @Override
            protected void _onNext(GoodsListEntity goodsListEntity) {

                List<GoodsEntity> brand_all = goodsListEntity.getGoodsList();//同brand品牌商品

                fragment.switchData("", "", brand_all);


            }

            @Override
            protected void _onError(String message) {
                Log.v("BaseQuickAdapter", message);
            }
        });


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


    public void onPulsTotalPrice(double t) {
        TotalPrice = TotalPrice + t;
        tv_totalPrice.setText(String.format("合计：￥%s", TotalPrice));

    }

    public void setListMap(String category_id, String brand_id, List<GoodsEntity> list) {
        this.listMap.put(category_id + brand_id, list);

    }


}
