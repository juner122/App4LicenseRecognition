package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Server;

import java.util.List;

public class SimpleServiceInfoAdpter extends BaseItemDraggableAdapter<Server, BaseViewHolder> {


    boolean isShowPlusAndReduce;

    public SimpleServiceInfoAdpter(@Nullable List<Server> data, boolean is) {
        super(R.layout.activity_simple_good_list_item, data);
        isShowPlusAndReduce = is;
    }

    @Override
    protected void convert(BaseViewHolder helper, Server item) {


        helper.setText(R.id.name, item.getName())
                .setText(R.id.price, "￥" + item.getPrice())
                .setText(R.id.tv_number, "x"+item.getNumber());


        if (!isShowPlusAndReduce) {

            helper.setVisible(R.id.ib_plus, false);
            helper.setVisible(R.id.ib_reduce, false);

        }

    }


}
