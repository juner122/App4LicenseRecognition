package com.eb.geaiche.coupon;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.NullDataEntity;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

import static com.juner.mvp.Configure.shop_id;
import static com.juner.mvp.Configure.shop_name;

public class CouponNewActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    EditText name;
    @BindView(R.id.tv_standard)
    TextView tv_standard;
    @BindView(R.id.tv_price)
    EditText tv_price;//减免度

    @BindView(R.id.tv_price_in)
    EditText tv_price_in;//门槛金额

    @BindView(R.id.time)
    EditText time;//有效天数

    @BindView(R.id.is_die_jia)
    Switch is_die_jia;//是否能叠加

    @OnClick({R.id.reset, R.id.enter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:

                finish();
                break;

            case R.id.enter:
                addCoupon();
                break;

        }
    }

    private void addCoupon() {

        if (TextUtils.isEmpty(name.getText())) {
            ToastUtils.showToast("劵名称不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_price.getText())) {
            ToastUtils.showToast("减免额度不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tv_price_in.getText())) {
            ToastUtils.showToast("门槛金额不能为空！");
            return;
        }
        if (TextUtils.isEmpty(time.getText())) {
            ToastUtils.showToast("有效天数不能为空！");
            return;
        }


        Api().addShopCoupon(setCoupon()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("操作成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("操作失败！" + message);
            }
        });


    }

    private Coupon2 setCoupon() {
        Coupon2 coupon2 = new Coupon2();
        coupon2.setName(name.getText().toString());
        coupon2.setMin_amount(tv_price_in.getText().toString());
        coupon2.setType_money(tv_price.getText().toString());
        coupon2.setCycle(time.getText().toString());
        coupon2.setType("1");
        coupon2.setSuperposition(is_die_jia.isChecked() ? "1" : "2");
        coupon2.setDept_id(new AppPreferences(this).getString(shop_id, "-"));
        return coupon2;
    }

    @Override
    protected void init() {

        tv_title.setText("添加优惠劵");

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_new;
    }
}
