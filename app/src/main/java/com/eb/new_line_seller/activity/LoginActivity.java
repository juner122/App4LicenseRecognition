package com.eb.new_line_seller.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.Token;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";
    @BindView(R.id.btu_get_phone_code)
    protected TextView tv_code;

    @BindView(R.id.et_phone)
    EditText et_phone;


    @BindView(R.id.et_car_code)
    EditText et_car_code;


    Disposable smsDisposable;

    @Override
    protected void init() {
        super.hideHeadView();
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.but_login, R.id.btu_get_phone_code})
    public void onClick(View view) {
        if (TextUtils.isEmpty(et_phone.getText())) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        final String phone = et_phone.getText().toString();

        switch (view.getId()) {
            case R.id.btu_get_phone_code:

                smsDisposable = Api().smsSendSms(phone, 1, tv_code, LoginActivity.this);
                break;

            case R.id.but_login:

                if (TextUtils.isEmpty(et_car_code.getText())) {
                    Toast.makeText(LoginActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = et_car_code.getText().toString();
                Api().login(phone, code).subscribe(new RxSubscribe<Token>(LoginActivity.this, true) {
                    @Override
                    protected void _onNext(Token token) {
                        new AppPreferences(LoginActivity.this).put(Configure.Token, token.getToken().getToken());
                        new AppPreferences(LoginActivity.this).put(Configure.moblie, phone);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        toActivity(MainActivity.class);
                        finish();
                    }

                    @Override
                    protected void _onError(String message) {
                        Log.e(TAG, message);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });


                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != smsDisposable)
            smsDisposable.dispose();
    }
}
