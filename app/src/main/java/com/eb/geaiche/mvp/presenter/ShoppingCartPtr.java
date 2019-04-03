package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.eb.geaiche.mvp.contacts.ShoppingCartContacts;
import com.eb.geaiche.mvp.model.ShoppingCartMdl;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.CartList;

public class ShoppingCartPtr extends BasePresenter<ShoppingCartContacts.ShoppingCartUI> implements ShoppingCartContacts.ShoppingCartPtr {

    private ShoppingCartContacts.ShoppingCartMdl mdl;
    private Activity context;

    public ShoppingCartPtr(@NonNull ShoppingCartContacts.ShoppingCartUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new ShoppingCartMdl(context);
    }

    @Override
    public void getShoppingCartInfo() {
        mdl.getShoppingCartInfo(new RxSubscribe<CartList>(context, true) {
            @Override
            protected void _onNext(CartList cartList) {
                getView().setShoppingCartInfo(cartList);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }
}
