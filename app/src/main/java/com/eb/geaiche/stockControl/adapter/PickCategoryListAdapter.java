package com.eb.geaiche.stockControl.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsCategory;

import java.util.List;

public class PickCategoryListAdapter extends BaseQuickAdapter<GoodsCategory, BaseViewHolder> {

    Context context;


    public PickCategoryListAdapter(@Nullable List<GoodsCategory> data, Context c) {
        super(R.layout.activity_stock_add_pick_brand , data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsCategory item) {
        helper.setText(R.id.name, item.getName());


    }
}
