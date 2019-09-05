package com.eb.geaiche.activity;


import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.eb.geaiche.MyApplication;
import com.eb.geaiche.mvp.FixInfoDescribeActivity;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.view.ConfirmDialog;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CarListAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.eb.geaiche.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberInfoInputActivity extends BaseActivity {
    public final static String TAG = "queryByCarEntity";


    @BindView(R.id.et_user_mobile)
    EditText et_mobile;
    @BindView(R.id.et_user_name)
    EditText name;
    @BindView(R.id.tv_e1)
    TextView tv_e1;
    @BindView(R.id.tv_e2)
    TextView tv_e2;
    @BindView(R.id.tv_check)
    TextView tv_check;

    @BindView(R.id.tv_new_car_numb)
    TextView tv_new_car_numb;
    @BindView(R.id.ll_car_list)
    View ll_car_list;

    @BindView(R.id.ll)
    View ll;

    @BindView(R.id.ll_name)
    View ll_name;//用户名

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CarListAdapter carListAdapter = new CarListAdapter(null);

    static String car_number, mobile;
    List<CarInfoRequestParameters> cars = new ArrayList<>();

    String new_car_number;

    int user_id, car_id, new_car_id;
    String car_vin, mileage;//下单要判断为空;

    @SuppressLint("CheckResult")
    @Override
    protected void init() {

//        if (MyAppPreferences.getShopType()) {
//            ll_name.setVisibility(View.GONE);//用户名输入框
//            tv_check.setVisibility(View.GONE);//注册用户按钮
//        } else
//            ll_name.setVisibility(View.VISIBLE);


        new_car_id = getIntent().getIntExtra("new_car_id", 0);
        new_car_number = new AppPreferences(this).getString(Configure.car_no, "");
        tv_new_car_numb.setText(new_car_number);


        ll_car_list.setVisibility(View.GONE);
        //测试

        carListAdapter = new CarListAdapter(cars);

        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (editable.length() >= 11) {
                    getAddUser();
                }
            }
        });
        et_mobile.setOnFocusChangeListener(mOnFocusChangeListener);
        name.setOnFocusChangeListener(mOnFocusChangeListener);


    }

    @Override
    protected void setUpView() {
        tv_title.setText("新增会员");
//        setRTitle("匿名注册");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carListAdapter);
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_member_info_input;
    }


    @OnClick({R.id.tv_enter_order, R.id.tv_add_car, R.id.tv_check, R.id.tv_fix, R.id.tv_title_r})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_enter_order:
//                if (TextUtils.isEmpty(name.getText())) {
//                    ToastUtils.showToast("请输入车主姓名！");
//                    return;
//                }

                toActivity(1);

                break;

            case R.id.tv_fix://新增检修单
                toActivity(0);

                break;
            case R.id.tv_add_car:

                new AppPreferences(MemberInfoInputActivity.this).put(Configure.user_id, user_id);
                Intent intent3 = new Intent(MemberInfoInputActivity.this, CarInfoInputActivity.class);
                intent3.putExtra(Configure.car_no, new_car_number);
                intent3.putExtra("new_car_id", new_car_id);
                startActivity(intent3);

                break;

            case R.id.tv_check:

                if (TextUtils.isEmpty(et_mobile.getText()) || TextUtils.isEmpty(name.getText())) {
                    ToastUtils.showToast("请填写完整信息!");
                    return;
                }
                if (et_mobile.getText().length() < 11) {
                    ToastUtils.showToast("请填写正确手机号码!");
                    return;
                }
                String name_s = name.getText().toString();
                String mobile_s = et_mobile.getText().toString();

                final ConfirmDialog confirmDialog = new ConfirmDialog(this, name_s, mobile_s);
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String new_name) {
                        remakeName(new_name, mobile_s);
                        confirmDialog.cancel();


                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.cancel();
                    }
                });

                break;

            case R.id.tv_title_r:
                //弹出对话框
                final ConfirmDialogCanlce c2 = new ConfirmDialogCanlce(this, "匿名注册您将失去该用户的联系！\n请谨慎使用匿名注册功能！", "重要提示！", "匿名注册", "去完善注册信息");
                c2.show();
                c2.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        c2.dismiss();
                    }

                    @Override
                    public void doCancel() {
                        c2.dismiss();
                        getAddUser();

                    }
                });
                break;
        }

    }

    private void getAddUser() {
        String userName;
        if (TextUtils.isEmpty(name.getText())) {//快速用车牌号码+车主作为用户名云注册用户
            userName = new_car_number + "车主";
        } else {
            userName = name.getText().toString();
        }


        Api().addUser(et_mobile.getText().toString(), userName).subscribe(new RxSubscribe<SaveUserAndCarEntity>(MemberInfoInputActivity.this, true) {
            @Override
            protected void _onNext(SaveUserAndCarEntity s) {

                if (s.getUser_id() == 0) {//录入不成功
                    Toast.makeText(MemberInfoInputActivity.this, "会员尚未注册，请完善注册信息", Toast.LENGTH_SHORT).show();
                    tv_check.setVisibility(View.VISIBLE);
                    name.setFocusable(true);

                } else {
                    et_mobile.setFocusable(false);

                    if (null != s.getCarList() && s.getCarList().size() > 0) {//新车 老会员
                        Intent intent = new Intent(MemberInfoInputActivity.this, MemberManagementInfoActivity.class);
                        intent.putExtra(Configure.user_id, s.getUser_id());
                        intent.putExtra(Configure.car_no, new_car_number);
                        intent.putExtra("new_car_id", new_car_id);
                        startActivity(intent);
                        finish();

                    } else {
                        //保存UserID
                        user_id = s.getUser_id();
                        name.setText(s.getUser_name());
                        mobile = et_mobile.getText().toString();
                        tv_check.setVisibility(View.VISIBLE);
                        ll_car_list.setVisibility(View.VISIBLE);
                        ll_name.setVisibility(View.VISIBLE);
                        carListAdapter.setNewData(s.getCarList());
                        initAdapter();

                        new AppPreferences(MemberInfoInputActivity.this).put(Configure.user_id, user_id);
                        Intent intent3 = new Intent(MemberInfoInputActivity.this, CarInfoInputActivity.class);
                        intent3.putExtra(Configure.car_no, new_car_number);
                        intent3.putExtra("new_car_id", new_car_id);
                        startActivity(intent3);


                    }
                }


            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(MemberInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //修改用户名
    public void remakeName(String name_s, String mobile) {

        Api().remakeName(user_id, name_s, mobile).subscribe(new RxSubscribe<NullDataEntity>(MemberInfoInputActivity.this, true) {

            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("修改成功！");

                name.setText(name_s);


            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);

            }
        });

    }


    private void initAdapter() {
        carListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            //查看更新车况

            toActivity(CarInfoInputActivity.class, Configure.CARID, ((CarInfoRequestParameters) adapter.getData().get(position)).getId());

        });
        carListAdapter.setOnItemClickListener((adapter, view, position) -> {


            setCarInfo(carListAdapter.getData().get(position).getCarNo(), carListAdapter.getData().get(position).getId(), carListAdapter.getData().get(position).getVin(), carListAdapter.getData().get(position).getMileage());

            for (CarInfoRequestParameters c : cars) {
                c.setSelected(false);
            }

            cars.get(position).setSelected(true);
            adapter.notifyDataSetChanged();


        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        memberOrderList(user_id);
        tv_new_car_numb.setVisibility(View.GONE);

    }

    private void setCarInfo(String number, int id, String vin, String mileage) {
        ll.setVisibility(View.VISIBLE);
        car_number = number;
        car_id = id;
        car_vin = vin;
        this.mileage = mileage;
    }

    private void memberOrderList(int user_id) {
        Api().memberOrderList(user_id).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder entity) {
                if (entity.getCarList().size() == 0)
                    return;

                cars = entity.getCarList();
                cars.get(0).setSelected(true);
                setCarInfo(cars.get(0).getCarNo(), cars.get(0).getId(), cars.get(0).getVin(), cars.get(0).getMileage());

                carListAdapter.setNewData(cars);
            }

            @Override
            protected void _onError(String message) {

                Log.d(TAG, message);
                ToastUtils.showToast(message);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new AppPreferences(getApplicationContext()).put(Configure.car_no, "");
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = (v, hasFocus) -> {
        EditText textView = (EditText) v;
        String hint;
        if (hasFocus) {
            hint = textView.getHint().toString();
            textView.setTag(hint);
            textView.setHint("");
        } else {
            hint = textView.getTag().toString();
            textView.setHint(hint);
        }
    };


    private void toActivity(int way) {
        if (car_id == 0) {
            ToastUtils.showToast("请选择一辆车！");
            return;
        }
        if (!MyAppPreferences.getShopType()) {//直营版
            if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(et_mobile.getText().toString())) {
                ToastUtils.showToast("请完善用户信息！");
                return;
            }
            if (null == car_vin) {
                ToastUtils.showToast("请完善车辆信息,缺少车架号！");
                return;
            }
            if (null == mileage || "".equals(mileage)) {
                ToastUtils.showToast("请完善车辆里程数信息！");
                return;

            }
        }

        plateUpdate();

        if (way == 0) {//检修下单
            Intent intent2 = new Intent(MemberInfoInputActivity.this, FixInfoDescribeActivity.class);
            intent2.putExtra(Configure.car_no, car_number);
            intent2.putExtra(Configure.car_id, car_id);
            intent2.putExtra(Configure.user_name, name.getText().toString());
            intent2.putExtra(Configure.moblie, mobile);
            intent2.putExtra(Configure.user_id, user_id);
            startActivity(intent2);
        } else {
            toMakeOrder(user_id, car_id, mobile, name.getText().toString(), car_number, mileage);
        }

    }

    String plateId;//自动识别车辆的进店队列id

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
