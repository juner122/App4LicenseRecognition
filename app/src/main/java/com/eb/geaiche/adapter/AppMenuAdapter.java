package com.eb.geaiche.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.AppMenu;

import java.util.List;

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
