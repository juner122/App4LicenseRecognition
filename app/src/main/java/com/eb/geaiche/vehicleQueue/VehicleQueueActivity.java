package com.eb.geaiche.vehicleQueue;


import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.MemberInfoInputActivity;
import com.eb.geaiche.activity.MemberManagementInfoActivity;

import com.eb.geaiche.adapter.UserlistListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.QueryByCarEntity;


import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;


//车辆进店队列   队列保存在本地
public class VehicleQueueActivity extends BaseActivity {

    QueueListAdapter adapter;

    @BindView(R.id.rv)
    RecyclerView rv;

    @Override
    protected void init() {
        tv_title.setText("待接车辆");

    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new QueueListAdapter(MyApplication.vehicleQueueUtils.getDataFromLocal(), this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener((a, view, position) -> {
            //前往
            onQueryByCar(adapter.getData().get(position).getPlateNumber());
        });
    }

    /**
     * type 0普通下单 1快速下单
     */
    private void onQueryByCar(String plate) {

        Api().queryByCar(plate).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(final QueryByCarEntity entity) {
                new AppPreferences(VehicleQueueActivity.this).put(Configure.car_no, plate);//存入待服务车辆
                //从进店车辆队列去除
                MyApplication.vehicleQueueUtils.reduceData(plate);

                if (null == entity.getUsers() || entity.getUsers().size() == 0) {
                    //普通接单
                    toActivity(MemberInfoInputActivity.class);
                    finish();
                } else {
                    //弹出用户列表
                    showUserList(entity, plate);
                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    /**
     * 弹出用户列表
     */

    private void showUserList(QueryByCarEntity entity, String plate) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_carlist);


        final UserlistListAdapter userlistListAdapter = new UserlistListAdapter(entity.getUsers(), this);
        RecyclerView rv = bottomSheetDialog.findViewById(R.id.rv);
        View tv_cancel = bottomSheetDialog.findViewById(R.id.tv_cancel);//新增会员
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(userlistListAdapter);


        userlistListAdapter.setOnItemClickListener((adapter, view, position) -> {

            Intent intent = new Intent(this, MemberManagementInfoActivity.class);
            intent.putExtra(Configure.user_id, userlistListAdapter.getData().get(position).getUserId());
            intent.putExtra(Configure.car_no, plate);
            startActivity(intent);
            finish();
        });

        if (null == entity.getCarinfo()) {//没有车况信息
            tv_cancel.setVisibility(View.INVISIBLE);
        } else {
            tv_cancel.setVisibility(View.INVISIBLE);
            tv_cancel.setOnClickListener(view -> {
                int new_car_id = entity.getCarinfo().getId();
                toActivity(MemberInfoInputActivity.class, "new_car_id", new_car_id);
                finish();
            });
        }


        bottomSheetDialog.show();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_vehicle_queue;
    }
}
