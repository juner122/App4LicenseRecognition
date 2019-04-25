package com.eb.geaiche.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.MessageModleEntity;

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
