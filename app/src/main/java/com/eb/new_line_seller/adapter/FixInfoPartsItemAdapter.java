package com.eb.new_line_seller.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.util.MathUtil;
import com.juner.mvp.bean.FixParts;

import java.util.List;


//检修单信息中的配件
public class FixInfoPartsItemAdapter extends BaseQuickAdapter<FixParts, BaseViewHolder> {

    int status;//检修单状态


    public void setStatus(int status) {
        this.status = status;
    }

    public FixInfoPartsItemAdapter(@Nullable List<FixParts> data) {
        super(R.layout.activity_fix_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FixParts item) {


        String tv1 = item.getGoods_name();
        String tv2 = "-";
        String tv3 = "x" + 1;
        String tv4 = "￥" + MathUtil.twoDecimal((item).getRetail_price());


        helper.setText(R.id.tv1, tv1);
        helper.setText(R.id.tv2, tv2);
        helper.setText(R.id.tv3, tv3);
        helper.setText(R.id.tv4, tv4);


        ImageView iv = helper.getView(R.id.iv);
        TextView tv = helper.getView(R.id.tv);
        switch (status) {
            case 0:
            case 1:
                if (item.selectde())
                    iv.setImageResource(R.drawable.icon_pick2);
                else
                    iv.setImageResource(R.drawable.icon_unpick2);
                helper.addOnClickListener(R.id.iv);

                tv.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.VISIBLE);
                break;
            case 2:
            case 3:
            case 4:
                switch (item.getSelected()) {
                    case 0:
                        iv.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        iv.setImageResource(R.drawable.icon_unpick2);
                        break;
                    case 1:
                        iv.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("未确认");
                        tv.setTextColor(Color.parseColor("#FF000000"));
                        break;
                    case 2:
                        iv.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("已确认");
                        tv.setTextColor(Color.parseColor("#FF666666"));
                        break;
                }
                break;

        }


    }
}
