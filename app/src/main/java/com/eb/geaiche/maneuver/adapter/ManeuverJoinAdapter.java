package com.eb.geaiche.maneuver.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.Maneuver;

import java.util.List;

public class ManeuverJoinAdapter extends BaseQuickAdapter<Maneuver.Join, BaseViewHolder> {

    Context context;

    public ManeuverJoinAdapter(@Nullable List<Maneuver.Join> data, Context c) {
        super(R.layout.activity_maneuver_join_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Maneuver.Join item) {


        ImageUtils.loadCircle(context, item.getUnityImg(), helper.getView(R.id.iv));

    }
}
