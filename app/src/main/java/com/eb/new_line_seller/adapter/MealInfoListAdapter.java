package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.MealEntity;
import com.juner.mvp.bean.CarInfoRequestParameters;

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
