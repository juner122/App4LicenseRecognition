package com.eb.new_line_seller.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.eb.new_line_seller.util.DateUtil;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class OrderDoneActivity extends BaseActivity {

    private static final String TAG = "OrderDoneActivity";
    OrderInfo infoEntity;

    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;
    @BindView(R.id.tv_car_no)
    TextView tv_car_no;


    @BindView(R.id.tv_pay_type)
    TextView tv_pay_type;


    @BindView(R.id.tv_price)
    TextView tv_price;


    @BindView(R.id.tv_order_price)
    TextView tv_order_price;


    @BindView(R.id.tv_price3)
    TextView tv_price3;


    @BindView(R.id.tv_price4)
    TextView tv_price4;


    @BindView(R.id.tv_expect_date)
    TextView tv_expect_date;
    @BindView(R.id.tv_pay_time)
    TextView tv_pay_time;


    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;

    int id;

    @Override
    protected void init() {


        hideReturnView();

        tv_title.setText("完成订单");
        setRTitle("凭证打印");
        id = getIntent().getIntExtra(Configure.ORDERINFOID, -1);


        Api().orderDetail(id).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                infoEntity = o;
                setData();

            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                Toast.makeText(OrderDoneActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setData() {

        tv_order_sn.append(infoEntity.getOrderInfo().getOrder_sn());
        tv_car_no.append(infoEntity.getOrderInfo().getCar_no());
        tv_pay_type.append(infoEntity.getOrderInfo().getPay_name());
        tv_pay_time.append(infoEntity.getOrderInfo().getPay_time());


        tv_price.append(MathUtil.twoDecimal(infoEntity.getOrderInfo().getActual_price()));


        tv_order_price.append(MathUtil.twoDecimal(infoEntity.getOrderInfo().getOrder_price()));
        tv_price3.append(MathUtil.twoDecimal(infoEntity.getOrderInfo().getCoupon_price()));
        tv_price4.append(String.valueOf(MathUtil.twoDecimal(infoEntity.getOrderInfo().getOrder_price() - infoEntity.getOrderInfo().getActual_price())));


        tv_expect_date.append(DateUtil.getFormatedDateTime(infoEntity.getOrderInfo().getPlanfinishi_time()));

        tv_shopName.append(null == infoEntity.getShop().getShopName() ? "-" : infoEntity.getShop().getShopName());
        tv_name.append(null == infoEntity.getShop().getName() ? "-" : infoEntity.getShop().getName());
        tv_phone.append(null == infoEntity.getShop().getPhone() ? "-" : infoEntity.getShop().getPhone());
        tv_address.append(null == infoEntity.getShop().getAddress() ? "-" : infoEntity.getShop().getAddress());


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_done;
    }


    @OnClick({R.id.tv_done, R.id.tv_title_r})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_done:
                Api().confirmFinish(infoEntity.getOrderInfo().getId()).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {

                        toMain(1);
                    }

                    @Override
                    protected void _onError(String message) {
                        Log.d(TAG, message);
                        Toast.makeText(OrderDoneActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.tv_title_r:
                ToastUtils.showToast("未连接蓝牙打印机！");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        Log.i(TAG, "onBackPressed");

        ToastUtils.showToast("请完成订单");
    }
}
