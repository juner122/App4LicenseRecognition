package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.MyMultipleItem;
import com.eb.geaiche.stockControl.activity.StockAddStandardsActivity;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.Goods;

import java.util.List;

import static com.eb.geaiche.stockControl.activity.StockControlActivity.stockCartUtils;

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
        Goods goods = null;
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                goods = (Goods) item;
                int goodsId = goods.getId();
                String goodsTitle = goods.getGoodsTitle();
                boolean isExpanded = goods.isExpanded();


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
                    if (isExpanded) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });


                if (isExpanded)
                    v_add.setVisibility(View.VISIBLE);
                else
                    v_add.setVisibility(View.GONE);


                v_add.setOnClickListener(v -> {
                    //新增规格

                    Intent intent = new Intent(context, StockAddStandardsActivity.class);
                    intent.putExtra("goodsId", goodsId);
                    intent.putExtra("goodsTitle", goodsTitle);

                    context.startActivity(intent);

                });


                break;
            case MyMultipleItem.SECOND_TYPE:
                final Goods.GoodsStandard gs = (Goods.GoodsStandard) item;

                TextView tv_button_name = helper.getView(R.id.button);
                View ll_button = helper.getView(R.id.ll_button);
                helper.setText(R.id.name, gs.getGoodsStandardTitle()).setText(R.id.num, String.valueOf(gs.getNum()));

                //规格添加入库按钮
                ll_button.setOnClickListener(v -> {
                    if (gs.isSelected()) {
                        gs.setSelected(false);
                        tv_button_name.setText("已入库");
                        tv_button_name.setBackgroundResource(R.drawable.button_background_ccc);
                        tv_button_name.setTextColor(Color.parseColor("#999999"));

                    } else {
                        gs.setSelected(true);
                        tv_button_name.setText("添加入库");
                        tv_button_name.setBackgroundResource(R.drawable.button_background_bbb);
                        tv_button_name.setTextColor(Color.parseColor("#ff4a9de3"));
//                        stockCartUtils.addDataNoCommit(goods, gs);
                    }
                });


                break;

        }
    }
}
