package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.OffLinePayType;

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
