package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.FixPickServiceContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixServiceList;
import com.juner.mvp.bean.FixServieEntity;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixPickServiceMdl extends BaseModel implements FixPickServiceContacts.FixPickServiceMdl {
    Context context;

    public FixPickServiceMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getServiceData(RxSubscribe<List<GoodsCategory>> rxSubscribe) {

        Map<String, Object> map = new HashMap<>();
        map.put("type", Configure.Goods_TYPE_3);
        map.put("X-Nideshop-Token", getToken(context));

        sendRequest(HttpUtils.getApi().queryShopcategoryAll(map).compose(RxHelper.<List<GoodsCategory>>observe()), rxSubscribe);

    }

    @Override
    public void searchServer(int id, String key, RxSubscribe<FixServieEntity> rxSubscribe) {

        if (id == -1)
            sendRequest(HttpUtils.getFix().searchServer(getToken(context), key).compose(RxHelper.<FixServieEntity>observe()), rxSubscribe);
        else
            sendRequest(HttpUtils.getFix().searchServer(getToken(context), id, key).compose(RxHelper.<FixServieEntity>observe()), rxSubscribe);
    }

    @Override
    public void getGoodList(RxSubscribe<GoodsList> rxSubscribe, String goodsTitle, int page, String categoryId) {
        Map<String, Object> map = new HashMap<>();
        if (null != goodsTitle && !goodsTitle.equals(""))
            map.put("goodsTitle", goodsTitle);
        if (null != categoryId && !categoryId.equals(""))
            map.put("categoryId", categoryId);


        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        map.put("type", Configure.Goods_TYPE_3);
        map.put("X-Nideshop-Token", getToken(context));
        sendRequest(HttpUtils.getApi().xgxshopgoodsList(map).compose(RxHelper.<GoodsList>observe()), rxSubscribe);
    }

}
