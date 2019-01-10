package com.eb.new_line_seller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.List;

public class FixInfoListAdapter extends BaseQuickAdapter<FixInfo, BaseViewHolder> {

    Context context;


    public FixInfoListAdapter(int layoutResId, @Nullable List<FixInfo> data, Context context) {
        super(layoutResId, data);
        this.context = context;

    }


    @Override
    protected void convert(BaseViewHolder helper, FixInfo item) {


        helper.setText(R.id.tv_plate_number, item.getCarNo());
        helper.setText(R.id.tv_order_number, String.format("检修单号:%s", item.getQuotationSn()));
        helper.setText(R.id.tv_date, item.getAddTime());
        helper.setText(R.id.tv_order_state, item.getStatusText());


        helper.setText(R.id.tv_money, String.format("￥%s", item.getActualPrice()));


        ImageView imageView = (helper.getView(R.id.iv_icon));

        imageView.setImageResource(R.mipmap.icon_enter);


    }


}
