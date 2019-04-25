package com.eb.geaiche.adapter;

import android.app.Activity;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.juner.mvp.bean.ResourcePojos;

import java.util.List;

//课程中第一节列表
public class ResourcePojosAdapter extends BaseQuickAdapter<ResourcePojos, BaseViewHolder> {

    Activity activity;


    public ResourcePojosAdapter(Activity activity, @Nullable List<ResourcePojos> data) {
        super(R.layout.activity_resourcepojos_list_item, data);
        this.activity = activity;
    }


    @Override
    protected void convert(BaseViewHolder helper, ResourcePojos item) {


        helper.setText(R.id.tv_title, item.getName())
                .setText(R.id.tv_isstudy, "未学习")
                .setText(R.id.tv_time, String.format("时长：%s分钟", item.getTimeLength()));


    }


}
