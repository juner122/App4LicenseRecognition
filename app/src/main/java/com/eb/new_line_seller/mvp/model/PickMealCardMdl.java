package com.eb.new_line_seller.mvp.model;

import android.content.Context;

import com.eb.new_line_seller.bean.Meal2;
import com.eb.new_line_seller.mvp.contacts.PickMealCardContacts;
import com.eb.new_line_seller.util.HttpUtils;
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
