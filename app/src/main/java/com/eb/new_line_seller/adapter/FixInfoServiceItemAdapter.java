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
import com.juner.mvp.bean.FixServie;

import java.util.List;


//检修单信息中的工时和配件
public class FixInfoServiceItemAdapter extends BaseQuickAdapter<FixServie, BaseViewHolder> {


    int status;//检修单状态

    public void setStatus(int status) {
        this.status = status;
    }


    public FixInfoServiceItemAdapter(@Nullable List<FixServie> data) {
        super(R.layout.activity_fix_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FixServie item) {


        String tv1 = item.getName();
        String tv2 = item.getExplain();
        String tv3 = "x" + item.getNumber();
        String tv4 = "￥" + MathUtil.twoDecimal(item.getPrice());

        helper.setText(R.id.tv1, tv1);
        helper.setText(R.id.tv2, tv2);
        helper.setText(R.id.tv3, tv3);
        helper.setText(R.id.tv4, tv4);


        ImageView iv = helper.getView(R.id.iv);
        TextView tv = helper.getView(R.id.tv);
        switch (status) {
            case 0:
            case 1:

                switch (item.getSelected()) {
                    case 0:
                        iv.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        iv.setImageResource(R.drawable.icon_unpick2);
                        helper.addOnClickListener(R.id.iv);
                        helper.addOnClickListener(R.id.ll);
                        break;
                    case 1:
                        iv.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        iv.setImageResource(R.drawable.icon_pick2);
                        helper.addOnClickListener(R.id.iv);
                        helper.addOnClickListener(R.id.ll);
                        break;
                    case 2:
                        iv.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("已确认");
                        tv.setTextColor(Color.parseColor("#FF666666"));
                        break;
                }

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
                        tv.setText("待确认");
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
