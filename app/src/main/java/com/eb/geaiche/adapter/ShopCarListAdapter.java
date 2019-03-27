package com.eb.geaiche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.ShopCar;

import java.util.List;


//门店服务过的车辆管理
public class ShopCarListAdapter extends BaseQuickAdapter<ShopCar, BaseViewHolder> {


    public ShopCarListAdapter(@Nullable List<ShopCar> data) {
        super(R.layout.activity_shop_car_list_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ShopCar item) {
        helper.setText(R.id.tv_car_no, item.getCar_no());
        helper.setText(R.id.tv_name, item.getConsignee());
        helper.setText(R.id.tv_phone, item.getMobile());
        helper.setText(R.id.order_sum, String.valueOf("历史订单：" + item.getOrder_sum()));
        helper.setText(R.id.quotation_sum, String.valueOf("检修报价单：" + item.getOrder_sum()));

    }
}
