package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsSpec;

import java.util.List;

public class GoodSpecListAdapter extends BaseQuickAdapter<GoodsSpec, BaseViewHolder> {

    public GoodSpecListAdapter(@Nullable List<GoodsSpec> data) {
        super(R.layout.good_spec_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsSpec item) {

        helper.setText(R.id.tv_car_no, item.getName());




    }


}
