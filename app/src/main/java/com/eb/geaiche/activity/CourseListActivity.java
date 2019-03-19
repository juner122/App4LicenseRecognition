package com.eb.geaiche.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CollegeListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.mvp.CourseInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Courses;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseListActivity extends BaseActivity {


    @BindView(R.id.rv)
    RecyclerView recyclerView;


    @BindView(R.id.et_key)
    EditText et;

    CollegeListAdapter collegeListAdapter;


    @OnClick({R.id.iv_search, R.id.back})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_search:
                if (TextUtils.isEmpty(et.getText())) {
                    ToastUtils.showToast("请输入搜索内容！");
                    return;
                }
                getData(et.getText().toString());
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    @Override
    protected void init() {
        collegeListAdapter = new CollegeListAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(collegeListAdapter);


        collegeListAdapter.setEmptyView(R.layout.list_empty_search, recyclerView);

        collegeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                toActivity(CourseInfoActivity.class, "id", ((Courses) adapter.getData().get(position)).getId());
                Intent intent = new Intent(CourseListActivity.this, CourseInfoActivity.class);
                intent.putExtra("id", ((Courses) adapter.getData().get(position)).getId());
                intent.putExtra("courseName", ((Courses) adapter.getData().get(position)).getCourseName());
                startActivity(intent);
            }
        });
    }


    public void getData(String name) {

        Api().courseListSearch(name).subscribe(new RxSubscribe<List<Courses>>(this, true) {
            @Override
            protected void _onNext(List<Courses> courses) {

                collegeListAdapter.setNewData(courses);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });
    }


    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_course_list;
    }
}
