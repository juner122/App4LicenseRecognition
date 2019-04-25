package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.ShoppingCartListAdapter;
import com.eb.geaiche.mvp.contacts.ShoppingCartContacts;
import com.eb.geaiche.mvp.model.ShoppingCartMdl;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.CartItem;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.NullDataEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartPtr extends BasePresenter<ShoppingCartContacts.ShoppingCartUI> implements ShoppingCartContacts.ShoppingCartPtr {

    private ShoppingCartContacts.ShoppingCartMdl mdl;
    private Activity context;
    ShoppingCartListAdapter adapter;

    public ShoppingCartPtr(@NonNull ShoppingCartContacts.ShoppingCartUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new ShoppingCartMdl(context);
        adapter = new ShoppingCartListAdapter(null, context);


    }

    /**
     * 获取购物车信息
     */
    @Override
    public void getShoppingCartInfo() {
        mdl.getShoppingCartInfo(new RxSubscribe<CartList>(context, true) {
            @Override
            protected void _onNext(CartList cartList) {
                adapter.setNewData(cartList.getCartList());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    @Override
    public void upDataPrice() {
        if (adapter.getData().size() == 0)
            getView().upAllPrice("0.00");
        else {
            BigDecimal allPrice = new BigDecimal(0);

            for (CartItem cartItem : adapter.getData()) {
                if (cartItem.isSelected()) {
                    BigDecimal price = new BigDecimal(cartItem.getRetail_product_price());
                    BigDecimal num = new BigDecimal(cartItem.getNumber());
                    allPrice = allPrice.add(price.multiply(num));
                }
            }
            getView().upAllPrice(MathUtil.twoDecimal(allPrice.doubleValue()));
        }
    }

    @Override
    public void infoRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter a, View view, int position) {


                if (view.getId() == R.id.tv_title) {//查看商品详情

                    getView().toGoodsInfoActivity(adapter.getData().get(position).getGoods_id());

                } else {
                    int num = adapter.getData().get(position).getNumber();//当前数量
                    if (view.getId() == R.id.add_btn) {//增加数量
                        num++;
                    } else if (view.getId() == R.id.reduce_btn) {//减少数量
                        if (num != 0) {
                            num--;
                        } else
                            ToastUtils.showToast("数量不能少于0！");

                    }
                    adapter.getData().get(position).setNumber(num);
                    adapter.notifyDataSetChanged();
                    upDataPrice();

                    onUpDateCart(adapter.getData().get(position).getGoods_id(), adapter.getData().get(position).getProduct_id(), num);
                }
            }
        });


        //用于计算总价格
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                List<CartItem> cartItems = new ArrayList<>();//用于计算总价格

                if (adapter.getData().get(position).isSelected()) {
                    adapter.getData().get(position).setSelected(false);
                    cartItems.remove(adapter.getData().get(position));
                } else {
                    adapter.getData().get(position).setSelected(true);
                    cartItems.add(adapter.getData().get(position));
                }
                adapter.notifyDataSetChanged();
                upDataPrice();


                boolean isAllPick = true;
                //判断是否取消全选
                for (CartItem cartItem : adapter.getData()) {

                    if (!cartItem.isSelected()) {
                        isAllPick = false;
                        break;
                    }
                }
                getView().onChangeAllPick(isAllPick);

            }
        });
    }

    //全选择
    @Override
    public void allPick(boolean isAllPick) {

        for (int i = 0; i < adapter.getData().size(); i++) {
            adapter.getData().get(i).setSelected(isAllPick);
        }
        adapter.notifyDataSetChanged();

        upDataPrice();
    }

    @Override
    public void onUpDateCart(Integer goodsId, Integer productId, int number) {


        mdl.shoppingCartUpdate(new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败！" + message);
            }
        }, goodsId, productId, number);
    }

    @Override
    public List<CartItem> getCartItemList() {

        List<CartItem> cartItems = new ArrayList<>();

        for (CartItem cartItem : adapter.getData()) {
            if (cartItem.isSelected()) {
                cartItems.add(cartItem);
            }
        }

        return cartItems;
    }


}
