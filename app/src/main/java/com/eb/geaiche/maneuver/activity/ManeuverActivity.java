package com.eb.geaiche.maneuver.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.maneuver.adapter.ManeuverAdapter;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Maneuver;

import java.util.List;

import butterknife.BindView;

public class ManeuverActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    ManeuverAdapter adapter;

    @Override
    protected void init() {

        tv_title.setText("活动管理");
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
