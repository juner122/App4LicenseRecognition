package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.util.DateUtil;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class StockInOrOutRecordAdapter extends BaseQuickAdapter<StockInOrOut, BaseViewHolder> {

    Context context;


    public StockInOrOutRecordAdapter(@Nullable List<StockInOrOut> data, Context c) {
        super(R.layout.activity_stock_out_in_record, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, StockInOrOut item) {
        helper.setText(R.id.name, item.getUserId());
        helper.setText(R.id.time, DateUtil.getFormatedDateTime(item.getAddTime()));
        helper.setText(R.id.price, item.getTotalPrice());


    }
}
