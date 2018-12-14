package com.frank.plate.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.frank.plate.R;
import com.frank.plate.bean.AutoModel;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.SetProject;

import java.util.List;

public class SetProjectAdapter extends BaseQuickAdapter<SetProject, BaseViewHolder> {


    public SetProjectAdapter(@Nullable List<SetProject> data) {
        super(R.layout.activity_setproject_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SetProject item) {

        helper.setText(R.id.tv1, item.getName());
    }


}
