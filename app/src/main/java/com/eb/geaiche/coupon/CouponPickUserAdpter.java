package com.eb.geaiche.coupon;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.MemberEntity;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class CouponPickUserAdpter extends BaseQuickAdapter<MemberEntity, BaseViewHolder> {


    public CouponPickUserAdpter(@Nullable List<MemberEntity> data) {
        super(R.layout.activity_technician_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberEntity item) {
        helper.setText(R.id.tv_name, item.getUsername());
        helper.setText(R.id.phone, item.getMobile());
//        helper.setText(R.id.lv, item.getRoleName());
        helper.setVisible(R.id.lv,false);

        ImageView iv = helper.getView(R.id.iv);
        if (item.isSelected())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);


    }
}
