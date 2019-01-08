package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.MemberEntity;

import java.util.List;

public class MemberListAdpter extends BaseQuickAdapter<MemberEntity, BaseViewHolder> {

    public MemberListAdpter(@Nullable List<MemberEntity> data) {
        super(R.layout.activity_member_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberEntity item) {

        helper.setText(R.id.name, item.getUsername())
                .setText(R.id.phone, item.getMobile());

        helper.setText(R.id.tv1, item.getUsername().substring(0,1));

        int p = helper.getLayoutPosition();
        int s = getData().size();




        if (p == s - 1) {

            helper.setVisible(R.id.v, false);
        } else {
            helper.setVisible(R.id.v, true);
        }

    }
}
