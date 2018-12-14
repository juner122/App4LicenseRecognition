package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.GoodsEntity;

import java.util.List;

public class SimpleMealInfoAdpter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {


    public SimpleMealInfoAdpter(@Nullable List<GoodsEntity> data) {
        super(R.layout.activity_simple_good_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.name, item.getName())
                .setText(R.id.price, "免费")
                .setText(R.id.tv_number, String.valueOf("剩余" + item.getGoodsNum()+ "次"));


        helper.setVisible(R.id.ib_plus, false);
        helper.setVisible(R.id.ib_reduce, false);

    }


}
