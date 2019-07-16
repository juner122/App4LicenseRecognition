package com.eb.geaiche.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.TechnicianAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.BasePage;

import com.juner.mvp.bean.Technician;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TechnicianListActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView rv;


    TechnicianAdpter adpter;
    List<Technician> list = new ArrayList<>();
    List<Technician> pick_list;

    @Override
    protected void init() {
        tv_title.setText("选择技师");

        pick_list = getIntent().getParcelableArrayListExtra("Technician");
        rv.setLayoutManager(new LinearLayoutManager(this));
        adpter = new TechnicianAdpter(list);
        rv.setAdapter(adpter);

        Api().sysuserList().subscribe(new RxSubscribe<BasePage<Technician>>(this, true) {
            @Override
            protected void _onNext(BasePage<Technician> t) {

                list = t.getList();
                setPick();
                adpter.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(TechnicianListActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        adpter.setOnItemClickListener((adapter, view, position) -> {

            if (null == pick_list) {
                pick_list = new ArrayList<>();
            }
            if (list.get(position).isSelected()) {
                list.get(position).setSelected(false);
                pick_list.remove(list.get(position));
            } else {
                list.get(position).setSelected(true);
                pick_list.add(list.get(position));
            }
            adapter.notifyDataSetChanged();


        });

    }

    //设置选择的项
    private void setPick() {

        if (null != pick_list && pick_list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int p = 0; p < pick_list.size(); p++) {
                    if (pick_list.get(p).getUserId() == list.get(i).getUserId()) {
                        list.get(i).setSelected(true);
                        pick_list.remove(p);
                        pick_list.add(p, list.get(i));
                    }
                }

            }
        }


    }


    @OnClick({R.id.but_enter})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.but_enter:

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Technician", (ArrayList) pick_list);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;


        }
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
