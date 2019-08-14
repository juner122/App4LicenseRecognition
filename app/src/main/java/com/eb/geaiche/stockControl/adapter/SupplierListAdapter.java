package com.eb.geaiche.stockControl.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class SupplierListAdapter extends BaseQuickAdapter<Supplier, BaseViewHolder> {

    Context context;


    public SupplierListAdapter(@Nullable List<Supplier> data, Context c) {
        super(R.layout.activity_supplier_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, Supplier item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.address, item.getAddress());
        helper.setText(R.id.operation, item.getOperation());
        helper.addOnClickListener(R.id.iv2);

    }
}
