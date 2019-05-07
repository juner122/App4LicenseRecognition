package com.eb.geaiche.mvp;


import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eb.geaiche.R;

import com.eb.geaiche.activity.CarCheckResultListActivity;
import com.eb.geaiche.activity.CarInfoInputActivity;
import com.eb.geaiche.activity.MainActivity;
import com.eb.geaiche.mvp.contacts.FixInfoContacts;
import com.eb.geaiche.mvp.presenter.FixInfoPtr;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.FixInfoEntity;

import butterknife.BindView;
import butterknife.OnClick;


public class FixInfoActivity extends BaseActivity<FixInfoContacts.FixInfoPtr> implements FixInfoContacts.FixInfoUI {


    @BindView(R.id.ll_to_car_check)
    View ll_to_car_check;


    @BindView(R.id.tv_car_no)
    TextView tv_car_no;

    @BindView(R.id.tv_fix_sn)
    TextView tv_fix_sn;
    @BindView(R.id.tv_fix_time)
    TextView tv_fix_time;


    @BindView(R.id.tv_mobile)
    TextView tv_mobile;

    @BindView(R.id.tv_consignee)
    TextView tv_consignee;

    @BindView(R.id.tv_new_order)
    TextView tv_new_order;


    @BindView(R.id.ll_car_fix_list)
    View ll_car_fix_list;


    @BindView(R.id.tv_save)
    TextView tv_save;//保存退出


    @BindView(R.id.tv_post_fix)
    TextView tv_post_fix;//提交修改
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


    String car_no;
    int car_id;

    @OnClick({R.id.iv_add1, R.id.iv_add2, R.id.tv_new_order, R.id.tv_car_info, R.id.tv_save, R.id.tv_fix_dec, R.id.tv_post_fix, R.id.tv_title_r, R.id.tv_notice, R.id.tv_back, R.id.ll_to_car_check})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add1:
                //添加工时
                getPresenter().remakeSave(1);
                break;

            case R.id.iv_add2:
                //添加配件
                getPresenter().remakeSave(2);
                break;

            case R.id.tv_new_order:
                //生成估价单
                getPresenter().onInform();
                break;

            case R.id.tv_save:
                //保存退出
                getPresenter().remakeSave(0);
                break;
            case R.id.tv_post_fix:
                //提交修改
                getPresenter().remakeSelected();
                break;
            case R.id.tv_car_info:
                //查看车况
                getPresenter().toCarInfoActivity();
                break;

            case R.id.tv_title_r:
                //授权凭证
                getPresenter().toAuthorizeActivity();
                break;

            case R.id.tv_notice:
                //通知客户
                getPresenter().notice();
                break;

            case R.id.tv_fix_dec:
                //修改备注
                tv_dec.setFocusableInTouchMode(true);
                tv_dec.setFocusable(true);
                tv_dec.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tv_dec, 0);

                break;

            case R.id.tv_back:
                if (getIntent().getBooleanExtra("push", false))
                    toActivity(MainActivity.class);
                else
                    finish();

                break;
            case R.id.ll_to_car_check:


                //车况检修记录列表
                Intent intent = new Intent(this, CarCheckResultListActivity.class);
                intent.putExtra(Configure.car_no, car_no);
                intent.putExtra(Configure.car_id, car_id);
                intent.putExtra(Configure.isShow, 1);

                startActivity(intent);

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
        car_id = fixInfo.getCarId();
        car_no = fixInfo.getCarNo();
        tv_car_no.setText(fixInfo.getCarNo());
        tv_fix_sn.setText("单号：" + fixInfo.getQuotationSn());


        tv_dec.setText(fixInfo.getDescribe());
        tv_mobile.setText(fixInfo.getMobile());
        tv_consignee.setText(null == fixInfo.getUserName() || fixInfo.getUserName().equals("") ? "匿名" : fixInfo.getUserName());


        int status = fixInfo.getStatus();
        switch (status) {
            case 0:
            case 1:

                tv_fix_time.setText("接单时间：" + fixInfo.getAddTime());
                break;
            case 2:
                tv_fix_time.setText("报价时间：" + fixInfo.getInformTime());
                break;
            case 3:
                tv_fix_time.setText("确认时间：" + fixInfo.getInformTime());
                break;
            case 4:
                tv_fix_time.setText("出单时间：" + fixInfo.getInformTime());
                break;


        }


    }

    @Override
    public void createOrderSuccess(int i) {

//        finish();

        if (i == 0) {
            toFixList(0);
            ToastUtils.showToast("检修单已生成！");
        } else if (i == 1) {
            toOrderList(0);
            ToastUtils.showToast("订单已生成！");
        } else if (i == 2) {
            toFixList(0);
            ToastUtils.showToast("检修单已确认！");
        }
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
        ll_car_fix_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddButton() {
        iv_add1.setVisibility(View.INVISIBLE);
        iv_add2.setVisibility(View.INVISIBLE);
        ll_car_fix_list.setVisibility(View.GONE);
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
    public void showPostFixButton() {
        tv_post_fix.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRTitle() {
        setRTitle("授权凭证");
    }


    @Override
    public String getDec() {
        return tv_dec.getText().toString();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int from = intent.getIntExtra("from", -1);
        if (from == 1)//从添加工时，配件页面返回
            getPresenter().handleCallback(intent);
        if (from == 101)//从授权凭证页面返回
            getPresenter().setlpvUrl(intent.getStringExtra(Configure.Domain));
    }
}
