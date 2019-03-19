package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.juner.mvp.api.http.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.RemakeActCard;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.eb.geaiche.mvp.contacts.ActivityCardContacts;
import com.juner.mvp.bean.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivateCardMdl extends BaseModel implements ActivityCardContacts.ActivityCardMdl {
    Context context;

    public ActivateCardMdl(Context context) {
        this.context = context;
    }

    /**
     * 会员快捷录入
     */
    @Override
    public void checkMember(String phone, String name, RxSubscribe<SaveUserAndCarEntity> rxSubscribe) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("username", name);
        map.put("X-Nideshop-Token", getToken(context));

        sendRequest(HttpUtils.getApi().addUser(map).compose(RxHelper.<SaveUserAndCarEntity>observe()), rxSubscribe);
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public void getInfo(RxSubscribe<UserEntity> rxSubscribe) {

        sendRequest(HttpUtils.getApi().getInfo(getToken(context)).compose(RxHelper.<UserEntity>observe()), rxSubscribe);
    }


    /**
     * 录入卡
     */
    @Override
    public void confirmInput(List<RemakeActCard> list, RxSubscribe<NullDataEntity> rxSubscribe) {

        sendRequest(HttpUtils.getApi().remakeActCard(getToken(context), list).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }

    @Override
    public void confirmAdd(List<RemakeActCard> list, RxSubscribe<NullDataEntity> rxSubscribe) {
        sendRequest(HttpUtils.getApi().addActCard(getToken(context), list).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);

    }
}
