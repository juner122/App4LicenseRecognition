package com.eb.new_line_seller.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.eb.new_line_seller.mvp.FixInfoDescribeActivity;
import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.adapter.CarListAdapter;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.QueryByCarEntity;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.eb.new_line_seller.util.ToastUtils;

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

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CarListAdapter carListAdapter = new CarListAdapter(null);

    static String car_number, mobile;
    List<CarInfoRequestParameters> cars = new ArrayList<>();

    String new_car_number;

    int user_id, car_id;

    @SuppressLint("CheckResult")
    @Override
    protected void init() {

        new_car_number = new AppPreferences(this).getString(Configure.car_no, "");
        tv_new_car_numb.setText(new_car_number);

        tv_check.setVisibility(View.GONE);
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
        tv_title.setText("会员信息录入");

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


    @OnClick({R.id.tv_enter_order, R.id.tv_add_car, R.id.tv_check, R.id.tv_fix})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_enter_order:
                if (TextUtils.isEmpty(name.getText())) {
                    ToastUtils.showToast("请输入车主姓名！");
                    return;
                }
                toMakeOrder(user_id, car_id, mobile, name.getText().toString(), car_number);
                break;

            case R.id.tv_fix://新增检修单

                if (TextUtils.isEmpty(name.getText())) {
                    ToastUtils.showToast("请输入车主姓名！");
                    return;
                }

                Intent intent2 = new Intent(this, FixInfoDescribeActivity.class);
                intent2.putExtra(Configure.car_no, car_number);
                intent2.putExtra(Configure.car_id, car_id);
                intent2.putExtra(Configure.user_name, name.getText().toString());
                intent2.putExtra(Configure.moblie, mobile);
                intent2.putExtra(Configure.user_id, user_id);

                startActivity(intent2);
                break;
            case R.id.tv_add_car:

                new AppPreferences(this).put(Configure.user_id, user_id);

                toActivity(CarInfoInputActivity.class, Configure.car_no, new_car_number);
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
                getAddUser();
                break;
        }

    }

    private void getAddUser() {
        Api().addUser(et_mobile.getText().toString(), name.getText().toString()).subscribe(new RxSubscribe<SaveUserAndCarEntity>(MemberInfoInputActivity.this, true) {
            @Override
            protected void _onNext(SaveUserAndCarEntity s) {

                if (s.getUser_id() == 0) {//录入不成功
                    Toast.makeText(MemberInfoInputActivity.this, "会员尚未注册，请完善注册信息", Toast.LENGTH_SHORT).show();
                    tv_check.setVisibility(View.VISIBLE);
                    name.setFocusable(true);

                } else {

                    if (null != s.getCarList() && s.getCarList().size() > 0) {//新车 老会员
                        Intent intent = new Intent(MemberInfoInputActivity.this, MemberManagementInfoActivity.class);
                        intent.putExtra(Configure.user_id, s.getUser_id());
                        intent.putExtra(Configure.car_no, new_car_number);
                        startActivity(intent);
                        finish();

                    } else {
                        //保存UserID
                        user_id = s.getUser_id();
                        name.setText(s.getUser_name());
                        mobile = et_mobile.getText().toString();
                        tv_check.setVisibility(View.GONE);
                        ll_car_list.setVisibility(View.VISIBLE);
                        carListAdapter.setNewData(s.getCarList());
                        initAdapter();
                    }
                }


            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(MemberInfoInputActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAdapter() {
        carListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                //查看更新车况
//                toActivity(CarInfoInputActivity.class, ((CarInfoRequestParameters) adapter.getData().get(position)).getId(), Configure.CARINFO);
                toActivity(CarInfoInputActivity.class, Configure.CARID, ((CarInfoRequestParameters) adapter.getData().get(position)).getId());

            }
        });
        carListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                ll.setVisibility(View.VISIBLE);
                car_number = carListAdapter.getData().get(position).getCarNo();
                car_id = carListAdapter.getData().get(position).getId();

                for (CarInfoRequestParameters c : cars) {
                    c.setSelected(false);
                }

                cars.get(position).setSelected(true);
                adapter.notifyDataSetChanged();


            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        memberOrderList(user_id);
        tv_new_car_numb.setVisibility(View.GONE);

    }

    private void memberOrderList(int user_id) {
        Api().memberOrderList(user_id).subscribe(new RxSubscribe<MemberOrder>(this, true) {
            @Override
            protected void _onNext(MemberOrder entity) {
                if (entity.getCarList().size() == 0)
                    return;

                cars = entity.getCarList();
                cars.get(0).setSelected(true);
                ll.setVisibility(View.VISIBLE);
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

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
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
        }
    };

}
