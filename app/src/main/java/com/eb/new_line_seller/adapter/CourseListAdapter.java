package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.Course;

import java.util.List;

public class CourseListAdapter extends BaseQuickAdapter<Course, BaseViewHolder> {

    Fragment fragment;

    public CourseListAdapter(@Nullable List<Course> data, Fragment f) {
        super(R.layout.activity_meal_list_item, data);
        this.fragment = f;
    }

    @Override
    protected void convert(BaseViewHolder helper, Course item) {
        helper.setText(R.id.tv_title, item.getCourseName());
        helper.setText(R.id.tv_type, item.getCourseMarke());
        helper.setText(R.id.tv_price1, item.getCoursePrice().equals("0.00") ? "免费" : "￥" + item.getCoursePrice());
        helper.setText(R.id.tv_action, "查看");


        Glide.with(fragment)
                .load(item.getCourseImg())
                .into((ImageView) helper.getView(R.id.iv));
    }
}
