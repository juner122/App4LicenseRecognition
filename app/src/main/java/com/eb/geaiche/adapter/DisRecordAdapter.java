package com.eb.geaiche.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.DateUtil;
import com.juner.mvp.bean.CouponRecode;
import com.juner.mvp.bean.DisRecord;

import java.util.List;

public class DisRecordAdapter extends BaseQuickAdapter<DisRecord, BaseViewHolder> {

    Context context;


    public DisRecordAdapter(@Nullable List<DisRecord> data, Context c) {
        super(R.layout.activity_dis_record_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, DisRecord item) {
        helper.setText(R.id.user, getTypeS(item.getType()));
        helper.setText(R.id.name, String.format("提现记录号:%s", item.getId()));
        helper.setText(R.id.time, DateUtil.getFormatedDateTime(item.getCreateTime()));
        helper.setText(R.id.num, String.format("提现金额:%s￥", item.getBalance()));

    }


    private String getTypeS(String type) {

        if (type.equals("1")) {
            return "申请中";
        }
        if (type.equals("2")) {

            return "通过";
        }
        return "拒绝";

    }
}
