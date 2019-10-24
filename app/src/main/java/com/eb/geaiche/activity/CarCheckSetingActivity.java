package com.eb.geaiche.activity;


import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CarCheckItem2Adapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.CheckOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//车况检查信息修改页面
public class CarCheckSetingActivity extends BaseActivity {
    @BindView(R.id.rv)
    RecyclerView rv;
    CarCheckItem2Adapter adapter;

    @Override
    protected void init() {
        tv_title.setText("检查项目");
        setRTitle("新增检修项目");
    }

    @Override
    protected void setUpView() {
        adapter = new CarCheckItem2Adapter(null);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

//                toActivity(CarCheckAddActivity.class);
                Intent i = new Intent(CarCheckSetingActivity.this, CarCheckAddActivity.class);
                i.putExtra("CheckOptions", adapter.getData().get(position));
                i.putExtra("isAdd", false);
                startActivity(i);

            }
        });
    }

    @Override
    protected void setUpData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取新的检修项列表
        Api().queryCheckOptions().subscribe(new RxSubscribe<List<CheckOptions>>(this, true) {
            @Override
            protected void _onNext(List<CheckOptions> checkOptions) {
                adapter.setNewData(checkOptions);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_check_seting;
    }


    @OnClick({R.id.tv_title_r})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_r://

                toActivity(CarCheckAddActivity.class);
                break;



        }
    }
}
