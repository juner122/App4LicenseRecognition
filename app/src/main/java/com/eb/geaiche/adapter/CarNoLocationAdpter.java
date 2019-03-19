package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.CarNoLocation;

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
