package com.frank.plate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.SimpleGoodInfo2Adpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.util.DateUtil;
import com.frank.plate.util.String2Utils;
import com.frank.plate.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MakeOrderSuccessActivity extends BaseActivity {

    private static final String TAG = "SuccessActivity";
    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;
    @BindView(R.id.tv_car_no)
    TextView tv_car_no;
    @BindView(R.id.tv_make_date)
    TextView tv_make_date;
    @BindView(R.id.tv_expect_date)
    TextView tv_expect_date;

    @BindView(R.id.tv_remarks)
    TextView tv_remarks;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_now_pay)
    TextView tv_now_pay;

    @BindView(R.id.all_price)
    TextView all_price;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;


    OrderInfo info;
    SimpleGoodInfo2Adpter simpleGoodInfo2Adpter;


    @Override
    protected void init() {

        tv_title.setText("订单确认");

        info = getIntent().getParcelableExtra(Configure.ORDERINFO);

        Api().orderDetail(info.getOrderInfo().getId()).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                Log.i("OrderInfo订单信息：", o.getOrderInfo().toString());
                info = o;
                setInfo();
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                ToastUtils.showToast(message);

            }
        });

    }

    private void setInfo() {


        tv_order_sn.append(info.getOrderInfo().getOrder_sn());
        tv_car_no.append(info.getOrderInfo().getCar_no());
        tv_make_date.append(info.getOrderInfo().getAdd_time());

        tv_expect_date.append(DateUtil.getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));
        tv_remarks.setText(info.getOrderInfo().getPostscript());


        if (info.getOrderInfo().getPay_status() == 0)//是否隐藏支付button
            tv_now_pay.setVisibility(View.VISIBLE);
        else
            tv_now_pay.setVisibility(View.INVISIBLE);


        tv_shopName.append(null == info.getShop().getShopName() ? "-" : info.getShop().getShopName());
        tv_name.append(null == info.getShop().getName() ? "-" : info.getShop().getName());
        tv_phone.append(null == info.getShop().getPhone() ? "-" : info.getShop().getPhone());
        tv_address.append(null == info.getShop().getAddress() ? "-" : info.getShop().getAddress());


        simpleGoodInfo2Adpter = new SimpleGoodInfo2Adpter(info.getOrderInfo().getGoodsAndSkillList());


        double goodsPrice = String2Utils.getOrderGoodsPrice(info.getOrderInfo().getGoodsList());

        double ServerPrice = String2Utils.getOrderServicePrice(info.getOrderInfo().getSkillList());

        rv_goods.setLayoutManager(new LinearLayoutManager(this));
        rv_goods.setAdapter(simpleGoodInfo2Adpter);


        all_price.append(String.valueOf((goodsPrice + ServerPrice)));
    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_make_order_success;
    }

    @OnClick({R.id.tv_now_pay, R.id.tv_start_service, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_now_pay:

                sendOrderInfo(OrderPayActivity.class, info);
                break;
            case R.id.tv_start_service:
                Api().beginServe(info.getOrderInfo().getId(), info.getOrderInfo().getOrder_sn()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        toMain(1);
                    }

                    @Override
                    protected void _onError(String message) {
                        Log.d(TAG, message);
                    }
                });
                break;
            case R.id.tv_back:
                toMain(1);
                break;


        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity

        Log.i(TAG, "onBackPressed");

        toMain(1);

    }


}
