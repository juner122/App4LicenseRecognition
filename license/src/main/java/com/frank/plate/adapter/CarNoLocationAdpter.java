package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.CarNoLocation;
import com.frank.plate.bean.GoodsEntity;

import java.util.List;

public class CarNoLocationAdpter extends BaseQuickAdapter<CarNoLocation, BaseViewHolder> {


    public CarNoLocationAdpter(@Nullable List<CarNoLocation> data) {
        super(R.layout.dialog_pick_car_no_location_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CarNoLocation item) {


        helper.setText(R.id.tv_location, item.getLocation());

    }

}
