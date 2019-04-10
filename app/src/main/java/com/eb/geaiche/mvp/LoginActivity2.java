package com.eb.geaiche.mvp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.MainActivity;
import com.eb.geaiche.mvp.contacts.LoginContacts;
import com.eb.geaiche.mvp.presenter.LoginPtr;
import com.eb.geaiche.util.ToastUtils;
import com.google.gson.Gson;

import com.juner.mvp.bean.AppMenu;

import net.grandcentrix.tray.AppPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity2 extends BaseActivity<LoginContacts.LoginPtr> implements LoginContacts.LoginUI {

    public static final String action = "getcid";
    @BindView(R.id.btu_get_phone_code)
    TextView tv_code;

    @BindView(R.id.ll_hide)
    View ll_hide;

    @BindView(R.id.et_phone)
    EditText et_phone;


    @BindView(R.id.et_car_code)
    EditText et_car_code;
    String cid;


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.but_login, R.id.btu_get_phone_code})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_login:
                //登录
                cid = new AppPreferences(getApplicationContext()).getString("cid", "");

                if (null == cid || "".equals(cid)) {
                    ToastUtils.showToast("数据准备中,请稍等...");
                    return;
                }
                getPresenter().login(et_phone.getText().toString(), et_car_code.getText().toString(), cid);
                break;
            case R.id.btu_get_phone_code:
                //发送短信
                getPresenter().smsSendSms(et_phone.getText().toString());
                break;
        }
    }

    @Override
    protected void init() {
        hideHeadView();

        //注册广播
        registBroadcast();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cid = new AppPreferences(getApplicationContext()).getString("cid", "");
        if (null == cid || "".equals(cid)) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }


    }

    @Override
    public LoginContacts.LoginPtr onBindPresenter() {
        return new LoginPtr(this);
    }

    @Override
    public void loginSuccess(List<AppMenu> list) {
        showToast("登录成功");
        Gson gson = new Gson();
        new AppPreferences(this).put("AppMenu", gson.toJson(list));
        toActivity(MainActivity.class);
        finish();
    }


    @Override
    public void loginFailure(String masage) {
        showToast(masage);

    }

    @Override
    public void countDown(String cou) {
        tv_code.setText(cou);
    }

    @Override
    public void setCodeViewClickable(boolean isClickable) {
        tv_code.setClickable(isClickable);
    }


    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().stopCountDown();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void registBroadcast() {
        //实例化广播对象
        broadcastReceiver = new MyBroadcastReceiver();
        //实例化广播过滤器，只拦截指定的广播
        IntentFilter filter = new IntentFilter(action);
        //注册广播
        this.registerReceiver(broadcastReceiver, filter);

    }


    MyBroadcastReceiver broadcastReceiver; //个推广播接收器

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //没接收到一次广播就执行一次方法
            ll_hide.setVisibility(View.GONE);
        }
    }

}
