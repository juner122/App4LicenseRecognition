package com.eb.new_line_seller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.ActivityEntityItem;
import com.juner.mvp.bean.MessageRecordEntity;

import java.util.List;

public class MessageRecordAdapter extends BaseQuickAdapter<MessageRecordEntity, BaseViewHolder> {

    Context context;

    public MessageRecordAdapter(@Nullable List<MessageRecordEntity> data, Context c) {
        super(R.layout.activity_message_marketing_rv3_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageRecordEntity item) {

    }
}
