package com.eb.new_line_seller.mvp.model;

import android.content.Context;

import com.eb.new_line_seller.mvp.contacts.FixPickPartsContacts;
import com.eb.new_line_seller.mvp.contacts.FixPickServiceContacts;
import com.eb.new_line_seller.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;
import com.juner.mvp.bean.FixServiceList;

public class FixPickPartsMdl extends BaseModel implements FixPickPartsContacts.FixPickPartsMdl {
    Context context;

    public FixPickPartsMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getPartsData(RxSubscribe<FixPartsList> rxSubscribe) {
        sendRequest(HttpUtils.getFix().componentList(getToken(context)).compose(RxHelper.<FixPartsList>observe()), rxSubscribe);
    }

    @Override
    public void seekParts(int id, RxSubscribe<FixPartsEntityList> rxSubscribe) {
        sendRequest(HttpUtils.getFix().seekParts(getToken(context), id, "").compose(RxHelper.<FixPartsEntityList>observe()), rxSubscribe);
    }

    @Override
    public void seekPartsforKey(int id, String key, RxSubscribe<FixPartsEntityList> rxSubscribe) {
        sendRequest(HttpUtils.getFix().seekParts(getToken(context), id, key).compose(RxHelper.<FixPartsEntityList>observe()), rxSubscribe);
    }

}
