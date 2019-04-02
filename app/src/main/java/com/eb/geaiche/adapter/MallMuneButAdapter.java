package com.eb.geaiche.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BillListActivity;
import com.eb.geaiche.activity.CollegeActivity;
import com.eb.geaiche.activity.CourseRecordActivity;
import com.eb.geaiche.activity.MemberManagementActivity;
import com.eb.geaiche.activity.OrderListActivity;
import com.eb.geaiche.activity.ProductListActivity;
import com.eb.geaiche.activity.RecruitActivity;
import com.eb.geaiche.activity.StaffManagementActivity;
import com.eb.geaiche.mvp.ActivateCardActivity;
import com.eb.geaiche.mvp.FixInfoListActivity;
import com.eb.geaiche.mvp.FixPickPartsActivity;
import com.eb.geaiche.mvp.FixPickServiceActivity;
import com.eb.geaiche.mvp.MarketingToolsActivity;
import com.eb.geaiche.mvp.MessageMarketingActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.MenuBut;

import java.util.List;

public class MallMuneButAdapter extends BaseQuickAdapter<GoodsCategory, BaseViewHolder> {

    Context activity;

    public MallMuneButAdapter(@Nullable List<GoodsCategory> data, Context activity) {
        super(R.layout.fragment1_main_button_item, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GoodsCategory item) {
        helper.setText(R.id.but, item.getName());

        ImageView icon = helper.getView(R.id.icon);

        Glide.with(activity)
                .load(item.getSrc())
                .into(icon);

    }
}
