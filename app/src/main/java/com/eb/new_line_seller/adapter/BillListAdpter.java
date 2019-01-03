package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.BillEntityItem;
import com.eb.new_line_seller.util.MathUtil;

import java.util.List;

public class BillListAdpter extends BaseQuickAdapter<BillEntityItem, BaseViewHolder> {

    public BillListAdpter(@Nullable List<BillEntityItem> data) {
        super(R.layout.activity_bill_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillEntityItem item) {

        helper.setText(R.id.tv_order_number, String.format("订单号：%s", item.getOrderSn()));
        helper.setText(R.id.tv_order_state, item.getTypeString());
        helper.setText(R.id.tv_date, MathUtil.toDate(Long.parseLong(item.getCreateTime())));
        helper.setText(R.id.tv_money, String.format("￥%s", MathUtil.twoDecimal(Double.parseDouble(item.getBalance()))));
    }
}
