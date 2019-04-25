package com.eb.geaiche.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.CartItem;

import java.util.List;

public class ShoppingCartListAdapter extends BaseQuickAdapter<CartItem, BaseViewHolder> {

    Context context;

    public ShoppingCartListAdapter(@Nullable List<CartItem> data, Context c) {
        super(R.layout.activity_shopping_cart_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, CartItem item) {

        helper.setText(R.id.tv_title, item.getGoods_name());
        helper.setText(R.id.tv_product, item.getGoodsStandardTitle());
        helper.setText(R.id.rv_price, String.format("￥%s", MathUtil.twoDecimal(item.getRetail_product_price())));
        helper.setText(R.id.tv_num, String.format("%s", item.getNumber()));


        helper.addOnClickListener(R.id.add_btn);
        helper.addOnClickListener(R.id.reduce_btn);
        helper.addOnClickListener(R.id.tv_title);

        Glide.with(context)
                .load(item.getImage())
                .into((ImageView) helper.getView(R.id.iv));


        ImageView iv_pick = helper.getView(R.id.iv_pick);

        if (item.isSelected())
            iv_pick.setImageResource(R.drawable.icon_pick2);
        else
            iv_pick.setImageResource(R.drawable.icon_unpick2);


    }

}
