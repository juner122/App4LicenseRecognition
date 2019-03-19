package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.FixService2item;

import java.util.List;


//选工时页面的2级分类列表
public class FixPickService2ItemAdapter extends BaseQuickAdapter<FixService2item, BaseViewHolder> {


    public FixPickService2ItemAdapter(@Nullable List<FixService2item> data) {
        super(R.layout.activity_fix_pick_service_item2, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FixService2item item) {

        helper.setText(R.id.tv_name, item.getServiceName());

    }
}
