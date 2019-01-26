package com.eb.new_line_seller.mvp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.CarInfoInputActivity;
import com.eb.new_line_seller.mvp.contacts.FixInfoContacts;
import com.eb.new_line_seller.mvp.presenter.FixInfoPtr;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfoEntity;

import butterknife.BindView;
import butterknife.OnClick;


public class FixInfoActivity extends BaseActivity<FixInfoContacts.FixInfoPtr> implements FixInfoContacts.FixInfoUI {


    @BindView(R.id.tv_car_no)
    TextView tv_car_no;

    @BindView(R.id.tv_fix_sn)
    TextView tv_fix_sn;


    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    @BindView(R.id.tv_consignee)
    TextView tv_consignee;

    @BindView(R.id.tv_new_order)
    TextView tv_new_order;


    @BindView(R.id.tv_save)
    TextView tv_save;

    @BindView(R.id.tv_text)
    TextView tv_text;//总价


    @BindView(R.id.tv_dec)
    EditText tv_dec;//车况描述


    @BindView(R.id.tv_price1)
    TextView tv_price1;//工时小计


    @BindView(R.id.tv_price2)
    TextView tv_price2;//配件


    @BindView(R.id.iv_add1)
    ImageView iv_add1;//按钮1


    @BindView(R.id.iv_add2)
    ImageView iv_add2;//


    @BindView(R.id.rv)
    RecyclerView rv;//工时

    @BindView(R.id.rv2)
    RecyclerView rv2;//服务


    @OnClick({R.id.iv_add1, R.id.iv_add2, R.id.tv_new_order, R.id.tv_car_info, R.id.tv_save, R.id.tv_fix_dec})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add1:
                //添加工时
                toActivity(FixPickServiceActivity.class);
                break;

            case R.id.iv_add2:
                //添加配件
                toActivity(FixPickPartsActivity.class);
                break;


            case R.id.tv_new_order:
                //生成估价单
                getPresenter().onInform();
                break;

            case R.id.tv_save:
                //保存退出
                getPresenter().remakeSave();
                break;
            case R.id.tv_car_info:
                //查看车况
                getPresenter().toCarInfoActivity();
                break;

            case R.id.tv_fix_dec:
                //修改备注
                tv_dec.setFocusableInTouchMode(true);
                tv_dec.setFocusable(true);
                tv_dec.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tv_dec,0);

                break;
        }
    }


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info;
    }

    @Override
    protected void init() {
        tv_title.setText("汽车检修单");
        getPresenter().initRecyclerView(rv, rv2);
        getPresenter().getInfo();
    }


    @Override
    public FixInfoContacts.FixInfoPtr onBindPresenter() {
        return new FixInfoPtr(this);
    }

    @Override
    public void setInfo(FixInfoEntity fixInfo) {

        tv_car_no.setText(fixInfo.getCarNo());
        tv_fix_sn.setText("单号：" + fixInfo.getQuotationSn());
        tv_dec.setText(fixInfo.getDescribe());
        tv_mobile.setText(fixInfo.getMobile());
        tv_consignee.setText(fixInfo.getUserName());


    }

    @Override
    public void createOrderSuccess(int i) {
        ToastUtils.showToast("生成成功！");
        finish();

        toMain(i);
    }

    @Override
    public void setServicePrice(String price) {
        tv_price1.setText("金额小计：￥" + MathUtil.twoDecimal(price));

    }

    @Override
    public void setPartsPrice(String price) {
        tv_price2.setText("金额小计：￥" + MathUtil.twoDecimal(price));


    }

    @Override
    public void setAllPrice(String price) {
        tv_text.setText("总价：￥" + MathUtil.twoDecimal(price));
    }

    @Override
    public void showAddButton() {
        iv_add1.setVisibility(View.VISIBLE);
        iv_add2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        iv_add1.setVisibility(View.INVISIBLE);
        iv_add2.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setButtonText(String text) {
        tv_new_order.setText(text);
    }

    @Override
    public void onToCarInfoActivity(int car_id) {


        Intent intent = new Intent(this, CarInfoInputActivity.class);
        intent.putExtra(Configure.CARID, car_id);
        intent.putExtra("result_code", 999);
        startActivity(intent);
    }

    @Override
    public void showSaveButton() {
        tv_save.setVisibility(View.VISIBLE);
    }

    @Override
    public String getDec() {
        return tv_dec.getText().toString();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().handleCallback(intent);
    }
}
