package com.eb.new_line_seller.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.TechnicianInfoAdpter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.BasePage;
import com.eb.new_line_seller.bean.Technician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_staff_management;
    }

}
