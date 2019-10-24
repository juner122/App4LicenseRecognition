package com.eb.geaiche.mvp;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;

import com.eb.geaiche.activity.CarCheckResultListActivity;
import com.eb.geaiche.activity.CarInfoInputActivity;
import com.eb.geaiche.activity.MainActivity;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.buletooth.PrinterCommand;
import com.eb.geaiche.mvp.contacts.FixInfoContacts;
import com.eb.geaiche.mvp.presenter.FixInfoPtr;
import com.eb.geaiche.util.BluetoothUtils;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.BtConfirmDialog;
import com.gprinter.command.EscCommand;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.GoodsEntity;

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
import static com.gprinter.command.EscCommand.JUSTIFICATION.CENTER;
import static com.gprinter.command.EscCommand.JUSTIFICATION.LEFT;
import static com.gprinter.command.EscCommand.JUSTIFICATION.RIGHT;


public class FixInfoActivity extends BaseActivity<FixInfoContacts.FixInfoPtr> implements FixInfoContacts.FixInfoUI {
    @BindView(R.id.tv_bluetooth)
    TextView tv_bluetooth;

    @BindView(R.id.ll_to_car_check)
    View ll_to_car_check;


    @BindView(R.id.tv_car_no)
    TextView tv_car_no;

    @BindView(R.id.tv_fix_sn)
    TextView tv_fix_sn;
    @BindView(R.id.tv_fix_time)
    TextView tv_fix_time;


    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    @BindView(R.id.tv_consignee)
    TextView tv_consignee;

    @BindView(R.id.tv_new_order)
    TextView tv_new_order;


    @BindView(R.id.ll_car_fix_list)
    View ll_car_fix_list;


    @BindView(R.id.tv_save)
    TextView tv_save;//保存退出


    @BindView(R.id.tv_post_fix)
    TextView tv_post_fix;//提交修改
    @BindView(R.id.tv_text)
    TextView tv_text;//总价


    @BindView(R.id.tv_dec)
    EditText tv_dec;//车况描述


    @BindView(R.id.tv_price1)
    TextView tv_price1;//工时小计


    @BindView(R.id.tv_price2)
    TextView tv_price2;//配件


    @BindView(R.id.iv_add1)
    ImageView iv_add1;//按钮1


    @BindView(R.id.iv_add2)
    ImageView iv_add2;//


    @BindView(R.id.rv)
    RecyclerView rv;//工时

    @BindView(R.id.rv2)
    RecyclerView rv2;//服务


    String car_no;
    int car_id;

    @OnClick({R.id.tv_bluetooth, R.id.iv_add1, R.id.iv_add2, R.id.tv_title_r2, R.id.tv_new_order, R.id.tv_car_info, R.id.tv_save, R.id.tv_fix_dec, R.id.tv_post_fix, R.id.tv_title_r, R.id.tv_notice, R.id.tv_back, R.id.ll_to_car_check})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add1:
                //添加工时
                getPresenter().remakeSave(1);
                break;

            case R.id.iv_add2:
                //添加配件
                getPresenter().remakeSave(2);
                break;

            case R.id.tv_new_order:
                //生成估价单
                getPresenter().onInform();
                break;

            case R.id.tv_save:
                //保存退出
                getPresenter().remakeSave(0);
                break;
            case R.id.tv_post_fix:
                //提交修改
                getPresenter().remakeSelected();
                break;
            case R.id.tv_car_info:
                //查看车况
                getPresenter().toCarInfoActivity();
                break;

            case R.id.tv_title_r:
                //授权凭证
                getPresenter().toAuthorizeActivity();
                break;

            case R.id.tv_notice:
                //通知客户
                getPresenter().notice();
                break;

            case R.id.tv_fix_dec:
                //修改备注
                tv_dec.setFocusableInTouchMode(true);
                tv_dec.setFocusable(true);
                tv_dec.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tv_dec, 0);

                break;

            case R.id.tv_back:
                if (getIntent().getBooleanExtra("push", false))
                    toActivity(MainActivity.class);
                else
                    finish();

                break;
            case R.id.ll_to_car_check:


                //车况检修记录列表
                Intent intent = new Intent(this, CarCheckResultListActivity.class);
                intent.putExtra(Configure.car_no, car_no);
                intent.putExtra(Configure.car_id, car_id);
                intent.putExtra(Configure.isShow, 1);

                startActivity(intent);

                break;

            case R.id.tv_bluetooth://连接蓝牙

                if (isConnectable) {
                    ToastUtils.showToast("打印机已连接,请打印！");
                } else
                    connectBluetooth(false);//连接蓝牙


                break;

            case R.id.tv_title_r2://蓝牙打印

                btnReceiptPrint();//蓝牙打印

                break;
        }
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

    int id;//订单id

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info;
    }

    @Override
    protected void init() {
        tv_title.setText("汽车检修单");
        setRTitle2("凭证打印");
        id = getIntent().getIntExtra("id", -1);
        getPresenter().initRecyclerView(rv, rv2);

        tv_bluetooth.setVisibility(View.VISIBLE);


        // //初始化蓝牙
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!TextUtils.isEmpty(MyAppPreferences.getString(Configure.BluetoothAddress))) {//有连接过的设备就自动连接
            //自动连接蓝牙
            connectBluetooth(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getInfo();
    }

    @Override
    public FixInfoContacts.FixInfoPtr onBindPresenter() {
        return new FixInfoPtr(this);
    }

    @Override
    public int getOrderId() {
        return id;
    }

    @Override
    public void setInfo(FixInfoEntity fixInfo) {
        car_id = fixInfo.getCarId();
        car_no = fixInfo.getCarNo();
        tv_car_no.setText(fixInfo.getCarNo());
        tv_fix_sn.setText("单号：" + fixInfo.getQuotationSn());


        tv_dec.setText(fixInfo.getDescribe());
        tv_mobile.setText(fixInfo.getMobile());
        tv_consignee.setText(null == fixInfo.getUserName() || fixInfo.getUserName().equals("") ? "匿名" : fixInfo.getUserName());


        int status = fixInfo.getStatus();
        switch (status) {
            case 0:
            case 1:

                tv_fix_time.setText("接单时间：" + fixInfo.getAddTime());
                break;
            case 2:
                tv_fix_time.setText("报价时间：" + fixInfo.getInformTime());
                break;
            case 3:
                tv_fix_time.setText("确认时间：" + fixInfo.getInformTime());
                break;
            case 4:
                tv_fix_time.setText("出单时间：" + fixInfo.getInformTime());
                break;


        }


    }

    /**
     * 处理订单状态改变的页面跳转
     */
    @Override
    public void createOrderSuccess(int i, int orderId) {
        getPresenter().changeView();

        if (i == 0) {
            ToastUtils.showToast("检修单已生成！");
            onResume();
        } else if (i == 1) {
            toOrderList(0);
            ToastUtils.showToast("订单已生成！");
        } else if (i == 2) {
            ToastUtils.showToast("检修单已确认！");
            onResume();
        }
    }

    @Override
    public void setServicePrice(String price) {
        tv_price1.setText("小计：￥" + MathUtil.twoDecimal(price));

    }

    @Override
    public void setPartsPrice(String price) {
        tv_price2.setText("小计：￥" + MathUtil.twoDecimal(price));


    }

    @Override
    public void setAllPrice(String price) {
        tv_text.setText("总价：￥" + MathUtil.twoDecimal(price));
    }

    @Override
    public void showAddButton() {
        iv_add1.setVisibility(View.VISIBLE);
        iv_add2.setVisibility(View.VISIBLE);
//        ll_car_fix_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        iv_add1.setVisibility(View.INVISIBLE);
        iv_add2.setVisibility(View.INVISIBLE);
//        ll_car_fix_list.setVisibility(View.GONE);
    }

    @Override
    public void setButtonText(String text) {
        tv_new_order.setText(text);
    }

    @Override
    public void onToCarInfoActivity(int car_id) {


        Intent intent = new Intent(this, CarInfoInputActivity.class);
        intent.putExtra(Configure.CARID, car_id);
        intent.putExtra("result_code", 999);
        startActivity(intent);
    }

    @Override
    public void showSaveButton() {
        tv_save.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSaveButton() {
        tv_save.setVisibility(View.GONE);
    }

    @Override
    public void showPostFixButton() {
        tv_post_fix.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRTitle() {
        setRTitle("授权凭证");
    }


    @Override
    public String getDec() {
        return tv_dec.getText().toString();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int from = intent.getIntExtra("from", -1);
        if (from == 1)//从添加工时，配件页面返回
            getPresenter().handleCallback(intent);
        if (from == 101)//从授权凭证页面返回
            getPresenter().setlpvUrl(intent.getStringExtra(Configure.Domain));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.cartUtils.deleteAllData();
        DeviceConnFactoryManager.closeAllPort();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
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
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    public int ID = 0;

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

    ;

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

        FixInfo info = getPresenter().putInfo();


        esc.addText(info.getQuotation().getCarNo() + "\n");//打印车牌
        esc.addPrintAndLineFeed();


        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 手机号码
        esc.addText("手机号码：" + info.getQuotation().getMobile() + "\n");

        // 车型
//        if (null == info.getUserCarCondition() || null == info.getUserCarCondition().getBrand() || "".equals(info.getUserCarCondition().getBrand()))
//            esc.addText("车型：" + "-" + "\n");//打印里程数
//        else
//            esc.addText("车型：" + info.getUserCarCondition().getBrand() + info.getUserCarCondition().getName() + "\n");//打印里程数

//
//        if (null == info.getQuotation() || null == info.getUserCarCondition().getMileage() || "".equals(info.getUserCarCondition().getMileage()))
//            esc.addText("里程数：" + "未填写" + "\n");//打印里程数
//        else
//            esc.addText("里程数：" + info.getUserCarCondition().getMileage() + "km" + "\n");//打印里程数
//

        if (info.getQuotation().getUserName().equals("匿名")) {
            // 会员姓名
            esc.addText("会员姓名：" + "匿名" + "\n");
        } else {
            // 会员姓名
            esc.addText("会员姓名：" + info.getQuotation().getUserName() + "\n");
        }

        // 打印文字
        esc.addText(info.getQuotation().getQuotationSn() + "\n");//打印订单号


//        // 打印文字
//        esc.addText(tv2.getText().toString() + "\n");//打印下单时间
//        if (order_status == 2)
//            esc.addText(tv3.getText().toString() + "\n");//打印完成时间

        esc.addText("================================\n");

        List<FixParts> fixParts = getPresenter().putPartsData();
        List<FixServie> fixServies = getPresenter().putServiceData();


        if (null != fixServies && fixServies.size() > 0) {
            esc.addText("服务工时\t" + tv_price1.getText().toString() + "\n");
            for (FixServie ge : fixServies) {

                if (ge.getType() == 3 && ge.getSelected() != 0) {
                    esc.addSelectJustification(LEFT);
                    esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
                    esc.addText(ge.getName());
                    esc.addPrintAndLineFeed();
                    esc.addSelectJustification(LEFT);

                    esc.addSetAbsolutePrintPosition((short) 7);
                    esc.addText("x" + ge.getNumber());

                    esc.addSetAbsolutePrintPosition((short) 12);

                    esc.addSelectJustification(RIGHT);
                    esc.addText(String.valueOf(Double.parseDouble(ge.getPrice()) * ge.getNumber()));
                    esc.addPrintAndLineFeed();
                    esc.addPrintAndLineFeed();
                }

            }
            esc.addText("--------------------------------\n");//打印完成时间
            esc.addSelectJustification(LEFT);
        }

        if (null != fixParts && fixParts.size() > 0) {

            esc.addText("商品项目\t" + tv_price2.getText().toString() + "\n");
            for (FixParts ge : fixParts) {

                if (ge.getType() == 4 && ge.getSelected() != 0) {
                    esc.addSelectJustification(LEFT);
                    esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
                    esc.addText(ge.getGoods_name());
                    esc.addPrintAndLineFeed();
                    esc.addSelectJustification(LEFT);

                    esc.addSetAbsolutePrintPosition((short) 7);
                    esc.addText("x" + ge.getNumber());

                    esc.addSetAbsolutePrintPosition((short) 12);

                    esc.addSelectJustification(RIGHT);
                    esc.addText(String.valueOf(Double.parseDouble(ge.getRetail_price()) * ge.getNumber()));
                    esc.addPrintAndLineFeed();
                    esc.addPrintAndLineFeed();
                }

            }
        }


//
        esc.addSelectJustification(RIGHT);
        esc.addText(tv_text.getText().toString());
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(LEFT);
        esc.addText("================================\n");//打印完成时间
        esc.addText("备注\n");
        esc.addText(tv_dec.getText().toString() + "\n");
        esc.addText("--------------------------------\n");

        esc.addPrintAndLineFeed();
//        if (null != iv_lpv.getDrawable()) {
//            esc.addSelectJustification(LEFT);
//            Bitmap b = ((BitmapDrawable) iv_lpv.getDrawable()).getBitmap();
//            esc.addText("签名\n");
//            esc.addSelectJustification(CENTER);
//            esc.addRastBitImage(b, 230, 0);
//            esc.addPrintAndLineFeed();
//            esc.addText("================================\n");//打印完成时间
//            esc.addSelectJustification(LEFT);
//            esc.addPrintAndLineFeed();
//        }


//        esc.addText(info.getShop().getShopName() + "\n");
//        esc.addText("门店店长：" + info.getShop().getName() + "\n");
//        esc.addText("联系信息：" + info.getShop().getPhone() + "\n");
//        esc.addText("地址：" + info.getShop().getAddress() + "\n");
//        esc.addPrintAndLineFeed();
//        esc.addSelectJustification(LEFT);

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
}
