package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class TechnicianInfoAdpter extends BaseQuickAdapter<Technician, BaseViewHolder> {


    public TechnicianInfoAdpter(@Nullable List<Technician> data) {
        super(R.layout.activity_staff_management_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {

        helper.setText(R.id.phone, item.getMobile());
        helper.setText(R.id.lv, item.getRoleName());

        if (null == item.getNickName() || item.getNickName().equals("")) {
            helper.setText(R.id.name, "匿名");
            helper.setText(R.id.tv1, "匿");
        }else {
            helper.setText(R.id.name, item.getNickName());
            helper.setText(R.id.tv1, item.getNickName().substring(0, 1));
        }
    }
}
