package com.eb.geaiche.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;

import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.Server;


import java.util.List;

public class ServeListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {


    public ServeListAdapter(@Nullable List<Goods> data) {
        super(R.layout.activity_server_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {

        Goods.GoodsStandard goodsStandard;
        if (null == item.getGoodsStandard())
            goodsStandard = item.getXgxGoodsStandardPojoList().get(0);//第一个规格
        else {
            goodsStandard = item.getGoodsStandard();
        }

        helper.setText(R.id.tv_product_name, item.getGoodsTitle())
                .setText(R.id.tv_product_ts, item.getFirstCategoryTitle())
                .setText(R.id.tv_price, String.format("现价：￥%s", goodsStandard.getGoodsStandardPrice()));
        TextView tv_market_price = helper.getView(R.id.tv_market_price);
        //添加删除线
        tv_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_market_price.setText(String.format("市场价：￥%s", goodsStandard.getGoodsStandardPrice()));

        ImageView iv = helper.getView(R.id.iv);

        if (item.isSelected())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);

    }
}
