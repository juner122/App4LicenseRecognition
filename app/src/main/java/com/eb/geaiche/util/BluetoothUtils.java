package com.eb.geaiche.util;

import android.os.Looper;

import com.eb.geaiche.buletooth.DeviceConnFactoryManager;

public class BluetoothUtils {

    /**
     * 打开蓝牙端口
     *
     * @param address 蓝牙地址
     */
    public static void openPort(String address, int ID) {

        CameraThreadPool.execute(() -> {//生成一个线程去打开蓝牙端口
            Looper.prepare();

            new DeviceConnFactoryManager.Build()
                    .setId(ID)
                    //设置连接方式
                    .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                    //设置连接的蓝牙mac地址
                    .setMacAddress(address)
                    .build();
            //打开端口
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].openPort();
            Looper.loop();// 进入loop中的循环，查看消息队列
        });

    }
}
