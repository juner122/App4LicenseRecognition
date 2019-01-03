package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.ProductValue;

import java.util.List;

public class ProductListAdpter extends BaseQuickAdapter<ProductValue, BaseViewHolder> {


    public ProductListAdpter(@Nullable List<ProductValue> data) {
        super(R.layout.dialog_product_list_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ProductValue item) {


        helper.setText(R.id.tv1, item.getValue());
        View v = helper.getView(R.id.tv1);

        if (item.isSelected()) {
            v.setBackgroundResource(R.drawable.button_background_b);
        } else
            v.setBackgroundResource(R.drawable.button_background_z);


    }

}
