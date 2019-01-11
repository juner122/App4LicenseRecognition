package com.eb.new_line_seller.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.new_line_seller.buletooth.DeviceConnFactoryManager;
import com.eb.new_line_seller.buletooth.PrinterCommand;
import com.eb.new_line_seller.buletooth.ThreadPool;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.SimpleActivityInfo2Adpter;
import com.eb.new_line_seller.adapter.SimpleGoodInfo2Adpter;
import com.eb.new_line_seller.adapter.SimpleServerInfo2Adpter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.eb.new_line_seller.util.DateUtil;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.String2Utils;
import com.eb.new_line_seller.util.ToastUtils;

import java.io.File;
import java.util.Set;
import java.util.Vector;

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

    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private int id = 0;

    private BluetoothAdapter mBluetoothAdapter;//蓝牙
    public static final int REQUEST_ENABLE_BT = 2;

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

        initBluetooth();

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
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,
                        REQUEST_ENABLE_BT);
            } else {
                getDeviceList();
            }
        }
    }

    protected void getDeviceList() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                tv_bluetooth.append("(" + device.getName() + "\t" + device.getAddress() + ")");
                //初始化话DeviceConnFactoryManager
                new DeviceConnFactoryManager.Build()
                        .setId(id)
                        //设置连接方式
                        .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                        //设置连接的蓝牙mac地址
                        .setMacAddress(device.getAddress())
                        .build();
                //打开端口
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                break;
            }

        } else {
            ToastUtils.showToast("没有配对的蓝牙设备！");
        }
    }


    private ThreadPool threadPool;

    //票据打印
    public void btnReceiptPrint() {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
            ToastUtils.showToast(getString(R.string.str_cann_printer));
            return;
        }
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC) {
                    sendReceiptWithResponse();
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }


    /**
     * 发送票据
     */
    void sendReceiptWithResponse() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
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

        // 打印文字
        esc.addText(tv_order_sn.getText().toString() + "\n");//打印订单号
        // 打印文字
        esc.addText(tv_make_date.getText().toString() + "\n");//打印下单时间

        esc.addText(tv_expect_date.getText().toString() + "\n");//打印完成时间


        esc.addText("--------------------------------\n");//打印完成时间
        esc.addText("--------------------------------\n");//打印完成时间



        /* 绝对位置 具体详细信息请查看GP58编程手册 */

        esc.addText("项目名称");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);

        esc.addSetAbsolutePrintPosition((short) 7);
        esc.addText("数量");

        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("单价");

        esc.addSetAbsolutePrintPosition((short) 13);
        esc.addText("金额");
        esc.addText("--------------------------------\n");//打印完成时间









        // esc.addSetAbsolutePrintPosition((short) 13);
        esc.addText("签名\n");
        Bitmap a = BitmapFactory.decodeResource(getResources(),
                R.mipmap.qm);

        esc.addRastBitImage(a, 300, 0);





//        String fileName = "/sdcard/qm.png";
//        File file = new File(fileName);
//
//        Bitmap b = BitmapFactory.decodeFile(fileName);

        // 打印图片
//        esc.addOriginRastBitImage(a, 384, 0);
//        esc.addRastBitImage(b, 200, 0);
//        esc.addRastBitImage(b, 300, 0);
//        esc.addRastBitImage(b, 384, 0);
//

        /*
         * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
         */



        // 设置纠错等级
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31);
        // 设置qrcode模块大小
        esc.addSelectSizeOfModuleForQRCode((byte) 5);
        // 设置qrcode内容
        esc.addStoreQRCodeData("www.smarnet.cc");
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        //打印文字
        esc.addText("Completed!\r\n");

        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand();
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }


    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    private static final int PRINTER_COMMAND_ERROR = 0x008;
    private static final int CONN_PRINTER = 0x12;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                    }
                    break;
                case PRINTER_COMMAND_ERROR:

                    ToastUtils.showToast(getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:

                    ToastUtils.showToast(getString(R.string.str_cann_printer));
                    break;
//                case MESSAGE_UPDATE_PARAMETER:
//                    String strIp = msg.getData().getString("Ip");
//                    String strPort = msg.getData().getString("Port");
//                    //初始化端口信息
//                    new DeviceConnFactoryManager.Build()
//                            //设置端口连接方式
//                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
//                            //设置端口IP地址
//                            .setIp(strIp)
//                            //设置端口ID（主要用于连接多设备）
//                            .setId(id)
//                            //设置连接的热点端口号
//                            .setPort(Integer.parseInt(strPort))
//                            .build();
//                    threadPool = ThreadPool.getInstantiation();
//                    threadPool.addTask(new Runnable() {
//                        @Override
//                        public void run() {
//                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
//                        }
//                    });
//                    break;
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

    @OnClick({R.id.tv_now_pay, R.id.tv_start_service, R.id.tv_back, R.id.ll_autograph, R.id.tv_title_r})
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
                        ToastUtils.showToast(message);
                    }
                });
                break;
            case R.id.tv_back:
                toMain(1);
                break;

            case R.id.ll_autograph://签名
                toActivity(AutographActivity.class);
                break;


            case R.id.tv_title_r://蓝牙打印
                btnReceiptPrint();
                break;

        }

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity

        Log.i(TAG, "onBackPressed");

        toMain(1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
        DeviceConnFactoryManager.closeAllPort();
        if (threadPool != null) {
            threadPool.stopThreadPool();
        }
    }

}
