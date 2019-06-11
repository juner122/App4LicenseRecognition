package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.ShoppingCartContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.NullDataEntity;

import net.grandcentrix.tray.AppPreferences;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartMdl extends BaseModel implements ShoppingCartContacts.ShoppingCartMdl {

    Context context;

    public ShoppingCartMdl(Context context) {
        this.context = context;
    }


    //获取购物车信息
    @Override
    public void getShoppingCartInfo(RxSubscribe<CartList> rxSubscribe) {
        sendRequest(HttpUtils.getApi().getShoppingCart(getToken(context), "1", "1").compose(RxHelper.<CartList>observe()), rxSubscribe);
    }

    //更新商品购物车数据
    @Override
    public void shoppingCartUpdate(RxSubscribe<NullDataEntity> rxSubscribe, Integer goodsId, Integer productId, int number) {

        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", getToken(context));
        if (null != goodsId)
            map.put("goodsId", goodsId);
        if (null != productId)
            map.put("productId", productId);

        map.put("number", number);

        sendRequest(HttpUtils.getApi().shoppingCartUpdate(map).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }

    @Override
    public void delete(RxSubscribe<NullDataEntity> rxSubscribe, int[] cartIds) {
        sendRequest(HttpUtils.getApi().delete(getToken(context), cartIds).compose(RxHelper.observe()), rxSubscribe);

    }


}
