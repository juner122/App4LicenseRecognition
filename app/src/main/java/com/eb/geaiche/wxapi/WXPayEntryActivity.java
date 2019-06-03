package com.eb.geaiche.wxapi;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.BaseActivity;
import com.eb.geaiche.activity.MallMakeOrderInfoActivity;
import com.eb.geaiche.util.MyAppPreferences;
import com.juner.mvp.Configure;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    @Override
    public int setLayoutResourceID() {
        return R.layout.pay_result;
    }

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;


    @BindView(R.id.price)
    TextView price;//实付金额

    @BindView(R.id.pay_status)
    TextView pay_status;//

    @BindView(R.id.prepayid)
    TextView prepayid;//交易流水号

    @BindView(R.id.timestamp)
    TextView timestamp;//支付时间


    @OnClick({R.id.back_home})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_home://
                finish();
//                toActivity(MallMakeOrderInfoActivity.class, Configure.ORDERINFOID, MyAppPreferences.getInt(Configure.ORDERINFOID));
                break;
        }
    }

    @Override
    protected void init() {

        api = WXAPIFactory.createWXAPI(this, Configure.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void setUpView() {
        tv_title.setText("订单详情");
    }

    @Override
    protected void setUpData() {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG, "onPayFinish, errCode = " + baseResp.errCode);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.i("WXPayEntryActivity",
                    "onPayFinish, errCode = " + baseResp.errCode
                            + ", errStr: " + baseResp.errStr
                            + ", openId: " + baseResp.openId
                            + ", transaction: " + baseResp.transaction);
            int errCord = baseResp.errCode;

            switch (errCord) {
                case 0://成功
                    onPaySuccess();
                    break;
                case -1:// 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                    onPayFail1();
                    break;
                case -2://用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
                    onPayFail2();
                    break;
            }

        }
    }

    //支付成功
    private void onPaySuccess() {
        price.setText(String.format("￥%s", MyAppPreferences.getString(Configure.WXPay_PRICE)));
        pay_status.setText("支付成功");
        prepayid.setText(String.format("交易流水号:  %s", MyAppPreferences.getString(Configure.WXPay_SN)));
        timestamp.setText(String.format("交易时间:  %s", MyAppPreferences.getString(Configure.WXPay_TIME)));
    }    //支付失败-1

    private void onPayFail1() {
        price.setText(String.format("￥%s", MyAppPreferences.getString(Configure.WXPay_PRICE)));
        pay_status.setText("支付失败，请联系管理员");
        prepayid.setText(String.format("交易流水号:  %s", MyAppPreferences.getString(Configure.WXPay_SN)));
        timestamp.setText(String.format("交易时间:  %s", MyAppPreferences.getString(Configure.WXPay_TIME)));
    }    //支付失败-2

    private void onPayFail2() {
        price.setText(String.format("￥%s", MyAppPreferences.getString(Configure.WXPay_PRICE)));
        pay_status.setText("支付失败，用户取消支付");
        prepayid.setText(String.format("交易流水号:  %s", MyAppPreferences.getString(Configure.WXPay_SN)));
        timestamp.setText(String.format("交易时间:  %s", "-"));
    }
}
