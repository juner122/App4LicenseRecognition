package com.eb.geaiche.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.eb.geaiche.view.ConfirmDialog3;
import com.eb.geaiche.view.ConfirmDialog4;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;
import com.eb.geaiche.MyApplication;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.SimpleGoodInfoAdpter;
import com.eb.geaiche.adapter.SimpleMealInfoAdpter;
import com.eb.geaiche.adapter.SimpleServiceInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsListEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.Technician;
import com.eb.geaiche.util.CartServerUtils;
import com.eb.geaiche.util.CartUtils;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.String2Utils;
import com.eb.geaiche.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf;


public class MakeOrderActivity extends BaseActivity {

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_make_order;
    }


    public static final String TAG = "MakeOrderActivity";
    @BindView(R.id.bto_top1)
    View view;

    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;
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

    @BindView(R.id.tv_top1)
    TextView tv_top1;

    @BindView(R.id.tv_top2)
    TextView tv_top2;

    @BindView(R.id.tv_top3)
    TextView tv_top3;

    @BindView(R.id.tv_top4)
    TextView tv_top4;

    @BindViews({R.id.tv_re1, R.id.tv_re2, R.id.tv_re3})
    public List<TextView> textViews;

    String car_number, moblie, user_name;

    int user_id, car_id;
    OrderInfoEntity infoEntity;
    SimpleGoodInfoAdpter simpleGoodInfoAdpter;
    SimpleServiceInfoAdpter simpleServiceInfoAdpter;
    SimpleMealInfoAdpter sma;


    List<Technician> technicians = new ArrayList<>();//选择员工列表

    List<GoodsEntity> goods_top;


    CartUtils cartUtils;

    String phone;//当前登录员工

    @Override
    protected void onResume() {
        super.onResume();

        simpleGoodInfoAdpter = new SimpleGoodInfoAdpter(cartUtils.getProductList(), true); //false 不显示加减按键

        simpleServiceInfoAdpter = new SimpleServiceInfoAdpter(cartUtils.getServerList(), false);

        sma = new SimpleMealInfoAdpter(cartUtils.getMealList());


        rv_goods.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_goods.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_goods.setAdapter(simpleGoodInfoAdpter);

        rv_servers.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv_servers.setAdapter(simpleServiceInfoAdpter);

        rv_meal.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
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

        //调整价格
        simpleGoodInfoAdpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                String price = simpleGoodInfoAdpter.getData().get(position).getRetail_price();
                int num = simpleGoodInfoAdpter.getData().get(position).getNumber();
                final ConfirmDialog4 confirmDialog = new ConfirmDialog4(MakeOrderActivity.this, price, num, true);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog4.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String price, int num) {
                        confirmDialog.dismiss();
                        //修改价数量
                        cartUtils.fixDataPrice(simpleGoodInfoAdpter.getData().get(position).getGoods_id(), price, num);
                        refreshData();
                    }

                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                    }
                });
            }
        });

        //调整价格
        simpleServiceInfoAdpter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final BaseQuickAdapter adapter, View view, final int position) {
                String price = simpleServiceInfoAdpter.getData().get(position).getRetail_price();
                int num = simpleServiceInfoAdpter.getData().get(position).getNumber();
                final ConfirmDialog4 confirmDialog = new ConfirmDialog4(MakeOrderActivity.this, price, num, false);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog4.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String price, int num) {
                        confirmDialog.dismiss();
                        //修改价数量
                        cartUtils.fixDataPrice(simpleServiceInfoAdpter.getData().get(position).getGoods_id(), price, num);
                        refreshData();
                    }

                    @Override
                    public void doCancel() {
                        // TODO Auto-generated method stub
                        confirmDialog.dismiss();
                    }
                });
            }
        });


        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(simpleServiceInfoAdpter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(rv_servers);


        // 开启滑动删除
        simpleServiceInfoAdpter.enableSwipeItem();
        simpleServiceInfoAdpter.setOnItemSwipeListener(onItemSwipeListener);

        refreshData();


    }

    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {


        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {


        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
            ToastUtils.showToast("删除成功！");

            cartUtils.reduceData(simpleServiceInfoAdpter.getData().get(pos));

            simpleServiceInfoAdpter.getData().remove(pos);
            simpleServiceInfoAdpter.notifyDataSetChanged();

            setGoodsPric();
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    };


    @Override
    protected void init() {
        cartUtils = MyApplication.cartUtils;

        tv_title.setText("下单信息");
        setRTitle("套卡下单");
        getTopData();
        car_number = new AppPreferences(this).getString(Configure.car_no, "null_car_no");
        user_id = new AppPreferences(this).getInt(Configure.user_id, 0);
        moblie = new AppPreferences(this).getString(Configure.moblie, "null_moblie");
        car_id = new AppPreferences(this).getInt(Configure.car_id, 0);
        user_name = new AppPreferences(this).getString(Configure.user_name, "null_user_name");


        infoEntity = new OrderInfoEntity(user_id, moblie, car_id, car_number, user_name);
        tv_car_no.setText(car_number);


        pickMap = new HashMap<>();


        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setOnClickListener(clickListener);
            textViews.get(i).setTag(i);
            pickMap.put(i, false);
        }

        phone = new AppPreferences(this).getString(Configure.moblie_s, "");
        sysuserList();
    }

    Map<Integer, Boolean> pickMap;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tip = ((TextView) view).getText().toString();
            if (!pickMap.get(view.getTag())) {//选中
                view.setBackgroundResource(R.drawable.button_background_b);
                ((TextView) view).setTextColor(Color.parseColor("#ffffff"));
                et_postscript.append(String.format("#%s#", tip));
                pickMap.put((Integer) view.getTag(), true);
            } else {//取消选中
                view.setBackgroundResource(R.drawable.button_background_z);
                ((TextView) view).setTextColor(Color.parseColor("#111011"));
                tip = String.format("#%s#", tip);
                cleanText(tip);
                pickMap.put((Integer) view.getTag(), false);
            }
        }
    };

    public void cleanText(String ct) {
        String re = et_postscript.getText().toString();
        et_postscript.setText(re.replace(ct, ""));

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    String iv_lpv_url = "";//签名图片 七牛云url

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Glide.with(this)
                .asDrawable()
                .load(Uri.fromFile(new File(Configure.LinePathView_url)))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(skipMemoryCacheOf(true))
                .into(iv_lpv);


        iv_lpv_url = intent.getStringExtra(Configure.Domain);

    }

    @OnClick({R.id.but_product_list, R.id.but_meal_list, R.id.but_to_technician_list, R.id.but_set_date, R.id.but_enter_order, R.id.bto_top1, R.id.bto_top2, R.id.bto_top3, R.id.bto_top4, R.id.tv_title_r, R.id.ll_autograph})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_product_list:


                Intent intent = new Intent(this, ProductMealListActivity.class);
                intent.putExtra(Configure.user_id, user_id);
                intent.putExtra(Configure.car_no, car_number);

                intent.putExtra(Configure.isFixOrder, false);
                startActivity(intent);


                break;
            case R.id.but_meal_list:


                Intent intent2 = new Intent(this, ServeListActivity.class);
                startActivity(intent2);

                break;
            case R.id.but_to_technician_list:


                Intent intent4 = new Intent(this, TechnicianListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Technician", (ArrayList<? extends Parcelable>) technicians);
                intent4.putExtras(bundle);
                startActivityForResult(intent4, new ResultBack() {
                    @Override
                    public void resultOk(Intent data) {
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
                        .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                        .setCancelColor(Color.WHITE)//取消按钮文字颜色
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
                    cartUtils.addProductData(goods_top.get(0));

                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该项不能选择");
                }
                break;
            case R.id.bto_top2:
                try {
                    cartUtils.addProductData(goods_top.get(1));

                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该项不能选择");
                }
                break;
            case R.id.bto_top3:

                try {
                    cartUtils.addProductData(goods_top.get(2));

                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该项不能选择");
                }

                break;
            case R.id.bto_top4:
                try {
                    cartUtils.addProductData(goods_top.get(3));
                    refreshData();
                } catch (Exception e) {
                    ToastUtils.showToast("该项不能选择");

                }
                break;

            case R.id.tv_title_r:


                Intent r = new Intent(this, ProductMealListActivity.class);
                r.putExtra(Configure.user_id, user_id);
                r.putExtra(Configure.car_no, car_number);
                r.putExtra("currentTab", 1);

                r.putExtra(Configure.isFixOrder, false);
                startActivity(r);

                break;

            case R.id.ll_autograph://签名

                //弹出对话框
                final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(this, getResources().getString(R.string.agreement), getResources().getString(R.string.agreement_title));
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        confirmDialog.dismiss();
                        toActivity(AutographActivity.class, "class", "MakeOrder");
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });

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
        infoEntity.setGoodsList(cartUtils.getAllGoods());//设置所有
        infoEntity.setUserActivityList(cartUtils.getMealList());
        infoEntity.setSysUserList(technicians);


        Log.e(TAG, "下单信息：" + infoEntity.toString());
        Api().submit(infoEntity).subscribe(new RxSubscribe<OrderInfo>(this, true) {
            @Override
            protected void _onNext(OrderInfo orderInfo) {
                ToastUtils.showToast("下单成功");


                //todo 以下是根据app内部或对外使用而要更换的代码  功能:对外使用app直接跳过订单确认页面而进入订单详情页面

//                //以下代码内部APP使用
//                sendOrderInfo(MakeOrderSuccessActivity.class, orderInfo);
//                finish();


                //以下代码为对外APP使用
                //直接开始服务
                beginServe(orderInfo.getOrderInfo());


            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);
                ToastUtils.showToast("下单失败");
                finish();
            }
        });
        carDestroy();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        carDestroy();
    }


    private void carDestroy() {
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
                try {
                    tv_top4.setText(goods_top.get(3).getGoods_name());
                    tv_top3.setText(goods_top.get(2).getGoods_name());
                    tv_top2.setText(goods_top.get(1).getGoods_name());
                    tv_top1.setText(goods_top.get(0).getGoods_name());
                } catch (Exception e) {

                }


            }

            @Override
            protected void _onError(String message) {
                Log.e(TAG, message);

            }
        });

    }

    private void refreshData() {
        simpleGoodInfoAdpter.setNewData(cartUtils.getProductList());
        simpleServiceInfoAdpter.setNewData(cartUtils.getServerList());
        setGoodsPric();
    }


    //获取员工列表
    private void sysuserList() {
        Api().sysuserList().subscribe(new RxSubscribe<BasePage<Technician>>(this, true) {
            @Override
            protected void _onNext(BasePage<Technician> t) {

                for (Technician technician : t.getList()) {

                    //查找当前登录员工的对象
                    if (technician.getMobile().equals(phone)) {
                        technicians.add(technician);
                        but_to_technician_list.setText(String2Utils.getString(technicians));
                        break;
                    }


                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);

            }
        });
    }


    //开始服务
    private void beginServe(final OrderInfoEntity infoEntity) {
        Api().beginServe(infoEntity.getId(), infoEntity.getOrder_sn(), iv_lpv_url).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                toMain(0);
                toActivity(OrderInfoActivity.class, Configure.ORDERINFOID, infoEntity.getId());

            }

            @Override
            protected void _onError(String message) {
                Log.d(TAG, message);
                ToastUtils.showToast(message);
            }
        });
    }
}
