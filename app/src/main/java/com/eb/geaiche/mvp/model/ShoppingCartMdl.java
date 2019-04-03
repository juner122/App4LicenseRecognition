package com.eb.geaiche.mvp.model;

import android.content.Context;
import com.eb.geaiche.mvp.contacts.ShoppingCartContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.CartList;

public class ShoppingCartMdl extends BaseModel implements ShoppingCartContacts.ShoppingCartMdl {

    Context context;

    public ShoppingCartMdl(Context context) {
        this.context = context;
    }

    @Override
    public void getShoppingCartInfo(RxSubscribe<CartList> rxSubscribe) {
        sendRequest(HttpUtils.getApi().getShoppingCart(getToken(context)).compose(RxHelper.<CartList>observe()), rxSubscribe);
    }
}
