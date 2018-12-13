package com.frank.plate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.SimpleGoodInfoAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.util.String2Utils;

import java.util.ArrayList;
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
    OrderInfoEntity infoEntity;
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

    @BindView(R.id.but_product_list)
    ImageButton but_product_list;
    @BindView(R.id.but_meal_list)
    ImageButton but_meal_list;


    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.rv2)
    RecyclerView rv2;

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
                break;

            case R.id.but_product_list://选择商品
                break;

            case R.id.tv_pick_technician://选择技师
                break;
            case R.id.ib_pick_date://选择预计完成时间
                break;
            case R.id.tv_car_info://车信息

                toActivity(CarInfoActivity.class, Configure.CARID, info.getOrderInfo().getCar_id());
                break;
        }


    }


    private int id;//订单ID

    private SimpleGoodInfoAdpter adpter1;
    private SimpleGoodInfoAdpter adpter2;

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
        tv_pick_technician.setVisibility(View.VISIBLE);
        ib_pick_date.setVisibility(View.VISIBLE);
        isFixOrder = true;
    }

    //保存修改  隐藏控件
    private void onFixOrderDone() {
        tv_fix_order.setText("修改下单");
        but_product_list.setVisibility(View.INVISIBLE);
        but_meal_list.setVisibility(View.INVISIBLE);
        tv_pick_technician.setVisibility(View.INVISIBLE);
        ib_pick_date.setVisibility(View.INVISIBLE);
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
                info = o;
                setInfo(o);

            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                Toast.makeText(OrderInfoActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setInfo(OrderInfo info) {

        infoEntity = info.getOrderInfo();
        pay_status = infoEntity.getPay_status();
        order_status = infoEntity.getOrder_status();

        setRTitle(infoEntity.getOrder_status_text());

        tv_order_sn.append(infoEntity.getOrder_sn());
        tv_car_no.setText(infoEntity.getCar_no());
        tv_mobile.setText(infoEntity.getMobile());
        tv_consignee.setText(infoEntity.getConsignee());


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
                tv_pick_date.setText(getFormatedDateTime(infoEntity.getPlanfinishi_time()));
                ll_price3.setVisibility(View.GONE);
                break;
            case 1://服务中
                tv2.setText("下单时间:");
                tv_fix_order.setText("通知客户取车");
                if (pay_status == 0)
                    tv_enter_order.setText("完成去结算");
                else {
                    tv_enter_order.setText("确认完成");
                    tv_enter_order.setBackgroundColor(getResources().getColor(R.color.D9D9D9));
                }
                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(infoEntity.getPlanfinishi_time()));
                ll_price3.setVisibility(View.GONE);
                break;
            case 2://完成
                tv2.setText("下单时间:");
                tv3.setVisibility(View.VISIBLE);
                tv3.setText(String.format("完成时间:%s", infoEntity.getConfirm_time()));
                ll_bottom.setVisibility(View.GONE);
                ll_pick_date.setVisibility(View.GONE);
                ll_price3.setVisibility(View.VISIBLE);
                break;

        }

        tv2.append(infoEntity.getAdd_time());

        tv_technician.setText(String2Utils.getString(infoEntity.getSysUserList()));


        adpter1 = new SimpleGoodInfoAdpter(getList(1, infoEntity.getGoodsList()), false);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);


        adpter2 = new SimpleGoodInfoAdpter(getList(2, infoEntity.getGoodsList()), false);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adpter2);


        double goodsPrice = String2Utils.getOrderGoodsPrice(infoEntity.getGoodsList(),1);
        double goodsPrice2 = String2Utils.getOrderGoodsPrice(infoEntity.getGoodsList(),2);

        tv_goods_price.append(String.valueOf(goodsPrice));

        tv_goods_price2.append(String.valueOf(goodsPrice2));


        tv_price1.append(String.valueOf(goodsPrice));
        tv_price2.append(String.valueOf(0.00d));
        tv_price3.append(String.valueOf(0.00d));
        tv_price4.append(String.valueOf(goodsPrice));

        tv_pay_status.append(infoEntity.getPay_status_text());
        tv_pay_type.append(getPayTypeText(infoEntity.getPay_type()));

        tv_price4_s.setText(infoEntity.getPay_status() == 2 ? "实收金额" : "应收金额");
        tv_jdy.setText(info.getReceptionist().getUsername());

    }


    //确认下单
    private void confirmOrder() {

        sendOrderInfo(MakeOrderSuccessActivity.class, info);


    }


    private List<GoodsEntity> getList(int type, List<GoodsEntity> list) {
        List<GoodsEntity> gs = new ArrayList<>();
        for (GoodsEntity g : list) {
            if (g.getType() == type)
                gs.add(g);
        }
        return gs;
    }

}
