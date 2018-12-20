package com.frank.plate.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.OrderList2Adapter;
import com.frank.plate.adapter.SimpleCarInfoAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.CarEntity;
import com.frank.plate.bean.MemberOrder;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.util.String2Utils;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberManagementInfoActivity extends BaseActivity {
    private static final String TAG = "MemberManagementInfo";
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.rv1)
    RecyclerView rv1;
    @BindView(R.id.rv2)
    RecyclerView rv2;

    SimpleCarInfoAdpter adpter1;
    OrderList2Adapter adapter2;

    String car_number, moblie, user_name;

    int user_id, car_id;


    List<CarEntity> cars = new ArrayList<>();

    @Override
    protected void init() {

        user_id = getIntent().getIntExtra(Configure.user_id, 0);
        new AppPreferences(this).put(Configure.user_id, user_id);


        tv_title.setText("会员信息");
        adpter1 = new SimpleCarInfoAdpter(cars);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);

        adapter2 = new OrderList2Adapter(null);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adapter2);

        adpter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                car_number = cars.get(position).getCarNo();
                car_id = cars.get(position).getId();

                for (CarEntity c : cars) {
                    c.setSelected(false);
                }

                cars.get(position).setSelected(true);
                adapter.notifyDataSetChanged();


            }
        });

        adpter1.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                //查看更新车况
//                toActivity(CarInfoInputActivity.class, ((CarEntity) adapter.getData().get(position)), Configure.CARINFO);
                Intent intent = new Intent(MemberManagementInfoActivity.this, CarInfoInputActivity.class);
                intent.putExtra("result_code", 001);
                intent.putExtra(Configure.CARINFO, ((CarEntity) adapter.getData().get(position)));
                reCarListInfo(intent);


            }
        });


        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderInfoEntity o = (OrderInfoEntity) adapter.getData().get(position);
                toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, o.getId());

            }
        });


    }

    @Override
    protected void setUpView() {
        memberOrderList();
    }


    private void memberOrderList() {

        Api().memberOrderList(user_id).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder memberOrder) {

                moblie = memberOrder.getMember().getMobile();
                user_name = memberOrder.getMember().getUsername();

                phone.setText(moblie);
                name.setText(user_name);

                cars = memberOrder.getCarList();//设置第一辆车为选中
                cars.get(0).setSelected(true);//设置第一辆车为选中


                adpter1.setNewData(cars);
                adapter2.setNewData(memberOrder.getOrderList());


                car_number = memberOrder.getCarList().get(0).getCarNo();//默认选择第一辆车
                car_id = memberOrder.getCarList().get(0).getId();


            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
            }
        });


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_management_member_info;
    }

    @OnClick({R.id.tv_new_order, R.id.tv_add_car})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_new_order:
                toMakeOrder(user_id, car_id, moblie, user_name, car_number);
                break;
            case R.id.tv_add_car:


                Intent intent = new Intent(this, CarInfoInputActivity.class);
                intent.putExtra("result_code", 001);
                reCarListInfo(intent);
                break;
        }
    }


    private void reCarListInfo(Intent intent) {

        startActivityForResult(new Intent(intent), new ResultBack() {
            @Override
            public void resultOk(Intent data) {
                //to do what you want when resultCode == RESULT_OK
                memberOrderList();

            }
        });

    }
}
