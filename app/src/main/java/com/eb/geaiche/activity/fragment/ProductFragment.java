package com.eb.geaiche.activity.fragment;


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


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;


import com.eb.geaiche.adapter.Brandadapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.SoftInputUtil;
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

public class ProductFragment extends BaseFragment {
    public static final int type = 4;

    @BindView(R.id.rg_type)
    RadioGroup radioGroup;
    @BindView(R.id.et_key)
    EditText et_key;

    ProductListFragment fragment;
    CommonPopupWindow popupWindow;
    RecyclerView commonPopupRecyclerView;
    Brandadapter brandadapter;
    Integer checkedTag;
    List<Category> categories;

    private Map<String, List<GoodsEntity>> listMap = new HashMap<>();//所有商品Map


    public static ProductFragment getInstance() {
        ProductFragment sf = new ProductFragment();

        return sf;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_product_list_fr;
    }


    @Override
    protected void setUpView() {


        replaceFragment();


        Api().categoryBrandList().subscribe(new RxSubscribe<CategoryBrandList>(getContext(), true) {
            @Override
            protected void _onNext(CategoryBrandList o) {
                listMap.put(o.getCategoryList().get(0).getId() + "", o.getGoodList());//保存初始List<GoodsEntity> key为种类id+品牌id

                fragment.switchData(o.getCategoryList().get(0).getId(), "", o.getGoodList());
                categories = o.getCategoryList();
                checkedTag = 0;
                for (int i = 0; i < categories.size(); i++) {
                    RadioButton radioButton = new RadioButton(getContext());
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

        commonPopupRecyclerView = new RecyclerView(getContext());
        commonPopupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        popupWindow = new CommonPopupWindow.Builder(getContext())
                .setView(commonPopupRecyclerView)
                .create();


        //新接口
        xgxshopgoodsList(null);


    }

    /**
     * 新商品接口
     *
     * @param key 搜索关键字
     */

    private void xgxshopgoodsList(String key) {
        Api().xgxshopgoodsList(key, null, null, 1, type).subscribe(new RxSubscribe<GoodsList>(getContext(), true) {
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
            onQueryAnyGoods(categories.get(checkedTag).getId(), "", "");
        }
    }

    private void onQueryAnyGoods(final String category_id, final String brand_id, String name) {


        if (null != listMap && null != listMap.get(category_id + brand_id)) {
            fragment.switchData(category_id, brand_id, listMap.get(category_id + brand_id));


        } else {
            Api().queryAnyGoods(category_id, brand_id, name).subscribe(new RxSubscribe<GoodsListEntity>(getContext(), true) {
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


        Api().queryAnyGoods(name).subscribe(new RxSubscribe<GoodsListEntity>(getContext(), true) {
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

    @OnClick({R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText())) {
//                    onQueryAnyGoods4Key(et_key.getText().toString());
                    xgxshopgoodsList(et_key.getText().toString());
                    SoftInputUtil.hideSoftInput(getContext(), et_key);
                } else
                    ToastUtils.showToast("请输入搜索关键字！");

                break;

        }


    }

    private void replaceFragment() {
        fragment = ProductListFragment.getInstance(1);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        transaction.commit();
    }


    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
