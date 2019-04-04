package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class SimpleGoodInfo2Adpter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    public SimpleGoodInfo2Adpter(@Nullable List<GoodsEntity> data) {
        super(R.layout.activity_simple_good_list_item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.name, item.getGoods_name());

        helper.setText(R.id.price, "￥" + item.getRetail_price()).setText(R.id.tv_number, "x" + String.valueOf(item.getNumber()));

    }
}
