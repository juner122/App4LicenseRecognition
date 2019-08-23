package com.eb.geaiche.vehicleQueue;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.OrderNews;

import java.util.List;

public class QueueListAdapter extends BaseQuickAdapter<VehicleQueue, BaseViewHolder> {

    Context context;

    public QueueListAdapter(@Nullable List<VehicleQueue> data, Context c) {
        super(R.layout.activity_vehicle_queue_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, VehicleQueue item) {

        helper.setText(R.id.tv_info, item.getPlateNumber());

        helper.setText(R.id.tv_time, MathUtil.toDateFormNow(item.getTime()));

        ImageView iv = helper.getView(R.id.iv);

        iv.setImageResource(R.mipmap.icon_queue);


    }
}
