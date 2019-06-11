package com.eb.geaiche.mvp.contacts;

import androidx.recyclerview.widget.RecyclerView;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CartItem;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.NullDataEntity;

import java.util.List;

public class ShoppingCartContacts {
    /**
     * view 层接口
     */
    public interface ShoppingCartUI extends IBaseView {


        //更新总价
        void upAllPrice(String allPrice);

        void onChangeAllPick(boolean isAllPick);//改变是否全选

        void toGoodsInfoActivity(int goodsId);//
    }

    /**
     * presenter 层接口
     */
    public interface ShoppingCartPtr extends IBasePresenter {

        //获取购物车信息
        void getShoppingCartInfo();

        void upDataPrice();

        void infoRecyclerView(RecyclerView rv);

        void allPick(boolean isAllPick);//

        void onUpDateCart(Integer goodsId, Integer productId, int number);//


        List<CartItem> getCartItemList();//根据购物车商品对象转换成商品对象

        int getCartItemNum();

        void delete();
    }

    /**
     * model 层接口
     */
    public interface ShoppingCartMdl {


        void getShoppingCartInfo(RxSubscribe<CartList> rxSubscribe);

        void shoppingCartUpdate(RxSubscribe<NullDataEntity> rxSubscribe, Integer goodsId, Integer productId, int number);

        void delete(RxSubscribe<NullDataEntity> rxSubscribe, int[] cartIds);
    }

}
