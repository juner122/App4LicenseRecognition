package com.frank.plate.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.activity.fragment.ProductListFragment;
import com.frank.plate.adapter.Brandadapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Category;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.SubCategoryEntity;
import com.frank.plate.util.CartUtils;
import com.frank.plate.view.CommonPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ProductListActivity extends BaseActivity {

    @BindView(R.id.rg_type)
    RadioGroup radioGroup;


    @BindView(R.id.tv_totalPrice)
    TextView tv_totalPrice;
    ProductListFragment fragment;
    CommonPopupWindow popupWindow;
    RecyclerView commonPopupRecyclerView;
    Brandadapter brandadapter;
    Integer checkedTag;
    List<Category> categories;

    private double TotalPrice;//总价格


    private Map<String, List<GoodsEntity>> listMap = new HashMap<>();//所有商品Map


    private List<GoodsEntity> pickGoods;//选择了的商品


    @Override
    protected void init() {




        tv_title.setText("商品列表");
        tv_totalPrice.append(String.valueOf(TotalPrice));


        replaceFragment();

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
                    radioButton.setBackground(ContextCompat.getDrawable(ProductListActivity.this, R.drawable.radiobutton_background_a));
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
                Toast.makeText(ProductListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @OnClick({R.id.but_enter_order})
    public void onClick() {

      finish();
    }

    private void showPopupWindow(View v) {

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
            onQueryAnyGoods(categories.get(checkedTag).getId(), "", "");
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

                onQueryAnyGoods(categories.get(checkedTag).getId(), categories.get(checkedTag).getSubCategoryList().get(position).getId(), "");

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

//                    List<GoodsEntity> all = listMap.get(category_id);//同category_id种类下所有商品
                    List<GoodsEntity> brand_all = goodsListEntity.getGoodsList();//同brand品牌商品
//                    if (null != all) {
//
//                        for (int i = 0; i < all.size(); i++) {
//                            GoodsEntity category_good = all.get(i);
//                            for (int j = 0; j < brand_all.size(); j++) {
//                                if (category_good.getBrand_id().equals(brand_all.get(j).getBrand_id())) {
//
//                                    brand_all.get(j).setNumber(category_good.getNumber());
//                                }
//                            }
//                        }
//
//                    }
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
        fragment = new ProductListFragment();
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
