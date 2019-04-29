package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.MessageMarketingContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.MessageRecordEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.SmsTemplates;

import java.util.List;


public class MessageMarketingMdl extends BaseModel implements MessageMarketingContacts.MessageMarketingMdl {

    Context context;

    public MessageMarketingMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getSmsTemplates(RxSubscribe<List<SmsTemplates>> rxSubscribe) {
        sendRequest(HttpUtils.getApi().smsTemplates(getToken(context)).compose(RxHelper.observe()), rxSubscribe);
    }

    @Override
    public void getSmsMarketLogs(RxSubscribe<List<MessageRecordEntity>> rxSubscribe) {
        sendRequest(HttpUtils.getApi().marketLogs(getToken(context)).compose(RxHelper.observe()), rxSubscribe);

    }

    @Override
    public void onSendSms(RxSubscribe<NullDataEntity> rxSubscribe, SmsTemplates smsTemplates) {
        sendRequest(HttpUtils.getApi().sendAdSms(getToken(context), smsTemplates).compose(RxHelper.observe()), rxSubscribe);
    }


}

