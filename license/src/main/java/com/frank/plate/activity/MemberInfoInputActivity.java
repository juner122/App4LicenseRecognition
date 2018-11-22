package com.frank.plate.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.frank.plate.R;
import com.frank.plate.adapter.CarListAdapter;
import com.frank.plate.api.MySubscriber;
import com.frank.plate.api.SubscribeOnNextListener;
import com.frank.plate.bean.QueryByCarEntity;
import com.tamic.novate.Throwable;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberInfoInputActivity extends BaseActivity {
    public final static String key = "queryByCarEntity";

    private QueryByCarEntity queryByCarEntity;


    @BindView(R.id.et_user_mobile)
    EditText mobile;
    @BindView(R.id.et_user_name)
    EditText name;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CarListAdapter carListAdapter = new CarListAdapter(null);

    @Override
    protected void init() {


        Api().queryByCar(new MySubscriber<>(this, new SubscribeOnNextListener<QueryByCarEntity>() {
            @Override
            public void onNext(QueryByCarEntity entity) {

                Log.d("MemberInfoInputActivity", entity.toString());

                queryByCarEntity = entity;
                if (entity.getOrderInfo() != null) {//有订单 跳到订单详情

                    toActivity(OrderInfoActivity.class, queryByCarEntity, key);
                    finish();

                } else if (entity.getUser() != null) {
                    mobile.setText(entity.getUser().getMobile());
                    name.setText(entity.getUser().getUsername());
                    carListAdapter.setNewData(queryByCarEntity.getCarList());
                }

            }

            @Override
            public void onError(Throwable e) {

                Toast.makeText(MemberInfoInputActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        }), 1111);


    }

    @Override
    protected void setUpView() {
        tv_title.setText("会员信息录入");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carListAdapter);
        carListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {

                    case R.id.tv_to_car_info:
                        toActivity(CarInfoInputActivity.class);
                        break;
                    case R.id.tv_to_make_order:
                        toActivity(MakeOrderActivity.class);
                        break;
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


    @OnClick({R.id.tv_add_car})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_car:
                toActivity(CarInfoInputActivity.class);
                break;


        }

    }

}
