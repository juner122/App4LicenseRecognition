package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class StockOutListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    Context context;



    public StockOutListAdapter(@Nullable List<Goods> data, Context c) {
        super(R.layout.activity_stock_out_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_name, item.getGoodsTitle());



        if (item.getXgxGoodsStandardPojoList().size() == 0)
            helper.setText(R.id.tv_price, "暂无报价");
        else
            helper.setText(R.id.tv_price, String.format("￥%s", item.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice()));



        String pic = "";

        if (item.getGoodsDetailsPojoList().size() == 0)
            pic = "";
        else
            pic = item.getGoodsDetailsPojoList().get(0).getImage();

        ImageView iv = (ImageView) helper.getView(R.id.iv_src);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context)
                .load(pic)
                .into(iv);
    }
}
