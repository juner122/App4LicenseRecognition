package com.frank.plate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frank.plate.MyApplication;
import com.frank.plate.R;
import com.frank.plate.api.ApiLoader;

import net.grandcentrix.tray.AppPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    protected TextView tv_title;
    @BindView(R.id.tv_title_r)
    protected TextView tv_title_r;
    @BindView(R.id.iv_r)
    protected View iv_r;

    @BindView(R.id.head_view)
    View head_view;

    private Unbinder mUnbinder;

    ApiLoader apiLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            apiLoader = new ApiLoader();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected void toActivity(Class c) {

        startActivity(new Intent(this, c));

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

    protected void msgManagement(int what, int tag) {
    }

    /**
     * 发送不延时的消息
     *
     * @param what 发送的消息
     */
    public void sendMsg(final int what, final int tag) {

        Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                msgManagement(what, tag);
            }
        }).subscribe();
    }


}
