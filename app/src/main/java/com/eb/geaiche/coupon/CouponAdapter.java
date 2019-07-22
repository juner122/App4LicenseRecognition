package com.eb.geaiche.coupon;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MuneButAdapter;
import com.eb.geaiche.util.DateUtil;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.Coupon2;

import java.util.List;

public class CouponAdapter extends BaseQuickAdapter<Coupon2, BaseViewHolder> {

    Context context;


    public CouponAdapter(@Nullable List<Coupon2> data, Context c) {
        super(R.layout.activity_coupon_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Coupon2 item) {
        helper.setText(R.id.price, String.format("￥%s", item.getType_money()));
        helper.setText(R.id.term, String.format("满%s可使用", item.getMin_amount()));
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.time, String.format("有效期至：%s", item.getUse_end_date()));
    }
}
