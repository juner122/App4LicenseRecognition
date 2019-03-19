package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.FixPickPartsContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;

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

        if (id == -1)
            sendRequest(HttpUtils.getFix().seekParts(getToken(context), key).compose(RxHelper.<FixPartsEntityList>observe()), rxSubscribe);
        else
            sendRequest(HttpUtils.getFix().seekParts(getToken(context), id, key).compose(RxHelper.<FixPartsEntityList>observe()), rxSubscribe);
    }

}
