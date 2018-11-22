package com.frank.plate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.frank.plate.R;
import com.frank.plate.api.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    protected TextView tv_title;

    @BindView(R.id.head_view)
    View head_view;

    private Unbinder mUnbinder;


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

    public RetrofitClient Api() {
        return RetrofitClient.getInstance(this);

    }

    @OnClick(R.id.tv_back)
    protected void onClick() {
        finish();
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

    protected void toActivity(Class c, Intent intent) {

        startActivity(new Intent(this, c));

    }


}
