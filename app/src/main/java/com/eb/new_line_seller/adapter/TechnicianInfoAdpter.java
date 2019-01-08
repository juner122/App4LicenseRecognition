package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class TechnicianInfoAdpter extends BaseQuickAdapter<Technician, BaseViewHolder> {


    public TechnicianInfoAdpter(@Nullable List<Technician> data) {
        super(R.layout.activity_staff_management_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {
        helper.setText(R.id.name, item.getUsername());
        helper.setText(R.id.phone, item.getMobile());
        helper.setText(R.id.lv, item.getRoleName());
        helper.setText(R.id.tv1, item.getUsername().substring(0,1));
    }
}
