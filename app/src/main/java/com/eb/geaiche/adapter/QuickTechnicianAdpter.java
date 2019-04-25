package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Technician;

import java.util.List;

//员工下拉列表
public class QuickTechnicianAdpter extends BaseQuickAdapter<Technician, BaseViewHolder> {


    public QuickTechnicianAdpter(@Nullable List<Technician> data) {
        super(R.layout.activity_technician_list_item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {
        helper.setText(R.id.tv_name, item.getNickName());
        helper.setText(R.id.phone, item.getMobile());
        helper.setText(R.id.lv, item.getRoleName());

        ImageView iv = helper.getView(R.id.iv);
        if (item.isSelected())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);


    }
}
