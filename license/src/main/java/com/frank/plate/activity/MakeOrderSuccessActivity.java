package com.frank.plate.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.SimpleActivityInfo2Adpter;
import com.frank.plate.adapter.SimpleGoodInfo2Adpter;
import com.frank.plate.adapter.SimpleServerInfo2Adpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.util.DateUtil;
import com.frank.plate.util.MathUtil;
import com.frank.plate.util.String2Utils;
import com.frank.plate.util.StringUtils;
import com.frank.plate.util.ToastUtils;
import com.frank.plate.view.LinePathView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf;

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
    @BindView(R.id.rv_act)
    RecyclerView rv_act;
    @BindView(R.id.rv_servers)
    RecyclerView rv_servers;

    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;


    OrderInfo info;
    SimpleGoodInfo2Adpter simpleGoodInfo2Adpter;
    SimpleActivityInfo2Adpter simpleActivityInfo2Adpter;
    SimpleServerInfo2Adpter serverInfo2Adpter;
    String iv_lpv_url;//签名图片 七牛云url

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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Glide.with(this)
                .asDrawable()
                .load(Uri.fromFile(new File(Configure.LinePathView_url)))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(skipMemoryCacheOf(true))
                .into(iv_lpv);

        iv_lpv_url = intent.getStringExtra(Configure.Domain);
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


        simpleGoodInfo2Adpter = new SimpleGoodInfo2Adpter(info.getOrderInfo().getGoodsList());
        simpleActivityInfo2Adpter = new SimpleActivityInfo2Adpter(info.getOrderInfo().getUserActivityList());
        serverInfo2Adpter = new SimpleServerInfo2Adpter(info.getOrderInfo().getSkillList());


        double goodsPrice = String2Utils.getOrderGoodsPrice(info.getOrderInfo().getGoodsList());

        double ServerPrice = String2Utils.getOrderServicePrice(info.getOrderInfo().getSkillList());

        rv_goods.setLayoutManager(new LinearLayoutManager(this));
        rv_goods.setAdapter(simpleGoodInfo2Adpter);

        rv_act.setLayoutManager(new LinearLayoutManager(this));
        rv_act.setAdapter(simpleActivityInfo2Adpter);

        rv_servers.setLayoutManager(new LinearLayoutManager(this));
        rv_servers.setAdapter(serverInfo2Adpter);


        all_price.append(MathUtil.twoDecimal(goodsPrice + ServerPrice));
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

    @OnClick({R.id.tv_now_pay, R.id.tv_start_service, R.id.tv_back, R.id.ll_autograph})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_now_pay:

                sendOrderInfo(OrderPayActivity.class, info);
                break;
            case R.id.tv_start_service:

                Api().beginServe(info.getOrderInfo().getId(), info.getOrderInfo().getOrder_sn(), iv_lpv_url).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
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

            case R.id.ll_autograph://签名
                toActivity(AutographActivity.class);
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
