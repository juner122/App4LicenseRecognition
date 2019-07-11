package com.eb.geaiche.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.StaffPerformance;

import java.util.List;

//员工绩效分配
public class MeeitsAdapter extends BaseQuickAdapter<StaffPerformance, BaseViewHolder> {

    Context context;


    public MeeitsAdapter(@Nullable List<StaffPerformance> data, Context c) {
        super(R.layout.activity_meeits_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, StaffPerformance item) {
        helper.setText(R.id.tv_name, item.getUserName());
        helper.setText(R.id.tv_price, item.getDeductionBase());
        helper.setText(R.id.tv_meeits, item.getPercentage());
        helper.setText(R.id.tv_meeits_v, item.getDeduction());
    }
}
