package com.frank.plate.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.activity.fragment.ProductFragment;
import com.frank.plate.activity.fragment.ProductMealFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductMealListActivity extends BaseActivity {


    @BindView(R.id.st)
    SlidingTabLayout stl;
    @BindView(R.id.vp)
    ViewPager vp;

    @BindView(R.id.tv_totalPrice)
    TextView tv_totalPrice;


    private String[] title = {"商品列表", "套餐列表"};

    ArrayList<Fragment> fragments = new ArrayList<>();
    public static int user_id;//


    @BindView(R.id.ll)
    View ll;

    private double TotalPrice;//总价格

    boolean isFixOrder;

    @Override
    protected void init() {
        tv_title.setText("商品套餐列表");
        user_id = getIntent().getIntExtra(Configure.user_id, 0);
        isFixOrder = getIntent().getBooleanExtra(Configure.isFixOrder, false);

        if (isFixOrder)//是否是修改订单 清空购物车
            MyApplication.cartUtils.deleteAllData();


        fragments.add(new ProductFragment());
        fragments.add(ProductMealFragment.getInstance(user_id));
        onPulsTotalPrice(MyApplication.cartUtils.getProductPrice());

    }

    @Override
    protected void setUpView() {

        stl.setViewPager(vp, title, this, fragments);
    }

    @Override
    protected void setUpData() {

    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_meal_list;
    }


    public void onPulsTotalPrice(double t) {

        tv_totalPrice.setText(String.format("合计：￥%s", t));

    }


    @OnClick({R.id.but_enter_order})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_enter_order:
                Log.e("购物车+++", MyApplication.cartUtils.getDataFromLocal().toString());

                finish();
                break;

        }


    }


}
