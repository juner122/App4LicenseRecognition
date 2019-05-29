package com.eb.geaiche.mvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.geaiche.R;
import com.eb.geaiche.activity.AutographActivity;
import com.eb.geaiche.buletooth.DeviceConnFactoryManager;
import com.eb.geaiche.mvp.contacts.FixInfoDesContacts;
import com.eb.geaiche.mvp.presenter.FixInfoDesPtr;
import com.eb.geaiche.util.ButtonUtils;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.view.ConfirmDialogCanlce;
import com.juner.mvp.Configure;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf;

//维修故障描述页面
public class FixInfoDescribeActivity extends BaseActivity<FixInfoDesContacts.FixInfoDesPtr> implements FixInfoDesContacts.FixInfoDesUI {

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;

    @BindView(R.id.but_to_technician_list)
    TextView technician_list;//技师

    @BindView(R.id.but_set_date)
    TextView but_set_date;

    @BindView(R.id.et)
    EditText et;

    @BindView(R.id.tv_bluetooth)
    TextView tv_bluetooth;

    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;
    @BindView(R.id.et_deputy)
    EditText et_deputy;//送修人


    @BindView(R.id.ll_deputy)
    View ll_deputy;//送修人

    @BindView(R.id.ll_deputy_m)
    View ll_deputy_m;//送修人电话
    @BindView(R.id.et_deputy_mobile)
    EditText et_deputy_mobile;//送修人电话


    @BindViews({R.id.tv_re7, R.id.tv_re8, R.id.tv_re9, R.id.tv_re10, R.id.tv_re11, R.id.tv_re12, R.id.tv_re13, R.id.tv_re14, R.id.tv_re15})
    public List<TextView> textViews;

    @OnClick({R.id.but_to_technician_list, R.id.tv_fix_order, R.id.tv_enter_order, R.id.tv_title_r, R.id.tv_bluetooth, R.id.ll_autograph, R.id.but_set_date})
    public void onClick(View view) {

        if (ButtonUtils.isFastDoubleClick(view.getId())) {//防止按钮多次重复点击
            return;
        }

        switch (view.getId()) {
            case R.id.but_to_technician_list:
                getPresenter().toTechnicianListActivity();
                break;

            case R.id.tv_fix_order:

                getPresenter().showConfirmDialog(true);

                break;

            case R.id.tv_enter_order:
                getPresenter().showConfirmDialog(false);

                break;

            case R.id.tv_bluetooth://连接蓝牙

                getPresenter().initBluetooth();//连接蓝牙


                break;
            case R.id.tv_title_r://蓝牙打印

                getPresenter().btnReceiptPrint();//蓝牙打印

                break;

            case R.id.ll_autograph://签名


                //弹出对话框
                final ConfirmDialogCanlce confirmDialog = new ConfirmDialogCanlce(this, getResources().getString(R.string.agreement), getResources().getString(R.string.agreement_title));
                confirmDialog.show();
                confirmDialog.setClicklistener(new ConfirmDialogCanlce.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        confirmDialog.dismiss();
                        toActivity(AutographActivity.class, "class", "FixInfoDescribe");

                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });
                break;

            case R.id.but_set_date:

                //选择时间
                getPresenter().pickdoneTime();

                break;
        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_describe;
    }

    @Override
    protected void init() {
        tv_title.setText("检修接单");
        setRTitle("凭证打印");
        getPresenter().getInfo();
        getPresenter().setTipClickListener(textViews);
        getPresenter().setEtText(et);

        if (MyAppPreferences.getShopType()) {
            ll_deputy.setVisibility(View.GONE);
            ll_deputy_m.setVisibility(View.GONE);
        }
    }


    @Override
    public FixInfoDesContacts.FixInfoDesPtr onBindPresenter() {
        return new FixInfoDesPtr(this);
    }

    @Override
    public void setCarNo(String carNo) {
        tv_car_no.setText(carNo);
    }

    @Override
    public void setTechnician(String technicians) {
        technician_list.setText(technicians);
    }

    @Override
    public void setDate(String date) {
        but_set_date.setText(date);
    }

    @Override
    public void setTip(String tip) {
        et.append(tip);
    }

    @Override
    public void toFixInfoActivity(int id) {

        toActivity(FixInfoActivity.class, "id", id);
    }

    @Override
    public void toMian() {

        toFixList(0);
    }

    @Override
    public void setBluetoothText(String str) {
        tv_bluetooth.setText(str);
    }

    @Override
    public String getDescribe() {
        return et.getText().toString();
    }

    @Override
    public String getDeputy() {
        return et_deputy.getText().toString();
    }

    @Override
    public String getDeputyMobile() {
        return et_deputy_mobile.getText().toString();
    }

    @Override
    public Bitmap getDrawableBitmap() {
        return ((BitmapDrawable) iv_lpv.getDrawable()).getBitmap();
    }

    @Override
    public void cleanText(String ct) {
        String re = et.getText().toString();
        et.setText(re.replace(ct, ""));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Glide.with(this)
                .asDrawable()
                .load(Uri.fromFile(new File(Configure.LinePathView_url)))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(skipMemoryCacheOf(true))
                .into(iv_lpv);

        getPresenter().setPicUrl(intent.getStringExtra(Configure.Domain));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DeviceConnFactoryManager.closeAllPort();
        //蓝牙 中止线程池中全部的线程的执行
        CameraThreadPool.shutdownNow();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onStop();

    }

}
