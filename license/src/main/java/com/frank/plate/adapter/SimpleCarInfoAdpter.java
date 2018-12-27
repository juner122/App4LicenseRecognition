package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;

import com.frank.plate.bean.CarInfoRequestParameters;


import java.util.List;

public class SimpleCarInfoAdpter extends BaseQuickAdapter<CarInfoRequestParameters, BaseViewHolder> {

    public SimpleCarInfoAdpter(@Nullable List<CarInfoRequestParameters> data) {
        super(R.layout.activity_car_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoRequestParameters item) {

        helper.setText(R.id.tv_car_no, item.getCarNo())
                .setText(R.id.tv_car_model, item.getBrand() + "\t" + item.getName())
                .addOnClickListener(R.id.tv_check_car);

        ImageView iv = helper.getView(R.id.iv);

        if (item.isSelected())
            iv.setImageResource(R.mipmap.icon_pick);
        else
            iv.setImageResource(R.mipmap.icon_unpick);

        if (helper.getLayoutPosition() == getData().size() - 1) {

            helper.setVisible(R.id.v, false);
        } else
            helper.setVisible(R.id.v, true);


    }


}
