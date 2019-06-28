package com.eb.geaiche.maneuver.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.MuneButAdapter;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.ImageUtils;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.Maneuver;

import java.util.List;

public class ManeuverAdapter extends BaseQuickAdapter<Maneuver, BaseViewHolder> {

    Context context;

    public ManeuverAdapter(@Nullable List<Maneuver> data, Context c) {
        super(R.layout.activity_maneuver_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Maneuver item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.tv_time, DateUtil.getFormatedDateTime(item.getEndApplyTime()));
        helper.setText(R.id.tv_owner, item.getJoinNum() + "家");
        helper.setText(R.id.tv_leave, item.getMommentNum() + "条");

        ImageUtils.load(context, item.getImg(), helper.getView(R.id.iv));

    }
}
