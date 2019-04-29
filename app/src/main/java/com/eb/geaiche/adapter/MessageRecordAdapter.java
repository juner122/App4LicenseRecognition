package com.eb.geaiche.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.DateUtil;
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

        helper.setText(R.id.content, item.getContent())
                .setText(R.id.data, DateUtil.getFormatedDateTime(item.getAddTime()))
                .setText(R.id.all, String.format("%s位车主", item.getSuc() + item.getFail()))
                .setText(R.id.users_string, item.getUsersString())
                .setText(R.id.suc, String.format("发送成功：%s条", item.getSuc()))
                .setText(R.id.fail, String.format("发送失败：%s条", item.getFail()));

    }
}
