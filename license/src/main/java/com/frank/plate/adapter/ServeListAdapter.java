package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;

import com.frank.plate.bean.GoodsEntity;

import java.util.List;

public class ServeListAdapter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    Fragment fragment;

    public ServeListAdapter(Fragment fragment, @Nullable List<GoodsEntity> data) {
        super(R.layout.activity_server_list_fr_item, data);
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.tv_product_name, item.getName())
                .setText(R.id.tv_product_ts, item.getGoods_brief())
                .setText(R.id.tv_price, String.format("￥%s", item.getRetail_price()));


        Glide.with(fragment)
                .load(item.getPrimary_pic_url())
                .into((ImageView) helper.getView(R.id.iv_pic));
    }
}
