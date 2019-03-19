package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.SubCategoryEntity;

import java.util.List;

public class Brandadapter extends BaseQuickAdapter<SubCategoryEntity, BaseViewHolder> {
    public Brandadapter(@Nullable List<SubCategoryEntity> data) {
        super(R.layout.popup, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubCategoryEntity item) {

        helper.setText(R.id.tv_brand_name, item.getName());
    }
}
