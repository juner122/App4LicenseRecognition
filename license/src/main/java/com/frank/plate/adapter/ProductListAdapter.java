package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.ProductListItemEntity;

import java.util.List;

public class ProductListAdapter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    Fragment fragment;

    public ProductListAdapter(Fragment fragment, @Nullable List<GoodsEntity> data) {
        super(R.layout.activity_product_list_fr_item, data);

        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {
        helper.setText(R.id.tv_product_name, item.getName());
        helper.setText(R.id.tv_product_ts, item.getGoods_brief());


        Glide.with(fragment)
                .load(item.getPrimary_pic_url())
                .into((ImageView) helper.getView(R.id.iv_pic));


    }
}
