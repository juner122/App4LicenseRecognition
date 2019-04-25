package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Store;

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
