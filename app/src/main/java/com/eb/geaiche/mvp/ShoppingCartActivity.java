package com.eb.geaiche.mvp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.MallGoodsActivity;
import com.eb.geaiche.activity.MallGoodsInfoActivity;
import com.eb.geaiche.activity.MallMakeOrderActivity;
import com.eb.geaiche.mvp.contacts.ShoppingCartContacts;
import com.eb.geaiche.mvp.presenter.ShoppingCartPtr;
import com.eb.geaiche.util.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

//购物车
public class ShoppingCartActivity extends BaseActivity<ShoppingCartContacts.ShoppingCartPtr> implements ShoppingCartContacts.ShoppingCartUI {


    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_price_all)
    TextView tv_price_all;

    @BindView(R.id.iv_pick_all)
    ImageView iv_pick_all;

    boolean isAllPick;


    @OnClick({R.id.iv_pick_all, R.id.tv_total})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pick_all:
                if (isAllPick) {
                    isAllPick = false;
                    iv_pick_all.setImageResource(R.drawable.icon_unpick2);
                    tv_price_all.setText("0.00");
                    getPresenter().allPick(isAllPick);
                } else {
                    isAllPick = true;
                    iv_pick_all.setImageResource(R.drawable.icon_pick2);
                    getPresenter().allPick(isAllPick);
                }
                break;

            case R.id.tv_total://结算
                if (getPresenter().getCartItemList().size() == 0) {
                    ToastUtils.showToast("请最少选择一件商品！");
                    return;
                }
//                toActivity(MallMakeOrderActivity.class, getPresenter().getCartItemList(), "cart_goods");
                Intent intent = new Intent(this, MallMakeOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("cart_goods", (ArrayList<? extends Parcelable>) getPresenter().getCartItemList());
                bundle.putInt("buyType", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void init() {
        tv_title.setText("购物车");


        getPresenter().infoRecyclerView(rv);
        //获取数据
        getPresenter().getShoppingCartInfo();


    }

    @Override
    public ShoppingCartContacts.ShoppingCartPtr onBindPresenter() {
        return new ShoppingCartPtr(this);
    }


    @Override
    public void upAllPrice(String allPrice) {
        tv_price_all.setText(allPrice);
    }

    @Override
    public void onChangeAllPick(boolean is) {

        if (is) {
            iv_pick_all.setImageResource(R.drawable.icon_pick2);
        } else {
            iv_pick_all.setImageResource(R.drawable.icon_unpick2);
        }

        isAllPick = is;
    }

    @Override
    public void toGoodsInfoActivity(int goodsId) {
        toActivity(MallGoodsInfoActivity.class, MallGoodsActivity.goodsId, goodsId);
    }
}
