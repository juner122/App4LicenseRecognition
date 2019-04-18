package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class SimpleMealInfoAdpter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {


    public SimpleMealInfoAdpter(@Nullable List<GoodsEntity> data) {
        super(R.layout.activity_simple_good_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.name, item.getGoodsName())
                .setText(R.id.price, "套餐抵扣")
                .setText(R.id.tv_number, String.valueOf("1"));


        helper.setVisible(R.id.ib_plus, false);
        helper.setVisible(R.id.ib_reduce, false);

    }


}
