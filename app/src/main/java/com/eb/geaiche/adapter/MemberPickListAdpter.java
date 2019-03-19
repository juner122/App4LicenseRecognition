package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.MemberEntity;

import java.util.List;

public class MemberPickListAdpter extends BaseQuickAdapter<MemberEntity, BaseViewHolder> {

    public MemberPickListAdpter(@Nullable List<MemberEntity> data) {
        super(R.layout.activity_member_pick_list_item, data);
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


        ImageView iv = helper.getView(R.id.iv);
        if (item.isSelected())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);

    }
}
