package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.juner.mvp.bean.Store;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class StoreListAdpter extends BaseQuickAdapter<Store, BaseViewHolder> {


    Store pickStore;

    public StoreListAdpter(@Nullable List<Store> data) {
        super(R.layout.activity_technician_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Store item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.phone, item.getPhone());

        ImageView iv = helper.getView(R.id.iv);
        if (item.isSelected()) {
            pickStore = item;
            iv.setImageResource(R.drawable.icon_pick2);
        } else {
            iv.setImageResource(R.drawable.icon_unpick2);
        }

    }


    public Store getPickStore() {
        return pickStore;
    }

    public void setPickStore(Store pickStore) {
        this.pickStore = pickStore;
    }

}