package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MealListAdapter2;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.Meal;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ProductMealActivity extends BaseActivity {
    private int id;
    private String car_no;

    @BindView(R.id.rv)
    RecyclerView rv;
    MealListAdapter2 mealListAdapter;
    List<MultiItemEntity> list;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_product_meal;
    }

    @Override
    protected void init() {
        tv_title.setText("可用套卡");
        id = getIntent().getIntExtra(Configure.user_id, 0);
        car_no = getIntent().getStringExtra(Configure.car_no);

    }

    @Override
    protected void setUpView() {

        mealListAdapter = new MealListAdapter2(null);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mealListAdapter);
        mealListAdapter.setEmptyView(R.layout.order_list_empty_view_p, rv);

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

}
