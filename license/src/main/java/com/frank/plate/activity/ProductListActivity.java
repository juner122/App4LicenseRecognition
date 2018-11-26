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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.R;
import com.frank.plate.activity.fragment.ProductListFragment;
import com.frank.plate.adapter.Brandadapter;

import com.frank.plate.api.MySubscriber;
import com.frank.plate.api.SubscribeOnNextListener;
import com.frank.plate.bean.Category;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.SubCategoryEntity;
import com.frank.plate.view.CommonPopupWindow;
import com.tamic.novate.Throwable;

import java.util.List;

import butterknife.BindView;

public class ProductListActivity extends BaseActivity {

    @BindView(R.id.rg_type)
    RadioGroup radioGroup;
    ProductListFragment fragment;
    CommonPopupWindow popupWindow;
    RecyclerView commonPopupRecyclerView;
    Brandadapter brandadapter;
    Integer checkedTag;

    List<Category> categories;


    @Override
    protected void init() {
        tv_title.setText("商品列表");
        replaceFragment();

        Api().categoryBrandList(new MySubscriber<>(this, new SubscribeOnNextListener<CategoryBrandList>() {
            @Override
            public void onNext(CategoryBrandList categoryBrandList) {
                fragment.switchData(categoryBrandList.getGoodList());

                categories = categoryBrandList.getCategoryList();
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
            public void onError(Throwable e) {
                Toast.makeText(ProductListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));


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
                //设置PopupWindow布局
//                .setView(R.layout.popup)
                .setView(commonPopupRecyclerView)
                //设置动画
//                .setAnimationStyle(R.style.animHorizontal)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
//                .setBackGroundLevel(0.5f)
                //设置PopupWindow里的子View及点击事件
                //开始构建
                .create();
    }


    private void onQueryAnyGoods(String category_id, String brand_id, String name) {

        Toast.makeText(ProductListActivity.this, "商品类别ID：" + category_id + "\n品牌ID:" + brand_id, Toast.LENGTH_SHORT).show();


        Api().queryAnyGoods(new MySubscriber<>(ProductListActivity.this, new SubscribeOnNextListener<GoodsListEntity>() {
            @Override
            public void onNext(GoodsListEntity goodsListEntity) {
                fragment.switchData(goodsListEntity.getGoodsList());

            }

            @Override
            public void onError(Throwable e) {
                Log.v("BaseQuickAdapter", e.getMessage());
            }
        }), category_id, brand_id, name);
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

}
