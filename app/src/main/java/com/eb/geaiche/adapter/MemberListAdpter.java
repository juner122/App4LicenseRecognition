package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.MemberEntity;

import java.util.List;

public class MemberListAdpter extends BaseQuickAdapter<MemberEntity, BaseViewHolder> {

    public MemberListAdpter(@Nullable List<MemberEntity> data) {
        super(R.layout.activity_member_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberEntity item) {

        helper.setText(R.id.phone, item.getMobile());

        if (null == item.getUsername() || item.getUsername().equals("")) {
            helper.setText(R.id.tv1, "匿");
            helper.setText(R.id.name, "匿名");
        } else {
            helper.setText(R.id.tv1, item.getUsername().substring(0, 1));
            helper.setText(R.id.name, item.getUsername());
        }
        int p = helper.getLayoutPosition();
        int s = getData().size();


        if (p == s - 1) {

            helper.setVisible(R.id.v, false);
        } else {
            helper.setVisible(R.id.v, true);
        }

    }
}
