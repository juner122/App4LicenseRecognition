package com.eb.geaiche.activity;


import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.mvp.ActivateCardActivity;
import com.eb.geaiche.mvp.FixInfoDescribeActivity;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.FixNameDialog;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SimpleCarInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.NullDataEntity;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.Collections;
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


    SimpleCarInfoAdpter adpter1;


    String car_number = "", new_car_number, moblie, user_name, car_vin, mileage;

    int user_id, car_id, new_car_id;


    List<CarInfoRequestParameters> cars = new ArrayList<>();

    String plateId;//自动识别车辆的进店队列id

    @Override
    protected void init() {

        user_id = getIntent().getIntExtra(Configure.user_id, 0);

        MyAppPreferences.putString(Configure.user_id, String.valueOf(user_id));

        new_car_id = getIntent().getIntExtra("new_car_id", 0);
        new_car_number = getIntent().getStringExtra(Configure.car_no);


        new AppPreferences(this).put(Configure.user_id, user_id);


        tv_title.setText("会员信息");
        showRView("新增车辆");

        adpter1 = new SimpleCarInfoAdpter(cars);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);


        adpter1.setOnItemClickListener((adapter, view, position) -> {

            car_number = cars.get(position).getCarNo();
            car_id = cars.get(position).getId();
            car_vin = cars.get(position).getVin();
            mileage = cars.get(position).getMileage();//里程数

            for (CarInfoRequestParameters c : cars) {
                c.setSelected(false);
            }

            cars.get(position).setSelected(true);
            adapter.notifyDataSetChanged();


        });

        adpter1.setOnItemChildClickListener((adapter, view, position) -> {

            //查看更新车况
            Intent intent = new Intent(MemberManagementInfoActivity.this, CarInfoInputActivity.class);
            intent.putExtra("result_code", 1);
            intent.putExtra(Configure.CARID, ((CarInfoRequestParameters) adapter.getData().get(position)).getId());
            reCarListInfo(intent);


        });


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        car_id = 0;
        memberOrderList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        memberOrderList();

    }

    private void memberOrderList() {
        new AppPreferences(this).put(Configure.user_id, user_id);

        Api().memberOrderList(user_id).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder memberOrder) {

                moblie = memberOrder.getMember().getMobile();
                user_name = memberOrder.getMember().getUsername();

                phone.setText(moblie);
                name.setText(null == user_name || "".equals(user_name) ? "匿名" : user_name);

                cars = memberOrder.getCarList();

                if (cars.size() == 0) {
                    return;
                }

                car_vin = cars.get(0).getVin();//设置车架号
                mileage = cars.get(0).getMileage();//设置里程数


                if (!"".equals(new_car_number)) {
                    for (int i = 0; i < cars.size(); i++) {
                        if (cars.get(i).getCarNo().equals(new_car_number)) {
                            cars.get(i).setSelected(true);//设置车为选中
                            car_number = memberOrder.getCarList().get(i).getCarNo();
                            car_id = memberOrder.getCarList().get(i).getId();

                            new_car_number = "";//清空
                            Collections.swap(cars, 0, i);//换位
                            break;
                        }
                    }
                }

                adpter1.setNewData(cars);


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
        return R.layout.activity_member_management_member_info2;
    }

    @OnClick({R.id.tv_new_order, R.id.tv_fix_order, R.id.ll_change_name, R.id.tv_title_r, R.id.but_1, R.id.but_2, R.id.but_3})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_new_order:
                if (!isPerfect())
                    return;


                plateUpdate();
                toMakeOrder(user_id, car_id, moblie, user_name, car_number, mileage);

                break;

            case R.id.tv_fix_order:

                if (!isPerfect())
                    return;

                plateUpdate();
                Intent intent2 = new Intent(this, FixInfoDescribeActivity.class);
                intent2.putExtra(Configure.car_no, car_number);
                intent2.putExtra(Configure.car_id, car_id);
                intent2.putExtra(Configure.user_name, user_name);
                intent2.putExtra(Configure.moblie, moblie);
                intent2.putExtra(Configure.user_id, user_id);

                startActivity(intent2);
                break;
            case R.id.ll_change_name://修改用户名

                //弹出对话框
                final FixNameDialog fixNameDialog = new FixNameDialog(this, user_id, phone.getText().toString(), user_name);
                fixNameDialog.show();


                break;
            case R.id.tv_title_r:
                Intent intent3 = new Intent(this, CarInfoInputActivity.class);
                intent3.putExtra(Configure.car_no, new_car_number);
                intent3.putExtra("result_code", 1);
                intent3.putExtra("new_car_id", new_car_id);
                startActivity(intent3);
                break;

            case R.id.but_1:
                //有效套卡
                Intent i = new Intent(MemberManagementInfoActivity.this, ProductMealActivity.class);
                i.putExtra(Configure.user_id, user_id);
                i.putExtra(Configure.car_no, car_number);
                startActivity(i);

                break;
            case R.id.but_2:
                //会员开卡
                Intent intent = new Intent(MemberManagementInfoActivity.this, ActivateCardActivity.class);
                intent.putExtra(Configure.moblie, moblie);
                intent.putExtra(Configure.user_name, user_name);
                startActivity(intent);
                break;
            case R.id.but_3:
                //消费记录
                toActivity(MemberRecordActivity.class, Configure.user_id, user_id + "");

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


    private boolean isPerfect() {
        if (car_id == 0) {
            ToastUtils.showToast("请选择一辆车！");
            return false;
        }
        if (!MyAppPreferences.getShopType()) {//直营版
            if (null == user_name || user_name.equals("") || null == moblie || moblie.equals("")) {
                ToastUtils.showToast("请完善用户信息！");
                return false;
            }
            if (null == car_vin) {
                ToastUtils.showToast("请完善车辆信息,缺少车架号！");
                return false;
            }
            if (null == mileage || "".equals(mileage)) {
                ToastUtils.showToast("请完善车辆里程数信息！");
                return false;

            }

        }
        MyAppPreferences.putString(Configure.CAR_MILEAGE, mileage);
        return true;

    }

    //扫描车辆池改变接车状态
    private void plateUpdate() {

        plateId = getIntent().getStringExtra("plateId");
        if (null == plateId)
            return;

        Api().plateUpdate(plateId).subscribe(new RxSubscribe<NullDataEntity>(this, false) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

}
