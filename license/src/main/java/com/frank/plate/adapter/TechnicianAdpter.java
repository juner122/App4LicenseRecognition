package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.Technician;

import java.util.List;

public class TechnicianAdpter  extends BaseQuickAdapter<Technician, BaseViewHolder> {


    public TechnicianAdpter( @Nullable List<Technician> data) {
        super(R.layout.activity_technician_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {
       helper.setText(R.id.tv_name,item.getUsername());







    }
}
