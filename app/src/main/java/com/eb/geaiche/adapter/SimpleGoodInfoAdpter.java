package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.GoodsEntity;

import java.util.List;

public class SimpleGoodInfoAdpter extends BaseQuickAdapter<GoodsEntity, BaseViewHolder> {


    boolean isShowPlusAndReduce;

    public SimpleGoodInfoAdpter(@Nullable List<GoodsEntity> data, boolean is) {
        super(R.layout.activity_simple_good_list_item, data);
        isShowPlusAndReduce = is;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity item) {

        if (null == item.getGoodsStandard())
            helper.setText(R.id.name, item.getGoods_name());
        else {
            helper.setText(R.id.name, item.getGoods_name() + "(" + item.getGoodsStandard().getGoodsStandardTitle() + ")");
        }

        helper.setText(R.id.price, null != item.getRetail_price() ? "￥" + item.getRetail_price() : "免费")
                .setText(R.id.tv_number, String.valueOf("x" + item.getNumberString()));


        if (!isShowPlusAndReduce) {

            helper.setVisible(R.id.ib_plus, false);
            helper.setVisible(R.id.ib_reduce, false);
            return;


        }


        helper.addOnClickListener(R.id.ib_plus)
                .addOnClickListener(R.id.ib_reduce);


        View ib_reduce = helper.getView(R.id.ib_reduce);
        View ib_plus = helper.getView(R.id.ib_plus);
        View tv_number = helper.getView(R.id.tv_number);

        if (item.getNumber() == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
        }


    }


}
