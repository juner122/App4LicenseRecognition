package com.eb.geaiche.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.FixInfoEntity;

import java.util.List;

public class FixInfoListAdapter extends BaseQuickAdapter<FixInfoEntity, BaseViewHolder> {

    Context context;


    public FixInfoListAdapter(int layoutResId, @Nullable List<FixInfoEntity> data, Context context) {
        super(layoutResId, data);
        this.context = context;

    }


    @Override
    protected void convert(BaseViewHolder helper, FixInfoEntity item) {


        helper.setText(R.id.tv_plate_number, item.getCarNo());
        helper.setText(R.id.tv_order_number, String.format("检修单号:%s", item.getQuotationSn()));
        helper.setText(R.id.tv_date, item.getAddTime());
        helper.setText(R.id.tv_order_state, item.getStatusText());


        helper.setText(R.id.tv_money, String.format("￥%s", MathUtil.twoDecimal(Double.parseDouble(item.getActualPrice()))));


        ImageView imageView = (helper.getView(R.id.iv_icon));

        imageView.setImageResource(R.mipmap.icon_enter);


    }


}
