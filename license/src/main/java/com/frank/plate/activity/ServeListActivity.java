package com.frank.plate.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;

import com.frank.plate.activity.fragment.ServeListFragment;

import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Category;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.GoodsListEntity;

import com.frank.plate.util.ToastUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ServeListActivity extends BaseActivity {


    @BindView(R.id.rg_type)
    RadioGroup radioGroup;


    @BindView(R.id.et_key)
    EditText et_key;


    @BindView(R.id.tv_totalPrice)
    TextView tv_totalPrice;

    @BindView(R.id.ll)
    View ll;
    @BindView(R.id.ll_search)
    View ll_search;

    ServeListFragment fragment;


    Integer checkedTag;
    List<Category> categories;

    private double TotalPrice;//总价格


    private Map<String, List<GoodsEntity>> listMap = new HashMap<>();//所有商品Map

    public static int isShow;//是否显示选择数量和价格 0不显示  1显示

    @Override
    protected void init() {
        ll_search.setVisibility(View.GONE);


        isShow = getIntent().getIntExtra(Configure.isShow, 0);

        if (isShow == 0) {
            ll.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.VISIBLE);
        }


        if (getIntent().getBooleanExtra(Configure.isFixOrder, false))//是否是修改订单 清空购物车
            MyApplication.cartUtils.deleteAllData();


        tv_title.setText("服务工时列表");
        tv_totalPrice.append(String.valueOf(TotalPrice));


        replaceFragment();

        Api().categoryServeList().subscribe(new RxSubscribe<CategoryBrandList>(this, true) {
            @Override
            protected void _onNext(CategoryBrandList o) {
                listMap.put(o.getCategoryList().get(0).getId() + "", o.getGoodList());//保存初始List<GoodsEntity> key为种类id+品牌id

                fragment.switchData(o.getCategoryList().get(0).getId(), o.getGoodList());
                categories = o.getCategoryList();
                checkedTag = 0;
                for (int i = 0; i < categories.size(); i++) {
                    RadioButton radioButton = new RadioButton(ServeListActivity.this);
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


    }

    @OnClick({R.id.but_enter_order, R.id.iv_search})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_enter_order:

                finish();
                break;
            case R.id.iv_search:

                if (!TextUtils.isEmpty(et_key.getText()))
                    onQueryAnyGoods("", et_key.getText().toString());
                else

                    ToastUtils.showToast("请输入搜索关键字！");

                break;

        }


    }

    private void showPopupWindow(View v) {

        if (v.getTag() == checkedTag) {//判断当前选择的View Tag是否为已选中的View
            Log.d("radioGroup", "showPopupWindow+++当前选中的id为：" + v.getTag().toString());
        } else {
            checkedTag = (Integer) v.getTag();
            onQueryAnyGoods(categories.get(checkedTag).getId(), "");
        }
    }


    @Override
    protected void setUpView() {
    }


    private void onQueryAnyGoods(final String category_id, String name) {


        if (null != listMap && null != listMap.get(category_id)) {
            fragment.switchData(category_id, listMap.get(category_id));


        } else {
            Api().queryAnyGoods(category_id, "", name).subscribe(new RxSubscribe<GoodsListEntity>(this, true) {
                @Override
                protected void _onNext(GoodsListEntity goodsListEntity) {

                    List<GoodsEntity> brand_all = goodsListEntity.getGoodsList();//同brand品牌商品

                    listMap.put(category_id, brand_all);
                    fragment.switchData(category_id, brand_all);

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
        fragment = new ServeListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_layout, fragment);
        transaction.commit();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_list;
    }


    public void onPulsTotalPrice() {
        tv_totalPrice.setText(String.format("合计：￥%s", MyApplication.cartUtils.getServerPrice()));

    }

}
