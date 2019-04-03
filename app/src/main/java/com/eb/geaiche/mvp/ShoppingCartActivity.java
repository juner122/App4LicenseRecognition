package com.eb.geaiche.mvp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.ShoppingCartListAdapter;
import com.eb.geaiche.mvp.contacts.ShoppingCartContacts;
import com.eb.geaiche.mvp.presenter.ShoppingCartPtr;
import com.juner.mvp.bean.CartList;

import butterknife.BindView;
import butterknife.OnClick;

//购物车
public class ShoppingCartActivity extends BaseActivity<ShoppingCartContacts.ShoppingCartPtr> implements ShoppingCartContacts.ShoppingCartUI {

    ShoppingCartListAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView rv;

    boolean isAllPick = false;


    @OnClick({R.id.iv_pick_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pick_all:
                if (isAllPick) {
                    isAllPick = false;
                    ((ImageView) v).setImageResource(R.drawable.icon_unpick2);
                } else {
                    isAllPick = true;
                    ((ImageView) v).setImageResource(R.drawable.icon_pick2);
                }
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
        adapter = new ShoppingCartListAdapter(null, getSelfActivity());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.add_btn) {//增加数量

                    
                } else if (view.getId() == R.id.reduce_btn) {//减少数量


                }
            }
        });

    }

    @Override
    public ShoppingCartContacts.ShoppingCartPtr onBindPresenter() {
        return new ShoppingCartPtr(this);
    }


    @Override
    public void setShoppingCartInfo(CartList cartInfo) {
        adapter.setNewData(cartInfo.getCartList());
    }
}
