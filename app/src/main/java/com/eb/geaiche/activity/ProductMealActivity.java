package com.eb.geaiche.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MealListAdapter;
import com.eb.geaiche.adapter.MealListAdapter2;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.Meal;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.util.SoftInputUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.zbar.CaptureActivity;
import com.juner.mvp.Configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductMealActivity extends BaseActivity {
    private int id;
    private String car_no;

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.but_enter_order)
    View but_enter_order;
    MealListAdapter mealListAdapter;
    List<MultiItemEntity> list;

    boolean isShow;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_meal;
    }

    @Override
    protected void init() {
        tv_title.setText("可用套卡");

        id = getIntent().getIntExtra(Configure.user_id, 0);
        car_no = getIntent().getStringExtra(Configure.car_no);
        isShow = getIntent().getBooleanExtra("isShow", false);

        if (isShow) {
            setRTitle("套卡扫码");
            but_enter_order.setVisibility(View.VISIBLE);
        } else {
            but_enter_order.setVisibility(View.GONE);
        }

    }

    @Override
    protected void setUpView() {

        mealListAdapter = new MealListAdapter(null, isShow);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mealListAdapter);
        mealListAdapter.setEmptyView(R.layout.order_list_empty_view_p, rv);

        mealListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            try {
                MealEntity m = (MealEntity) adapter.getData().get(position);
                if (m.getNumber() == 0) {
                    return;
                }
                if (m.isSelected()) {
                    m.setSelected(false);

                    MyApplication.cartUtils.reduceMeal(m);
                } else {
                    m.setSelected(true);
                    MyApplication.cartUtils.addMeal(m);
                }
                adapter.notifyDataSetChanged();
            } catch (ClassCastException E) {
                E.printStackTrace();
            }


        });


        Api().queryUserAct(id, car_no).subscribe(new RxSubscribe<Meal>(this, true) {
            @Override
            protected void _onNext(Meal mealList) {

                list = generateData(mealList.getList());
                mealListAdapter.setNewData(list);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }

    @Override
    protected void setUpData() {

    }

    private List<MultiItemEntity> generateData(Map<String, List<MealEntity>> map) {
        List<MultiItemEntity> res = new ArrayList<>();

        if (null == map || map.size() == 0) {
            ToastUtils.showToast("没可用套卡");
            return res;
        }


        for (List<MealEntity> list : map.values()) {

            MealL0Entity lv0 = new MealL0Entity(list.get(0).getActivityName(), list.get(0).getActivitySn());

            for (MealEntity entity : list) {
                lv0.setCarNo(entity.getCarNo());
                lv0.setEndTime(Long.parseLong(entity.getEndTime()));
                lv0.addSubItem(entity);
            }
            res.add(lv0);

        }
        return res;
    }

    @OnClick({R.id.tv_title_r, R.id.but_enter_order})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_title_r://套卡扫码
                toActivity(CaptureActivity.class, "view_type", 1);
                finish();
                break;

            case R.id.but_enter_order://确认选择
                MyApplication.cartUtils.commit();//确认商品
                finish();
                break;


        }


    }
}
