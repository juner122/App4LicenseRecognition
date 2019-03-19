package com.eb.geaiche.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.TechnicianInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.Technician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//员工管理
public class StaffManagementActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    TechnicianInfoAdpter adpter;

    List<Technician> list = new ArrayList<>();

    @Override
    protected void init() {
        tv_title.setText("员工列表");

    }

    @OnClick({R.id.tv_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                toActivity(StaffInfoFixActivity.class);

                break;

        }
    }

    @Override
    protected void setUpView() {


        rv.setLayoutManager(new LinearLayoutManager(this));
        adpter = new TechnicianInfoAdpter(list);
        rv.setAdapter(adpter);

        Api().sysuserList().subscribe(new RxSubscribe<BasePage<Technician>>(this, true) {
            @Override
            protected void _onNext(BasePage<Technician> t) {

                list = t.getList();

                adpter.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(StaffManagementActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        adpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toActivity(TechnicianInfoActivity.class, "id", list.get(position).getUserId());
            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_staff_management;
    }

}
