package com.frank.plate.activity;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Shop;
import com.frank.plate.bean.ShopImage;

import butterknife.BindView;

public class ShopInfoActivity extends BaseActivity {


    @BindView(R.id.shopName)
    TextView shopName;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.cb)
    ConvenientBanner cb;


    @Override
    protected void init() {
        tv_title.setText("门店信息");
    }

    @Override
    protected void setUpView() {


        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, true) {
            @Override
            protected void _onNext(Shop shop) {
                shopName.setText(shop.getShop().getShopName());
                phone.setText(shop.getShop().getPhone());
                address.setText(shop.getShop().getAddress());


                cb.setPages(new CBViewHolderCreator() {
                    @Override
                    public ImageHolderView createHolder(View itemView) {
                        return new ImageHolderView(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_car_info_pic;
                    }


                }, shop.getShop().getShopImageList());

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

    public class ImageHolderView extends Holder<ShopImage> {
        private ImageView imageView;

        public ImageHolderView(View itemView) {
            super(itemView);
        }


        @Override
        protected void initView(View itemView) {

            imageView = itemView.findViewById(R.id.iv);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override
        public void updateUI(ShopImage data) {


            Glide.with(ShopInfoActivity.this)
                    .load(data.getShopImage())
                    .into(imageView);


        }
    }
}
