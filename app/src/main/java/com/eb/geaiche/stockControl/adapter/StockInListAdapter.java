package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.MyMultipleItem;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.Goods;
import java.util.List;


public class StockInListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    Context context;


    public StockInListAdapter(@Nullable List<MultiItemEntity> data, Context c) {
        super(data);

        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_stock_in_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_stock_in_item_item);
        this.context = c;

    }


    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

        Goods goods = null;
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                goods = (Goods) item;

                String goodsTitle = goods.getGoodsTitle();


                helper.setText(R.id.tv_name, goodsTitle);
                if (null == goods.getXgxGoodsStandardPojoList() || goods.getXgxGoodsStandardPojoList().size() == 0)
                    helper.setText(R.id.tv_price, "暂无报价");
                else
                    helper.setText(R.id.tv_price, String.format("￥%s", goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice()));


                if (null != goods.getGoodsDetailsPojoList() && goods.getGoodsDetailsPojoList().size() > 0) {
                    ImageUtils.load(context, goods.getGoodsDetailsPojoList().get(0).getImage(), helper.getView(R.id.iv_src));
                }

                expandAll();


                break;
            case MyMultipleItem.SECOND_TYPE:
                final Goods.GoodsStandard gs = (Goods.GoodsStandard) item;

                helper.addOnClickListener(R.id.reduce);
                helper.addOnClickListener(R.id.add);

                helper.setText(R.id.name, gs.getGoodsStandardTitle()).setText(R.id.num, String.valueOf(gs.getNum())).setText(R.id.price,String.format("￥%s",gs.getGoodsStandardPrice())).setText(R.id.stock, null == gs.getStock() ? "库存:0" : "库存:" + gs.getStock());
                break;
        }
    }

}
