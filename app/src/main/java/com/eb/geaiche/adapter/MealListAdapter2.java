package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.MealEntity;
import com.eb.geaiche.bean.MealL0Entity;
import com.eb.geaiche.bean.MyMultipleItem;
import com.eb.geaiche.util.MathUtil;

import java.util.List;

public class MealListAdapter2 extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public MealListAdapter2(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_product_meal_list_item2);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_pick_meal_list_item_item2);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final MealL0Entity m = (MealL0Entity) item;

                helper.setText(R.id.tv_name, m.getActivityName());
                helper.setText(R.id.tv_mobile, null == m.getCarNo() || m.getCarNo().equals("") ? "不限车牌" : m.getCarNo());
                helper.setText(R.id.tv_time, String.format("有效期:%s", MathUtil.toDate4Day(m.getEndTime())));

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
                helper.setText(R.id.tv_name, me.getGoodsName()).setText(R.id.tv_2, String.valueOf(me.getNumber() + "次"));

                break;

        }
    }
}