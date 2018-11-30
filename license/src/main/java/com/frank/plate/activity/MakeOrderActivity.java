package com.frank.plate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.Technician;
import com.frank.plate.util.DateUtil;

import net.grandcentrix.tray.AppPreferences;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MakeOrderActivity extends BaseActivity {
    public static final String TAG = "MakeOrderActivity";
    @BindView(R.id.bto_top1)
    View view;

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;


    @BindView(R.id.et_postscript)
    EditText et_postscript;

    @BindView(R.id.but_set_date)
    TextView but_set_date;
    @BindView(R.id.but_to_technician_list)
    TextView but_to_technician_list;


    String car_number, user_id, moblie, car_id;


    OrderInfoEntity infoEntity;

    @Override
    protected void init() {
        tv_title.setText("下单信息");

        car_number = new AppPreferences(this).getString(Configure.car_no, "null_car_no");
        user_id = new AppPreferences(this).getString(Configure.user_id, "null_user_id");
        moblie = new AppPreferences(this).getString(Configure.moblie, "null_moblie");
        car_id = new AppPreferences(this).getString(Configure.car_id, "null_car_id");


        tv_car_no.setText(car_number);
        infoEntity = new OrderInfoEntity(user_id, moblie, car_id, car_number);

    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_make_order;
    }


    @OnClick({R.id.but_product_list, R.id.but_meal_list, R.id.but_to_technician_list, R.id.but_set_date, R.id.but_enter_order, R.id.bto_top1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_product_list:
                toActivity(ProductListActivity.class);
                break;
            case R.id.but_meal_list:
                toActivity(MealListActivity.class);
                break;
            case R.id.but_to_technician_list:

                startActivityForResult(new Intent(this, TechnicianListActivity.class), new ResultBack() {
                    @Override
                    public void resultOk(Intent data) {
                        //to do what you want when resultCode == RESULT_OK
                        but_to_technician_list.setText("");
                        List<Technician> s = data.getParcelableArrayListExtra("Technician");
                        for (Technician t : s) {

                            but_to_technician_list.append(t.getUsername() + "\t");

                        }


                    }
                });

                break;

            case R.id.but_set_date:


                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                //正确设置方式 原因：注意事有说明
                startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DATE), startDate.get(Calendar.HOUR_OF_DAY) + 1, startDate.get(Calendar.MINUTE));
                endDate.set(2020, 11, 31);


                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        but_set_date.setText(DateUtil.getFormatedDateTime(date));
//                        infoEntity.setPlanfinishi_time(DateUtil.getFormatedDateTime(date));
                        infoEntity.setPlanfinishi_time(date.getTime());
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setRangDate(startDate, endDate)//起始终止年月日设定
                        .setTitleBgColor(getResources().getColor(R.color.appColor))//标题背景颜色 Night mode
                        .build();
                pvTime.show();

                break;

            case R.id.but_enter_order:

                onMakeOrder();
                break;


            case R.id.bto_top1:


                break;
        }
    }

    private void onMakeOrder() {

        infoEntity.setPostscript(et_postscript.getText().toString());
        Api().submit(infoEntity).subscribe(new RxSubscribe<OrderInfo>(this,true) {
            @Override
            protected void _onNext(OrderInfo orderInfo) {
                toActivity(MakeOrderSuccessActivity.class, infoEntity, "orderInfo");
            }
            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);

            }
        });
    }

}
