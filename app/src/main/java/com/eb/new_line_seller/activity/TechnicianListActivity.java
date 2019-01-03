package com.eb.new_line_seller.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.TechnicianAdpter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.BasePage;

import com.eb.new_line_seller.bean.Technician;

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

        Api().sysuserList().subscribe(new RxSubscribe<BasePage<Technician>>(this, true) {
            @Override
            protected void _onNext(BasePage<Technician> t) {

                list = t.getList();

                adpter.setNewData(list);
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(TechnicianListActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });

        adpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                if (list.get(position).isSelected()) {
                    list.get(position).setSelected(false);
                    pick_list.remove(list.get(position));
                } else {
                    list.get(position).setSelected(true);
                    pick_list.add(list.get(position));
                }

                adapter.notifyDataSetChanged();


            }
        });

    }

    @OnClick({R.id.but_enter})
    public void onClick() {

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
