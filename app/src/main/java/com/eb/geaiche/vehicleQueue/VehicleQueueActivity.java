package com.eb.geaiche.vehicleQueue;


import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.MemberInfoInputActivity;
import com.eb.geaiche.activity.MemberManagementInfoActivity;

import com.eb.geaiche.adapter.UserlistListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.maneuver.activity.ManeuverInfoActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.ConfirmDialogEt;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.QueryByCarEntity;


import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

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
        adapter = new QueueListAdapter(null, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setEmptyView(R.layout.order_list_empty_view, rv);

        adapter.setOnItemClickListener((a, view, position) -> {

            // 改变接车状态
//            plateUpdate(adapter.getData().get(position).getId());

            //前往
            onQueryByCar(adapter.getData().get(position).getPlateNumber(), adapter.getData().get(position).getId());

        });

        adapter.setOnItemChildClickListener((a, view, position) -> {

            final ConfirmDialogEt dialogCanlce = new ConfirmDialogEt(VehicleQueueActivity.this, "误扫操作", "请输入删除车辆原因!");
            dialogCanlce.show();
            dialogCanlce.setClicklistener((String postscript) -> {
                dialogCanlce.dismiss();

                updateUnable(adapter.getData().get(position).getId(), postscript);
                getPlateList();
            });

        });
    }

    private void updateUnable(String id, String changeReason) {
        Api().updateUnable(id, changeReason).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                ToastUtils.showToast("操作成功");

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPlateList();
    }

    /**
     * type 0普通下单 1快速下单
     *
     * @param id 自动识别车辆的进店队列id
     */
    private void onQueryByCar(String plate, String id) {

        Api().queryByCar(plate).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(final QueryByCarEntity entity) {
                new AppPreferences(VehicleQueueActivity.this).put(Configure.car_no, plate);//存入待服务车辆
//                //从进店车辆队列去除
//                MyApplication.vehicleQueueUtils.reduceData(plate);

                if (null == entity.getUsers() || entity.getUsers().size() == 0) {
                    //普通接单
                    toActivity(MemberInfoInputActivity.class, "plateId", id);
                    finish();
                } else {
                    //弹出用户列表
                    showUserList(entity, plate, id);
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

    private void showUserList(QueryByCarEntity entity, String plate, String id) {
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
            intent.putExtra("plateId", id);
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

    //查询自动扫描待接车辆池
    private void getPlateList() {

        Api().platelist().subscribe(new RxSubscribe<List<VehicleQueue>>(this, true) {
            @Override
            protected void _onNext(List<VehicleQueue> list) {

                List<VehicleQueue> list_not = new ArrayList<>();//未接车的才显示 status=0;

                for (VehicleQueue v : list) {
                    if (v.getStatus().equals("0")) {
                        list_not.add(v);
                    }
                }
                adapter.setNewData(list_not);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("查询失败:" + message);
            }
        });

    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_vehicle_queue;
    }
}
