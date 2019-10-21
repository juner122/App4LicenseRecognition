package com.eb.geaiche.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;

import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.buletooth.PrinterCommand;
import com.eb.geaiche.util.BluetoothUtils;
import com.eb.geaiche.util.ButtonUtils;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.view.BtConfirmDialog;
import com.eb.geaiche.view.ConfirmDialogInfo;
import com.eb.geaiche.view.NoticeDialog;
import com.eb.geaiche.zbar.CaptureActivity;
import com.gprinter.command.EscCommand;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SimpleGoodInfoAdpter;
import com.eb.geaiche.adapter.SimpleMealInfoAdpter;
import com.eb.geaiche.adapter.SimpleServiceInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;

import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.OrderInfo;

import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.Technician;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eb.geaiche.buletooth.BuletoothUtil.BLUETOOTH_DISCOVERABLE_DURATION;
import static com.eb.geaiche.buletooth.BuletoothUtil.CONN_PRINTER;
import static com.eb.geaiche.buletooth.BuletoothUtil.CONN_STATE_DISCONN;
import static com.eb.geaiche.buletooth.BuletoothUtil.CONN_STATE_FAILED;
import static com.eb.geaiche.buletooth.BuletoothUtil.NO_DERVER;
import static com.eb.geaiche.buletooth.BuletoothUtil.PRINTER_COMMAND_ERROR;
import static com.eb.geaiche.buletooth.BuletoothUtil.REQUEST_CODE_BLUETOOTH_ON;
import static com.eb.geaiche.util.DateUtil.getFormatedDateTime;
import static com.gprinter.command.EscCommand.JUSTIFICATION.CENTER;
import static com.gprinter.command.EscCommand.JUSTIFICATION.LEFT;
import static com.gprinter.command.EscCommand.JUSTIFICATION.RIGHT;

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
    @BindView(R.id.tv_order_state)
    TextView tv_order_state;

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;
    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;
    @BindView(R.id.tv_bluetooth)
    TextView tv_bluetooth;


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

    @BindView(R.id.tv_notice)
    TextView tv_notice;

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


    @BindView(R.id.et_deputy)
    TextView et_deputy;//送修人

    @BindView(R.id.et_deputy_mobile)
    TextView et_deputy_mobile;//送修人电话

    @BindView(R.id.ll_deputy)
    View ll_deputy;//送修人

    @BindView(R.id.ll_deputy_m)
    View ll_deputy_m;//送修人电话


    SimpleMealInfoAdpter sma;

    List<Technician> technicians;
    String iv_lpv_url = "";//签名图片 七牛云url

    @OnClick({R.id.tv_fix_order, R.id.et_info, R.id.tv_enter_order, R.id.but_meal_list, R.id.but_product_list, R.id.tv_pick_technician, R.id.ib_pick_date, R.id.tv_car_info, R.id.tv_notice, R.id.tv_back, R.id.tv_title_r, R.id.tv_title_r2, R.id.tv_bluetooth})
    public void onClick(View v) {

        if (ButtonUtils.isFastDoubleClick(v.getId())) {//防止按钮多次重复点击
            return;
        }

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

            case R.id.tv_pick_technician:
                if (order_status == 2) {//查看绩效分配

                    toActivity(MeritsDistributionActivity.class, "orderId", id);

                } else {//选择技师


                    Intent intent4 = new Intent(this, TechnicianListActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putParcelableArrayList("Technician", (ArrayList<? extends Parcelable>) technicians);
                    intent4.putExtras(bundle);
                    intent4.putExtra("type", 1);
                    startActivity(intent4);

                }
                break;
            case R.id.ib_pick_date://选择预计完成时间

                TimePickerView pvTime = new TimePickerBuilder(this, (date, v1) -> {
                    tv_pick_date.setText(DateUtil.getFormatedDateTime2(date));
                    info.getOrderInfo().setPlanfinishi_time(date.getTime());

                    remake();
                }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                        .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                        .setCancelColor(Color.WHITE)//取消按钮文字颜色
                        .setRangDate(DateUtil.getStartDate(), DateUtil.getEndDate())//起始终止年月日设定
                        .setTitleBgColor(getResources().getColor(R.color.appColor))//标题背景颜色 Night mode
                        .build();
                pvTime.show();


                break;
            case R.id.tv_car_info://车信息

                Intent intent3 = new Intent(OrderInfoActivity.this, CarInfoInputActivity.class);
                intent3.putExtra("result_code", 001);
                intent3.putExtra(Configure.CARID, info.getOrderInfo().getCar_id());
                startActivity(intent3);

                break;

            case R.id.tv_notice:
                //通知客户

                final NoticeDialog nd = new NoticeDialog(this, info.getOrderInfo().getMobile());
                nd.show();
                nd.setClicklistener(() -> {
                    // TODO Auto-generated method stub
                    nd.dismiss();
                });
                break;
            case R.id.tv_back:
                if (getIntent().getBooleanExtra("push", false))
                    toActivity(MainActivity.class);
                else
                    finish();

                break;

            case R.id.tv_bluetooth://连接蓝牙

                if (isConnectable) {
                    ToastUtils.showToast("打印机已连接,请打印！");
                } else
                    connectBluetooth(false);//连接蓝牙


                break;
            case R.id.tv_title_r://蓝牙打印

                btnReceiptPrint();//蓝牙打印

                break;
            case R.id.et_info://修改备注  弹出修改框

                final ConfirmDialogInfo d = new ConfirmDialogInfo(this, et_info.getText().toString());
                d.show();
                d.setClicklistener(new ConfirmDialogInfo.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String postscript) {
                        d.dismiss();
                        fixInfo(postscript);
                    }


                });

                break;
            case R.id.tv_title_r2://套卡录入

                Intent i = new Intent(this, CaptureActivity.class);
                i.putExtra("view_type", 2);
                i.putExtra("type", 1);
                i.putExtra("id", String.valueOf(info.getOrderInfo().getId()));

                startActivity(i);
                break;

            case R.id.ll_autograph://签名

//                //弹出对话框
//                final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(this, getResources().getString(R.string.agreement), getResources().getString(R.string.agreement_title));
//                confirmDialog.show();
//                confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
//                    @Override
//                    public void doConfirm() {
//                        confirmDialog.dismiss();
//                        toActivity(AutographActivity.class, "class", "OrderInfo");
//
//                    }
//                    @Override
//                    public void doCancel() {
//                        confirmDialog.dismiss();
//                    }
//                });

                break;
        }


    }


    private int id;//订单ID

    private SimpleGoodInfoAdpter adpter1;
    private SimpleServiceInfoAdpter adpter2;


    List<GoodsEntity> productList = new ArrayList<>();//显示的商品
    List<GoodsEntity> server = new ArrayList<>();//显示的工时
    List<GoodsEntity> meal = new ArrayList<>();//显示的套餐

    @Override
    protected void init() {


        id = getIntent().getIntExtra(Configure.ORDERINFOID, 0);


        if (MyAppPreferences.getShopType()) {
            ll_deputy.setVisibility(View.GONE);
            ll_deputy_m.setVisibility(View.GONE);
        }


        // //初始化蓝牙
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        getOrderInfoData();


//        if (!TextUtils.isEmpty(MyAppPreferences.getString(Configure.BluetoothAddress))) {//有连接过的设备就自动连接
//            //自动连接蓝牙
//            connectBluetooth(true);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConnectable && !TextUtils.isEmpty(MyAppPreferences.getString(Configure.BluetoothAddress))) {//有连接过的设备就自动连接
            //自动连接蓝牙
            connectBluetooth(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getBooleanExtra("CaptureActivity", false)) {
            getOrderInfoData();
        } else {


            tv_technician.setText("");
            technicians = intent.getParcelableArrayListExtra("Technician");
            tv_technician.setText(String2Utils.getString(technicians));
            info.getOrderInfo().setSysUserList(technicians);
            remake();
        }
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


//        if (MyAppPreferences.getShopType()) {//加盟店才能在订单详情打印凭证
        setRTitle("凭证打印");

        tv_bluetooth.setVisibility(View.VISIBLE);


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

                if (MyAppPreferences.getShopType() && info.getOrderInfo().getOrder_status() != 2)
                    setRTitle2("套卡扫码");
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                ToastUtils.showToast(message);
            }
        });

    }

    private void setInfo() {


        et_deputy.setText(info.getOrderInfo().getDeputy());
        et_deputy_mobile.setText(info.getOrderInfo().getDeputy_mobile());


        pay_status = info.getOrderInfo().getPay_status();
        order_status = info.getOrderInfo().getOrder_status();

        productList = getGoodsList(info.getOrderInfo().getGoodsList(), false);//
        server = getGoodsList(info.getOrderInfo().getGoodsList(), true);//服务

        meal = info.getOrderInfo().getUserActivityList();

        tv_order_state.setText(info.getOrderInfo().getOrder_status_text());

        tv_order_sn.setText("订单号:" + info.getOrderInfo().getOrder_sn());
        tv_car_no.setText(info.getOrderInfo().getCar_no());

        if (null == info.getOrderInfo().getMobile() || info.getOrderInfo().getMobile().equals("")) {
            tv_notice.setVisibility(View.GONE);
            tv_mobile.setText("暂无手机号");
        } else
            tv_mobile.setText(info.getOrderInfo().getMobile());

        tv_consignee.setText(info.getOrderInfo().getConsignee());

        et_info.setText(info.getOrderInfo().getPostscript());

        tv_technician.setText(String2Utils.getString(info.getOrderInfo().getSysUserList()));
        technicians = info.getOrderInfo().getSysUserList();

        tv_jdy.setText(null == info.getReceptionist() || null == info.getReceptionist().getNickName() ? "-" : info.getReceptionist().getNickName());

        tv_price4_s.setText(info.getOrderInfo().getPay_status() == 2 ? "实收金额" : "应收金额");


        tv_pay_type.setText(String.valueOf("支付方式：" + info.getOrderInfo().getPay_name()));


        if (null != info.getOrderInfo().getPay_type() && info.getOrderInfo().getPay_type() == 21) {//套卡核销加卡号
            tv_pay_type.setText(String.valueOf("支付方式：" + info.getOrderInfo().getProvince()));
        }


        tv_pay_status.setText(String.valueOf("支付状态：" + info.getOrderInfo().getPay_status_text()));

        switch (order_status) {
            case 0://待服务
                tv2.setText("预约时间:" + info.getOrderInfo().getAdd_time());

                tv_enter_order.setText("确认下单");
                if (pay_status == 0) {
                    tv_fix_order.setText("修改订单");
                    tv_fix_order.setVisibility(View.GONE);
                    ll_price3.setVisibility(View.GONE);
                    tv_price4.setText(("￥" + MathUtil.twoDecimal(info.getOrderInfo().getOrder_price())));
                } else {
                    tv_fix_order.setVisibility(View.GONE);
                    ll_price3.setVisibility(View.VISIBLE);
                    tv_price4.setText(("￥" + MathUtil.twoDecimal(info.getOrderInfo().getActual_price())));
                }

                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));
                tv_pick_technician.setVisibility(View.VISIBLE);

                break;
            case 1://服务中
                tv2.setText("下单时间:" + info.getOrderInfo().getAdd_time());
                tv_fix_order.setText("通知客户取车");
                tv_fix_order.setVisibility(View.GONE);
                if (pay_status == 0) {
                    tv_enter_order.setText("完成去结算");
                    tv_price4.setText("￥" + MathUtil.twoDecimal(info.getOrderInfo().getOrder_price()));
                    ll_price3.setVisibility(View.GONE);
                } else {
                    tv_enter_order.setText("确认完成");

                    tv_price4.setText("￥" + MathUtil.twoDecimal(info.getOrderInfo().getActual_price()));
                    ll_price3.setVisibility(View.VISIBLE);
                }
//                tv_enter_order.setBackgroundColor(getResources().getColor(R.color.D9D9D9));
                ll_pick_date.setVisibility(View.VISIBLE);
                tv_pick_date.setText(getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));

                tv_pick_technician.setVisibility(View.VISIBLE);

                tv_fix_order.setOnClickListener(view -> ToastUtils.showToast("短信已发送！"));

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

        double ServerPrice = String2Utils.getOrderGoodsPrice(server);

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


    //获取商品或工时
    /**
     * @param isService 是否查工时
     */
    private List<GoodsEntity> getGoodsList(List<GoodsEntity> goodsEntities, boolean isService) {
        if (null == goodsEntities || goodsEntities.size() == 0) {
            return new ArrayList<>();
        }
        List<GoodsEntity> list = new ArrayList<>();
        for (GoodsEntity entity : goodsEntities) {

            if (isService) {
                if (entity.getType() != Configure.Goods_TYPE_4) {
                    list.add(entity);
                }
            } else {
                if (entity.getType() == Configure.Goods_TYPE_4) {
                    list.add(entity);
                }
            }
        }
        return list;


    }


    private BluetoothAdapter mBluetoothAdapter;//蓝牙


    /**
     * 连接蓝牙 ，分自动和手动连接
     *
     * @param isAuto 是否自动连接
     */
    private void connectBluetooth(boolean isAuto) {

        if (null != mBluetoothAdapter) {
            if (mBluetoothAdapter.isEnabled())//蓝牙已打开
                getDeviceList(isAuto);
            else turnOnBluetooth();//未开蓝牙 打开系统设置

        } else ToastUtils.showToast("设备不支持Bluetooth");

    }


    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    public int ID = 0;

    //票据打印
    public void btnReceiptPrint() {

        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].getConnState()) {
            ToastUtils.showToast(getString(R.string.str_cann_printer));
            return;

        }
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].getCurrentPrinterCommand() == PrinterCommand.ESC) {
            sendReceiptWithResponse();
        } else {
            mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
        }

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].closePort(ID);
                    }
                    break;
                case PRINTER_COMMAND_ERROR:

                    ToastUtils.showToast(getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:

                    ToastUtils.showToast(getString(R.string.str_cann_printer));
                    break;

                case NO_DERVER:

                    ToastUtils.showToast(getString(R.string.no_dervier));
                    tv_bluetooth.setText(getString(R.string.no_dervier));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 发送票据
     */
    void sendReceiptWithResponse() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addSetPrintingAreaWidth((short) 500);//设置打印区域宽度，默认打印区域宽度为384点
        esc.addPrintAndFeedLines((byte) 3);
        // 设置打印居中
        esc.addSelectJustification(CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);

//        esc.addText("Sample\n");
        esc.addText(info.getOrderInfo().getCar_no() + "\n");//打印车牌
        esc.addPrintAndLineFeed();


        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 手机号码
        esc.addText("手机号码：" + info.getOrderInfo().getMobile() + "\n");

        // 车型
//        if (null == info.getUserCarCondition() || null == info.getUserCarCondition().getBrand() || "".equals(info.getUserCarCondition().getBrand()))
//            esc.addText("车型：" + "-" + "\n");//打印里程数
//        else
//            esc.addText("车型：" + info.getUserCarCondition().getBrand() + info.getUserCarCondition().getName() + "\n");//打印里程数


        if (null == info.getUserCarCondition() || null == info.getUserCarCondition().getMileage() || "".equals(info.getUserCarCondition().getMileage()))
            esc.addText("里程数：" + "未填写" + "\n");//打印里程数
        else
            esc.addText("里程数：" + info.getUserCarCondition().getMileage() + "km" + "\n");//打印里程数


        if (info.getOrderInfo().getConsignee().equals("匿名")) {
            // 会员姓名
            esc.addText("会员姓名：" + "匿名" + "\n");
        } else {
            // 会员姓名
            esc.addText("会员姓名：" + info.getOrderInfo().getConsignee() + "\n");
        }

        // 打印文字
        esc.addText(tv_order_sn.getText().toString() + "\n");//打印订单号


        // 打印文字
        esc.addText(tv2.getText().toString() + "\n");//打印下单时间
        if (order_status == 2)
            esc.addText(tv3.getText().toString() + "\n");//打印完成时间

        esc.addText("================================\n");//打印完成时间

        if (null != info.getOrderInfo().getGoodsList() || info.getOrderInfo().getGoodsList().size() > 0) {
            esc.addText("服务工时\t小计:" + String2Utils.getOrderGoodsPrice(getGoodsList(info.getOrderInfo().getGoodsList(), true)) + "\n");
            for (GoodsEntity ge : getGoodsList(info.getOrderInfo().getGoodsList(), true)) {
                esc.addSelectJustification(LEFT);
                esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
                esc.addText(ge.getGoods_name());
                esc.addPrintAndLineFeed();
                esc.addSelectJustification(LEFT);
                esc.addSetAbsolutePrintPosition((short) 7);
//                esc.addText("x1");
                esc.addText(ge.getNumberStringX());
                esc.addSetAbsolutePrintPosition((short) 12);
                esc.addSelectJustification(RIGHT);
                esc.addText("" + ge.getRetail_price());
                esc.addPrintAndLineFeed();
                esc.addPrintAndLineFeed();
            }
            esc.addText("--------------------------------\n");//打印完成时间
            esc.addSelectJustification(LEFT);
        }

        if (null != info.getOrderInfo().getGoodsList() || info.getOrderInfo().getGoodsList().size() > 0) {

            esc.addText("商品项目\t小计:" + String2Utils.getOrderGoodsPrice(getGoodsList(info.getOrderInfo().getGoodsList(), false)) + "\n");
            for (GoodsEntity ge : getGoodsList(info.getOrderInfo().getGoodsList(), false)) {

                esc.addSelectJustification(LEFT);
                esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
                esc.addText(ge.getGoods_name());
                esc.addPrintAndLineFeed();
                esc.addSelectJustification(LEFT);

                esc.addSetAbsolutePrintPosition((short) 7);
                esc.addText(ge.getNumberStringX());

                esc.addSetAbsolutePrintPosition((short) 12);

                esc.addSelectJustification(RIGHT);
                esc.addText(String.valueOf(Double.parseDouble(ge.getRetail_price()) * ge.getNumber()));
                esc.addPrintAndLineFeed();
                esc.addPrintAndLineFeed();
            }
        }

        if (null != info.getOrderInfo().getUserActivityList()) {
            for (GoodsEntity gu : info.getOrderInfo().getUserActivityListReverse()) {
                esc.addSelectJustification(LEFT);
                esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
                esc.addText(gu.getGoodsName());
                esc.addPrintAndLineFeed();
                esc.addSelectJustification(LEFT);

                esc.addSetAbsolutePrintPosition((short) 7);
//                esc.addText("x1");
                esc.addText(gu.getNumberStringX());
                esc.addSetAbsolutePrintPosition((short) 12);

                esc.addSelectJustification(RIGHT);
                esc.addText("抵扣");
                esc.addPrintAndLineFeed();
                esc.addPrintAndLineFeed();
            }
        }

        esc.addSelectJustification(RIGHT);
        esc.addPrintAndLineFeed();
        esc.addText("总计："+tv_price4.getText().toString());
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(LEFT);
        esc.addText("================================\n");//打印完成时间
        esc.addText("备注\n");
        esc.addText(info.getOrderInfo().getPostscript() + "\n");
        esc.addText("--------------------------------\n");//打印完成时间

        esc.addPrintAndLineFeed();
        if (null != iv_lpv.getDrawable()) {
            esc.addSelectJustification(LEFT);
            Bitmap b = ((BitmapDrawable) iv_lpv.getDrawable()).getBitmap();
            esc.addText("签名\n");
            esc.addSelectJustification(CENTER);
            esc.addRastBitImage(b, 230, 0);
            esc.addPrintAndLineFeed();
            esc.addText("================================\n");//打印完成时间
            esc.addSelectJustification(LEFT);
            esc.addPrintAndLineFeed();
        }


        esc.addText(info.getShop().getShopName() + "\n");
        esc.addText("门店店长：" + info.getShop().getName() + "\n");
        esc.addText("联系信息：" + info.getShop().getPhone() + "\n");
        esc.addText("地址：" + info.getShop().getAddress() + "\n");
        esc.addPrintAndLineFeed();
        esc.addSelectJustification(LEFT);

        esc.addText("--------------------------------\n");//打印完成时间
        esc.addText("备注：签字代表您已经完全了解并接受《用户委托服务及质保协议》，并授权我司进行本单所列范围内服务。\n");


        esc.addPrintAndLineFeed();
        esc.addPrintAndLineFeed();

        esc.addText("--------------------------------\n");//打印完成时间
        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand();

        for (int i = 0; i < Configure.Printing; i++) {
            // 发送数据
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].sendDataImmediately(datas);
        }
    }

    /**
     * 弹出系统弹框提示用户打开 Bluetooth
     */
    private void turnOnBluetooth() {
        // 请求打开 Bluetooth
        Intent requestBluetoothOn = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);

        // 设置 Bluetooth 设备可以被其它 Bluetooth 设备扫描到
        requestBluetoothOn
                .setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        // 设置 Bluetooth 设备可见时间
        requestBluetoothOn.putExtra(
                BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                BLUETOOTH_DISCOVERABLE_DURATION);

        // 请求开启 Bluetooth
        this.startActivityForResult(requestBluetoothOn,
                REQUEST_CODE_BLUETOOTH_ON);
    }


    /**
     * 连接蓝牙
     *
     * @param isAuto 是否自动连接
     */
    protected void getDeviceList(boolean isAuto) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            if (!isAuto)
                new BtConfirmDialog(new ArrayList<>(pairedDevices), ID, this).show();
            else
                BluetoothUtils.openPort(MyAppPreferences.getString(Configure.BluetoothAddress), ID);

        } else
            mHandler.obtainMessage(NO_DERVER).sendToTarget();//没有可配对的设备

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        DeviceConnFactoryManager.closeAllPort();
        CameraThreadPool.shutdownNow();
        unregisterReceiver(receiver);
    }


    boolean isConnectable;//是否可打印


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            switch (action) {

                //Usb连接断开、蓝牙连接断开广播
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:

                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (ID == deviceId) {
                                tv_bluetooth.setText(getString(R.string.str_conn_state_disconnect));
                            }
                            isConnectable = false;
//                            MyAppPreferences.remove(Configure.BluetoothAddress);//删除蓝牙地址

                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connecting));
                            tv_bluetooth.setText("打印机连接中...");
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connected));
                            tv_bluetooth.setText("打印机已连接");
                            isConnectable = true;
//                            ToastUtils.showToast("蓝牙已连接,请打印！");
                            break;
                        case CONN_STATE_FAILED:
//                            ToastUtils.showToast(getString(R.string.str_conn_fail));
                            tv_bluetooth.setText(getString(R.string.str_conn_state_disconnect));
                            isConnectable = false;
//                            MyAppPreferences.remove(Configure.BluetoothAddress);//删除蓝牙地址

                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
        if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
            switch (resultCode) {
                // 点击确认按钮
                case Activity.RESULT_OK: {
                    // TODO 用户选择开启 Bluetooth，Bluetooth 会被开启

                    getDeviceList(false);

                }
                break;

                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED: {
                    // TODO 用户拒绝打开 Bluetooth, Bluetooth 不会被开启
//                    finish();
                }
                break;
                default:
                    break;
            }
        }
    }


    //修改备注
    private void fixInfo(String postscript) {


        Api().remake(new OrderInfoEntity(info.getOrderInfo().getId(), postscript)).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo o) {
                ToastUtils.showToast("修改成功");

                et_info.setText(o.getOrderInfo().getPostscript());
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("修改失败:" + message);
            }
        });

    }
}
