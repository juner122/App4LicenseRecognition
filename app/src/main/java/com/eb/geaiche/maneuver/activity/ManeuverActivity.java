package com.eb.geaiche.maneuver.activity;


import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.maneuver.adapter.ManeuverAdapter;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Maneuver;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ManeuverActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    ManeuverAdapter adapter;


    @OnClick({R.id.tv_title_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_r:
                //我已报名列表
                toActivity(ManeuverJoinListActivity.class);
                break;
        }
    }

    @Override
    protected void init() {

        tv_title.setText("活动管理");
        setRTitle("我已报名");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {
        adapter = new ManeuverAdapter(null, this);
        adapter.setOnItemClickListener((a, view, position) -> {
            toActivity(ManeuverInfoActivity.class, "id", adapter.getItem(position).getId());
        });


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Api().listShopunity().subscribe(new RxSubscribe<List<Maneuver>>(this, true) {
            @Override
            protected void _onNext(List<Maneuver> maneuvers) {
                adapter.setNewData(maneuvers);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("活动列表获取失败！" + message);
            }
        });


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_maneuver;
    }
}
