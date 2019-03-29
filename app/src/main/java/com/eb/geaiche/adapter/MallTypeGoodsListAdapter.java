package com.eb.geaiche.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsCategory;

import java.util.List;

public class MallTypeGoodsListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    Context context;

    public MallTypeGoodsListAdapter(@Nullable List<Goods> data, Context c) {
        super(R.layout.activity_mall_goods_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_price, String.format("￥%s", item.getPrice()));
        helper.setText(R.id.tv_info, item.getInfo());
        helper.setText(R.id.tv_num, String.format("已售%s件", item.getNum()));

//
//        Glide.with(context)
//                .load(item.getSrc())
//                .into((ImageView) helper.getView(R.id.imageView3));
    }
}
