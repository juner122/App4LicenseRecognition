package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.MemberEntity;

import java.util.List;

public class MemberListAdpter extends BaseQuickAdapter<MemberEntity, BaseViewHolder> {

    public MemberListAdpter(@Nullable List<MemberEntity> data) {
        super(R.layout.activity_member_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberEntity item) {

        helper.setText(R.id.name, item.getUsername())
                .setText(R.id.phone, item.getMobile());


    }
}
