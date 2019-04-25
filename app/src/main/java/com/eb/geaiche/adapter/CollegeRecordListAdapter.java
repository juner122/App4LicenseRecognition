package com.eb.geaiche.adapter;

import android.app.Activity;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.MathUtil;
import com.juner.mvp.bean.CourseRecord;

import java.math.BigDecimal;
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
                .setText(R.id.tv_time, String.format("已学习：%s", item.getHistoryTime()));


        Glide.with(activity)
                .load(item.getResourceImg())
                .into((ImageView) helper.getView(R.id.iv_pic));


        ProgressBar progressBar = helper.getView(R.id.progressBar);
        progressBar.setMax(100);


        String historyTime = item.getHistoryTime();
        historyTime = historyTime.substring(0, historyTime.length() - 1);

        try {
            BigDecimal pro = new BigDecimal(historyTime);
            progressBar.setProgress(pro.intValue());
        } catch (Exception e) {
            progressBar.setProgress(0);
        }


    }


}
