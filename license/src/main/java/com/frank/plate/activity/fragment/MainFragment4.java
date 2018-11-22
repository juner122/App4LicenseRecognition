package com.frank.plate.activity.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioGroup;

import com.frank.plate.R;
import com.frank.plate.adapter.CourseListAdapter;
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

    List<CourseListItemEntity> list, list2;
    CourseListAdapter courseListAdapter;

    @Override
    public int setLayoutResourceID() {
        return R.layout.fragment4_main;
    }

    @Override
    protected void setUpView() {

        list = new ArrayList<>();
        list2 = new ArrayList<>();
        list.add(new CourseListItemEntity("", "汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list.add(new CourseListItemEntity("", "汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list.add(new CourseListItemEntity("", "汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list.add(new CourseListItemEntity("", "汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list.add(new CourseListItemEntity("", "汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list2.add(new CourseListItemEntity("", "2汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list2.add(new CourseListItemEntity("", "22汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list2.add(new CourseListItemEntity("", "222汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));
        list2.add(new CourseListItemEntity("", "222汽车电池知识你了解多少？", "由于锂电池在混合动力汽车和纯电动汽车领域的出色电能和功率特性……", "免费"));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseListAdapter = new CourseListAdapter(list);
        recyclerView.setAdapter(courseListAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:

                        courseListAdapter.setNewData(list);

                        break;
                    case R.id.rb2:
                        courseListAdapter.setNewData(list2);
                        break;


                }

            }
        });


    }
}
