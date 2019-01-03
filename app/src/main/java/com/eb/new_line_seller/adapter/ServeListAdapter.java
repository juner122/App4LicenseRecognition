package com.eb.new_line_seller.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;

import com.eb.new_line_seller.bean.Server;


import java.util.List;

public class ServeListAdapter extends BaseQuickAdapter<Server, BaseViewHolder> {


    public ServeListAdapter(@Nullable List<Server> data) {
        super(R.layout.activity_server_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Server item) {


        helper.setText(R.id.tv_product_name, item.getName())
                .setText(R.id.tv_product_ts, item.getExplain())
                .setText(R.id.tv_price, String.format("现价：￥%s", item.getPrice()));
        TextView tv_market_price = helper.getView(R.id.tv_market_price);
        //添加删除线
        tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_market_price.setText(String.format("市场价：￥%s", item.getMarketPrice()));

        ImageView iv = helper.getView(R.id.iv);

        if (item.isSelected())
            iv.setImageResource(R.mipmap.icon_pick);
        else
            iv.setImageResource(R.mipmap.icon_unpick);

    }
}
