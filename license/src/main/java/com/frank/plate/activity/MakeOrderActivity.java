package com.frank.plate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.adapter.SimpleGoodInfoAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.Technician;
import com.frank.plate.util.CartUtils;
import com.frank.plate.util.DateUtil;

import net.grandcentrix.tray.AppPreferences;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MakeOrderActivity extends BaseActivity {


    public static final String TAG = "MakeOrderActivity";
    @BindView(R.id.bto_top1)
    View view;


    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;


    @BindView(R.id.et_postscript)
    EditText et_postscript;

    @BindView(R.id.but_set_date)
    TextView but_set_date;
    @BindView(R.id.but_to_technician_list)
    TextView but_to_technician_list;

    @BindView(R.id.tv_goods_price)
    TextView tv_goods_price;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;


    String car_number, user_id, moblie, car_id;


    OrderInfoEntity infoEntity;
    SimpleGoodInfoAdpter simpleGoodInfoAdpter;
    List<Technician> technicians;

    List<GoodsEntity> goods_top;


    boolean top_button1_pick;
    boolean top_button2_pick;
    boolean top_button3_pick;
    boolean top_button4_pick;
    CartUtils cartUtils;

    @Override
    protected void onResume() {
        super.onResume();

        simpleGoodInfoAdpter = new SimpleGoodInfoAdpter(cartUtils.getDataFromLocal());
        rv_goods.setLayoutManager(new LinearLayoutManager(this));
        rv_goods.setAdapter(simpleGoodInfoAdpter);
        simpleGoodInfoAdpter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                List<GoodsEntity> goodsEntities = cartUtils.getDataFromLocal();
                try {
                    TextView tv_number = (TextView) adapter.getViewByPosition(rv_goods, position, R.id.tv_number);
                    View ib_reduce = adapter.getViewByPosition(rv_goods, position, R.id.ib_reduce);

                    int number = goodsEntities.get(position).getNumber();//获取


                    switch (view.getId()) {
                        case R.id.ib_plus:
                            if (number == 0) {
                                assert tv_number != null;
                                tv_number.setVisibility(View.VISIBLE);
                                assert ib_reduce != null;
                                ib_reduce.setVisibility(View.VISIBLE);
                            }
                            number++;
                            tv_number.setText(String.valueOf(number));
                            cartUtils.addData(goodsEntities.get(position));
                            break;

                        case R.id.ib_reduce:

                            number--;
                            tv_number.setText(String.valueOf(number));
                            cartUtils.reduceData(goodsEntities.get(position));


                            if (number == 0) {
                                view.setVisibility(View.INVISIBLE);//隐藏减号
                                tv_number.setVisibility(View.INVISIBLE);
                            }
                            break;
                    }


                    refreshData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        refreshData();
    }

    @Override
    protected void init() {
        cartUtils = MyApplication.cartUtils;

        tv_title.setText("下单信息");
        getTopData();
        car_number = new AppPreferences(this).getString(Configure.car_no, "null_car_no");
        user_id = new AppPreferences(this).getString(Configure.user_id, "null_user_id");
        moblie = new AppPreferences(this).getString(Configure.moblie, "null_moblie");
        car_id = new AppPreferences(this).getString(Configure.car_id, "null_car_id");
        infoEntity = new OrderInfoEntity(user_id, moblie, car_id, car_number);
        tv_car_no.setText(car_number);


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


    @OnClick({R.id.but_product_list, R.id.but_meal_list, R.id.but_to_technician_list, R.id.but_set_date, R.id.but_enter_order, R.id.bto_top1, R.id.bto_top2, R.id.bto_top3, R.id.bto_top4})
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
                        technicians = data.getParcelableArrayListExtra("Technician");
                        for (Technician t : technicians) {
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

                if (!top_button1_pick) {

                    view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    cartUtils.addData(goods_top.get(0));
                    top_button1_pick = true;
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    cartUtils.reduceData(goods_top.get(0));
                    top_button1_pick = false;
                }
                refreshData();

                break;
            case R.id.bto_top2:

                if (!top_button2_pick) {

                    view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    cartUtils.addData(goods_top.get(1));
                    top_button2_pick = true;
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    cartUtils.reduceData(goods_top.get(1));
                    top_button2_pick = false;
                }
                refreshData();

                break;
            case R.id.bto_top3:


                if (!top_button3_pick) {

                    view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    cartUtils.addData(goods_top.get(2));
                    top_button3_pick = true;
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    cartUtils.reduceData(goods_top.get(2));
                    top_button3_pick = false;
                }
                refreshData();
                break;
            case R.id.bto_top4:

                if (!top_button4_pick) {

                    view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    cartUtils.addData(goods_top.get(3));
                    top_button4_pick = true;
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    cartUtils.reduceData(goods_top.get(3));
                    top_button4_pick = false;
                }
                refreshData();

                break;
        }
    }

    private void onMakeOrder() {

        infoEntity.setPostscript(et_postscript.getText().toString());
        infoEntity.setGoodsList(cartUtils.getDataFromLocal());
        infoEntity.setSysUserList(technicians);
        Api().submit(infoEntity).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo orderInfo) {
                toActivity(MakeOrderSuccessActivity.class, orderInfo, "orderInfo");

            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);

            }
        });

        cartUtils.deleteAllData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartUtils.deleteAllData();

    }


    private void setGoodsPric() {


        tv_goods_price.setText("已选：￥" + cartUtils.getTotalPrice());
        tv_total_price.setText("已选：￥" + cartUtils.getTotalPrice());

    }


    private void getTopData() {

        Api().queryAnyGoods().subscribe(new RxSubscribe<GoodsListEntity>(this, true) {
            @Override
            protected void _onNext(GoodsListEntity goodsListEntity) {
                goods_top = goodsListEntity.getGoodsList();

            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);

            }
        });

    }

    private void refreshData() {
        simpleGoodInfoAdpter.setNewData(cartUtils.getDataFromLocal());
        setGoodsPric();
    }
}
