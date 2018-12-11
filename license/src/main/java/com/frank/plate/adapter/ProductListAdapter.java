package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.activity.ProductListActivity;
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


        helper.setText(R.id.tv_product_name, item.getName())
                .setText(R.id.tv_product_ts, item.getGoods_brief())
                .setText(R.id.tv_number, String.valueOf(item.getNumber()))
                .setText(R.id.tv_price, String.format("￥%s", item.getRetail_price()))
                .addOnClickListener(R.id.ib_plus)
                .addOnClickListener(R.id.ib_reduce);

        Glide.with(fragment)
                .load(item.getPrimary_pic_url())
                .into((ImageView) helper.getView(R.id.iv_pic));

        View ib_reduce = helper.getView(R.id.ib_reduce);
        View tv_number = helper.getView(R.id.tv_number);
        View ib_plus = helper.getView(R.id.ib_plus);

        if (item.getNumber() == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
        }


        if (ProductListActivity.isShow == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
            ib_plus.setVisibility(View.INVISIBLE);
        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
            ib_plus.setVisibility(View.VISIBLE);
        }


    }
}
