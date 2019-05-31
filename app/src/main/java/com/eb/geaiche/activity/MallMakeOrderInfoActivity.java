package com.eb.geaiche.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MallOrderGoodsListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CartItem;
import com.juner.mvp.bean.NullDataEntity;
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

    ShopEntity shop;
    int id;
    MallOrderGoodsListAdapter adapter;


    @OnClick({R.id.order_pay})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.order_pay://支付

                tuneUpWXPay2();
                break;
        }
    }

    private IWXAPI api;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_mall_make_order_info;
    }

    List<CartItem> cartItems = new ArrayList<>();//确认页面传来的商品列表

    @Override
    protected void init() {

        api = WXAPIFactory.createWXAPI(this, Configure.APP_ID);


        tv_title.setText("订单详情");
        tv_title_r.setText("待支付");

        shop = getIntent().getParcelableExtra(Configure.shop_info);
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

        getOrderInfo();

        name.setText(shop.getShopName());
        phone.setText(shop.getPhone());
        address.setText(shop.getAddress());


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

                switch (x.getPayType()) {
                    case 1:
                        tv_title_r.setText("待支付");
                        pay_status.setText("待支付");
                        break;
                    case 2:
                        tv_title_r.setText("已支付");
                        pay_status.setText("已支付");
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


    /**
     * 调起微信支付
     */
    private void tuneUpWXPay() {
        Api().prepay(id).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity n) {
                //支付成功
//                ToastUtils.showToast("支付成功");


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    private void tuneUpWXPay2() {

        PayReq req = new PayReq();
        req.appId = Configure.APP_ID;
        req.partnerId = "1408952102";//商户号
        req.prepayId = "WX1217752501201407033233368018";//预支付交易会话ID
        req.nonceStr = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";//随机字符串
        req.timeStamp = "1412000000";
        req.packageValue = "Sign=WXPay";
        req.sign = "C380BEC2BFD727A4B6845133519F3AD6";//签名
        req.extData = "app data"; // optional

        ToastUtils.showToast("正常调起支付");
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }
}

