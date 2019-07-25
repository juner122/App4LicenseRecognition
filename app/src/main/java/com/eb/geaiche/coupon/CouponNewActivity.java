package com.eb.geaiche.coupon;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Shop;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

import static com.juner.mvp.Configure.shop_address;
import static com.juner.mvp.Configure.shop_id;
import static com.juner.mvp.Configure.shop_name;
import static com.juner.mvp.Configure.shop_phone;
import static com.juner.mvp.Configure.shop_user_name;

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

    int type;//1添加，2查看
    String id;//优惠劵id
    String dept_id;//门店id

    Coupon2 coupon2;//查看的优惠劵详情对象


    @OnClick({R.id.reset, R.id.enter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:

                finish();
                break;

            case R.id.enter:
                if (type == 1)
                    addCoupon();
                else
                    fixCoupon();
                break;

        }
    }


    @Override
    protected void init() {

        type = getIntent().getIntExtra("view_type", -1);

        if (type == 1)
            tv_title.setText("添加优惠劵");
        else {
            tv_title.setText("优惠券详情");
            getCouponInfo();

        }
    }

    private void getCouponInfo() {
        id = getIntent().getStringExtra("id");


        //获取优惠劵信息
        Api().shopCouponInfo(id).subscribe(new RxSubscribe<Coupon2>(this, true) {
            @Override
            protected void _onNext(Coupon2 c) {
                coupon2 = c;
                name.setText(coupon2.getName());
                tv_price.setText(coupon2.getType_money());
                tv_price_in.setText(coupon2.getMin_amount());
                time.setText(coupon2.getCycle());
                is_die_jia.setChecked(coupon2.getSuperposition().equals("1"));

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("优惠劵信息获取失败！" + message);
            }
        });


    }


    //新增
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
        if (TextUtils.isEmpty(dept_id)) {
            ToastUtils.showToast("门店ID获取失败！");
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

    //修改
    private void fixCoupon() {

        Api().fixShopCoupon(setCoupon()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
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
        Coupon2 c_new;



        if (type == 1)
            c_new = new Coupon2();
        else
            c_new = coupon2;

        c_new.setName(name.getText().toString());
        c_new.setMin_amount(tv_price_in.getText().toString());
        c_new.setType_money(tv_price.getText().toString());
        c_new.setCycle(time.getText().toString());
        c_new.setType("1");//劵分类,暂为默认为1 满减劵
        c_new.setSuperposition(is_die_jia.isChecked() ? "1" : "0");
        c_new.setDept_id(dept_id);

        return c_new;
    }


    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, true) {
            @Override
            protected void _onNext(Shop shop) {
                dept_id = shop.getShop().getId();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("门店信息获取失败！" + message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_coupon_new;
    }


}
