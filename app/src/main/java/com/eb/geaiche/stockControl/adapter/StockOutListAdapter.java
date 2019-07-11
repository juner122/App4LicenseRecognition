package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class StockOutListAdapter extends BaseQuickAdapter<StockGoods, BaseViewHolder> {

    Context context;



    public StockOutListAdapter(@Nullable List<StockGoods> data, Context c) {
        super(R.layout.activity_stock_out_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, StockGoods item) {
        helper.setText(R.id.name, item.getGoodsTitle());
        helper.setText(R.id.standards, item.getStandardTitle());
        helper.setText(R.id.num, item.getNumber());
        helper.setText(R.id.price, item.getPrice());



        ImageView iv = (ImageView) helper.getView(R.id.iv);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context)
                .load(item.getImage())
                .into(iv);
    }
}
