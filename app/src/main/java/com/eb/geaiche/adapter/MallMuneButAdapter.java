package com.eb.geaiche.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsCategory;

import java.util.List;

public class MallMuneButAdapter extends BaseQuickAdapter<GoodsCategory, BaseViewHolder> {

    Context activity;

    public MallMuneButAdapter(@Nullable List<GoodsCategory> data, Context activity) {
        super(R.layout.fragment1_main_button_item, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GoodsCategory item) {
        helper.setText(R.id.but, item.getName());

        ImageView icon = helper.getView(R.id.icon);

        Glide.with(activity)
                .load(item.getSrc())
                .into(icon);

    }
}
