package com.eb.geaiche.adapter.decoration;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.CarCheckTypeUtil;
import com.juner.mvp.bean.CheckOptions;
import com.juner.mvp.bean.GoodsCategory;

import java.util.List;

public class PickCarCheckListAdapter extends BaseQuickAdapter<CheckOptions, BaseViewHolder> {

    Context context;


    public PickCarCheckListAdapter(@Nullable List<CheckOptions> data, Context c) {
        super(R.layout.activity_stock_add_pick_brand , data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckOptions item) {
        helper.setText(R.id.name, CarCheckTypeUtil.getType(item.getType()));
    }
}
