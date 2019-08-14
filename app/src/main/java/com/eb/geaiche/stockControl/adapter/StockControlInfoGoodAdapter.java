package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class StockControlInfoGoodAdapter extends BaseQuickAdapter<StockGoods, BaseViewHolder> {

    Context context;


    public StockControlInfoGoodAdapter(@Nullable List<StockGoods> data, Context c) {
        super(R.layout.activity_stock_info_good_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, StockGoods item) {
        helper.setText(R.id.tv_name, item.getGoodsTitle());
        helper.setText(R.id.tv_price, "￥" + item.getPrice());
        helper.setText(R.id.num, String.format("库存%s件", item.getAfterNumber()));


        ImageView iv = helper.getView(R.id.iv_src);
        ImageUtils.load(context, item.getImage(), iv);

    }
}
