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

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.buletooth.PrinterCommand;
import com.eb.geaiche.util.BluetoothUtils;
import com.eb.geaiche.util.ButtonUtils;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.view.BtConfirmDialog;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.eb.geaiche.zbar.CaptureActivity;
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
import java.util.Collections;
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
import static com.juner.mvp.Configure.Goods_TYPE;
import static com.juner.mvp.Configure.Goods_TYPE_1;
import static com.juner.mvp.Configure.Goods_TYPE_2;
import static com.juner.mvp.Configure.Goods_TYPE_3;
import static com.juner.mvp.Configure.Goods_TYPE_4;
import static com.juner.mvp.Configure.Goods_TYPE_5;

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


        if (MyAppPreferences.getShopType())
            setRTitle2("套卡扫码");
        info = getIntent().getParcelableExtra(Configure.ORDERINFO);


        // //初始化蓝牙
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        orderDetail(info.getOrderInfo().getId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConnectable && !TextUtils.isEmpty(MyAppPreferences.getString(Configure.BluetoothAddress))) {//有连接过的设备就自动连接
            //自动连接蓝牙
            connectBluetooth(true);
        }
    }

    //获取订单信息
    private void orderDetail(int id) {
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

        // 车型
//        if (null == info.getUserCarCondition() || null == info.getUserCarCondition().getBrand() || "".equals(info.getUserCarCondition().getBrand()))
//            esc.addText("车型：" + "-" + "\n");//打印里程数
//        else
//            esc.addText("车型：" + info.getUserCarCondition().getBrand() + info.getUserCarCondition().getName() + "\n");//打印里程数

        if (null == MyAppPreferences.getString(Configure.CAR_MILEAGE) || "".equals(MyAppPreferences.getString(Configure.CAR_MILEAGE)))
            esc.addText("里程数：" + "未填写" + "\n");//打印里程数
        else
            esc.addText("里程数：" + MyAppPreferences.getString(Configure.CAR_MILEAGE) + "km" + "\n");//打印里程数


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
        esc.addText(tv_make_date.getText().toString() + "\n");//打印下单时间

        esc.addText(tv_expect_date.getText().toString() + "\n");//打印完成时间

        esc.addText("================================\n");//打印完成时间


        esc.addSelectJustification(LEFT);


        if (null != info.getOrderInfo().getGoodsList() || info.getOrderInfo().getGoodsList().size() > 0) {
            esc.addText("服务工时\t小计:" + String2Utils.getOrderGoodsPrice(getGoodsList(info.getOrderInfo().getGoodsList(), true)) + "\n");
            for (GoodsEntity ge : getGoodsList(info.getOrderInfo().getGoodsListReverse(), true)) {
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
            for (GoodsEntity ge : getGoodsList(info.getOrderInfo().getGoodsListReverse(), false)) {

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

        if (null != info.getOrderInfo().getUserActivityList() || info.getOrderInfo().getUserActivityList().size() > 0) {
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
        for (int i = 0; i < Configure.Printing; i++) {
            // 发送数据
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].sendDataImmediately(datas);
        }
    }


    //获取商品或工时

    /**
     * @param isService 是否查工时
     */
    private List<GoodsEntity> getGoodsList(List<GoodsEntity> goodsEntities, boolean isService) {
        if (null == goodsEntities) {
            return null;
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

        if (intent.getBooleanExtra("CaptureActivity", false)) {
            orderDetail(info.getOrderInfo().getId());
        } else {

            Glide.with(this)
                    .asDrawable()
                    .load(Uri.fromFile(new File(Configure.LinePathView_url)))
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .apply(skipMemoryCacheOf(true))
                    .into(iv_lpv);

            iv_lpv_url = intent.getStringExtra(Configure.Domain);
            info.getOrderInfo().setDistrict(iv_lpv_url);//保存签名，防止用户直接支付
        }
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

        tv_order_sn.setText("订单编号：" + info.getOrderInfo().getOrder_sn());
        tv_car_no.setText("车牌号码：" + info.getOrderInfo().getCar_no());
        tv_make_date.setText("下单时间：" + info.getOrderInfo().getAdd_time());

        tv_expect_date.setText("预计完成：" + DateUtil.getFormatedDateTime(info.getOrderInfo().getPlanfinishi_time()));
        tv_remarks.setText(info.getOrderInfo().getPostscript());


        if (info.getOrderInfo().getPay_status() == 0)//是否隐藏支付button
            tv_now_pay.setVisibility(View.VISIBLE);
        else
            tv_now_pay.setVisibility(View.GONE);


        tv_shopName.setText(null == info.getShop().getShopName() ? "-" : info.getShop().getShopName());
        tv_name.setText("门店店长：" + null == info.getShop().getName() ? "-" : info.getShop().getName());
        tv_phone.setText("联系方式：" + null == info.getShop().getPhone() ? "-" : info.getShop().getPhone());
        tv_address.setText("地        址：" + null == info.getShop().getAddress() ? "-" : info.getShop().getAddress());


        simpleGoodInfo2Adpter = new SimpleGoodInfo2Adpter(getGood(info.getOrderInfo().getGoodsList()));
        simpleActivityInfo2Adpter = new SimpleActivityInfo2Adpter(info.getOrderInfo().getUserActivityList());
        serverInfo2Adpter = new SimpleServerInfo2Adpter(getService(info.getOrderInfo().getGoodsList()));


        double goodsPrice = String2Utils.getOrderGoodsPrice(info.getOrderInfo().getGoodsList());
        double ServerPrice = String2Utils.getOrderServicePrice(info.getOrderInfo().getSkillList());

        rv_goods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_goods.setAdapter(simpleGoodInfo2Adpter);

        rv_act.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_act.setAdapter(simpleActivityInfo2Adpter);

        rv_servers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_servers.setAdapter(serverInfo2Adpter);


        all_price.setText("总计：￥" + MathUtil.twoDecimal(goodsPrice + ServerPrice));

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

    @OnClick({R.id.tv_now_pay, R.id.tv_start_service, R.id.tv_back, R.id.ll_autograph, R.id.tv_title_r, R.id.tv_bluetooth, R.id.tv_title_r2})
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


                if (info.getOrderInfo().getPay_status() == 0) {
                    //弹出对话框
                    final ConfirmDialogCanlce c = new ConfirmDialogCanlce(this, "订单未付款，请确认是否同步erp！", "重要提示！");
                    c.show();
                    c.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            c.dismiss();
                            beginServe();
                        }

                        @Override
                        public void doCancel() {
                            c.dismiss();

                        }
                    });
                } else {
                    beginServe();
                }


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
                    ToastUtils.showToast("打印机已连接,请打印！");
                } else
                    connectBluetooth(false);//连接蓝牙


                break;
            case R.id.tv_title_r://蓝牙打印

                btnReceiptPrint();//蓝牙打印


                break;
            case R.id.tv_title_r2://套卡录入

                Intent intent = new Intent(this, CaptureActivity.class);
                intent.putExtra("view_type", 2);
                intent.putExtra("type", 0);
                intent.putExtra("id", String.valueOf(info.getOrderInfo().getId()));

                startActivity(intent);
                break;

        }

    }

    private void beginServe() {
        Api().beginServe(info.getOrderInfo().getId(), info.getOrderInfo().getOrder_sn(), iv_lpv_url).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                finish();
                toOrderList(0);
            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                ToastUtils.showToast(message);
            }
        });
    }


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
                            ToastUtils.showToast(getString(R.string.str_conn_fail));
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
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }




    //获取配件
    private List<GoodsEntity> getGood(List<GoodsEntity> goodsEntities) {

//        List<GoodsEntity> carts = new ArrayList<>();
//        List<GoodsEntity> list = goodsEntities;
//        for (GoodsEntity c : list) {
//            if (c.getType() == Goods_TYPE_4 || c.getType() == Goods_TYPE_1 || c.getType() == Goods_TYPE_5 || c.getType() == Goods_TYPE_2)
//                carts.add(c);
//        }
//        return carts;
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
            if (c.getType() != Goods_TYPE_4)
                carts.add(c);
        }
        return carts;

    }

}
