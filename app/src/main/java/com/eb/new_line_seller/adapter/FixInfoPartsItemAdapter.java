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
public class FixInfoPartsItemAdapter extends BaseQuickAdapter<FixParts, BaseViewHolder> {

    Context context;

    public FixInfoPartsItemAdapter(@Nullable List<FixParts> data, Context c) {
        super(R.layout.activity_fix_info_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, FixParts item) {
        String tv1 = "";
        String tv2 = "";
        String tv3 = "";
        String tv4 = "";

//        if (item instanceof FixParts) {
        tv1 = item.getGoods_name();
        tv2 = "-";
        tv3 = "x" + item.getNumber();
        tv4 = "￥" + MathUtil.twoDecimal((item).getMarket_price());

//        }
//        if (item instanceof FixServie) {
//            tv1 = ((FixServie) item).getName();
//            tv2 = ((FixServie) item).getExplain();
//            tv3 = "x1";
//            tv4 = "￥" + MathUtil.twoDecimal(((FixServie) item).getMarketPrice());
//        }
        helper.setText(R.id.tv1, tv1);
        helper.setText(R.id.tv2, tv2);
        helper.setText(R.id.tv3, tv3);
        helper.setText(R.id.tv4, tv4);


        ImageView iv = helper.getView(R.id.iv);
        if ((item).selectde())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);
        helper.addOnClickListener(R.id.iv);

    }
}
