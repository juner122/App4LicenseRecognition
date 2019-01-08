package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class SetProjectAdapter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {


    public SetProjectAdapter(@Nullable List<GoodsEntity> data) {
        super(R.layout.activity_setproject_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {

        helper.setText(R.id.tv1, item.getName());
    }


}
