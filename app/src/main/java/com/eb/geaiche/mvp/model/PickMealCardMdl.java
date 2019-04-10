package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.bean.Meal2;
import com.eb.geaiche.mvp.contacts.PickMealCardContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;

import java.util.List;

public class PickMealCardMdl extends BaseModel implements PickMealCardContacts.PickMealCardMdl {
    Context context;

    public PickMealCardMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getMealList(RxSubscribe<List<Meal2>> rxSubscribe) {

        sendRequest(HttpUtils.getApi().queryAct(getToken(context)).compose(RxHelper.<List<Meal2>>observe()), rxSubscribe);
    }


}
