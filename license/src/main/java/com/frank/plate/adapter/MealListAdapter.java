package com.frank.plate.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.frank.plate.R;

import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.MealEntity;
import com.frank.plate.bean.MealL0Entity;
import com.frank.plate.bean.MyMultipleItem;

import java.util.List;

public class MealListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    Fragment fragment;


    public MealListAdapter(Fragment fragment, @Nullable List<MultiItemEntity> data) {
        super(data);
        this.fragment = fragment;
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_product_meal_list_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_product_meal_list_item_item);


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

                ImageView iv = helper.getView(R.id.iv);
                TextView tv_goodName = helper.getView(R.id.tv_name);


                if (me.getGoodsNum() > 0) {//可用次数不为0
                    helper.addOnClickListener(R.id.ll_item);
                    tv_goodName.getPaint().setFlags(0);
                    if (me.isSelected())
                        iv.setImageResource(R.mipmap.icon_pick);
                    else
                        iv.setImageResource(R.mipmap.icon_unpick);
                } else {
                    iv.setImageResource(R.mipmap.icon_unpick);
                    tv_goodName.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //添加删除线
                }


                break;

        }
    }
}