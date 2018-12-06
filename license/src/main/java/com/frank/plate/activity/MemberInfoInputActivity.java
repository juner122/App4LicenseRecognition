package com.frank.plate.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.CarListAdapter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.CarEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.view.ConfirmDialog;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;
public class MemberInfoInputActivity extends BaseActivity {
    public final static String key = "queryByCarEntity";




    @BindView(R.id.et_user_mobile)
    EditText mobile;
    @BindView(R.id.et_user_name)
    EditText name;
    @BindView(R.id.tv_e1)
    TextView tv_e1;
    @BindView(R.id.tv_e2)
    TextView tv_e2;
    @BindView(R.id.tv_check)
    TextView tv_check;
    @BindView(R.id.ll_car_list)
    View ll_car_list;

    @BindView(R.id.tv_enter_order)
    View tv_enter_order;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CarListAdapter carListAdapter = new CarListAdapter(null);

    String car_number, car_id, user_name;


    String user_id, mobile_id;
    QueryByCarEntity entity;
    @SuppressLint("CheckResult")
    @Override
    protected void init() {

        car_number = new AppPreferences(this).getString(Configure.car_no, "");

        entity = getIntent().getParcelableExtra(Configure.QUERYBYCARINFO);
        if(null!= entity){
            tv_e1.setVisibility(View.GONE);
            tv_e2.setVisibility(View.GONE);
            mobile.setText(entity.getUser().getMobile());
            name.setText(entity.getUser().getUsername());
            mobile.setFocusable(false);
            name.setFocusable(false);
            carListAdapter.setNewData(entity.getCarList());
            user_id = entity.getUser().getUserId();
            mobile_id = entity.getUser().getMobile();
            user_name = entity.getUser().getUsername();
        }else {
            tv_check.setVisibility(View.VISIBLE);
            ll_car_list.setVisibility(View.GONE);
            //测试
            name.setText("");
            mobile.setText("");
        }

    }
    @Override
    protected void setUpView() {
        tv_title.setText("会员信息录入");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carListAdapter);
        carListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                //查看更新车况
                toActivity(CarInfoInputActivity.class, ((CarEntity) adapter.getData().get(position)), Configure.CARINFO);
            }
        });
        carListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    tv_enter_order.setVisibility(View.VISIBLE);
                    car_number = carListAdapter.getData().get(position).getCarNo();
                    car_id = carListAdapter.getData().get(position).getId();
                    new AppPreferences(MemberInfoInputActivity.this).put(Configure.car_no, car_number);//选择车辆时更新car_no  保存到Preferences
                    new AppPreferences(MemberInfoInputActivity.this).put(Configure.car_id, car_id);//选择车辆时更新car_no  保存到Preferences
                    adapter.getViewByPosition(recyclerView, position, R.id.iv1).setVisibility(View.VISIBLE);

                    for (int i = 0; i < adapter.getData().size(); i++) {
                        if (i != position)
                            adapter.getViewByPosition(recyclerView, i, R.id.iv1).setVisibility(View.INVISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_info_input;
    }


    @OnClick({R.id.tv_enter_order, R.id.tv_add_car, R.id.tv_check})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_enter_order:

                new AppPreferences(this).put(Configure.user_id, user_id);
                new AppPreferences(this).put(Configure.moblie, mobile_id);
                new AppPreferences(this).put(Configure.user_name, user_name);//选择车辆时更新car_no  保存到Preferences

                toActivity(MakeOrderActivity.class);
                break;
            case R.id.tv_add_car:

                new AppPreferences(this).put(Configure.user_id, user_id);
                toActivity(CarInfoInputActivity.class);
                break;
            case R.id.tv_check:

                if (TextUtils.isEmpty(mobile.getText()) || TextUtils.isEmpty(name.getText())) {
                    Toast.makeText(MemberInfoInputActivity.this, "请完整填写信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ConfirmDialog confirmDialog = new ConfirmDialog(this, name.getText().toString(), mobile.getText().toString());
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                        Api().addUser(mobile.getText().toString(), name.getText().toString()).subscribe(new RxSubscribe<SaveUserAndCarEntity>(MemberInfoInputActivity.this, true) {
                            @Override
                            protected void _onNext(SaveUserAndCarEntity s) {

                                //保存UserID
                                user_id = s.getUser_id();
                                mobile_id = mobile.getText().toString();
                                user_name = name.getText().toString();
                                carListAdapter.setNewData(s.getCarList());
                                tv_check.setVisibility(View.GONE);
                                mobile.setFocusable(false);
                                name.setFocusable(false);
                                ll_car_list.setVisibility(View.VISIBLE);
                            }

                            @Override
                            protected void _onError(String message) {
                                Toast.makeText(MemberInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                    }
                });

                break;
        }

    }

}
