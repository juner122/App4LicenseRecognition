package com.eb.geaiche.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.ActivityEntityItem;
import com.juner.mvp.bean.AppMenu;

import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

public class AppMenuAdapter extends BaseQuickAdapter<AppMenu, BaseViewHolder> {

    Context context;

    MuneButAdapter muneButAdapter;

    public AppMenuAdapter(@Nullable List<AppMenu> data, Context c) {
        super(R.layout.activity_appmenu_itemml, data);
        this.context = c;
        muneButAdapter = new MuneButAdapter(null, c);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppMenu item) {
        helper.setText(R.id.rv_name, item.getName());
        RecyclerView rv = helper.getView(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(context, 4) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv.setAdapter(new MuneButAdapter(item.getList(), context));

    }
}
