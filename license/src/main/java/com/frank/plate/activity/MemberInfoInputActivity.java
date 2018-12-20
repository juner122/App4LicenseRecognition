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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberInfoInputActivity extends BaseActivity {
    public final static String TAG = "queryByCarEntity";


    @BindView(R.id.et_user_mobile)
    EditText et_mobile;
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

    static String car_number, mobile, user_name;
    List<CarEntity> cars = new ArrayList<>();

    int user_id, car_id;

    @SuppressLint("CheckResult")
    @Override
    protected void init() {

        car_number = new AppPreferences(this).getString(Configure.car_no, "");


        tv_check.setVisibility(View.VISIBLE);
        ll_car_list.setVisibility(View.GONE);
        //测试
        name.setText("");
        et_mobile.setText("");
        carListAdapter = new CarListAdapter(cars);

    }

    @Override
    protected void setUpView() {
        tv_title.setText("会员信息录入");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carListAdapter);


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

                toMakeOrder(user_id, car_id, mobile, user_name, car_number);
                break;
            case R.id.tv_add_car:

                new AppPreferences(this).put(Configure.user_id, user_id);
                toActivity(CarInfoInputActivity.class);
                break;
            case R.id.tv_check:

                if (TextUtils.isEmpty(et_mobile.getText()) || TextUtils.isEmpty(name.getText())) {
                    Toast.makeText(MemberInfoInputActivity.this, "请完整填写信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ConfirmDialog confirmDialog = new ConfirmDialog(this, name.getText().toString(), et_mobile.getText().toString());
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        confirmDialog.dismiss();
                        getAddUser();
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });

                break;
        }

    }

    private void getAddUser() {
        Api().addUser(et_mobile.getText().toString(), name.getText().toString()).subscribe(new RxSubscribe<SaveUserAndCarEntity>(MemberInfoInputActivity.this, true) {
            @Override
            protected void _onNext(SaveUserAndCarEntity s) {

                //保存UserID
                user_id = s.getUser_id();
                mobile = et_mobile.getText().toString();
                user_name = name.getText().toString();

                tv_check.setVisibility(View.GONE);
                et_mobile.setFocusable(false);
                name.setFocusable(false);
                ll_car_list.setVisibility(View.VISIBLE);

                carListAdapter.setNewData(s.getCarList());
                initAdapter();
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(MemberInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAdapter() {
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


                tv_enter_order.setVisibility(View.VISIBLE);
                car_number = carListAdapter.getData().get(position).getCarNo();
                car_id = carListAdapter.getData().get(position).getId();


                for (CarEntity c : cars) {
                    c.setSelected(false);
                }

                cars.get(position).setSelected(true);
                adapter.notifyDataSetChanged();


            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        queryByCar(intent.getStringExtra(Configure.car_no));
    }

    private void queryByCar(String carNumber) {
        Api().queryByCar(carNumber).subscribe(new RxSubscribe<QueryByCarEntity>(this, true) {
            @Override
            protected void _onNext(QueryByCarEntity entity) {
                cars = entity.getCarList();

                carListAdapter.setNewData(cars);
            }

            @Override
            protected void _onError(String message) {

                Log.d(TAG, message);
            }
        });

    }

}
