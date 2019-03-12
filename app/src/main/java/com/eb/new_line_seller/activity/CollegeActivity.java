package com.eb.new_line_seller.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.CollegeListAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.mvp.CourseInfoActivity;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.bean.Courses;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CollegeActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    CollegeListAdapter collegeListAdapter;


    @OnClick({R.id.but_top1, R.id.but_top2, R.id.but_top3, R.id.but_top4, R.id.tv_iv_r})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_top1:
                getData("1");
                break;
            case R.id.but_top2:
                getData("2");
                break;
            case R.id.but_top3:
                getData("3");
                break;
            case R.id.but_top4:
                getData("0");
                break;
            case R.id.tv_iv_r://搜索
                toActivity(CourseListActivity.class);
                break;


        }
    }


    public void getData(String course_type) {

        Api().courseList2("", course_type).subscribe(new RxSubscribe<List<Courses>>(this, true) {
            @Override
            protected void _onNext(List<Courses> courses) {


                collegeListAdapter.setNewData(courses);
                if (courses.size() == 0)
                    ToastUtils.showToast("暂无课程!");

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });
    }

    @Override
    protected void init() {
        tv_title.setText("哥爱车学院");
        showIVR();
    }

    @Override
    protected void setUpView() {
        collegeListAdapter = new CollegeListAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(collegeListAdapter);


        collegeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(CollegeActivity.this, CourseInfoActivity.class);
                intent.putExtra("id", ((Courses) adapter.getData().get(position)).getId());
                intent.putExtra("courseName", ((Courses) adapter.getData().get(position)).getCourseName());
                startActivity(intent);


            }
        });
        getData("0");//查看全部
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_college;
    }
}
