package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.MyMultipleItem;
import com.eb.geaiche.stockControl.activity.StockAddStandardsActivity;
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
                int goodsId = goods.getId();
                String goodsTitle = goods.getGoodsTitle();


                boolean isExpanded = goods.isExpanded();


                helper.setText(R.id.tv_name, goods.getGoodsTitle());
                if (null == goods.getXgxGoodsStandardPojoList() || goods.getXgxGoodsStandardPojoList().size() == 0)
                    helper.setText(R.id.tv_price, "暂无报价");
                else
                    helper.setText(R.id.tv_price, String.format("￥%s", goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice()));


                String pic = "";

                if (null == goods.getGoodsDetailsPojoList() || goods.getGoodsDetailsPojoList().size() == 0)
                    pic = "";
                else
                    pic = goods.getGoodsDetailsPojoList().get(0).getImage();

                ImageUtils.load(context, pic, helper.getView(R.id.iv_src));

//                if (isShow) {
                    expandAll();

//                } else {
//                    helper.itemView.setOnClickListener(v -> {
//                        int pos = helper.getAdapterPosition();
//                        if (isExpanded) {
//                            collapse(pos);
//                        } else {
//                            expand(pos);
//                        }
//                    });
//                }

                break;
            case MyMultipleItem.SECOND_TYPE:
                final Goods.GoodsStandard gs = (Goods.GoodsStandard) item;

                View reduce = helper.getView(R.id.reduce);
                View add = helper.getView(R.id.add);
                EditText num = helper.getView(R.id.num);

                num.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                helper.setText(R.id.name, gs.getGoodsStandardTitle()).setText(R.id.num, String.valueOf(gs.getNum()));


                break;

        }
    }
}
