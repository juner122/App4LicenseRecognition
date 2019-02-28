package com.eb.new_line_seller.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.CollegeListAdapter;
import com.eb.new_line_seller.adapter.CollegeRecordListAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.mvp.CourseInfoActivity;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.Courses;

import java.util.List;

import butterknife.BindView;

public class CourseRecordActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    CollegeRecordListAdapter adapter;

    @Override
    protected void init() {
        tv_title.setText("学习记录");
        adapter = new CollegeRecordListAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(CourseRecordActivity.this, CourseInfoActivity.class);
                intent.putExtra("id", ((CourseRecord) adapter.getData().get(position)).getCourseId());
                intent.putExtra("courseName", ((CourseRecord) adapter.getData().get(position)).getCourseName());
                intent.putExtra("resourceId", ((CourseRecord) adapter.getData().get(position)).getResourceId());
                intent.putExtra("pastTime", ((CourseRecord) adapter.getData().get(position)).getPastTime());
                startActivity(intent);

            }
        });
    }

    @Override
    protected void setUpView() {


        Api().courseRecord().subscribe(new RxSubscribe<List<CourseRecord>>(this, true) {
            @Override
            protected void _onNext(List<CourseRecord> courses) {
                adapter.setNewData(courses);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_course_record;
    }
}
