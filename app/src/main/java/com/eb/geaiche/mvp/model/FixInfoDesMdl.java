package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.eb.geaiche.mvp.contacts.FixInfoDesContacts;
import com.juner.mvp.api.http.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Technician;

import net.grandcentrix.tray.AppPreferences;

import java.util.HashMap;
import java.util.Map;


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

    //保存维修单
    @Override
    public void sysuserList(RxSubscribe<BasePage<Technician>> rxSubscribe) {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", 100);//页数
        map.put("X-Nideshop-Token", getToken(context));
        sendRequest(HttpUtils.getApi().sysuserList(map).compose(RxHelper.<BasePage<Technician>>observe()), rxSubscribe);

    }
}
