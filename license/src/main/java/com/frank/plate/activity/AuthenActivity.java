package com.frank.plate.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.Card;
import com.frank.plate.bean.NullDataEntity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class AuthenActivity extends BaseActivity {
    Disposable smsDisposable;

    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.et4)
    EditText et4;
    @BindView(R.id.et5)
    EditText et5;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.tv_code)
    TextView tv_code;

    Card card;

    @Override
    protected void init() {
        tv_title.setText("我的认证");
    }


    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {


    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_authen;
    }

    @OnClick({R.id.but_enter, R.id.tv_code})
    public void onclick(View v) {

        switch (v.getId()) {

            case R.id.but_enter:

                auth();
                break;

            case R.id.tv_code:
                if (TextUtils.isEmpty(et_phone.getText())) {
                    Toast.makeText(AuthenActivity.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = et_phone.getText().toString();
                smsDisposable = Api().smsSendSms(phone, 3, tv_code, AuthenActivity.this);

                break;
        }

    }

    private void auth() {
        card = new Card();
        card.setBankAddr(et3.getText().toString());
        card.setBankName(et2.getText().toString());
        card.setCardNumber(et1.getText().toString());
        card.setCardholder(et4.getText().toString());


        Api().bankSave(et5.getText().toString(), card).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                Toast.makeText(AuthenActivity.this, "认证成功！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(AuthenActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (smsDisposable != null)
            smsDisposable.dispose();
    }
}
