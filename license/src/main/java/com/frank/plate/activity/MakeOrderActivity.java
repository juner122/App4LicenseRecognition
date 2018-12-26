package com.frank.plate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.adapter.SimpleGoodInfoAdpter;
import com.frank.plate.adapter.SimpleMealInfoAdpter;
import com.frank.plate.adapter.SimpleServiceInfoAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.Technician;
import com.frank.plate.util.CartUtils;
import com.frank.plate.util.DateUtil;
import com.frank.plate.util.MathUtil;
import com.frank.plate.util.String2Utils;
import com.frank.plate.util.ToastUtils;

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

    @BindView(R.id.rv_meal)
    RecyclerView rv_meal;

    @BindView(R.id.rv_servers)
    RecyclerView rv_servers;

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

    @BindView(R.id.tv_goods_price2)
    TextView tv_goods_price2;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;


    String car_number, moblie, user_name;

    int user_id, car_id;
    OrderInfoEntity infoEntity;
    SimpleGoodInfoAdpter simpleGoodInfoAdpter;
    SimpleServiceInfoAdpter simpleServiceInfoAdpter;
    SimpleMealInfoAdpter sma;
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

        simpleGoodInfoAdpter = new SimpleGoodInfoAdpter(cartUtils.getProductList(), true);

        simpleServiceInfoAdpter = new SimpleServiceInfoAdpter(cartUtils.getServerList(), false);

        sma = new SimpleMealInfoAdpter(cartUtils.getMealList());


        rv_goods.setLayoutManager(new LinearLayoutManager(this));
        rv_goods.setAdapter(simpleGoodInfoAdpter);

        rv_servers.setLayoutManager(new LinearLayoutManager(this));
        rv_servers.setAdapter(simpleServiceInfoAdpter);

        rv_meal.setLayoutManager(new LinearLayoutManager(this));
        rv_meal.setAdapter(sma);


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
                            cartUtils.addProductData(goodsEntities.get(position));
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
        user_id = new AppPreferences(this).getInt(Configure.user_id, 0);
        moblie = new AppPreferences(this).getString(Configure.moblie, "null_moblie");
        car_id = new AppPreferences(this).getInt(Configure.car_id, 0);
        user_name = new AppPreferences(this).getString(Configure.user_name, "null_user_name");


        infoEntity = new OrderInfoEntity(user_id, moblie, car_id, car_number, user_name);
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


                Intent intent = new Intent(this, ProductMealListActivity.class);
                intent.putExtra(Configure.user_id, user_id);
                intent.putExtra(Configure.isFixOrder, false);
                startActivity(intent);


                break;
            case R.id.but_meal_list:


                Intent intent2 = new Intent(this, ServeListActivity.class);
                intent2.putExtra(Configure.isShow, 1);
                intent2.putExtra(Configure.isFixOrder, false);
                startActivity(intent2);

                break;
            case R.id.but_to_technician_list:

                startActivityForResult(new Intent(this, TechnicianListActivity.class), new ResultBack() {
                    @Override
                    public void resultOk(Intent data) {
                        //to do what you want when resultCode == RESULT_OK
                        but_to_technician_list.setText("");
                        technicians = data.getParcelableArrayListExtra("Technician");
                        but_to_technician_list.setText(String2Utils.getString(technicians));
                    }
                });

                break;

            case R.id.but_set_date:


                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        but_set_date.setText(DateUtil.getFormatedDateTime2(date));
                        infoEntity.setPlanfinishi_time(date.getTime());
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setRangDate(DateUtil.getStartDate(), DateUtil.getEndDate())//起始终止年月日设定
                        .setTitleBgColor(getResources().getColor(R.color.appColor))//标题背景颜色 Night mode
                        .build();
                pvTime.show();

                break;

            case R.id.but_enter_order:

                onMakeOrder();
                break;


            case R.id.bto_top1:

                try {
                    if (!top_button1_pick) {

                        cartUtils.addProductData(goods_top.get(0));

                        top_button1_pick = true;
                        view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    } else {

                        cartUtils.reduceData(goods_top.get(0));
                        top_button1_pick = false;
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该商品不能选择");


                    top_button1_pick = false;
                }
                break;
            case R.id.bto_top2:


                try {
                    if (!top_button2_pick) {
                        cartUtils.addProductData(goods_top.get(1));
                        top_button2_pick = true;
                        view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    } else {

                        cartUtils.reduceData(goods_top.get(1));
                        top_button2_pick = false;
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该商品不能选择");
                    top_button2_pick = false;
                }
                break;
            case R.id.bto_top3:

                try {
                    if (!top_button3_pick) {

                        cartUtils.addProductData(goods_top.get(2));
                        top_button3_pick = true;
                        view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    } else {

                        cartUtils.reduceData(goods_top.get(2));
                        top_button3_pick = false;
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该商品不能选择");
                    top_button3_pick = false;
                }

                break;
            case R.id.bto_top4:
                try {
                    if (!top_button4_pick) {


                        cartUtils.addProductData(goods_top.get(3));
                        top_button4_pick = true;
                        view.setBackgroundColor(getResources().getColor(R.color.appColor));
                    } else {

                        cartUtils.reduceData(goods_top.get(3));
                        top_button4_pick = false;
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该商品不能选择");
                    top_button4_pick = false;
                }
                break;
        }
    }

    private void onMakeOrder() {
        if (cartUtils.isNull()) {
            ToastUtils.showToast("请最少选择一项商品或服务");
            return;
        }

        if (null == technicians || technicians.size() == 0) {
            ToastUtils.showToast("请最少选择一个技师");
            return;
        }


        infoEntity.setPostscript(et_postscript.getText().toString());
        infoEntity.setGoodsList(cartUtils.getProductList());
        infoEntity.setSkillList(cartUtils.getServerList());
        infoEntity.setUserActivityList(cartUtils.getMealList());
        infoEntity.setSysUserList(technicians);


        Log.e(TAG, "下单信息：" + infoEntity.toString());
        Api().submit(infoEntity).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo orderInfo) {
                ToastUtils.showToast("下单成功");
                sendOrderInfo(MakeOrderSuccessActivity.class, orderInfo);
                finish();
            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);
                ToastUtils.showToast("下单失败");
                finish();
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

        double goodsPrice = cartUtils.getProductPrice();
        double serverPrice = cartUtils.getServerPrice();
        double total = goodsPrice + serverPrice;

        tv_goods_price.setText("已选：￥" + MathUtil.twoDecimal(goodsPrice));
        tv_goods_price2.setText("已选：￥" + MathUtil.twoDecimal(serverPrice));


        tv_total_price.setText("已选：￥" + MathUtil.twoDecimal(total));

    }


    private void getTopData() {

        Api().shopeasyList().subscribe(new RxSubscribe<GoodsListEntity>(this, true) {
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
        simpleGoodInfoAdpter.setNewData(cartUtils.getProductList());
        setGoodsPric();
    }
}
