package com.frank.plate.activity;


import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.adapter.CarListAdapter;
import com.frank.plate.bean.CarEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MemberInfoInputActivity extends BaseActivity {
    public final static String key = "queryByCarEntity";

    private QueryByCarEntity queryByCarEntity;


    @BindView(R.id.et_user_mobile)
    EditText mobile;
    @BindView(R.id.et_user_name)
    EditText name;
    @BindView(R.id.tv_e1)
    TextView tv_e1;
    @BindView(R.id.tv_e2)
    TextView tv_e2;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CarListAdapter carListAdapter = new CarListAdapter(null);
    String car_number;

    @SuppressLint("CheckResult")
    @Override
    protected void init() {

        car_number = new AppPreferences(this).getString(Configure.car_no, "");

        Api().queryByCar(car_number).subscribe(new Consumer<QueryByCarEntity>() {
            @Override
            public void accept(QueryByCarEntity entity) {
                Log.d("MemberInfoInputActivity", entity.toString());
                queryByCarEntity = entity;
                if (entity.getOrderInfo() != null) {//有订单 跳到订单详情
                    toActivity(OrderInfoActivity.class, queryByCarEntity, key);
                    finish();

                } else if (entity.getUser() != null) {//没订单 有车况信息
                    tv_e1.setVisibility(View.GONE);
                    tv_e2.setVisibility(View.GONE);
                    mobile.setText(entity.getUser().getMobile());
                    name.setText(entity.getUser().getUsername());
                    mobile.setFocusable(false);
                    name.setFocusable(false);

                    carListAdapter.setNewData(queryByCarEntity.getCarList());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //没订单 有车况信息
                Toast.makeText(MemberInfoInputActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void setUpView() {
        tv_title.setText("会员信息录入");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carListAdapter);
        carListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //查看更新车况
                toActivity(CarInfoInputActivity.class, Configure.car_no, ((CarEntity) adapter.getData().get(position)).getCarNo());
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


    @OnClick({R.id.tv_enter_order, R.id.tv_add_car})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_enter_order:
                Api().saveUserAndCar(car_number, mobile.getText().toString(), name.getText().toString()).subscribe(new Consumer<SaveUserAndCarEntity>() {
                    @Override
                    public void accept(SaveUserAndCarEntity saveUserAndCarEntity) {
                        toActivity(MakeOrderActivity.class);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(MemberInfoInputActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


                break;
            case R.id.tv_add_car:
                toActivity(CarInfoInputActivity.class);
                break;


        }

    }

}
