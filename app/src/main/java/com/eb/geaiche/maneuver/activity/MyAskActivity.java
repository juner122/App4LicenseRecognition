package com.eb.geaiche.maneuver.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.maneuver.adapter.ManeuverAskAdapter;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Ask;
import com.juner.mvp.bean.UserEntity;

import java.util.List;

import butterknife.BindView;

public class MyAskActivity extends BaseActivity {

    ManeuverAskAdapter askAdapter;//反馈列表;
    @BindView(R.id.rv)
    RecyclerView rv;

    String unity_id;//活动id

    @Override
    protected void init() {

        tv_title.setText("我的反馈");
        unity_id = getIntent().getStringExtra("id");
    }

    @Override
    protected void setUpView() {
        askAdapter = new ManeuverAskAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(askAdapter);

    }

    @Override
    protected void setUpData() {


        Api().getInfo().subscribe(new RxSubscribe<UserEntity>(this, true) {
            @Override
            protected void _onNext(UserEntity ue) {
                getList(String.valueOf(ue.getUserId()));
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("员工信息获取失败！" + message);
            }
        });


    }


    private void getList(String uid) {
        Api().askList(unity_id, uid).subscribe(new RxSubscribe<List<Ask>>(this, true) {
            @Override
            protected void _onNext(List<Ask> asks) {
                askAdapter.setNewData(asks);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast("查询失败！" + message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_my_ask;
    }
}
