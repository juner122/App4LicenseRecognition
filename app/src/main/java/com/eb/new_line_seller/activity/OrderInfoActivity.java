package com.eb.new_line_seller.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.eb.new_line_seller.view.NoticeDialog;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.SimpleGoodInfoAdpter;
import com.eb.new_line_seller.adapter.SimpleMealInfoAdpter;
import com.eb.new_line_seller.adapter.SimpleServiceInfoAdpter;
import com.eb.new_line_seller.api.RxSubscribe;

import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.OrderInfo;

import com.juner.mvp.bean.Server;
import com.juner.mvp.bean.Technician;
import com.eb.new_line_seller.util.DateUtil;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.String2Utils;
import com.eb.new_line_seller.util.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


import static com.eb.new_line_seller.util.DateUtil.getFormatedDateTime;

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
    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;


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

    @OnClick({R.id.tv_fix_order, R.id.tv_enter_order, R.id.but_meal_list, R.id.but_product_list, R.id.tv_pick_technician, R.id.ib_pick_date, R.id.tv_car_info, R.id.tv_notice})
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
                            toActivity(OrderDoneActivity.class, Configure.ORDERINFOID, info.getOrderInfo().getId());
                        else
                            sendOrderInfo(OrderPayActivity.class, info);

                        break;
                    case 2://完成
                        break;

                }


                break;
            case R.id.but_meal_list:

                Intent intent2 = new Intent(this, ServeListActivity.class);
                startActivity(intent2);

                break;

            case R.id.but_product_list://选择商品

                Intent intent = new Intent(this, ProductMealListActivity.class);
                intent.putExtra(Configure.user_id, info.getOrderInfo().getUser_id());

                intent.putExtra(Configure.car_no, info.getOrderInfo().getCar_no());


                intent.putExtra(Configure.isFixOrder, true);

                startActivity(intent);


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
                        .setRangDate(DateUtil.getStartDate(), DateUtil.getEndDate())//起始终止年月日设定
                        .setTitleBgColor(getResources().getColor(R.color.appColor))//标题背景颜色 Night mode
                        .build();
                pvTime.show();


                break;
            case R.id.tv_car_info://车信息

//              toActivity(CarInfoInputActivity.class, Configure.CARID, info.getOrderInfo().getCar_id());

                Intent intent3 = new Intent(OrderInfoActivity.this, CarInfoInputActivity.class);
                intent3.putExtra("result_code", 001);
                intent3.putExtra(Configure.CARID, info.getOrderInfo().getCar_id());
                startActivity(intent3);

                break;

            case R.id.tv_notice:
                //通知客户

                final NoticeDialog nd = new NoticeDialog(this, info.getOrderInfo().getMobile());
                nd.show();
                nd.setClicklistener(new NoticeDialog.ClickListenerInterface() {
                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        nd.dismiss();
                    }
                });
                break;

        }


    }


    private int id;//订单ID

    private SimpleGoodInfoAdpter adpter1;
    private SimpleServiceInfoAdpter adpter2;


    List<GoodsEntity> productList = new ArrayList<>();//显示的商品
    List<Server> server = new ArrayList<>();//显示的工时
    List<GoodsEntity> meal = new ArrayList<>();//显示的套餐

    @Override
    protected void init() {


        id = getIntent().getIntExtra(Configure.ORDERINFOID, 0);

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
                ToastUtils.showToast(message);
            }
        });

    }

    private void setInfo() {


        pay_status = info.getOrderInfo().getPay_status();
        order_status = info.getOrderInfo().getOrder_status();

        productList = info.getOrderInfo().getGoodsList();
        server = info.getOrderInfo().getSkillList();
        meal = info.getOrderInfo().getUserActivityList();


        setRTitle(info.getOrderInfo().getOrder_status_text());

        tv_order_sn.setText(String.valueOf("订单号:" + info.getOrderInfo().getOrder_sn()));
        tv_car_no.setText(info.getOrderInfo().getCar_no());
        tv_mobile.setText(info.getOrderInfo().getMobile());
        tv_consignee.setText(info.getOrderInfo().getConsignee());
        et_info.setText(info.getOrderInfo().getPostscript());

        tv_technician.setText(String2Utils.getString(info.getOrderInfo().getSysUserList()));

        tv_jdy.setText(null == info.getReceptionist() || null == info.getReceptionist().getUsername() ? "-" : info.getReceptionist().getUsername());

        tv_price4_s.setText(info.getOrderInfo().getPay_status() == 2 ? "实收金额" : "应收金额");


        tv_pay_type.setText(String.valueOf("支付方式：" + info.getOrderInfo().getPay_name()));

        tv_pay_status.setText(String.valueOf("支付状态：" + info.getOrderInfo().getPay_status_text()));

        switch (order_status) {
            case 0://待服务
                tv2.setText("预约时间:" + info.getOrderInfo().getAdd_time());

                tv_enter_order.setText("确认下单");
                if (pay_status == 0) {
                    tv_fix_order.setText("修改订单");
                    tv_fix_order.setVisibility(View.INVISIBLE);
                    ll_price3.setVisibility(View.GONE);
                    tv_price4.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getOrder_price())));
                } else {
                    tv_fix_order.setVisibility(View.INVISIBLE);
                    ll_price3.setVisibility(View.VISIBLE);
                    tv_price4.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getActual_price())));
                }

                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));

                tv_pick_technician.setVisibility(View.VISIBLE);

                break;
            case 1://服务中
                tv2.setText("下单时间:" + info.getOrderInfo().getAdd_time());
                tv_fix_order.setText("通知客户取车");
                tv_fix_order.setVisibility(View.INVISIBLE);
                if (pay_status == 0) {
                    tv_enter_order.setText("完成去结算");
                    tv_price4.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getOrder_price())));
                    ll_price3.setVisibility(View.GONE);
                } else {
                    tv_enter_order.setText("确认完成");
                    tv_enter_order.setBackgroundColor(getResources().getColor(R.color.D9D9D9));
                    tv_price4.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getActual_price())));
                    ll_price3.setVisibility(View.VISIBLE);
                }
                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));

                tv_pick_technician.setVisibility(View.VISIBLE);

                tv_fix_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtils.showToast("短信已发送！");

                    }
                });

                break;
            case 2://完成
                tv2.setText("下单时间:" + info.getOrderInfo().getAdd_time());
                tv3.setVisibility(View.VISIBLE);
                tv3.setText(String.format("完成时间:%s", info.getOrderInfo().getConfirm_time()));
                ll_bottom.setVisibility(View.GONE);
                ll_pick_date.setVisibility(View.GONE);
                ll_price3.setVisibility(View.VISIBLE);
                tv_pick_technician.setVisibility(View.INVISIBLE);
                tv_price4.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getActual_price())));
                break;

        }

        tv_price1.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getOrder_price())));
        tv_price2.setText(String.valueOf("-￥" + MathUtil.twoDecimal(info.getOrderInfo().getCoupon_price())));//优惠券减少金额
//        tv_price3.setText(String.valueOf("-￥" + (MathUtil.twoDecimal(info.getOrderInfo().getYouweijie_price()))));//优惠金额
        tv_price3.setText(String.valueOf("-￥" + info.getOrderInfo().getCustom_cut_price()));//优惠金额


        double goodsPrice = String2Utils.getOrderGoodsPrice(productList);

        double ServerPrice = String2Utils.getOrderServicePrice(server);

        tv_goods_price.setText(String.valueOf("已选：￥" + MathUtil.twoDecimal(goodsPrice)));

        tv_goods_price2.setText(String.valueOf("已选：￥" + MathUtil.twoDecimal(ServerPrice)));


        adpter1 = new SimpleGoodInfoAdpter(productList, false);
        rv1.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv1.setAdapter(adpter1);


        adpter2 = new SimpleServiceInfoAdpter(server, false);//工时
        rv2.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv2.setAdapter(adpter2);

        sma = new SimpleMealInfoAdpter(meal);//套餐
        rv3.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv3.setAdapter(sma);


        Glide.with(this)
                .asDrawable()
                .load(info.getOrderInfo().getDistrict())
                .into(iv_lpv);
    }

    //确认下单
    private void confirmOrder() {

        if (!isFixOrder) {

            if (null == info.getOrderInfo().getSysUserList() || info.getOrderInfo().getSysUserList().size() == 0) {
                ToastUtils.showToast("请最少选择一个技师！");
                return;
            }

            sendOrderInfo(MakeOrderSuccessActivity.class, info);


        } else {
            ToastUtils.showToast("请先保存修改！");
        }


    }


    //订单修改
    private void remake() {
        info.getOrderInfo().setGoodsList(null);
        info.getOrderInfo().setSkillList(null);
        info.getOrderInfo().setUserActivityList(null);
        Log.i("OrderInfo订单修改：", info.getOrderInfo().toString());
        Api().remake(info.getOrderInfo()).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                ToastUtils.showToast("修改成功");
                info = o;
                setInfo();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败:" + message);
            }
        });

    }

}
