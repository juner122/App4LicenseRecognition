package com.eb.geaiche.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.ActivityEntityItem;

import java.util.List;

public class ActivityListAdapter extends BaseQuickAdapter<ActivityEntityItem, BaseViewHolder> {

    Context context;

    public ActivityListAdapter(@Nullable List<ActivityEntityItem> data, Context c) {
        super(R.layout.activity_meal_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityEntityItem item) {
        helper.setText(R.id.tv_title, item.getActivityName());
        helper.setText(R.id.tv_type, item.getActivityExplain());
        helper.setText(R.id.tv_price1, item.getActivityPrice().equals("0.00") ? "免费" : "￥" + item.getActivityPrice());
        helper.setText(R.id.tv_action, "查看详情");


        Glide.with(context)
                .load(item.getActivityImage())
                .into((ImageView) helper.getView(R.id.iv));
    }
}
