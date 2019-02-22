package com.eb.new_line_seller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.ActivityEntityItem;
import com.juner.mvp.bean.UserEntity;

import java.util.List;

public class UserlistListAdapter extends BaseQuickAdapter<UserEntity, BaseViewHolder> {

    Context context;

    public UserlistListAdapter(@Nullable List<UserEntity> data, Context c) {
        super(R.layout.dialog_user_list_item_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserEntity item) {
        helper.setText(R.id.tv_name, item.getUsername());
        helper.setText(R.id.tv_mobile, item.getMobile());

    }
}
