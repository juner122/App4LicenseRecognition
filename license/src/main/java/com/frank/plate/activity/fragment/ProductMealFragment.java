package com.frank.plate.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.adapter.MealListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Meal;
import com.frank.plate.bean.MealEntity;
import com.frank.plate.bean.MealL0Entity;
import com.frank.plate.util.ToastUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ProductMealFragment extends BaseFragment {

    private static int id;
    @BindView(R.id.rv)
    RecyclerView rv;
    MealListAdapter mealListAdapter;
    List<MultiItemEntity> list;

    public static ProductMealFragment getInstance(int user_id) {
        ProductMealFragment sf = new ProductMealFragment();
        id = user_id;
        return sf;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_meal_list_fr;
    }


    @Override
    protected void setUpView() {
        mealListAdapter = new MealListAdapter(this, null);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mealListAdapter);
        mealListAdapter.setEmptyView(R.layout.order_list_empty_view_p, rv);

        mealListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                CheckBox checkBox = (CheckBox) view;
                ToastUtils.showToast(checkBox.isChecked() ? "选中" : "取消");


                MealEntity m = (MealEntity) adapter.getData().get(position);


                if (checkBox.isChecked())
                    MyApplication.cartUtils.addMeal(m);
                else
                    MyApplication.cartUtils.reduceMeal(m);


            }
        });


    }


    @Override
    protected void onVisible() {
        super.onVisible();

        Api().queryUserAct(id).subscribe(new RxSubscribe<Meal>(getContext(), true) {
            @Override
            protected void _onNext(Meal mealList) {
                list = generateData(mealList.getList());
                mealListAdapter.setNewData(list);

            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.showToast("套餐列表为空");
            }
        });


    }

    private List<MultiItemEntity> generateData(Map<String, List<MealEntity>> map) {

        List<MultiItemEntity> res = new ArrayList<>();

        for (List<MealEntity> list : map.values()) {

            MealL0Entity lv0 = new MealL0Entity(list.get(0).getActivityName(), list.get(0).getActivitySn());

            for (MealEntity entity : list) {
                lv0.addSubItem(entity);
            }
            res.add(lv0);

        }
        return res;
    }


    public static final String TAG = "ProductListFragment";

    @Override
    protected String setTAG() {
        return TAG;
    }

}
