package com.eb.geaiche.activity.fragment;


import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.CourseRecordActivity;
import com.eb.geaiche.activity.CourseListActivity;

import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.adapter.CollegeListAdapter;
import com.eb.geaiche.mvp.CourseInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Courses;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页页面：学院
 */
public class MainFragment4 extends BaseFragment {
    @BindView(R.id.rv)
    RecyclerView recyclerView;

    CollegeListAdapter collegeListAdapter;


    @OnClick({R.id.but_top1, R.id.but_top2, R.id.but_top3, R.id.but_top4, R.id.iv_search, R.id.rv_record})
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
            case R.id.iv_search://搜索
                toActivity(CourseListActivity.class);
                break;
            case R.id.rv_record://学习记录
                toActivity(CourseRecordActivity.class);
                break;


        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        getData("0");//查看全部

    }

    @Override
    protected void setUpView() {
        collegeListAdapter = new CollegeListAdapter(getActivity(), null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(collegeListAdapter);


        collegeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                toActivity(CourseInfoActivity.class, "id", ((Courses) adapter.getData().get(position)).getId());

//                Intent intent = new Intent(getActivity(), AliyunPlayerSkinActivity.class);
                Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
                intent.putExtra("id", ((Courses) adapter.getData().get(position)).getId());
                intent.putExtra("courseName", ((Courses) adapter.getData().get(position)).getCourseName());
                startActivity(intent);


            }
        });
    }

    public void getData(String course_type) {

        Api().courseList2("", course_type).subscribe(new RxSubscribe<List<Courses>>(getActivity(), true) {
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
    protected String setTAG() {
        return "MainFragment4";
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment4_main;
    }

}
