package com.eb.geaiche.maneuver.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.Ask;
import com.juner.mvp.bean.Maneuver;

import java.util.List;

public class ManeuverAskAdapter extends BaseQuickAdapter<Ask, BaseViewHolder> {

    Context context;

    public ManeuverAskAdapter(@Nullable List<Ask> data, Context c) {
        super(R.layout.activity_maneuver_ask_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Ask item) {
        helper.setText(R.id.name, item.getShopName());
        helper.setText(R.id.time, DateUtil.getFormatedDateTime(item.getCreateTime()));
        helper.setText(R.id.content, item.getAskContent());

        ImageUtils.loadCircle(context, item.getUnityImg(), helper.getView(R.id.iv));

    }
}
