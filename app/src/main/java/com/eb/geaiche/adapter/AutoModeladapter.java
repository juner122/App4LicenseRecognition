package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.AutoModel;

import java.util.List;

public class AutoModeladapter extends BaseQuickAdapter<AutoModel, BaseViewHolder> {


    public AutoModeladapter(@Nullable List<AutoModel> data) {
        super(R.layout.popup2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AutoModel item) {

        helper.setText(R.id.tv_brand_name, item.getName());
    }
}
