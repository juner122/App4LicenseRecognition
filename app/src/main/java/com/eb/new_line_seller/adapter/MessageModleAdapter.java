package com.eb.new_line_seller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.MessageModleEntity;
import com.juner.mvp.bean.MessageRecordEntity;

import java.util.List;

public class MessageModleAdapter extends BaseQuickAdapter<MessageModleEntity, BaseViewHolder> {

    Context context;

    public MessageModleAdapter(@Nullable List<MessageModleEntity> data, Context c) {
        super(R.layout.activity_message_marketing_rv2_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageModleEntity item) {

    }
}
