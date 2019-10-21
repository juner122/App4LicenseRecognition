package com.eb.geaiche.activity.fragment;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MealListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.bean.Meal;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.juner.mvp.bean.GoodsEntity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ProductMealFragment extends BaseFragment {

    private static int id;
    private static String car_no;
    @BindView(R.id.rv)
    RecyclerView rv;
    MealListAdapter mealListAdapter;
    List<MultiItemEntity> list;

    public static ProductMealFragment getInstance(int user_id, String no) {
        ProductMealFragment sf = new ProductMealFragment();
        id = user_id;
        car_no = no;
        return sf;
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment_meal_list_fr;
    }


    @Override
    protected void setUpView() {
        mealListAdapter = new MealListAdapter(null,true);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
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


    }


    @Override
    protected void onVisible() {
        super.onVisible();

        Api().queryUserAct(id, car_no).subscribe(new RxSubscribe<Meal>(getContext(), true) {
            @Override
            protected void _onNext(Meal mealList) {
                list = generateData(mealList.getList());
                mealListAdapter.setNewData(list);
                mealListAdapter.expandAll();


            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.showToast("套餐列表为空");
            }
        });


    }

    private List<MultiItemEntity> generateData(Map<String, List<MealEntity>> map) {

        List<MultiItemEntity> res = new ArrayList<>();

        if (null == map || map.size() == 0) {
//            ToastUtils.showToast("没有可用套卡");
            return res;
        }


        for (List<MealEntity> list : map.values()) {

            MealL0Entity lv0 = new MealL0Entity(list.get(0).getActivityName(), list.get(0).getActivitySn());


            for (MealEntity entity : list) {
                lv0.setCarNo(entity.getCarNo());
                lv0.setEndTime(Long.parseLong(entity.getEndTime()));
                for (GoodsEntity ge : MyApplication.cartUtils.getMealList()) {
                    if (ge.getId().equals(String.valueOf(entity.getId()))) {
                        entity.setSelected(true);
                    }
                }
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
