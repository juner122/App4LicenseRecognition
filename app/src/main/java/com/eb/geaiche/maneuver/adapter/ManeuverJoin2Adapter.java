package com.eb.geaiche.maneuver.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.Joiner;
import com.juner.mvp.bean.Maneuver;

import java.util.List;

public class ManeuverJoin2Adapter extends BaseQuickAdapter<Joiner, BaseViewHolder> {

    Context context;

    public ManeuverJoin2Adapter(@Nullable List<Joiner> data, Context c) {
        super(R.layout.activity_maneuver_join_item2, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Joiner item) {

        helper.setText(R.id.tv_name, item.getUnityName());
        helper.setText(R.id.tv_time, String.format("报名时间：%s", DateUtil.getFormatedDateTime(item.getCreateTime())));


        ImageUtils.load(context, item.getUnityImg(), helper.getView(R.id.iv));


        String status = item.getStatus();//1审核中2已通过3未通过
        TextView tv_type = helper.getView(R.id.tv_type);


        if (status.equals("1")) {
            tv_type.setText("审核中");
            tv_type.setBackgroundResource(R.drawable.button_background_un2);
        }
        if (status.equals("2")) {
            tv_type.setText("已通过");
            tv_type.setBackgroundResource(R.drawable.button_background_un1);
        }
        if (status.equals("3")) {
            tv_type.setText("未通过");
            tv_type.setBackgroundResource(R.drawable.button_background_un3);
        }


    }
}
