package com.frank.plate.activity;


import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Shop;

public class ShopInfoActivity extends BaseActivity {


    @Override
    protected void init() {

    }

    @Override
    protected void setUpView() {


        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, true) {
            @Override
            protected void _onNext(Shop shop) {
                tv_title.setText(shop.getShop().getShopName());
            }

            @Override
            protected void _onError(String message) {

            }
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_shop_info;
    }
}
