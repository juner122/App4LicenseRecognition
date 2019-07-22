package com.eb.geaiche.coupon;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.MemberEntity;

import java.util.List;

public class CouponPostUserAdapter extends BaseQuickAdapter<MemberEntity, BaseViewHolder> {

    boolean isShowDel;

    public CouponPostUserAdapter(@Nullable List<MemberEntity> data, boolean isShowDel) {
        super(R.layout.activity_coupon_post_user_item, data);
        this.isShowDel = isShowDel;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberEntity item) {
        helper.setText(R.id.phone, item.getMobile());
        helper.addOnClickListener(R.id.delete);

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

        if (isShowDel)
            helper.setVisible(R.id.delete, true);
        else
            helper.setVisible(R.id.delete, false);
    }
}
