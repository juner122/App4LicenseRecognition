package com.eb.geaiche.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.OrderNews;

import java.util.List;

public class OrderNewsListAdapter extends BaseQuickAdapter<OrderNews, BaseViewHolder> {

    Context context;

    public OrderNewsListAdapter(@Nullable List<OrderNews> data, Context c) {
        super(R.layout.activity_order_news_list_item, data);
        this.context = c;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderNews item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_info, item.getContent());

        helper.setText(R.id.tv_time, MathUtil.toDate(item.getAddTime()));

        ImageView iv = helper.getView(R.id.iv);

        if (item.getType() == 2) {
            iv.setImageResource(R.mipmap.icon_fix);
        } else
            iv.setImageResource(R.mipmap.icon_order);
        TextView textView = helper.getView(R.id.tv_read);

        if (item.getRead() == 1) {
            textView.setText("已读");
            textView.setTextColor(Color.parseColor("#ff999999"));
        } else {
            textView.setText("未读");
            textView.setTextColor(Color.RED);
        }

    }
}
