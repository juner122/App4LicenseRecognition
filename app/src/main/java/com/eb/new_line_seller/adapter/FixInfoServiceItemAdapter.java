package com.eb.new_line_seller.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.util.MathUtil;
import com.juner.mvp.bean.FixInfoItem;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;

import java.util.List;


//检修单信息中的工时和配件
public class FixInfoServiceItemAdapter extends BaseQuickAdapter<FixServie, BaseViewHolder> {

    Context context;

    public FixInfoServiceItemAdapter(@Nullable List<FixServie> data, Context c) {
        super(R.layout.activity_fix_info_item, data);
        this.context = c;
    }

    public FixInfoServiceItemAdapter(@Nullable List<FixServie> data) {
        super(R.layout.activity_fix_info_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, FixServie item) {
        String tv1 = "";
        String tv2 = "";
        String tv3 = "";
        String tv4 = "";

        tv1 = item.getName();
        tv2 = item.getExplain();
        tv3 = "x1";
        tv4 = "￥" + MathUtil.twoDecimal(item.getPrice());

        helper.setText(R.id.tv1, tv1);
        helper.setText(R.id.tv2, tv2);
        helper.setText(R.id.tv3, tv3);
        helper.setText(R.id.tv4, tv4);


        ImageView iv = helper.getView(R.id.iv);
        if (item.selectde())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);
        helper.addOnClickListener(R.id.iv);

    }
}
