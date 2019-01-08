package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.MealPickListAdapter;
import com.eb.new_line_seller.bean.Meal2;
import com.eb.new_line_seller.bean.MealL0Entity;
import com.eb.new_line_seller.mvp.contacts.PickMealCardContacts;
import com.eb.new_line_seller.mvp.presenter.PickMealCardPtr;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


//纸卡录入 选择套卡
public class PickMealCardActivity extends BaseActivity<PickMealCardContacts.PickMealCardPtr> implements PickMealCardContacts.PickMealCardUI {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_enter_order)
    TextView tv_enter_order;//确认选择

    MealPickListAdapter adapter;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_pick_meal_card;
    }

    @Override
    protected void init() {
        tv_title.setText("选择套卡");
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealPickListAdapter(null);
        rv.setAdapter(adapter);

        getPresenter().getMealList();//获取列表

        getPresenter().setOnItemChildClickListener(adapter);

    }

    @OnClick({R.id.tv_enter_order})
    public void onClick(View v) {

        getPresenter().confirmPick();

    }

    @Override
    public PickMealCardContacts.PickMealCardPtr onBindPresenter() {
        return new PickMealCardPtr(this);
    }

    @Override
    public void setMealList(List mealList) {
        adapter.setNewData(mealList);
    }

    @Override
    public void setPickMealName(String name) {
        tv_name.setText(name);
    }

    @Override
    public void onConfirmPick(Meal2 meal2) {
        toActivity(ActivateCardActivity.class, meal2, "Meal2");

    }


}
