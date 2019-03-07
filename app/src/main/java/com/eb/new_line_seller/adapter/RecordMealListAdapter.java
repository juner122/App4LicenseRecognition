package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.MealEntity;
import com.eb.new_line_seller.bean.MealL0Entity;
import com.eb.new_line_seller.bean.MyMultipleItem;
import com.eb.new_line_seller.util.MathUtil;

import java.util.List;


//纸卡录入记录
public class RecordMealListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public RecordMealListAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_record_list_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_record_list_item_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final MealL0Entity m = (MealL0Entity) item;

                helper.setText(R.id.tv_name, "套卡卡号：" + m.getActivitySn());
                helper.setText(R.id.tv_date, "有效期：" + MathUtil.toDate4Day(m.getEndTime()));
                helper.setText(R.id.tv_phone, "手机号码：" + m.getMobile());

                if ("".equals(m.getCarNo()))
                    helper.setText(R.id.tv_car_no, "不限车牌");
                else
                    helper.setText(R.id.tv_car_no, "限车牌：" + m.getCarNo());

                helper.addOnClickListener(R.id.ll_item);


//                final ImageView iv = helper.getView(R.id.iv_pick);

//                pick(m.isExpanded(), iv);

                break;
            case MyMultipleItem.SECOND_TYPE:
                final MealEntity me = (MealEntity) item;
                helper.setText(R.id.tv1, me.getActivityName());
                helper.setText(R.id.tv_name, me.getGoodsName()).setText(R.id.tv_number, String.valueOf("x" + me.getGoodsNum()));


                break;

        }
    }


//    private void pick(boolean isPick, ImageView iv) {
//        if (isPick)
//            iv.setImageResource(R.drawable.icon_pick2);
//        else
//            iv.setImageResource(R.drawable.icon_unpick2);
//
//
//    }


}