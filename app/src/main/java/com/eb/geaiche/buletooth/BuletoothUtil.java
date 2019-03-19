package com.eb.geaiche.buletooth;

public  class BuletoothUtil {

    /**
     * 自定义的打开 Bluetooth 的请求码，与 onActivityResult 中返回的 requestCode 匹配。
     */
    public static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    /**
     * Bluetooth 设备可见时间，单位：秒。
     */
    public static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;

    public static final int REQUEST_ENABLE_BT = 2;

    /**
     * 连接状态断开
     */
    public static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    public static final int PRINTER_COMMAND_ERROR = 0x008;
    public static final int CONN_PRINTER = 0x12;
    public static final int NO_DERVER = 0x13;

    public static final int CONN_STATE_DISCONNECT = 0x90;
    public static final int CONN_STATE_FAILED = CONN_STATE_DISCONNECT << 2;
}
