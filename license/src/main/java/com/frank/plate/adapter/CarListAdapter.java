package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.CarEntity;


import java.util.List;

public class CarListAdapter extends BaseQuickAdapter<CarEntity, BaseViewHolder> {

    public CarListAdapter(@Nullable List<CarEntity> data) {
        super(R.layout.activity_member_info_car_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarEntity item) {

        helper.setText(R.id.tv_car_no, item.getCarNo());

    }
}
