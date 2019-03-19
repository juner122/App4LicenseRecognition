package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.CarInfoRequestParameters;


import java.util.List;

public class CarListAdapter extends BaseQuickAdapter<CarInfoRequestParameters, BaseViewHolder> {

    public CarListAdapter(@Nullable List<CarInfoRequestParameters> data) {
        super(R.layout.activity_member_info_car_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarInfoRequestParameters item) {


        ImageView iv = helper.getView(R.id.iv);

        if (item.isSelected())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);


        if (item.getId() == -1) {
            helper.setText(R.id.tv_car_no, "不限车牌");


            helper.getView(R.id.tv_check_car).setVisibility(View.INVISIBLE);
            helper.getView(R.id.tv_null).setVisibility(View.VISIBLE);
        } else {

            helper.setText(R.id.tv_car_no, item.getCarNo())
                    .addOnClickListener(R.id.tv_check_car);
            helper.getView(R.id.tv_check_car).setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_null).setVisibility(View.GONE);
        }


    }


}
