package com.eb.geaiche.view;


import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.BhAdapter;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.util.CameraThreadPool;

import java.util.List;


public class BtConfirmDialog extends Dialog {

    private Context context;

    List<BluetoothDevice> data;
    public int ID = 0;
    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {

        public void doPickDevice(BluetoothDevice device);

    }


    public BtConfirmDialog(List<BluetoothDevice> data, int id, Context context) {
        super(context, R.style.my_dialog);
        this.context = context;
        this.data = data;
        this.ID = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_bh, null);
        setContentView(view);


        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        BhAdapter bhAdapter = new BhAdapter(data, context);
        rv.setAdapter(bhAdapter);

        bhAdapter.setOnItemClickListener((adapter, view1, position) -> {
            dismiss();

            CameraThreadPool.execute(() -> {//生成一个线程去打开蓝牙端口
                Looper.prepare();

                new DeviceConnFactoryManager.Build()
                        .setId(ID)
                        //设置连接方式
                        .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                        //设置连接的蓝牙mac地址
                        .setMacAddress(bhAdapter.getData().get(position).getAddress())
                        .build();
                //打开端口
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[ID].openPort();
                Looper.loop();// 进入loop中的循环，查看消息队列
            });

        });


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


}