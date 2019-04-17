package com.eb.geaiche.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.CartItem;
import com.juner.mvp.bean.XgxPurchaseOrderGoodsPojo;

import java.util.List;

public class MallOrderGoodsListAdapter2 extends BaseQuickAdapter<XgxPurchaseOrderGoodsPojo, BaseViewHolder> {

    Context context;


    public MallOrderGoodsListAdapter2(@Nullable List<XgxPurchaseOrderGoodsPojo> data, Context c) {
        super(R.layout.activity_mall_order_goods_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, XgxPurchaseOrderGoodsPojo item) {
        helper.setText(R.id.tv_name, item.getGoodsTitle());
        helper.setText(R.id.tv_number, String.format("x%s", item.getNumber()));
        helper.setText(R.id.textView7, String.format("%s", item.getGoodsStandardTitle()));
        if (null == item.getGoodsStandardId() || "".equals(item.getGoodsStandardId()))
            helper.setText(R.id.tv_price, "暂无报价");
        else
            helper.setText(R.id.tv_price, String.format("￥%s", item.getGoodsStandardPrice()));


        TextView price = helper.getView(R.id.price2);
        //添加删除线
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        String pic = item.getImage();


        ImageView iv = (ImageView) helper.getView(R.id.iv_src);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context)
                .load(pic)
                .into(iv);
    }
}
