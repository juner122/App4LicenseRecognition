package com.eb.geaiche.stockControl.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsBrand;

import java.util.List;

public class StandardsListAdapter extends BaseQuickAdapter<Goods.GoodsStandard, BaseViewHolder> {

    Context context;


    public StandardsListAdapter(@Nullable List<Goods.GoodsStandard> data, Context c) {
        super(R.layout.activity_stock_standards_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods.GoodsStandard item) {
        helper.setText(R.id.name, item.getGoodsStandardTitle());
        helper.setText(R.id.price, String.format("￥%s", item.getGoodsStandardPrice()));
    }
}
