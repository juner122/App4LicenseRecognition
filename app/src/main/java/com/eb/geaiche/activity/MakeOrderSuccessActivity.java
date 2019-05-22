package com.eb.geaiche.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.buletooth.PrinterCommand;
import com.eb.geaiche.util.ButtonUtils;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.gprinter.command.EscCommand;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SimpleActivityInfo2Adpter;
import com.eb.geaiche.adapter.SimpleGoodInfo2Adpter;
import com.eb.geaiche.adapter.SimpleServerInfo2Adpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Server;

import net.grandcentrix.tray.AppPreferences;

import java.io.File;
import java.util.ArrayList;
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
import static com.gprinter.command.EscCommand.JUSTIFICATION.CENTER;
import static com.gprinter.command.EscCommand.JUSTIFICATION.LEFT;
import static com.gprinter.command.EscCommand.JUSTIFICATION.RIGHT;
import static com.juner.mvp.Configure.Goods_TYPE_3;
import static com.juner.mvp.Configure.Goods_TYPE_4;

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

    @BindView(R.id.tv_bluetooth)
    TextView tv_bluetooth;

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
    String iv_lpv_url = "";//签名图片 七牛云url


    private BluetoothAdapter mBluetoothAdapter;//蓝牙


    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    public int ID = 0;

    @Override
    protected void init() {

        tv_title.setText("订单确认");
        setRTitle("凭证打印");
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


    private void initBluetooth() {


        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastUtils.showToast("设备不支持Bluetooth");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {

                turnOnBluetooth();

            } else {
                getDeviceList();
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
        if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
            switch (resultCode) {
                // 点击确认按钮
                case Activity.RESULT_OK: {
                    // TODO 用户选择开启 Bluetooth，Bluetooth 会被开启

                    getDeviceList();

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


    protected void getDeviceList() {


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                CameraThreadPool.execute(() -> {//生成一个线程去打开蓝牙端口
                    Looper.prepare();

                    new DeviceConnFactoryManager.Build()
                            .setId(ID)
                            //设置连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                            //设置连接的蓝牙mac地址
                            .setMacAddress(device.getAddress())
                            .build();
                    //打开端口
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].openPort();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                });

                break;
            }
        } else {
            mHandler.obtainMessage(NO_DERVER).sendToTarget();
        }
    }


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

        esc.addText("里程数：" + MyAppPreferences.getString(Configure.CAR_MILEAGE) + "km" + "\n");//打印里程数


        if (info.getOrderInfo().getConsignee().equals("匿名")) {
            // 会员姓名
            esc.addText("会员姓名：" + "匿名" + "\n");
        } else {
            // 会员姓名
            esc.addText("会员姓名：" + new AppPreferences(this).getString(Configure.user_name, "匿名").substring(0, 1) + "**" + "\n");

        }


        // 打印文字
        esc.addText(tv_order_sn.getText().toString() + "\n");//打印订单号


        // 打印文字
        esc.addText(tv_make_date.getText().toString() + "\n");//打印下单时间

        esc.addText(tv_expect_date.getText().toString() + "\n");//打印完成时间

        esc.addText("================================\n");//打印完成时间


        esc.addSelectJustification(LEFT);


        if (null != info.getOrderInfo().getGoodsList()) {
            esc.addText("服务工时\t小计:" + String2Utils.getOrderGoodsPrice(getGoodsList(info.getOrderInfo().getGoodsList(), Configure.Goods_TYPE_3)) + "\n");
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
        }

        if (null != info.getOrderInfo().getGoodsList()) {

            esc.addText("商品配件\t小计:" + String2Utils.getOrderGoodsPrice(getGoodsList(info.getOrderInfo().getGoodsList(), Configure.Goods_TYPE_4)) + "\n");
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
        }

        if (null != info.getOrderInfo().getUserActivityList()) {
            for (GoodsEntity gu : info.getOrderInfo().getUserActivityList()) {
                esc.addSelectJustification(LEFT);
                esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
                esc.addText(gu.getGoodsName());
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
        }


        esc.addSelectJustification(RIGHT);
        esc.addText(all_price.getText().toString());
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


    //获取商品或工时
    private List<GoodsEntity> getGoodsList(List<GoodsEntity> goodsEntities, int Type) {
        if (null == goodsEntities) {
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
        info.getOrderInfo().setDistrict(iv_lpv_url);//保存签名，防止用户直接支付
    }

    private void setInfo() {

        //加载图片
        Glide.with(this)
                .asDrawable()
                .load(info.getOrderInfo().getDistrict())
                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(skipMemoryCacheOf(true))
                .into(iv_lpv);


        iv_lpv_url = info.getOrderInfo().getDistrict();

        tv_order_sn.append(info.getOrderInfo().getOrder_sn());
        tv_car_no.append(info.getOrderInfo().getCar_no());
        tv_make_date.append(info.getOrderInfo().getAdd_time());

        tv_expect_date.append(DateUtil.getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));
        tv_remarks.setText(info.getOrderInfo().getPostscript());


        if (info.getOrderInfo().getPay_status() == 0)//是否隐藏支付button
            tv_now_pay.setVisibility(View.VISIBLE);
        else
            tv_now_pay.setVisibility(View.GONE);


        tv_shopName.append(null == info.getShop().getShopName() ? "-" : info.getShop().getShopName());
        tv_name.append(null == info.getShop().getName() ? "-" : info.getShop().getName());
        tv_phone.append(null == info.getShop().getPhone() ? "-" : info.getShop().getPhone());
        tv_address.append(null == info.getShop().getAddress() ? "-" : info.getShop().getAddress());


        simpleGoodInfo2Adpter = new SimpleGoodInfo2Adpter(getGood(info.getOrderInfo().getGoodsList()));
        simpleActivityInfo2Adpter = new SimpleActivityInfo2Adpter(info.getOrderInfo().getUserActivityList());
        serverInfo2Adpter = new SimpleServerInfo2Adpter(getService(info.getOrderInfo().getGoodsList()));


        double goodsPrice = String2Utils.getOrderGoodsPrice(info.getOrderInfo().getGoodsList());
        double ServerPrice = String2Utils.getOrderServicePrice(info.getOrderInfo().getSkillList());

        rv_goods.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_goods.setAdapter(simpleGoodInfo2Adpter);

        rv_act.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_act.setAdapter(simpleActivityInfo2Adpter);

        rv_servers.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
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

    @OnClick({R.id.tv_now_pay, R.id.tv_start_service, R.id.tv_back, R.id.ll_autograph, R.id.tv_title_r, R.id.tv_bluetooth})
    public void onClick(View view) {

        if (ButtonUtils.isFastDoubleClick(view.getId())) {//防止按钮多次重复点击
            return;
        }

        switch (view.getId()) {
            case R.id.tv_now_pay:


                info.getOrderInfo().setDistrict(iv_lpv_url);//保存签名，防止用户直接支付

                sendOrderInfo(OrderPayActivity.class, info);
                break;
            case R.id.tv_start_service:

                Api().beginServe(info.getOrderInfo().getId(), info.getOrderInfo().getOrder_sn(), iv_lpv_url).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        finish();
//                        toMain(0);
                        toOrderList(0);
                    }

                    @Override
                    protected void _onError(String message) {
                        Log.d(TAG, message);
                        ToastUtils.showToast(message);
                    }
                });
                break;
            case R.id.tv_back:
                finish();
                break;

            case R.id.ll_autograph://签名

                //弹出对话框
                final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(this, getResources().getString(R.string.agreement), getResources().getString(R.string.agreement_title));
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        confirmDialog.dismiss();
                        toActivity(AutographActivity.class, "class", "MakeOrderSuccess");

                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });

                break;


            case R.id.tv_bluetooth://连接蓝牙


                if (isConnectable) {
                    ToastUtils.showToast("蓝牙已连接,请打印！");

                } else
                    initBluetooth();//连接蓝牙


                break;
            case R.id.tv_title_r://蓝牙打印

                btnReceiptPrint();//蓝牙打印


                break;

        }

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity

        Log.i(TAG, "onBackPressed");

        toMain(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        DeviceConnFactoryManager.closeAllPort();

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
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connecting));
                            tv_bluetooth.setText("打印机连接中...");
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connected));
                            tv_bluetooth.setText("打印机已连接");
                            isConnectable = true;
                            ToastUtils.showToast("蓝牙已连接,请打印！");
                            break;
                        case CONN_STATE_FAILED:
                            ToastUtils.showToast(getString(R.string.str_conn_fail));
                            tv_bluetooth.setText(getString(R.string.str_conn_state_disconnect));
                            isConnectable = false;
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
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);

    }


    //获取配件
    private List<GoodsEntity> getGood(List<GoodsEntity> goodsEntities) {

        List<GoodsEntity> carts = new ArrayList<>();
        List<GoodsEntity> list = goodsEntities;
        for (GoodsEntity c : list) {
            if (c.getType() == Goods_TYPE_4)
                carts.add(c);
        }
        return carts;


    }

    //获取工时服务
    private List<GoodsEntity> getService(List<GoodsEntity> goodsEntities) {
        List<GoodsEntity> carts = new ArrayList<>();
        List<GoodsEntity> list = goodsEntities;
        for (GoodsEntity c : list) {
            if (c.getType() == Goods_TYPE_3)
                carts.add(c);
        }
        return carts;

    }

}
