package com.frank.plate.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.R;
import com.frank.plate.adapter.ActivityListAdapter;
import com.frank.plate.adapter.CourseListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.ActivityEntity;

import butterknife.BindView;

public class ActivityPackageListActivity extends BaseActivity {

    private static final String TAG = "ActivityPackage";
    @BindView(R.id.rg_type)
    RadioGroup rg;

    @BindView(R.id.rv)
    RecyclerView rv;
    ActivityListAdapter ca;

    @Override
    protected void init() {
        tv_title.setText("活动列表");
    }

    @Override
    protected void setUpView() {

        rv.setLayoutManager(new LinearLayoutManager(this));
        ca = new ActivityListAdapter(null, this);
        rv.setAdapter(ca);

        getActivityList(0);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:

                        getActivityList(0);
                        break;
                    case R.id.rb2:
                        getActivityList(1);
                        break;


                }

            }
        });


        ca.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(ActivityInfoActivity.class);
            }
        });

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_activity_package_list;
    }


    private void getActivityList(int type) {
        //1.平台活动 =0.门店活动
        Api().activityList(type, "").subscribe(new RxSubscribe<ActivityEntity>(this, true) {
            @Override
            protected void _onNext(ActivityEntity a) {
                ca.setNewData(a.getPage().getList());

            }

            @Override
            protected void _onError(String message) {

                Log.e(TAG, message);
            }
        });

    }

}
