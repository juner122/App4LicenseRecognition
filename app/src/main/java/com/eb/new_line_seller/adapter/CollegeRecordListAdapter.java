package com.eb.new_line_seller.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.util.MathUtil;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.Courses;

import java.util.List;

public class CollegeRecordListAdapter extends BaseQuickAdapter<CourseRecord, BaseViewHolder> {

    Activity activity;


    public CollegeRecordListAdapter(Activity activity, @Nullable List<CourseRecord> data) {
        super(R.layout.activity_college_record_list_item, data);
        this.activity = activity;
    }


    @Override
    protected void convert(BaseViewHolder helper, CourseRecord item) {


        helper.setText(R.id.tv_name, item.getCourseName())
                .setText(R.id.tv_course, String.format("上次学习：%s", MathUtil.toDate(item.getAddTime())))
//                .setText(R.id.tv_product, String.format("￥%s", item.getCoursePrice()))
                .setText(R.id.tv_product, "免费")
                .setText(R.id.tv_time, String.format("已学：%s", item.getHistoryTime()));

        Glide.with(activity)
                .load(item.getResourceImg())
                .into((ImageView) helper.getView(R.id.iv_pic));


    }


}
