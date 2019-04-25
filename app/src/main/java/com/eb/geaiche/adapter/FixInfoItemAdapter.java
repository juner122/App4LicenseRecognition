package com.eb.geaiche.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.FixInfoItem;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;

import java.util.List;


//检修单信息中的工时和配件
public class FixInfoItemAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    Context context;

    public FixInfoItemAdapter(@Nullable List<Object> data, Context c) {
        super(R.layout.activity_fix_info_item, data);
        this.context = c;
    }



    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        String tv1 = "";
        String tv2 = "";
        String tv3 = "";
        String tv4 = "";

        if (item instanceof FixParts) {
            tv1 = ((FixParts) item).getGoods_name();
            tv2 = "-";
            tv3 = "x" + ((FixParts) item).getNumber();
            tv4 = "￥" + MathUtil.twoDecimal(((FixParts) item).getMarket_price());

        }
        if (item instanceof FixServie) {
            tv1 = ((FixServie) item).getName();
            tv2 = ((FixServie) item).getExplain();
            tv3 = "x1";
            tv4 = "￥" + MathUtil.twoDecimal(((FixServie) item).getMarketPrice());
        }
        helper.setText(R.id.tv1, tv1);
        helper.setText(R.id.tv2, tv2);
        helper.setText(R.id.tv3, tv3);
        helper.setText(R.id.tv4, tv4);


        ImageView iv = helper.getView(R.id.iv);
        if (((FixInfoItem)item).selectde())
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);
        helper.addOnClickListener(R.id.iv);

    }
}
