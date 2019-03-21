package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Roles;

import java.util.List;

public class RolesAdapter extends BaseQuickAdapter<Roles, BaseViewHolder> {


    public RolesAdapter(@Nullable List<Roles> data) {
        super(R.layout.popup3, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Roles item) {
        helper.setText(R.id.tv_text, item.getRoleName());
    }
}
