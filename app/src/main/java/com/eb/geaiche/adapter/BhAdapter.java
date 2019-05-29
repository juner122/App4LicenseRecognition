package com.eb.geaiche.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.AppMenu;

import java.util.List;

//蓝牙列表
public class BhAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    Context context;


    public BhAdapter(@Nullable List<BluetoothDevice> data, Context c) {
        super(R.layout.dialog_bh_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_address, item.getAddress());
    }
}
