package com.eb.geaiche.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.adapter.FixInfoListAdapter;
import com.eb.geaiche.mvp.ActivateCardActivity;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.mvp.FixInfoDescribeActivity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;
import com.eb.geaiche.view.ConfirmDialogReMakeName;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.OrderList2Adapter;
import com.eb.geaiche.adapter.SimpleCarInfoAdpter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;

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

    @BindView(R.id.rv3)
    RecyclerView rv3;


    SimpleCarInfoAdpter adpter1;
    OrderList2Adapter adapter2;//   订单
    FixInfoListAdapter fixAdapter;//检修单

    String car_number = "", new_car_number, moblie, user_name;

    int user_id, car_id, new_car_id;


    List<CarInfoRequestParameters> cars = new ArrayList<>();


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"订单历史", "检修单历史"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    @BindView(R.id.tv_new_car_numb)
    TextView tv_new_car_numb;


    @Override
    protected void init() {

        user_id = getIntent().getIntExtra(Configure.user_id, 0);
        new_car_id = getIntent().getIntExtra("new_car_id", 0);
        new_car_number = getIntent().getStringExtra(Configure.car_no);
        tv_new_car_numb.setText(new_car_number);


        new AppPreferences(this).put(Configure.user_id, user_id);


        tv_title.setText("会员信息");
        IvRSetSrc(R.mipmap.icon_onpe3);

        adpter1 = new SimpleCarInfoAdpter(cars);
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adpter1);

        adapter2 = new OrderList2Adapter(null);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adapter2);


        fixAdapter = new FixInfoListAdapter(R.layout.item_fragment2_main, null, this.getBaseContext());
        rv3.setLayoutManager(new LinearLayoutManager(this));
        rv3.setAdapter(fixAdapter);

        adapter2.setEmptyView(R.layout.order_list_empty_view, rv2);
        fixAdapter.setEmptyView(R.layout.fix_list_empty_view, rv3);


        adpter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                car_number = cars.get(position).getCarNo();
                car_id = cars.get(position).getId();

                for (CarInfoRequestParameters c : cars) {
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
                Intent intent = new Intent(MemberManagementInfoActivity.this, CarInfoInputActivity.class);
                intent.putExtra("result_code", 1);
                intent.putExtra(Configure.CARID, ((CarInfoRequestParameters) adapter.getData().get(position)).getId());
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

        fixAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toActivity(FixInfoActivity.class, "id", ((FixInfoEntity) adapter.getData().get(position)).getId());
            }
        });


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    rv2.setVisibility(View.VISIBLE);
                    rv3.setVisibility(View.GONE);
                } else {
                    rv3.setVisibility(View.VISIBLE);
                    rv2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
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
                name.setText(user_name);

                cars = memberOrder.getCarList();

                if (cars.size() == 0) {
                    return;
                }


                if (!"".equals(new_car_number)) {
                    for (int i = 0; i < cars.size(); i++) {
                        if (cars.get(i).getCarNo().equals(new_car_number)) {
                            cars.get(i).setSelected(true);//设置车为选中
                            car_number = memberOrder.getCarList().get(i).getCarNo();
                            car_id = memberOrder.getCarList().get(i).getId();

                            new_car_number = "";//清空
                            tv_new_car_numb.setVisibility(View.INVISIBLE);
                            break;
                        }
                    }
                }

                adpter1.setNewData(cars);
                adapter2.setNewData(memberOrder.getOrderList());
                fixAdapter.setNewData(memberOrder.getFixInfoEntities());
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

    @OnClick({R.id.tv_new_order, R.id.tv_add_car, R.id.tv_fix_order, R.id.ll_change_name, R.id.tv_iv_r})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_new_order:
                if (car_id == 0) {
                    ToastUtils.showToast("请选择一辆车！");
                    return;
                }
                toMakeOrder(user_id, car_id, moblie, user_name, car_number);

                break;

            case R.id.tv_fix_order:

                if (car_id == 0) {
                    ToastUtils.showToast("请选择一辆车！");
                    return;
                }
                Intent intent2 = new Intent(this, FixInfoDescribeActivity.class);
                intent2.putExtra(Configure.car_no, car_number);
                intent2.putExtra(Configure.car_id, car_id);
                intent2.putExtra(Configure.user_name, user_name);
                intent2.putExtra(Configure.moblie, moblie);
                intent2.putExtra(Configure.user_id, user_id);

                startActivity(intent2);
                break;
            case R.id.tv_add_car:
                Intent intent3 = new Intent(this, CarInfoInputActivity.class);
                intent3.putExtra(Configure.car_no, new_car_number);
                intent3.putExtra("result_code", 1);
                intent3.putExtra("new_car_id", new_car_id);
                startActivity(intent3);

                break;
            case R.id.ll_change_name://修改用户名

                //弹出对话框
                final ConfirmDialogReMakeName confirmDialog = new ConfirmDialogReMakeName(this);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialogReMakeName.ClickListenerInterface() {

                    @Override
                    public void doConfirm(final String str) {
                        confirmDialog.dismiss();

                        Api().remakeUserName(str, user_id).subscribe(new RxSubscribe<NullDataEntity>(MemberManagementInfoActivity.this, true) {

                            @Override
                            protected void _onNext(NullDataEntity entity) {
                                ToastUtils.showToast("修改成功！");
                                name.setText(str);
                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtils.showToast(message);
                            }
                        });

                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }

                });
                break;
            case R.id.tv_iv_r:
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_member_management_member_info_onpe_car, null);

                Button b1 = ll.findViewById(R.id.button_1);
                Button b2 = ll.findViewById(R.id.button_2);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //有效套卡
                        Intent i = new Intent(MemberManagementInfoActivity.this, ProductMealActivity.class);
                        i.putExtra(Configure.user_id, user_id);
                        i.putExtra(Configure.car_no, car_number);
                        startActivity(i);

                        popupWindow.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //会员开卡
                        Intent intent = new Intent(MemberManagementInfoActivity.this, ActivateCardActivity.class);
                        intent.putExtra(Configure.moblie, moblie);
                        intent.putExtra(Configure.user_name, user_name);
                        startActivity(intent);
                        popupWindow.dismiss();
                    }
                });
                //功能按钮
                popupWindow = new CommonPopupWindow.Builder(this)
                        .setView(ll)
                        .create();
                popupWindow.showAsDropDown(v, 0, 0);


                break;
        }
    }

    CommonPopupWindow popupWindow;

    private void reCarListInfo(Intent intent) {

        startActivityForResult(new Intent(intent), new ResultBack() {
            @Override
            public void resultOk(Intent data) {
                //to do what you want when resultCode == RESULT_OK
                memberOrderList();

            }
        });

    }

    class TabEntity implements CustomTabEntity {
        public String title;

        public TabEntity(String title) {
            this.title = title;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return 0;
        }

        @Override
        public int getTabUnselectedIcon() {
            return 0;
        }

    }
}