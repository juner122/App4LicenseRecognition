package com.eb.geaiche.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.ActivityEntityItem;
import com.juner.mvp.bean.Coupon;

import java.util.List;

public class CouponWriteRecordAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {

    Context context;

    public CouponWriteRecordAdapter(@Nullable List<Coupon> data, Context c) {
        super(R.layout.activity_coupon_write_record_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.number, "劵号：" + item.getCoupon_number());
        helper.setText(R.id.user, "会员名：" + item.getUsername());
        helper.setText(R.id.phone, "会员电话：" + item.getMobile());
        helper.setText(R.id.time, item.getConvert_time());

    }
}
