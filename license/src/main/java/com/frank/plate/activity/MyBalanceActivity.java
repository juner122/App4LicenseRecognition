package com.frank.plate.activity;

import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.util.MathUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MyBalanceActivity extends BaseActivity {


    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_in_applied)
    TextView tv_in_applied;
    @BindView(R.id.tv_forward)
    TextView tv_forward;


    @Override
    protected void init() {
        tv_title.setText("我的余额");

        Api().balanceInfo().subscribe(new RxSubscribe<MyBalanceEntity>(this, true) {
            @Override
            protected void _onNext(MyBalanceEntity data) {
                tv_balance.setText(String.format("￥%s", MathUtil.twoDecimal(Double.valueOf(data.getBalance()))));
                tv_in_applied.setText(String.format("%s元", MathUtil.twoDecimal(Double.valueOf(data.getAskMoney()))));
                tv_forward.setText(String.format("%s元", MathUtil.twoDecimal(Double.valueOf(data.getAuthMoney()))));
            }

            @Override
            protected void _onError(String message) {

            }
        });


    }

    @Override
    protected void setUpView() {


    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_my_balance;
    }


    @OnClick({R.id.tv_action_applied, R.id.ll_bill_list})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_bill_list:
                toActivity(BillListActivity.class);

                break;
            case R.id.tv_action_applied:


                toActivity(CashWithdrawActivity.class);

                break;
        }


    }
}
