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

public class MallTypeListAdapter extends BaseQuickAdapter<GoodsCategory, BaseViewHolder> {

    Context context;

    public MallTypeListAdapter(@Nullable List<GoodsCategory> data, Context c) {
        super(R.layout.activity_mall_type_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsCategory item) {
        helper.setText(R.id.tv_name, item.getName());
        Glide.with(context)
                .load(item.getSrc())
                .into((ImageView) helper.getView(R.id.imageView3));
    }
}
