package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.FixParts2item;

import java.util.List;


//选工时页面的2级分类列表
public class FixPickParts2ItemAdapter extends BaseQuickAdapter<FixParts2item, BaseViewHolder> {


    public FixPickParts2ItemAdapter(@Nullable List<FixParts2item> data) {
        super(R.layout.activity_fix_pick_service_item2, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FixParts2item item) {

        helper.setText(R.id.tv_name, item.getName());

    }
}
