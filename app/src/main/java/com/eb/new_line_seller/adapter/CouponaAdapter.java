package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.Coupon;

import java.util.List;

public class CouponaAdapter extends BaseQuickAdapter<Coupon, BaseViewHolder> {


    public CouponaAdapter(@Nullable List<Coupon> data) {
        super(R.layout.activity_user_pick_coupons_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon item) {

        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.use_end_date, String.format("有效期至：%s", item.getUse_end_date()));


        if (item.isSelected())
            helper.setBackgroundRes(R.id.ll, R.mipmap.background_coupons_pick);
        else
            helper.setBackgroundRes(R.id.ll, R.mipmap.background_coupons_unpick);


    }
}
