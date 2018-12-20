package com.frank.plate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.adapter.SimpleGoodInfoAdpter;
import com.frank.plate.adapter.SimpleMealInfoAdpter;
import com.frank.plate.adapter.SimpleServiceInfoAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.OrderInfo;

import com.frank.plate.bean.Technician;
import com.frank.plate.util.CartUtils;
import com.frank.plate.util.DateUtil;
import com.frank.plate.util.String2Utils;
import com.frank.plate.util.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


import static com.frank.plate.util.DateUtil.getFormatedDateTime;
import static com.frank.plate.util.String2Utils.getPayTypeText;

/**
 * 订单详情
 */
public class OrderInfoActivity extends BaseActivity {
    private static final String TAG = "OrderInfoActivity";
    private boolean isFixOrder;//是否正在修改订单

    int pay_status; //  订单支付状态   0 未支付,2已支付
    int order_status; //订单状态 0  待服务：pay_status=2  已预约：pay_status=0        1.(服务中) 2.完成

    OrderInfo info;
    @BindView(R.id.tv_order_sn)
    TextView tv_order_sn;

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;


    @BindView(R.id.tv_technician)
    TextView tv_technician;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv_price1)
    TextView tv_price1;
    @BindView(R.id.tv_price2)
    TextView tv_price2;
    @BindView(R.id.tv_price3)
    TextView tv_price3;
    @BindView(R.id.tv_price4)
    TextView tv_price4;
    @BindView(R.id.tv_price4_s)
    TextView tv_price4_s;

    @BindView(R.id.tv_pay_type)
    TextView tv_pay_type;
    @BindView(R.id.tv_pay_status)
    TextView tv_pay_status;

    @BindView(R.id.tv_mobile)
    TextView tv_mobile;
    @BindView(R.id.tv_consignee)
    TextView tv_consignee;

    @BindView(R.id.tv_pick_technician)
    ImageButton tv_pick_technician;

    @BindView(R.id.ib_pick_date)
    ImageButton ib_pick_date;

    @BindView(R.id.tv_fix_order)
    TextView tv_fix_order;

    @BindView(R.id.tv_enter_order)
    TextView tv_enter_order;

    @BindView(R.id.ll_price3)
    View ll_price3;


    @BindView(R.id.ll_bottom)
    View ll_bottom;

    @BindView(R.id.ll_pick_date)
    View ll_pick_date;

    @BindView(R.id.tv_pick_date)
    TextView tv_pick_date;

    @BindView(R.id.tv_jdy)
    TextView tv_jdy;

    @BindView(R.id.tv_goods_price)
    TextView tv_goods_price;

    @BindView(R.id.tv_goods_price2)
    TextView tv_goods_price2;

    @BindView(R.id.et_info)
    TextView et_info;

    @BindView(R.id.but_product_list)
    ImageButton but_product_list;
    @BindView(R.id.but_meal_list)
    ImageButton but_meal_list;


    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.rv2)
    RecyclerView rv2;
    @BindView(R.id.rv3)
    RecyclerView rv3;

    SimpleMealInfoAdpter sma;

    List<Technician> technicians;

    @OnClick({R.id.tv_fix_order, R.id.tv_enter_order, R.id.but_meal_list, R.id.but_product_list, R.id.tv_pick_technician, R.id.ib_pick_date, R.id.tv_car_info})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_fix_order:
                if (!isFixOrder)
                    onFixOrder();
                else
                    onFixOrderDone();
                break;

            case R.id.tv_enter_order:
                switch (order_status) {
                    case 0:

                        confirmOrder();

                        break;
                    case 1://服务中

                        if (pay_status == 2)
                            sendOrderInfo(OrderDoneActivity.class, info);
                        else
                            sendOrderInfo(OrderPayActivity.class, info);

                        break;
                    case 2://完成


                        break;

                }


                break;
            case R.id.but_meal_list:

                toActivity(ServeListActivity.class, Configure.isShow, 1);
                break;

            case R.id.but_product_list://选择商品
                toActivity(ProductMealListActivity.class, Configure.user_id, info.getOrderInfo().getUser_id());
                break;

            case R.id.tv_pick_technician://选择技师
                startActivityForResult(new Intent(this, TechnicianListActivity.class), new ResultBack() {
                    @Override
                    public void resultOk(Intent data) {
                        //to do what you want when resultCode == RESULT_OK
                        tv_technician.setText("");
                        technicians = data.getParcelableArrayListExtra("Technician");
                        tv_technician.setText(String2Utils.getString(technicians));
                        info.getOrderInfo().setSysUserList(technicians);
                        remake();

                    }
                });

                break;
            case R.id.ib_pick_date://选择预计完成时间
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                //正确设置方式 原因：注意事有说明
                startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DATE), startDate.get(Calendar.HOUR_OF_DAY) + 1, startDate.get(Calendar.MINUTE));
                endDate.set(2020, 11, 31);


                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tv_pick_date.setText(DateUtil.getFormatedDateTime2(date));
                        info.getOrderInfo().setPlanfinishi_time(date.getTime());

                        remake();
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setRangDate(startDate, endDate)//起始终止年月日设定
                        .setTitleBgColor(getResources().getColor(R.color.appColor))//标题背景颜色 Night mode
                        .build();
                pvTime.show();


                break;
            case R.id.tv_car_info://车信息

                toActivity(CarInfoActivity.class, Configure.CARID, info.getOrderInfo().getCar_id());
                break;
        }


    }


    private int id;//订单ID

    private SimpleGoodInfoAdpter adpter1;
    private SimpleServiceInfoAdpter adpter2;


    CartUtils cartUtils;

    @Override
    protected void init() {


        id = getIntent().getIntExtra(Configure.ORDERINFOID, 0);
        cartUtils = MyApplication.cartUtils;

        getOrderInfoData();

    }


    //修改订单  显示控件
    private void onFixOrder() {
        tv_fix_order.setText("保存修改");
        but_product_list.setVisibility(View.VISIBLE);
        but_meal_list.setVisibility(View.VISIBLE);

        isFixOrder = true;
    }

    //保存修改  隐藏控件
    private void onFixOrderDone() {
        tv_fix_order.setText("修改订单");
        but_product_list.setVisibility(View.INVISIBLE);
        but_meal_list.setVisibility(View.INVISIBLE);

        isFixOrder = false;


        info.getOrderInfo().setGoodsList(cartUtils.getProductList());
        info.getOrderInfo().setSkillList(cartUtils.getServerList());
        info.getOrderInfo().setUserActivityList(cartUtils.getMealList());

        remake();


    }


    @Override
    protected void setUpView() {
        tv_title.setText("订单详情");


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_order_info;
    }


    private void getOrderInfoData() {
        Api().orderDetail(id).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                Log.i("OrderInfo订单信息：", o.getOrderInfo().toString());
                info = o;
                setInfo();

            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                Toast.makeText(OrderInfoActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setInfo() {


        pay_status = info.getOrderInfo().getPay_status();
        order_status = info.getOrderInfo().getOrder_status();

        setRTitle(info.getOrderInfo().getOrder_status_text());

        tv_order_sn.append(info.getOrderInfo().getOrder_sn());
        tv_car_no.setText(info.getOrderInfo().getCar_no());
        tv_mobile.setText(info.getOrderInfo().getMobile());
        tv_consignee.setText(info.getOrderInfo().getConsignee());
        et_info.setText(info.getOrderInfo().getPostscript());

        tv_technician.setText(String2Utils.getString(info.getOrderInfo().getSysUserList()));

        tv_jdy.setText(info.getReceptionist().getUsername());
        tv2.append(info.getOrderInfo().getAdd_time());

        tv_price4_s.setText(info.getOrderInfo().getPay_status() == 2 ? "实收金额" : "应收金额");
        tv_pay_type.append(getPayTypeText(info.getOrderInfo().getPay_type()));

        tv_pay_status.append(info.getOrderInfo().getPay_status_text());

        switch (order_status) {
            case 0://待服务
                tv2.setText("预约时间:");

                tv_enter_order.setText("确认下单");
                if (pay_status == 0) {
                    tv_fix_order.setText("修改订单");
                    tv_fix_order.setVisibility(View.VISIBLE);
                } else {

                    tv_fix_order.setVisibility(View.INVISIBLE);
                }

                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));
                ll_price3.setVisibility(View.GONE);
                tv_pick_technician.setVisibility(View.VISIBLE);
                break;
            case 1://服务中
                tv2.setText("下单时间:");
                tv_fix_order.setText("通知客户取车");
                tv_fix_order.setVisibility(View.VISIBLE);
                if (pay_status == 0)
                    tv_enter_order.setText("完成去结算");
                else {
                    tv_enter_order.setText("确认完成");
                    tv_enter_order.setBackgroundColor(getResources().getColor(R.color.D9D9D9));
                }
                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));
                ll_price3.setVisibility(View.GONE);
                tv_pick_technician.setVisibility(View.VISIBLE);

                tv_fix_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showToast("短信已发送！");

                    }
                });

                break;
            case 2://完成
                tv2.setText("下单时间:");
                tv3.setVisibility(View.VISIBLE);
                tv3.setText(String.format("完成时间:%s", info.getOrderInfo().getConfirm_time()));
                ll_bottom.setVisibility(View.GONE);
                ll_pick_date.setVisibility(View.GONE);
                ll_price3.setVisibility(View.VISIBLE);
                tv_pick_technician.setVisibility(View.INVISIBLE);

                break;

        }


        cartUtils.setPrductDatas(info.getOrderInfo().getGoodsList());
        cartUtils.setServieDatas(info.getOrderInfo().getSkillList());
        cartUtils.setMealDatas2(info.getOrderInfo().getUserActivityList());

        reSetRecyclerViewData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reSetRecyclerViewData();
    }

    private void reSetRecyclerViewData() {


        adpter1 = new SimpleGoodInfoAdpter(cartUtils.getProductList(), false);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);


        adpter2 = new SimpleServiceInfoAdpter(cartUtils.getServerList(), false);//工时
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adpter2);

        sma = new SimpleMealInfoAdpter(cartUtils.getMealList());//套餐
        rv3.setLayoutManager(new LinearLayoutManager(this));
        rv3.setAdapter(sma);


        double goodsPrice = String2Utils.getOrderGoodsPrice(cartUtils.getProductList());

        double ServerPrice = String2Utils.getOrderServicePrice(cartUtils.getServerList());

        tv_goods_price.setText(String.valueOf("已选：￥" + goodsPrice));

        tv_goods_price2.setText(String.valueOf("已选：￥" + ServerPrice));


        tv_price1.setText(String.valueOf("￥" + (goodsPrice + ServerPrice)));
        tv_price2.setText(String.valueOf("-￥" + 0.00d));
        tv_price3.setText(String.valueOf("-￥" + 0.00d));
        tv_price4.setText(String.valueOf("￥" + (goodsPrice + ServerPrice)));


    }

    //确认下单
    private void confirmOrder() {

        if (!isFixOrder)
            sendOrderInfo(MakeOrderSuccessActivity.class, info);
        else {
            ToastUtils.showToast("请先保存修改！");
        }

    }


    //订单修改
    private void remake() {


        Log.i("OrderInfo订单修改：", info.getOrderInfo().toString());
        Api().remake(info.getOrderInfo()).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                ToastUtils.showToast("修改成功");
                info = o;
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败:" + message);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartUtils.deleteAllData();
    }
}
