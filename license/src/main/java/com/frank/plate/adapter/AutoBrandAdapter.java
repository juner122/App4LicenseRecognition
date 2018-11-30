package com.frank.plate.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.AutoBrand;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AutoBrandAdapter extends BaseQuickAdapter<AutoBrand, BaseViewHolder> implements Indexer {


    private HashMap<String, Integer> indexMap = new HashMap<>();

    List<AutoBrand> list;

    public AutoBrandAdapter(@Nullable List<AutoBrand> data) {
        super(R.layout.activity_auto_brand, data);
        if (null == data) return;

        list = data;


        // 列表特征和分组首项进行关联
        for (int i = 0; i < data.size(); i++) {
            AutoBrand ab = data.get(i);
            String type = ab.getType();

            if (!indexMap.containsKey(type)) {
                indexMap.put(type, i);
            }
        }


    }

    @Override
    protected void convert(BaseViewHolder helper, AutoBrand item) {

        helper.setText(R.id.name, item.getName());

        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.tvSection, true);
            helper.setText(R.id.tvSection, item.getType());
        } else {
            if (!item.getType().equals(list.get(helper.getLayoutPosition() - 1).getType())) {
                helper.setText(R.id.tvSection, item.getType());
                helper.setVisible(R.id.tvSection, true);
            } else
                helper.setVisible(R.id.tvSection, false);

        }
    }

    @Override
    public int getStartPositionOfSection(String section) {
        if (indexMap.containsKey(section))
            return indexMap.get(section);
        return -1;
    }
}
