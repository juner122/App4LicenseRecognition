package com.eb.geaiche.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;

import com.eb.geaiche.util.ImageUtils;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.Goods;


import java.util.List;

public class ProductListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {

    Fragment fragment;
    int isShow;

    public ProductListAdapter(Fragment fragment, @Nullable List<Goods> data, int isShow) {
        super(R.layout.activity_product_list_fr_item, data);
        this.fragment = fragment;
        this.isShow = isShow;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        Goods.GoodsStandard goodsStandard;
        if (null == item.getGoodsStandard())
            goodsStandard = item.getXgxGoodsStandardPojoList().get(0);//第一个规格
        else {
            goodsStandard = item.getGoodsStandard();
        }


        helper.setText(R.id.tv_product_name, item.getGoodsTitle())
                .setText(R.id.tv_product_ts, "")
                .setText(R.id.tv_number, item.getNum() + "")
                .setText(R.id.tv_price, String.format("￥%s", MathUtil.twoDecimal(goodsStandard.getGoodsStandardPrice())))
                .setText(R.id.tv_product_value, goodsStandard.getGoodsStandardTitle())
                .addOnClickListener(R.id.ib_plus)
                .addOnClickListener(R.id.ib_reduce)
                .addOnClickListener(R.id.tv_product_value);//选择规格

        String url = "";
        if (null != item.getGoodsDetailsPojoList() && item.getGoodsDetailsPojoList().size() > 0)
            url = item.getGoodsDetailsPojoList().get(0).getImage();


        ImageUtils.load(fragment.getContext(), url, helper.getView(R.id.iv_pic));


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


        if (item.getNum() == 0) {
            ib_reduce.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
        } else {
            ib_reduce.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
        }

    }
}
