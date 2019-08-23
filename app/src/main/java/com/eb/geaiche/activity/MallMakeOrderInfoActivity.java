package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallOrderGoodsListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.wxapi.WXPayHelper;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CartItem;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.PayInfo;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.ShopEntity;
import com.juner.mvp.bean.XgxPurchaseOrderPojo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MallMakeOrderInfoActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.pay_price)
    TextView pay_price;//实付金额
    @BindView(R.id.reduce_price)
    TextView reduce_price;//优惠金额
    @BindView(R.id.order_price)
    TextView order_price;//订单金额

    @BindView(R.id.pay_status)
    TextView pay_status;//支付状态

    @BindView(R.id.order_sn)
    TextView order_sn;//订单编号

    @BindView(R.id.order_time)
    TextView order_time;//下单时间

    @BindView(R.id.order_pay_info)
    TextView order_pay_info;//订单支付信息

    @BindView(R.id.order_pay)
    TextView order_pay;//按钮
    @BindView(R.id.et_postscript)
    EditText et_postscript;//留言

    ShopEntity shop;
    int id;
    MallOrderGoodsListAdapter adapter;


    @OnClick({R.id.order_pay})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.order_pay://支付
                if (payStatus == 1)
                    tuneUpWXPay();
                break;
        }
    }

    int payStatus;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_make_order_info;
    }

    List<CartItem> cartItems = new ArrayList<>();//确认页面传来的商品列表

    @Override
    protected void init() {
        tv_title.setText("订单详情");
        tv_title_r.setText("待支付");

        id = getIntent().getIntExtra(Configure.ORDERINFOID, 0);
        cartItems = getIntent().getParcelableArrayListExtra("cart_goods");
    }


    @Override
    protected void setUpView() {
        adapter = new MallOrderGoodsListAdapter(cartItems, this);
        rv.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv.setAdapter(adapter);


    }

    @Override
    protected void setUpData() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderInfo();
        getAddress();
    }

    //获取订单信息
    private void getOrderInfo() {
        Api().mallOrderInfo(id).subscribe(new RxSubscribe<XgxPurchaseOrderPojo>(this, true) {
            @Override
            protected void _onNext(XgxPurchaseOrderPojo x) {
                pay_price.setText("-￥" + x.getRealPrice());
                order_price.setText("-￥" + x.getOrderPrice());
                reduce_price.setText(null == x.getDiscountPrice() ? "-￥0.00" : "-￥" + x.getDiscountPrice());

                order_sn.setText(x.getOrderSn());
                order_time.setText(DateUtil.getFormatedDateTime(x.getCreateTime()));

                if (null == x.getBuyerMessage() || x.getBuyerMessage().equals(""))
                    et_postscript.setText("暂无留言");
                else
                    et_postscript.setText(x.getBuyerMessage());


                payStatus = x.getPayStatus();
                switch (payStatus) {
                    case 1:
                        tv_title_r.setText("待支付");
                        pay_status.setText("待支付");
                        break;
                    case 2:
                        tv_title_r.setText("已支付");
                        pay_status.setText("已支付");
                        showPayedView();

                        break;
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("订单信息获取失败！");
                finish();
            }
        });

    }

    //显示已支付页面
    private void showPayedView() {
        order_pay_info.setVisibility(View.VISIBLE);
        order_pay.setText("等待发货");
        order_pay.setBackgroundColor(Color.parseColor("#A7D4FA"));

    }

    /**
     * 调起微信支付
     */
    private void tuneUpWXPay() {

        Api().prepay(id).subscribe(new RxSubscribe<PayInfo>(this, true) {
            @Override
            protected void _onNext(PayInfo payInfo) {
                //支付成功
//
                new WXPayHelper(MallMakeOrderInfoActivity.this).pay(payInfo);//发

                MyAppPreferences.putString(Configure.WXPay_PRICE, payInfo.getPayInfo().getTotalFee());//价格 为分
                MyAppPreferences.putString(Configure.WXPay_SN, payInfo.getPayInfo().getOrderSn());
                MyAppPreferences.putString(Configure.WXPay_TIME, DateUtil.getFormatedDateTime(payInfo.getPayInfo().getTimestamp() * 1000));
                MyAppPreferences.putInt(Configure.ORDERINFOID, id);


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    //获取地址信息
    private void getAddress() {
        Api().shopInfo().subscribe(new RxSubscribe<Shop>(this, false) {
            @Override
            protected void _onNext(Shop s) {

                shop = s.getShop();
                name.setText(shop.getShopName());
                phone.setText(shop.getPhone());
                address.setText(shop.getAddress());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
                //判断是否是401 token失效
                SystemUtil.isReLogin(message, MallMakeOrderInfoActivity.this);
            }
        });

    }
}

