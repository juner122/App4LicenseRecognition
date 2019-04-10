package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.FixPickPartsContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixPickPartsMdl extends BaseModel implements FixPickPartsContacts.FixPickPartsMdl {
    Context context;

    public FixPickPartsMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getPartsData(RxSubscribe<List<GoodsCategory>> rxSubscribe) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", Configure.Goods_TYPE_4);
        map.put("X-Nideshop-Token", getToken(context));

        sendRequest(HttpUtils.getApi().queryShopcategoryAll(map).compose(RxHelper.<List<GoodsCategory>>observe()), rxSubscribe);
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

    @Override
    public void getGoodList(RxSubscribe<GoodsList> rxSubscribe, String goodsTitle, int page, String categoryId) {
        Map<String, Object> map = new HashMap<>();
        if (null != goodsTitle && !goodsTitle.equals(""))
            map.put("goodsTitle", goodsTitle);
        if (null != categoryId && !categoryId.equals(""))
            map.put("categoryId", categoryId);


        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        map.put("type", Configure.Goods_TYPE_4);
        map.put("X-Nideshop-Token", getToken(context));
        sendRequest(HttpUtils.getApi().xgxshopgoodsList(map).compose(RxHelper.<GoodsList>observe()), rxSubscribe);
    }

}
