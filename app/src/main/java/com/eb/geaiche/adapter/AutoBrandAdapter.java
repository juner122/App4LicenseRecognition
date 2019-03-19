package com.eb.geaiche.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.bean.AutoBrand;

import java.util.HashMap;
import java.util.List;

public class AutoBrandAdapter extends BaseQuickAdapter<AutoBrand, BaseViewHolder> implements Indexer {


    private HashMap<String, Integer> indexMap = new HashMap<>();
    private Context mContext;

    List<AutoBrand> list;

    public AutoBrandAdapter(Context context, @Nullable List<AutoBrand> data) {
        super(R.layout.activity_auto_brand, data);
        if (null == data) return;
        this.mContext = context;
        list = data;

        // 列表特征和分组首项进行关联
        for (int i = 0; i < list.size(); i++) {
            AutoBrand city = list.get(i);
            String type = city.getType();
            if (type == null || "".equals(type)) continue;
            if (!indexMap.containsKey(type)) {
                indexMap.put(type, i);
            }
        }


    }

    @Override
    protected void convert(BaseViewHolder helper, AutoBrand item) {
        if (null == list) return;
        helper.setText(R.id.name, item.getName()).addOnClickListener(R.id.ll_item);
        Glide.with(mContext)
                .load(item.getImage())
                .into((ImageView) helper.getView(R.id.iv));


        int position = helper.getLayoutPosition();


        if (position == 0) {
            helper.setVisible(R.id.tvSection, true);
            helper.setText(R.id.tvSection, list.get(position).getType());
        } else {
            String preLabel = list.get(position - 1).getType();


            if (!list.get(position).getType().equals(preLabel)) {
                helper.setText(R.id.tvSection, list.get(position).getType());
                helper.setGone(R.id.tvSection, true);
            } else
                helper.setGone(R.id.tvSection, false);

        }
    }

    @Override
    public int getStartPositionOfSection(String section) {
        if (indexMap.containsKey(section))
            return indexMap.get(section);
        return -1;
    }
}
