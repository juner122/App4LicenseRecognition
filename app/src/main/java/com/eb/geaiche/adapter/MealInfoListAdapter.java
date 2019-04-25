package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.MealEntity;

import java.util.List;

public class MealInfoListAdapter extends BaseQuickAdapter<MealEntity, BaseViewHolder> {

    public MealInfoListAdapter(@Nullable List<MealEntity> data) {
        super(R.layout.activity_pick_meal_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MealEntity item) {

        helper.setText(R.id.tv_name, item.getName()).setText(R.id.tv_number, "x"+item.getNumber());


    }


}
