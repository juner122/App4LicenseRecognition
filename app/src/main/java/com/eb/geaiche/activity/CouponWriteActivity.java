package com.eb.geaiche.activity;


import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon;
import com.juner.mvp.bean.NullDataEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponWriteActivity extends BaseActivity {

    Coupon coupon;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.info)
    TextView info;//劵号
    @BindView(R.id.time)
    TextView time;//时间
    @BindView(R.id.stock)
    TextView stock;//库存

    @BindView(R.id.post)
    TextView post;//

    @OnClick({R.id.post, R.id.tv_title_r})
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.post:

                if (coupon.getCoupon_status() == 1) {
                    convertCoupon();
                } else if (coupon.getCoupon_status() == 2) {
                    ToastUtils.showToast("该优惠劵已使用！");
                } else if (coupon.getCoupon_status() == 3) {
                    ToastUtils.showToast("该优惠劵已过期！");
                }
                break;
            case R.id.tv_title_r:
                //核销记录

//                toActivity(CouponWriteRecordActivity.class);
                break;
        }

    }

    @Override
    protected void init() {

        tv_title.setText("优惠劵核销");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        coupon = getIntent().getParcelableExtra("coupon");

        name.setText(coupon.getName());
        info.setText("劵号：" + coupon.getCoupon_number());
        time.setText("有效期至：" + coupon.getUsed_time());
        stock.setText("库存：" + coupon.getStock());
        switch (coupon.getCoupon_status()) { //1可用，2已用，3过期
            case 1:
                post.setText("点击核销");
                break;

            case 2:
                post.setText("已使用");
                break;

            case 3:
                post.setText("已过期");
                break;
        }


    }

    //核销券
    private void convertCoupon() {

        Api().convertCoupon(coupon.getCoupon_number()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                toActivity(CouponWriteSuccessActivity.class, coupon, "coupon");
                finish();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("核销失败：" + message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_write;
    }
}
