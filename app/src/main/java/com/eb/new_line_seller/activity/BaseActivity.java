package com.eb.new_line_seller.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.TextView;


import com.juner.mvp.Configure;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.api.ApiLoader;


import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

import static com.juner.mvp.Configure.ORDERINFO;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tv_title_r)
    protected TextView tv_title_r;
    @BindView(R.id.iv_r)
    protected View iv_r;
    @BindView(R.id.tv_back)
    protected View tv_back;

    @BindView(R.id.head_view)
    View head_view;

    private Unbinder mUnbinder;

    ApiLoader apiLoader;

    String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        //设置布局
        setContentView(setLayoutResourceID());


        mUnbinder = ButterKnife.bind(this);
        init();

        //初始化控件
        setUpView();

        //初始化数据
        setUpData();


    }


    public ApiLoader Api() {
        if (apiLoader == null)
            apiLoader = new ApiLoader(this);
        return apiLoader;

    }

    @OnClick(R.id.tv_back)
    protected void onClick() {
        finish();
    }

    public void setRTitle(String str) {
        tv_title_r.setVisibility(View.VISIBLE);
        tv_title_r.setText(str);
    }

    public void shwoProgressBar() {
        iv_r.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        iv_r.setVisibility(View.GONE);
    }


    protected abstract void init();

    /**
     * 一些View的相关操作
     */
    protected abstract void setUpView();

    protected abstract void setUpData();

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    public abstract int setLayoutResourceID();


    protected void hideHeadView() {
        head_view.setVisibility(View.GONE);
    }

    protected void hideReturnView() {
        tv_back.setVisibility(View.GONE);
    }

    protected void showRView(String str) {
        tv_title_r.setVisibility(View.VISIBLE);
        tv_title_r.setText(str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected void toActivity(Class c) {

        startActivity(new Intent(this, c));

    }

    protected void toMakeOrder(int user_id, int car_id, String moblie, String user_name, String car_number) {


        new AppPreferences(this).put(Configure.user_id, user_id);
        new AppPreferences(this).put(Configure.moblie, moblie);
        new AppPreferences(this).put(Configure.user_name, user_name);//选择车辆时更新car_no  保存到Preferences
        new AppPreferences(this).put(Configure.car_id, car_id);//选择车辆时更新car_no  保存到Preferences
        new AppPreferences(this).put(Configure.car_no, car_number);//选择车辆时更新car_no  保存到Preferences


        startActivity(new Intent(this, MakeOrderActivity.class));

    }

    protected void toActivity(Intent intent) {

        startActivity(intent);

    }

    protected void toActivity(Class c, Parcelable p, String key) {


        Intent intent = new Intent(this, c);
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected void toActivity(Class c, String key, String str) {


        Intent intent = new Intent(this, c);

        intent.putExtra(key, str);
        startActivity(intent);

    }

    protected void toActivity(Class c, String key, int str) {


        Intent intent = new Intent(this, c);
        intent.putExtra(key, str);
        startActivity(intent);

    }

    protected void sendOrderInfo(Class c, Parcelable p) {


        Intent intent = new Intent(this, c);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDERINFO, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected void toMain(int fragment_p) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Configure.show_fragment, fragment_p);
        toActivity(intent);

    }

    protected void msgManagement(int what, int tag) {
    }

    protected void msgManagement(int what, String tag) {
    }

    /**
     * 发送不延时的消息
     *
     * @param what 发送的消息
     */
    public void sendMsg(final int what, final int tag) {

        Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
            @Override
            public void run() {
                msgManagement(what, tag);
            }
        }).subscribe();
    }

    /**
     * 发送不延时的消息
     *
     * @param what 发送的消息
     */
    public void sendMsg(final int what, final String tag) {

        Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
            @Override
            public void run() {
                msgManagement(what, tag);
            }
        }).subscribe();
    }

    private ArrayList<ResultBack> list = new ArrayList<>();//保存所有activity返回的回调，跟随activity生命周期

    public synchronized void startActivityForResult(Intent intent, ResultBack resultBack) {
        if (list.indexOf(resultBack) < 0) {
            list.add(resultBack);
            startActivityForResult(intent, list.size() - 1);//requestCode就是list的Index
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == list) return;
        if (resultCode == RESULT_OK) {
            list.get(requestCode).resultOk(data);
        } else {
            list.get(requestCode).resultElse(resultCode, data);
        }
        list.set(requestCode, null);//释放已返回的ResultBack，不用remove防止乱序
    }


}
