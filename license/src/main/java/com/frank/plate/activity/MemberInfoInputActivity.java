package com.frank.plate.activity;


import android.annotation.SuppressLint;
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
import com.frank.plate.bean.CarEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.view.ConfirmDialog;

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
    @BindView(R.id.tv_check)
    TextView tv_check;
    @BindView(R.id.ll_car_list)
    View ll_car_list;

    @BindView(R.id.tv_enter_order)
    View tv_enter_order;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CarListAdapter carListAdapter = new CarListAdapter(null);

    String car_number;


    int activity_state;//页面状态   1（有订单） 2（无订单有车况） 3（无订单无车况）

    String user_id;

    @SuppressLint("CheckResult")
    @Override
    protected void init() {

        car_number = new AppPreferences(this).getString(Configure.car_no, "");
//        car_number = "闽AE7888";
        car_number = "测试A1126";
        car_number = "111";

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
                    activity_state = 2;

                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //没订单 没车况信息
//                Toast.makeText(MemberInfoInputActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                activity_state = 3;
                tv_check.setVisibility(View.VISIBLE);
                ll_car_list.setVisibility(View.GONE);
                tv_enter_order.setVisibility(View.GONE);


                //测试

                name.setText("李进武");
                mobile.setText("15737226472");

            }
        });

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
                toActivity(CarInfoInputActivity.class, ((CarEntity) adapter.getData().get(position)), "CarEntity");
            }
        });
        carListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tv_enter_order.setVisibility(View.VISIBLE);

                new AppPreferences(MemberInfoInputActivity.this).put(Configure.car_no, car_number);//选择车辆时更新car_no  保存到Preferences
                adapter.getViewByPosition(recyclerView, position, R.id.iv1).setVisibility(View.VISIBLE);

                for (int i = 0; i < adapter.getData().size(); i++) {
                    if (i != position)
                        adapter.getViewByPosition(recyclerView, i, R.id.iv1).setVisibility(View.INVISIBLE);
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

                toActivity(MakeOrderActivity.class);
                break;
            case R.id.tv_add_car:
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
                        Api().addUser(mobile.getText().toString(), name.getText().toString()).subscribe(new Consumer<SaveUserAndCarEntity>() {
                            @Override
                            public void accept(SaveUserAndCarEntity entity) {
                                Toast.makeText(MemberInfoInputActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                //保存UserID
                                user_id = entity.getUser_id();


                                carListAdapter.setNewData(entity.getCarList());

                                tv_check.setVisibility(View.GONE);
                                mobile.setFocusable(false);
                                name.setFocusable(false);
                                ll_car_list.setVisibility(View.VISIBLE);

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                Toast.makeText(MemberInfoInputActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

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
