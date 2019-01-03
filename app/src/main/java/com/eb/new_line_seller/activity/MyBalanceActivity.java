package com.eb.new_line_seller.activity;

import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.RxSubscribe;
import com.eb.new_line_seller.bean.BankList;
import com.eb.new_line_seller.bean.MyBalanceEntity;
import com.eb.new_line_seller.util.ToastUtils;

import net.grandcentrix.tray.AppPreferences;

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        Api().balanceInfo().subscribe(new RxSubscribe<MyBalanceEntity>(this, true) {
            @Override
            protected void _onNext(MyBalanceEntity data) {

                tv_balance.setText(String.format("￥%s", data.getBalanceDouble()));

                tv_in_applied.setText(String.format("%s元", data.getAskMoney()));
                tv_forward.setText(String.format("%s元", data.getAuthMoney()));

                new AppPreferences(MyBalanceActivity.this).put(Configure.Balance, data.getBalancefloat());

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("获取余额失败:" + message);
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
                toActivity(BillListActivity.class, "isShowAll", 1);

                break;
            case R.id.tv_action_applied:

                Api().bankList().subscribe(new RxSubscribe<BankList>(this, true) {
                    @Override
                    protected void _onNext(BankList bankList) {
                        if (bankList.getList().size() > 0 && bankList.getList().get(0).getType() == 2) {

                            new AppPreferences(MyBalanceActivity.this).put("bank_id", bankList.getList().get(0).getId());
                            new AppPreferences(MyBalanceActivity.this).put("band_num", bankList.getList().get(0).getBankNum());
                            toActivity(CashWithdrawActivity.class);
                        } else {
                            toActivity(AuthenActivity.class);
                        }

                    }

                    @Override
                    protected void _onError(String message) {

                        ToastUtils.showToast(message);
                    }
                });

                break;
        }


    }
}
