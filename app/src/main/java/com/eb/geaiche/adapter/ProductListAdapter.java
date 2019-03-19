package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;

import com.juner.mvp.bean.GoodsEntity;


import java.util.List;

public class ProductListAdapter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {

    Fragment fragment;
    int isShow;

    public ProductListAdapter(Fragment fragment, @Nullable List<GoodsEntity> data, int isShow) {
        super(R.layout.activity_product_list_fr_item, data);
        this.fragment = fragment;
        this.isShow = isShow;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {


        helper.setText(R.id.tv_product_name, item.getName())
                .setText(R.id.tv_product_ts, item.getGoods_brief())
                .setText(R.id.tv_number, String.valueOf(item.getNumber()))
                .setText(R.id.tv_price, String.format("￥%s", item.getRetail_price()))
                .setText(R.id.tv_product_value, item.getGoods_specifition_name_value())
                .addOnClickListener(R.id.ib_plus)
                .addOnClickListener(R.id.ib_reduce)
                .addOnClickListener(R.id.tv_product_value);//选择规格

        Glide.with(fragment)
                .load(item.getPrimary_pic_url())
                .into((ImageView) helper.getView(R.id.iv_pic));

        View ib_reduce = helper.getView(R.id.ib_reduce);
        View tv_number = helper.getView(R.id.tv_number);
        View ib_plus = helper.getView(R.id.ib_plus);
        View tv_product_value = helper.getView(R.id.tv_product_value);


        if (isShow == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
            ib_plus.setVisibility(View.INVISIBLE);
            tv_product_value.setVisibility(View.INVISIBLE);

        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
            ib_plus.setVisibility(View.VISIBLE);
            tv_product_value.setVisibility(View.VISIBLE);
        }


        if (item.getNumber() == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
        }

    }
}
