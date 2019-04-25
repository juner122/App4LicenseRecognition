package com.eb.geaiche.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CollegeRecordListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.CourseInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.CourseRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseRecordActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    CollegeRecordListAdapter adapter;

    @OnClick(R.id.tv_to_s)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_s:

                toActivity(CollegeActivity.class);
                finish();
                break;
        }
    }

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
                if (courses.size() == 0) {
                    recyclerView.setVisibility(View.GONE);

                } else {

                    adapter.setNewData(courses);
                    recyclerView.setVisibility(View.VISIBLE);
                }

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
