package com.eb.geaiche.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class MallTypeGoodsListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    Context context;


    public MallTypeGoodsListAdapter(@Nullable List<Goods> data, Context c, int layout) {
//        super(R.layout.activity_mall_goods_item, data);
        super(layout, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_name, item.getGoodsTitle());
        helper.addOnClickListener(R.id.add_cart);
        if (item.getXgxGoodsStandardPojoList().size() == 0)
            helper.setText(R.id.tv_price, "暂无报价");
        else
            helper.setText(R.id.tv_price, String.format("￥%s", item.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice()));


        TextView price = helper.getView(R.id.price2);
        //添加删除线
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

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
