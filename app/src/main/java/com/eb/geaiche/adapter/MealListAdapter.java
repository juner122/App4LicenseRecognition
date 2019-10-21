package com.eb.geaiche.adapter;

import android.graphics.Paint;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;

import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.bean.MyMultipleItem;

import java.util.List;

public class MealListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    boolean isShow;//是否显示选择按钮

    public MealListAdapter(@Nullable List<MultiItemEntity> data, boolean isShow) {
        super(data);
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_product_meal_list_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_product_meal_list_item_item);
        this.isShow = isShow;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final MealL0Entity m = (MealL0Entity) item;

                helper.setText(R.id.tv_name, m.getActivityName());

                //set view content
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (m.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });
                break;
            case MyMultipleItem.SECOND_TYPE:
                MealEntity me = (MealEntity) item;
                helper.setText(R.id.tv_name, me.getGoodsName()).setText(R.id.tv_2, "劵号：" + (null == me.getCouponSn() ? "-" : me.getCouponSn()));

                ImageView iv = helper.getView(R.id.iv);
                TextView tv_goodName = helper.getView(R.id.tv_name);

                if (isShow) {
                    iv.setVisibility(View.VISIBLE);

                } else {
                    iv.setVisibility(View.INVISIBLE);
                }

                if (me.getNumber() > 0) {//可用次数不为0
                    helper.addOnClickListener(R.id.ll_item);
                    tv_goodName.getPaint().setFlags(0);
                    if (me.isSelected())
                        iv.setImageResource(R.drawable.icon_pick2);
                    else
                        iv.setImageResource(R.drawable.icon_unpick2);
                } else {
                    iv.setImageResource(R.drawable.icon_unpick2);
                    tv_goodName.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //添加删除线
                }

                break;

        }
    }
}