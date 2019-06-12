package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.bean.MyMultipleItem;
import com.eb.geaiche.stockControl.activity.StockAddStandardsActivity;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class StockControlListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    Context context;


    public StockControlListAdapter(@Nullable List<MultiItemEntity> data, Context c) {
        super(data);

        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_stock_control_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_stock_control_item_item);
        this.context = c;


    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final Goods goods = (Goods) item;

                helper.setText(R.id.tv_name, goods.getGoodsTitle());
                View v_add = helper.getView(R.id.tv_add);//新增规格按钮

                if (goods.getXgxGoodsStandardPojoList().size() == 0)
                    helper.setText(R.id.tv_price, "暂无报价");
                else
                    helper.setText(R.id.tv_price, String.format("￥%s", goods.getXgxGoodsStandardPojoList().get(0).getGoodsStandardPrice()));


                String pic = "";

                if (goods.getGoodsDetailsPojoList().size() == 0)
                    pic = "";
                else
                    pic = goods.getGoodsDetailsPojoList().get(0).getImage();

                ImageUtils.load(context, pic, helper.getView(R.id.iv_src));


                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (goods.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });


                if (goods.isExpanded())
                    v_add.setVisibility(View.VISIBLE);
                else
                    v_add.setVisibility(View.GONE);


                v_add.setOnClickListener(v -> {

                    //新增规格

                    Intent intent = new Intent(context, StockAddStandardsActivity.class);
                    intent.putExtra("goodsId", goods.getId());
                    intent.putExtra("goodsTitle", goods.getGoodsTitle());

                    context.startActivity(intent);

                });


                break;
            case MyMultipleItem.SECOND_TYPE:
                final Goods.GoodsStandard gs = (Goods.GoodsStandard) item;

                helper.setText(R.id.name, gs.getGoodsStandardTitle()).setText(R.id.num, String.valueOf(gs.getNum()));
                helper.addOnClickListener(R.id.button);//按钮


                break;

        }
    }
}
