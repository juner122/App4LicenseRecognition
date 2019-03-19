package com.eb.geaiche.mvp.presenter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.ResultBack;
import com.eb.geaiche.activity.TechnicianListActivity;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.buletooth.PrinterCommand;
import com.eb.geaiche.mvp.FixInfoDescribeActivity;
import com.eb.geaiche.mvp.contacts.FixInfoDesContacts;
import com.eb.geaiche.mvp.model.FixInfoDesMdl;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;
import com.gprinter.command.EscCommand;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Technician;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static com.eb.geaiche.buletooth.BuletoothUtil.BLUETOOTH_DISCOVERABLE_DURATION;
import static com.eb.geaiche.buletooth.BuletoothUtil.CONN_PRINTER;
import static com.eb.geaiche.buletooth.BuletoothUtil.CONN_STATE_DISCONN;
import static com.eb.geaiche.buletooth.BuletoothUtil.CONN_STATE_FAILED;
import static com.eb.geaiche.buletooth.BuletoothUtil.NO_DERVER;
import static com.eb.geaiche.buletooth.BuletoothUtil.PRINTER_COMMAND_ERROR;
import static com.eb.geaiche.buletooth.BuletoothUtil.REQUEST_CODE_BLUETOOTH_ON;
import static com.gprinter.command.EscCommand.JUSTIFICATION.CENTER;
import static com.gprinter.command.EscCommand.JUSTIFICATION.LEFT;
import static com.juner.mvp.Configure.shop_address;
import static com.juner.mvp.Configure.shop_name;
import static com.juner.mvp.Configure.shop_phone;
import static com.juner.mvp.Configure.shop_user_name;

public class FixInfoDesPtr extends BasePresenter<FixInfoDesContacts.FixInfoDesUI> implements FixInfoDesContacts.FixInfoDesPtr {


    List<Technician> technicians;//选择的技师
    FixInfoDesContacts.FixInfoDesMdl mdl;
    String carNo, userName, mobile;
    int carId, userId;

    FixInfoEntity fixInfo;//请求对象

    private ProgressDialog dialog;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙


    String iv_lpv_url = "";//签名图片 七牛云url


    Map<Integer, Boolean> pickMap;


    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    public int ID = 0;


    public FixInfoDesPtr(@NonNull FixInfoDesContacts.FixInfoDesUI view) {
        super(view);
        mdl = new FixInfoDesMdl(view.getSelfActivity());
        pickMap = new HashMap<>();
    }

    /**
     * 添加快捷tip
     */
    @Override
    public void setTipClickListener(List<TextView> textViews) {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tip = ((TextView) view).getText().toString();
                if (!pickMap.get(view.getTag())) {//选中
                    view.setBackgroundResource(R.drawable.button_background_b);
                    getView().setTip(String.format("#%s#", tip));
                    pickMap.put((Integer) view.getTag(), true);
                } else {//取消选中
                    view.setBackgroundResource(R.drawable.button_background_z);
                    tip = String.format("#%s#", tip);
                    getView().cleanText(tip);
                    pickMap.put((Integer) view.getTag(), false);
                }
            }
        };

        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setOnClickListener(clickListener);
            textViews.get(i).setTag(i);
            pickMap.put(i, false);
        }

    }

    @Override
    public void getInfo() {

        dialog = new ProgressDialog(getView().getSelfActivity());
        dialog.setMessage("连接蓝牙中");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(true);


        carNo = getView().getSelfActivity().getIntent().getStringExtra(Configure.car_no);
        carId = getView().getSelfActivity().getIntent().getIntExtra(Configure.car_id, 0);
        userId = getView().getSelfActivity().getIntent().getIntExtra(Configure.user_id, 0);
        userName = getView().getSelfActivity().getIntent().getStringExtra(Configure.user_name);
        mobile = getView().getSelfActivity().getIntent().getStringExtra(Configure.moblie);
        getView().setCarNo(carNo);


    }


    @Override
    public void initBluetooth() {

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

    //票据打印
    public void btnReceiptPrint() {

        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].getConnState()) {
            ToastUtils.showToast(getView().getSelfActivity().getString(R.string.str_cann_printer));
            return;

        }
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].getCurrentPrinterCommand() == PrinterCommand.ESC) {
            sendReceiptWithResponse();
        } else {
            mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
        }

    }


    @Override
    public void showConfirmDialog(final boolean isFinish) {

        quotationSave(isFinish);//保存退出

    }

    @Override
    public void setEtText(EditText et) {

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
        esc.addText(carNo + "\n");//打印车牌

        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);


        // 打印文字
        esc.addText("手机号码：" + MathUtil.hidePhone(mobile) + "\n");//打印下单时间
        esc.addText("车主姓名：" + userName.substring(0, 1) + "**" + "\n");//打印下单时间
        esc.addText("接单时间：" + MathUtil.toNowDate() + "\n");//打印完成时间


        esc.addText("================================\n");//打印完成时间
        esc.addText("备注\n");
        esc.addText(getView().getDescribe() + "\n");
        esc.addText("--------------------------------\n");//打印完成时间

        esc.addPrintAndLineFeed();
        if (!"".equals(iv_lpv_url)) {
            esc.addSelectJustification(LEFT);
            esc.addText("签名\n");
            esc.addSelectJustification(CENTER);
            esc.addRastBitImage(getView().getDrawableBitmap(), 230, 0);
            esc.addPrintAndLineFeed();
            esc.addText("================================\n");//打印完成时间
            esc.addSelectJustification(LEFT);
            esc.addPrintAndLineFeed();
        }


        esc.addText(new AppPreferences(getView().getSelfActivity()).getString(shop_name, "新干线汽车服务连锁") + "\n");
        esc.addText("门店店长：" + new AppPreferences(getView().getSelfActivity()).getString(shop_user_name, "-") + "\n");
        esc.addText("联系信息：" + new AppPreferences(getView().getSelfActivity()).getString(shop_phone, "-") + "\n");
        esc.addText("地址：" + new AppPreferences(getView().getSelfActivity()).getString(shop_address, "-") + "\n");
        esc.addPrintAndLineFeed();
        esc.addSelectJustification(LEFT);
        esc.addText("================================\n");//打印完成时间
        esc.addText("备注：签字代表您已经完全了解并接受《用户委托服务及质保协议》，并授权我司进行本单所列范围内服务。\n");


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
        getView().getSelfActivity().startActivityForResult(requestBluetoothOn,
                REQUEST_CODE_BLUETOOTH_ON);
    }

    protected void getDeviceList() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                getView().setBluetoothText("打印机已连接(" + device.getName() + "\t" + device.getAddress() + ")");
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
    public void toTechnicianListActivity() {


        Intent intent = new Intent(getView().getSelfActivity(), TechnicianListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Technician", (ArrayList<? extends Parcelable>) technicians);
        intent.putExtras(bundle);
        ((FixInfoDescribeActivity) getView().getSelfActivity()).startActivityForResult(intent, new ResultBack() {
            @Override
            public void resultOk(Intent data) {
                technicians = data.getParcelableArrayListExtra("Technician");
                getView().setTechnician(String2Utils.getString(technicians));

            }
        });

    }

    public void quotationSave(final boolean isFinish) {

        if (null == technicians || technicians.size() == 0) {
            ToastUtils.showToast("请最少选择一个技师！");
            return;
        }

        if (null == getView().getDescribe() || "".equals(getView().getDescribe())) {
            ToastUtils.showToast("备注不能为空！");
            return;
        }


        fixInfo = new FixInfoEntity();
        fixInfo.setCarId(carId);
        fixInfo.setCarNo(carNo);
        fixInfo.setDescribe(getView().getDescribe());
        fixInfo.setMobile(mobile);
        fixInfo.setUserId(userId);
        fixInfo.setUserName(userName);
        fixInfo.setSysUserList(technicians);
        fixInfo.setSignPic(iv_lpv_url);

        mdl.quotationSave(fixInfo, new RxSubscribe<NullDataEntity>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(NullDataEntity entity) {

                getView().showToast("保存成功！");
                finish();
                if (!isFinish)
                    getView().toFixInfoActivity(entity.getId());
                else
                    getView().toMian();


            }

            @Override
            protected void _onError(String message) {
                getView().showToast("保存失败：" + message);
            }
        });

    }


    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        getView().getSelfActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        if (dialog != null)
            dialog.dismiss();
        getView().getSelfActivity().unregisterReceiver(receiver);
    }

    @Override
    public void setPicUrl(String url) {
        iv_lpv_url = url;
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
                                getView().setBluetoothText(getView().getSelfActivity().getString(R.string.str_conn_state_disconnect));

                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connecting));

                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
//                            tv_bluetooth.setText(getString(R.string.str_conn_state_connected));

                            break;
                        case CONN_STATE_FAILED:
                            ToastUtils.showToast(getView().getSelfActivity().getString(R.string.str_conn_fail));
                            getView().setBluetoothText(getView().getSelfActivity().getString(R.string.str_conn_state_disconnect));

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

                    ToastUtils.showToast(getView().getSelfActivity().getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:

                    ToastUtils.showToast(getView().getSelfActivity().getString(R.string.str_cann_printer));
                    break;

                case NO_DERVER:

                    ToastUtils.showToast(getView().getSelfActivity().getString(R.string.no_dervier));
                    break;
                default:
                    break;
            }
        }
    };
}
