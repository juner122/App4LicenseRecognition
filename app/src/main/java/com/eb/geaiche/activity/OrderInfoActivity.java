package com.eb.geaiche.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.buletooth.PrinterCommand;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.view.NoticeDialog;
import com.gprinter.command.EscCommand;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SimpleGoodInfoAdpter;
import com.eb.geaiche.adapter.SimpleMealInfoAdpter;
import com.eb.geaiche.adapter.SimpleServiceInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;

import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.OrderInfo;

import com.juner.mvp.bean.Server;
import com.juner.mvp.bean.Technician;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;


import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf;
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

    SimpleMealInfoAdpter sma;

    List<Technician> technicians;
    String iv_lpv_url = "";//签名图片 七牛云url

    @OnClick({R.id.tv_fix_order, R.id.tv_enter_order, R.id.but_meal_list, R.id.but_product_list, R.id.tv_pick_technician, R.id.ib_pick_date, R.id.tv_car_info, R.id.tv_notice, R.id.tv_back, R.id.tv_title_r, R.id.tv_bluetooth})
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


                Intent intent4 = new Intent(this, TechnicianListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Technician", (ArrayList<? extends Parcelable>) technicians);
                intent4.putExtras(bundle);
                startActivityForResult(intent4, new ResultBack() {
                    @Override
                    public void resultOk(Intent data) {
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
            case R.id.tv_back:
                if (getIntent().getBooleanExtra("push", false))
                    toActivity(MainActivity.class);
                else
                    finish();

                break;

            case R.id.tv_bluetooth://连接蓝牙

                initBluetooth();//连接蓝牙


                break;
            case R.id.tv_title_r://蓝牙打印

                btnReceiptPrint();//蓝牙打印

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
//
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

        getOrderInfoData();

        dialog = new ProgressDialog(this);
        dialog.setMessage("连接蓝牙中");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(true);


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
        setRTitle("凭证打印");

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

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        Glide.with(this)
//                .asDrawable()
//                .load(Uri.fromFile(new File(Configure.LinePathView_url)))
//                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .apply(skipMemoryCacheOf(true))
//                .into(iv_lpv);
//
//        iv_lpv_url = intent.getStringExtra(Configure.Domain);
//        info.getOrderInfo().setDistrict(iv_lpv_url);//保存签名，防止用户直接支付
//
//    }

    private void setInfo() {


        pay_status = info.getOrderInfo().getPay_status();
        order_status = info.getOrderInfo().getOrder_status();

        productList = getGoodsList(info.getOrderInfo().getGoodsList(), Configure.Goods_TYPE_4);//
        server = getGoodsList(info.getOrderInfo().getGoodsList(), Configure.Goods_TYPE_3);//服务

        meal = info.getOrderInfo().getUserActivityList();

        tv_order_state.setText(info.getOrderInfo().getOrder_status_text());

        tv_order_sn.setText(String.valueOf("订单号:" + info.getOrderInfo().getOrder_sn()));
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

        tv_jdy.setText(null == info.getReceptionist() || null == info.getReceptionist().getUsername() ? "-" : info.getReceptionist().getUsername());

        tv_price4_s.setText(info.getOrderInfo().getPay_status() == 2 ? "实收金额" : "应收金额");


        tv_pay_type.setText(String.valueOf("支付方式：" + info.getOrderInfo().getPay_name()));


        if (null != info.getOrderInfo().getPay_type() && info.getOrderInfo().getPay_type() == 21) {//套卡核销加卡号
            tv_pay_type.append(String.valueOf(":" + info.getOrderInfo().getProvince()));
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
                    tv_price4.setText(String.valueOf("￥" + MathUtil.twoDecimal(info.getOrderInfo().getOrder_price())));
                } else {
                    tv_fix_order.setVisibility(View.GONE);
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
                tv_fix_order.setVisibility(View.GONE);
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
    private List<GoodsEntity> getGoodsList(List<GoodsEntity> goodsEntities, int Type) {
        if (null == goodsEntities || goodsEntities.size() == 0) {
            return null;
        }
        List<GoodsEntity> list = new ArrayList<>();
        for (GoodsEntity entity : goodsEntities) {
            if (entity.getType() == Type) {
                list.add(entity);
            }
        }
        return list;


    }

    private ProgressDialog dialog;

    private BluetoothAdapter mBluetoothAdapter;//蓝牙

    private void initBluetooth() {

        dialog.show();
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastUtils.showToast("设备不支持Bluetooth");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                dialog.dismiss();
                turnOnBluetooth();

            } else {
                getDeviceList();
            }
        }
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
        esc.addText("手机号码：" + MathUtil.hidePhone(info.getOrderInfo().getMobile()) + "\n");
        // 会员姓名
        esc.addText("会员姓名：" + new AppPreferences(this).getString(Configure.user_name, "null_user_name").substring(0, 1) + "**" + "\n");


        // 打印文字
        esc.addText(tv_order_sn.getText().toString() + "\n");//打印订单号


        // 打印文字
        esc.addText(tv2.getText().toString() + "\n");//打印下单时间
        if (order_status == 2)
            esc.addText(tv3.getText().toString() + "\n");//打印完成时间

        esc.addText("================================\n");//打印完成时间


        esc.addSelectJustification(LEFT);
        esc.addText("服务工时\t小计:" + String2Utils.getOrderServicePrice(info.getOrderInfo().getSkillList()) + "\n");

        for (GoodsEntity ge : getGoodsList(info.getOrderInfo().getGoodsList(), Configure.Goods_TYPE_3)) {


            esc.addSelectJustification(LEFT);
            esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
            esc.addText(ge.getGoods_name());
            esc.addPrintAndLineFeed();
            esc.addSelectJustification(LEFT);


            esc.addSetAbsolutePrintPosition((short) 7);
            esc.addText("x1");

            esc.addSetAbsolutePrintPosition((short) 12);

            esc.addSelectJustification(RIGHT);
            esc.addText("" + ge.getRetail_price());
            esc.addPrintAndLineFeed();
            esc.addPrintAndLineFeed();
        }
        esc.addText("--------------------------------\n");//打印完成时间


        esc.addSelectJustification(LEFT);
        esc.addText("商品配件\t小计:" + String2Utils.getOrderGoodsPrice(info.getOrderInfo().getGoodsList()) + "\n");

        for (GoodsEntity ge : getGoodsList(info.getOrderInfo().getGoodsList(), Configure.Goods_TYPE_4)) {

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
        for (GoodsEntity gu : info.getOrderInfo().getUserActivityList()) {
            esc.addSelectJustification(LEFT);
            esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
            esc.addText(gu.getGoods_name());
            esc.addPrintAndLineFeed();
            esc.addSelectJustification(LEFT);

            esc.addSetAbsolutePrintPosition((short) 7);
            esc.addText("x1");

            esc.addSetAbsolutePrintPosition((short) 12);

            esc.addSelectJustification(RIGHT);
            esc.addText("抵扣");
            esc.addPrintAndLineFeed();
            esc.addPrintAndLineFeed();
        }

        esc.addSelectJustification(RIGHT);
        esc.addText(tv_price4.getText().toString());
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
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].sendDataImmediately(datas);
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].sendDataImmediately(datas);
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

    protected void getDeviceList() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                tv_bluetooth.setText("打印机已连接(" + device.getName() + "\t" + device.getAddress() + ")");
                //初始化话DeviceConnFactoryManager
                new DeviceConnFactoryManager.Build()
                        .setId(ID)
                        //设置连接方式
                        .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                        //设置连接的蓝牙mac地址
                        .setMacAddress(device.getAddress())
                        .build();
                //打开端口
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].openPort();


                break;
            }
        } else {
            mHandler.obtainMessage(NO_DERVER).sendToTarget();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null)
            dialog.dismiss();
        unregisterReceiver(receiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        DeviceConnFactoryManager.closeAllPort();

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (dialog != null)
                dialog.dismiss();

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
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connecting));

                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connected));

                            break;
                        case CONN_STATE_FAILED:
                            ToastUtils.showToast(getString(R.string.str_conn_fail));
                            tv_bluetooth.setText(getString(R.string.str_conn_state_disconnect));

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
}
