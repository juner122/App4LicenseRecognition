package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
        helper.setText(R.id.price, item.getPrice());


        int num = item.getNumber();//领料数量
        int stock = Integer.valueOf(item.getStock());//库存数量


        helper.setText(R.id.num, "x" + num);
        helper.setText(R.id.num_s, String.valueOf(num));
        helper.setText(R.id.tv_stock_num, String.valueOf(stock));

        ImageView iv = helper.getView(R.id.iv_type);
        TextView tv = helper.getView(R.id.tv_type);


        helper.addOnClickListener(R.id.num_s);

        if (num <= stock) {
            tv.setText("已匹配商品");
            iv.setImageResource(R.mipmap.icon_stock_out_s);
        } else {
            tv.setText("商品不足");
            iv.setImageResource(R.mipmap.icon_stock_out_no);
        }


    }
}
