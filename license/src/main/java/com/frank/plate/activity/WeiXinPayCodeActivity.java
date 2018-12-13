package com.frank.plate.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.WeixinCode;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import butterknife.BindView;

public class WeiXinPayCodeActivity extends BaseActivity {

    @BindView(R.id.tv_shopName)
    TextView shop_name;


    @BindView(R.id.iv_code)
    ImageView iv_code;


    @Override
    protected void init() {

        tv_title.setText("微信收款");
        shop_name.setText(getIntent().getStringExtra("shop_name"));
    }

    @Override
    protected void setUpView() {
        ZXingLibrary.initDisplayOpinion(this);
        Api().prepay(getIntent().getIntExtra(Configure.ORDERINFOID, -1)).subscribe(new RxSubscribe<WeixinCode>(this, true) {
            @Override
            protected void _onNext(WeixinCode weixinCode) {

                Bitmap mBitmap = CodeUtils.createImage(weixinCode.getCode_url(), 500, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));

                Glide.with(WeiXinPayCodeActivity.this)
                        .load(mBitmap)
                        .into(iv_code);



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
        return R.layout.activity_wei_xin_pay_code;
    }
}
