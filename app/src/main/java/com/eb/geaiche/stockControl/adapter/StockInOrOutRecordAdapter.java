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
        helper.setText(R.id.name, item.getUserName());
        helper.setText(R.id.time, null == item.getAddTime() ? "-" : DateUtil.getFormatedDateTime(item.getAddTime()));
        helper.setText(R.id.price, null == item.getTotalPrice() ? "-" : String.format("￥%s", item.getTotalPrice()));
        helper.setText(R.id.tv_sn, String.format("订单号：%s", item.getOrderSn()));

        if (item.getType().equals("2")) {
            helper.setVisible(R.id.tv_sn, false);

        } else
            helper.setVisible(R.id.tv_sn, true);

    }
}
