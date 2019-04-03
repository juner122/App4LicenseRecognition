package com.eb.geaiche.mvp.contacts;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CartList;

public class ShoppingCartContacts {
    /**
     * view 层接口
     */
    public interface ShoppingCartUI extends IBaseView {


        //更新购物车数据
        void setShoppingCartInfo(CartList cartInfo);

    }

    /**
     * presenter 层接口
     */
    public interface ShoppingCartPtr extends IBasePresenter {

        //获取购物车信息
        void getShoppingCartInfo();
    }

    /**
     * model 层接口
     */
    public interface ShoppingCartMdl {


        void getShoppingCartInfo(RxSubscribe<CartList> rxSubscribe);
    }

}
