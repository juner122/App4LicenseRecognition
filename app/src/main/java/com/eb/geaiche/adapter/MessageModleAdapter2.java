package com.eb.geaiche.adapter;
;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.SmsTemplates;

import java.util.List;

public class MessageModleAdapter2 extends BaseQuickAdapter<SmsTemplates, BaseViewHolder> {


    public MessageModleAdapter2(@Nullable List<SmsTemplates> data) {
        super(R.layout.popup, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SmsTemplates item) {
    helper.setText(R.id.tv_brand_name,item.getTitle());
    }
}
