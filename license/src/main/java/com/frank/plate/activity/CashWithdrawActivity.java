package com.frank.plate.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.UserBalanceAuthPojo;
import com.frank.plate.util.ToastUtils;


import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.OnClick;

public class CashWithdrawActivity extends BaseActivity {


    @BindView(R.id.card_num)
    TextView card_num;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.et)
    EditText et;

    float balance;

    @OnClick({R.id.tv_all, R.id.tv_action})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_all:

                et.setText(String.valueOf(balance));
                break;


            case R.id.tv_action:

                if (TextUtils.isEmpty(et.getText())) {
                    ToastUtils.showToast("金额不能为空！");
                    return;

                }

                Api().ask(new UserBalanceAuthPojo(Float.parseFloat(et.getText().toString()), new AppPreferences(CashWithdrawActivity.this).getInt("bank_id", 0))).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        ToastUtils.showToast("提现成功！");
                        finish();

                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast("提现失败！");
                    }
                });


                break;

        }


    }


    @Override
    protected void init() {
        tv_title.setText("申请提现");
        balance = new AppPreferences(CashWithdrawActivity.this).getFloat(Configure.Balance, 0f);
    }

    @Override
    protected void setUpView() {

        tv2.append(String.valueOf(balance));
        card_num.setText("");


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_cash_withdrawal;
    }
}
