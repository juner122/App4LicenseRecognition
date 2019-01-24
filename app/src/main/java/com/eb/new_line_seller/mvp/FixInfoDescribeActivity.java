package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.AutographActivity;
import com.eb.new_line_seller.buletooth.DeviceConnFactoryManager;
import com.eb.new_line_seller.mvp.contacts.FixInfoDesContacts;
import com.eb.new_line_seller.mvp.presenter.FixInfoDesPtr;
import com.eb.new_line_seller.view.ConfirmDialogCanlce;
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


    @BindViews({R.id.tv_re1, R.id.tv_re2, R.id.tv_re3, R.id.tv_re4, R.id.tv_re5, R.id.tv_re6})
    public List<TextView> textViews;

    @OnClick({R.id.but_to_technician_list, R.id.tv_fix_order, R.id.tv_enter_order, R.id.tv_title_r, R.id.tv_bluetooth, R.id.ll_autograph})
    public void onClick(View v) {
        switch (v.getId()) {
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
                toActivity(AutographActivity.class, "class", "FixInfoDescribe");
                break;

        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_describe;
    }

    @Override
    protected void init() {
        tv_title.setText("故障描述");
        setRTitle("凭证打印");
        getPresenter().getInfo();
        getPresenter().setTipClickListener(textViews);


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
        et.append(tip + ",");
    }

    @Override
    public void toFixInfoActivity(int id) {

        toActivity(FixInfoActivity.class, "id", id);
    }

    @Override
    public void toMian() {
        toMain(0);
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
    public Bitmap getDrawableBitmap() {
        return ((BitmapDrawable) iv_lpv.getDrawable()).getBitmap();
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
