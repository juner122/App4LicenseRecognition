package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;

import java.util.List;

public class Brandadapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public Brandadapter(@Nullable List<String> data) {
        super(R.layout.popup, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_brand_name, item);
    }
}
