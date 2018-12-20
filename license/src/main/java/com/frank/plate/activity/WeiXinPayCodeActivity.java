package com.frank.plate.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.WeixinCode;
import com.frank.plate.util.ToastUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WeiXinPayCodeActivity extends BaseActivity {

    @BindView(R.id.tv_shopName)
    TextView shop_name;


    @BindView(R.id.iv_code)
    ImageView iv_code;

    int order_id;
    OrderInfoEntity infoEntity;

    @Override
    protected void init() {

        tv_title.setText("微信收款");
        try {
            infoEntity = getIntent().getParcelableExtra(Configure.ORDERINFO);
            shop_name.setText(getIntent().getStringExtra("shop_name"));
            order_id = infoEntity.getId();
        } catch (Exception e) {
            ToastUtils.showToast(e.toString());
        }

    }

    @Override
    protected void setUpView() {
        ZXingLibrary.initDisplayOpinion(this);
        Api().prepay(infoEntity).subscribe(new RxSubscribe<WeixinCode>(this, true) {
            @Override
            protected void _onNext(WeixinCode weixinCode) {
                Log.i("tag  :", weixinCode.getCode_url());
                Bitmap mBitmap = CodeUtils.createImage(weixinCode.getCode_url(), 500, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));

                Glide.with(WeiXinPayCodeActivity.this)
                        .load(mBitmap)
                        .into(iv_code);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
                finish();
            }
        });


    }

    Disposable[] disposable;

    @Override
    protected void setUpData() {
        Log.e("微信支付轮询", "订单id:" + order_id);
        disposable = new Disposable[1];
        disposable[0] = Observable //计时器
                .interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {

                        Log.e("微信支付轮询", aLong + "次");

                        Api().payQuery(order_id).subscribe(new RxSubscribe<NullDataEntity>(WeiXinPayCodeActivity.this, false) {
                            @Override
                            protected void _onNext(NullDataEntity n) {

                                ToastUtils.showToast("收款成功!");
                                finish();

                                if (infoEntity.getOrder_status() == 0) {
                                    toMain(1);
                                } else if (infoEntity.getOrder_status() == 1)
                                    sendOrderInfo(OrderDoneActivity.class, infoEntity);
                            }

                            @Override
                            protected void _onError(String message) {
                                Log.i("微信支付轮询", message);
                            }
                        });
                    }
                });


    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable[0].dispose();
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_wei_xin_pay_code;
    }
}
