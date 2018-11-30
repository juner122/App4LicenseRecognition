package com.frank.plate.activity;

import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;

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

//        Api().getUserBalanceInfo(new MySubscriber<>(this, new SubscribeOnNextListener<MyBalanceEntity>() {
//            @Override
//            public void onNext(MyBalanceEntity data) {
//
//                tv_balance.setText(String.format("￥%s", MathUtil.twoDecimal(Double.valueOf(data.getBalance()))));
//                tv_in_applied.setText(String.format("%s元", MathUtil.twoDecimal(Double.valueOf(data.getAskMoney()))));
//                tv_forward.setText(String.format("%s元", MathUtil.twoDecimal(Double.valueOf(data.getAuthMoney()))));
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//        }));


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


                break;
        }


    }
}
