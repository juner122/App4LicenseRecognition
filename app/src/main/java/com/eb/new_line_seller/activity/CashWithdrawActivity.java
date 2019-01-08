package com.eb.new_line_seller.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.UserBalanceAuthPojo;
import com.eb.new_line_seller.util.ToastUtils;


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

    int bank_id;
    String band_num;

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

                Api().ask(new UserBalanceAuthPojo(Float.parseFloat(et.getText().toString()), bank_id)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
                    @Override
                    protected void _onNext(NullDataEntity nullDataEntity) {
                        ToastUtils.showToast("提现成功！");
                        finish();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast("提现失败:" + message);
                    }
                });
                break;
        }
    }

    @Override
    protected void init() {
        tv_title.setText("申请提现");
        balance = new AppPreferences(CashWithdrawActivity.this).getFloat(Configure.Balance, 0f);
        bank_id = new AppPreferences(CashWithdrawActivity.this).getInt("bank_id", 0);
        band_num = new AppPreferences(CashWithdrawActivity.this).getString("band_num", "");

    }

    @Override
    protected void setUpView() {

        tv2.append(String.valueOf(balance));
        card_num.setText(String.valueOf(band_num));


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_cash_withdrawal;
    }
}
