package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.Goods;

import java.util.List;

public class ProductListAdpter extends BaseQuickAdapter<Goods.GoodsStandard, BaseViewHolder> {


    public ProductListAdpter(@Nullable List<Goods.GoodsStandard> data) {
        super(R.layout.dialog_product_list_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Goods.GoodsStandard item) {


        helper.setText(R.id.tv1, item.getGoodsStandardTitle());
        TextView v = helper.getView(R.id.tv1);

        if (item.isSelected()) {
            v.setBackgroundResource(R.drawable.button_background_b);
            v.setTextColor(Color.parseColor("#ffffff"));
        } else {
            v.setBackgroundResource(R.drawable.button_background_z);
            v.setTextColor(Color.parseColor("#333333"));
        }
    }

}
