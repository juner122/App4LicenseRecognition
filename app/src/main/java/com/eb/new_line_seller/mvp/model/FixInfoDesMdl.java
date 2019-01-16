package com.eb.new_line_seller.mvp.model;

import android.content.Context;

import com.juner.mvp.api.http.RxSubscribe;
import com.eb.new_line_seller.mvp.contacts.FixInfoDesContacts;
import com.juner.mvp.api.http.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;


public class FixInfoDesMdl extends BaseModel implements FixInfoDesContacts.FixInfoDesMdl {
    Context context;

    public FixInfoDesMdl(Context context) {
        this.context = context;
    }


    //保存维修单
    @Override
    public void quotationSave(FixInfoEntity fixInfo, RxSubscribe<NullDataEntity> rxSubscribe) {

        sendRequest(HttpUtils.getApi().quotationSave(getToken(context), fixInfo).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);


    }
}
