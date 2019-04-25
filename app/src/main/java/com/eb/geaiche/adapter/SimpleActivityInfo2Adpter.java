package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class SimpleActivityInfo2Adpter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    public SimpleActivityInfo2Adpter(@Nullable List<GoodsEntity> data) {
        super(R.layout.activity_simple_good_list_item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.name, item.getGoodsName());

        helper.setText(R.id.price, "套餐抵扣").setText(R.id.tv_number, "x1");

    }
}
