package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class SimpleServiceInfoAdpter extends BaseItemDraggableAdapter<GoodsEntity, BaseViewHolder> {


    boolean isShowPlusAndReduce;

    public SimpleServiceInfoAdpter(@Nullable List<GoodsEntity> data, boolean is) {
        super(R.layout.activity_simple_good_list_item, data);
        isShowPlusAndReduce = is;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.name, item.getGoods_name())
                .setText(R.id.price, "￥" + item.getRetail_price())
                .setText(R.id.tv_number, String.valueOf("x"+item.getNumber()));


        if (!isShowPlusAndReduce) {

            helper.setVisible(R.id.ib_plus, false);
            helper.setVisible(R.id.ib_reduce, false);

        }

    }


}
