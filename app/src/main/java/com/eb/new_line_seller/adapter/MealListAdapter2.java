package com.eb.new_line_seller.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.MealEntity;
import com.eb.new_line_seller.bean.MealL0Entity;
import com.eb.new_line_seller.bean.MyMultipleItem;

import java.util.List;

public class MealListAdapter2 extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {




    public MealListAdapter2(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_product_meal_list_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_pick_meal_list_item_item2);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final MealL0Entity m = (MealL0Entity) item;

                helper.setText(R.id.tv_name, m.getActivityName());

                //set view content
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (m.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case MyMultipleItem.SECOND_TYPE:
                MealEntity me = (MealEntity) item;
                helper.setText(R.id.tv_name, me.getGoodsName()).setText(R.id.tv_2, String.valueOf(me.getGoodsNum() + "次"));

                break;

        }
    }
}