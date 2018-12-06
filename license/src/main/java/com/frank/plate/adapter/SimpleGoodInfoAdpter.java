package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.BillEntityItem;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.util.MathUtil;

import java.util.List;

public class SimpleGoodInfoAdpter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    public SimpleGoodInfoAdpter(@Nullable List<GoodsEntity> data) {
        super(R.layout.activity_simple_good_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {

        helper.setText(R.id.name, item.getName())
                .setText(R.id.price, "￥" + item.getRetail_price())
                .setText(R.id.tv_number, String.valueOf(item.getNumber()))
                .addOnClickListener(R.id.ib_plus)
                .addOnClickListener(R.id.ib_reduce);
        View ib_reduce = helper.getView(R.id.ib_reduce);
        View ib_plus = helper.getView(R.id.ib_plus);
        View tv_number = helper.getView(R.id.tv_number);

        if (item.getNumber() == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
        }

    }


}