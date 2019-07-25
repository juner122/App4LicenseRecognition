package com.eb.geaiche.coupon;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.DateUtil;
import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.CouponRecode;

import java.util.List;

public class PostRecordAdapter extends BaseQuickAdapter<CouponRecode, BaseViewHolder> {

    Context context;


    public PostRecordAdapter(@Nullable List<CouponRecode> data, Context c) {
        super(R.layout.activity_coupon_post_record_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponRecode item) {
        helper.setText(R.id.name, item.getCouponName());
        helper.setText(R.id.user, String.format("派发人：%s", item.getUserName()));
        helper.setText(R.id.time, DateUtil.getFormatedDateTime(item.getCreateTime()));
        helper.setText(R.id.num, String.format("收券人数：%s", item.getDealNum()));

    }
}
