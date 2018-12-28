package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.activity.OrderPayActivity;
import com.frank.plate.bean.OffLinePayType;
import com.frank.plate.bean.SubCategoryEntity;

import java.util.List;

public class Brandadapter2 extends BaseQuickAdapter<OffLinePayType, BaseViewHolder> {
    public Brandadapter2(@Nullable List<OffLinePayType> data) {
        super(R.layout.popup, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OffLinePayType item) {

        helper.setText(R.id.tv_brand_name, item.getType_string());
    }
}
