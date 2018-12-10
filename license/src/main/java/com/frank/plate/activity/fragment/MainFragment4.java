package com.frank.plate.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.R;
import com.frank.plate.activity.CourseInfoActivity;
import com.frank.plate.adapter.CourseListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Course;
import com.frank.plate.bean.CourseListItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主页页面：我的
 */
public class MainFragment4 extends BaseFragment {
    @BindView(R.id.rg_type)
    RadioGroup radioGroup;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<Course> list = new ArrayList<>();
    CourseListAdapter courseListAdapter;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment4_main;
    }

    @Override
    protected void setUpView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseListAdapter = new CourseListAdapter(list, this);
        recyclerView.setAdapter(courseListAdapter);
        getCourseList(2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:

                        getCourseList(2);
                        break;
                    case R.id.rb2:
                        getCourseList(1);
                        break;


                }

            }
        });


        courseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                toActivity(CourseInfoActivity.class);
            }
        });

    }

    public static final String TAG = "MainFragment4";

    @Override
    protected String setTAG() {
        return TAG;
    }


    private void getCourseList(int course_type) {
        Api().courseList(course_type).subscribe(new RxSubscribe<List<Course>>(getContext(), true) {
            @Override
            protected void _onNext(List<Course> course) {
                courseListAdapter.setNewData(course);
            }

            @Override
            protected void _onError(String message) {

                Log.e(TAG, message);
            }
        });


    }


}
