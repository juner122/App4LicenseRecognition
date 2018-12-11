package com.frank.plate.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.R;
import com.frank.plate.adapter.TechnicianAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.Technician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TechnicianListActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;


    TechnicianAdpter adpter;
    List<Technician> list = new ArrayList<>();
    List<Technician> pick_list = new ArrayList<>();

    @Override
    protected void init() {
        tv_title.setText("选择技师");

        rv.setLayoutManager(new LinearLayoutManager(this));
        adpter = new TechnicianAdpter(list);
        rv.setAdapter(adpter);



    }

    @OnClick({R.id.but_enter})
    public void onClick() {
        for (int i = 0; i < list.size(); i++) {
            CheckBox checkBox = (CheckBox) adpter.getViewByPosition(rv, i, R.id.cb);
            if(checkBox.isChecked()){
                pick_list.add(list.get(i));
            }
        }


        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Technician", (ArrayList) pick_list);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();


    }


    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_technician_list;
    }
}
