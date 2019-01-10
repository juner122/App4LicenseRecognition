package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class TechnicianAdpter extends BaseQuickAdapter<Technician, BaseViewHolder> {


    public TechnicianAdpter(@Nullable List<Technician> data) {
        super(R.layout.activity_technician_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {
        helper.setText(R.id.tv_name, item.getUsername());
        helper.setText(R.id.phone, item.getMobile());
        helper.setText(R.id.lv, item.getRoleName());

        ImageView iv = helper.getView(R.id.iv);
        if (item.isSelected())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);


    }
}
