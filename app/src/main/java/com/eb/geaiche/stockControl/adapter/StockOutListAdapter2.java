package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.stockControl.bean.StockGoods;

import java.util.List;

public class StockOutListAdapter2 extends BaseQuickAdapter<StockGoods, BaseViewHolder> {

    Context context;

    int type;

    public StockOutListAdapter2(@Nullable List<StockGoods> data, Context c, int type) {
        super(R.layout.activity_stock_out_item2, data);
        this.context = c;
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, StockGoods item) {
        helper.setText(R.id.name, item.getGoodsTitle());
        helper.setText(R.id.standards, item.getStandardTitle());
        helper.setText(R.id.price, "￥" + item.getPrice());
        helper.setText(R.id.price_stock, "￥" + item.getStockPrice());


        int num = item.getNumber();//领料数量
        int stock = item.getAfterNumber();//库存数量


        helper.setText(R.id.num, String.valueOf(num));

        helper.setText(R.id.tv_stock_num, String.valueOf(stock));

        ImageView iv = helper.getView(R.id.iv_type);
        TextView tv = helper.getView(R.id.tv_type);
        TextView tv_type = helper.getView(R.id.type);


        tv.setText("已匹配商品");
        iv.setImageResource(R.mipmap.icon_stock_out_s);
        if (type == 0) {
            tv_type.setText(",领料");

        } else {
            tv_type.setText(",入库");
        }


    }
}
