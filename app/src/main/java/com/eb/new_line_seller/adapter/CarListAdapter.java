package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.CarInfoRequestParameters;


import java.util.List;

public class CarListAdapter extends BaseQuickAdapter<CarInfoRequestParameters, BaseViewHolder> {

    public CarListAdapter(@Nullable List<CarInfoRequestParameters> data) {
        super(R.layout.activity_member_info_car_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoRequestParameters item) {

        helper.setText(R.id.tv_car_no, item.getCarNo())
                .addOnClickListener(R.id.tv_check_car);


        ImageView iv = helper.getView(R.id.iv);

        if (item.isSelected())
            iv.setImageResource(R.mipmap.icon_pick);
        else
            iv.setImageResource(R.mipmap.icon_unpick);


    }


}
