package com.eb.geaiche.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.ActivityEntityItem;

import java.util.List;

public class GoodsPicListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    Context context;

    public GoodsPicListAdapter(@Nullable List<String> data, Context c) {
        super(R.layout.activity_mall_goods_info_pic_list, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        Glide.with(context)
                .load(item)
                .into((ImageView) helper.getView(R.id.iv));
    }
}
