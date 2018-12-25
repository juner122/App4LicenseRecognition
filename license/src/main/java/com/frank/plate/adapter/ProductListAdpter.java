package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.CarNoLocation;
import com.frank.plate.bean.ProductValue;

import java.util.List;

public class ProductListAdpter extends BaseQuickAdapter<ProductValue, BaseViewHolder> {


    public ProductListAdpter(@Nullable List<ProductValue> data) {
        super(R.layout.dialog_product_list_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ProductValue item) {


        helper.setText(R.id.tv1, item.getValue());

    }

}
