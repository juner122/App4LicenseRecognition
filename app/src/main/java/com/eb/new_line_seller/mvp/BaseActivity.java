package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.juner.mvp.base.BaseXActivity;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * View的实现
 * 实现IBaseView中方法，并继承BaseXActivity
 */
public abstract class BaseActivity<P extends IBasePresenter> extends BaseXActivity<P> implements IBaseView {
    @BindView(R.id.head_view)
    View head_view;
    @BindView(R.id.tv_title_r)
    protected TextView tv_title_r;

    @BindView(R.id.tv_title)
    protected TextView tv_title;

    protected void hideHeadView() {
        head_view.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        //设置布局
        setContentView(setLayoutResourceID());
        ButterKnife.bind(this);
        init();

    }


    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(msg, getSelfActivity());
    }

    @OnClick(R.id.tv_back)
    protected void onClick() {
        finish();
    }
    protected void toActivity(Class c) {

        startActivity(new Intent(this, c));

    }

    public void setRTitle(String str) {
        tv_title_r.setVisibility(View.VISIBLE);
        tv_title_r.setText(str);
    }

    protected void toActivity(Class c, String key, int str) {


        Intent intent = new Intent(this, c);
        intent.putExtra(key, str);
        startActivity(intent);

    }
    protected void toActivity(Class c, Parcelable p, String key) {


        Intent intent = new Intent(this, c);
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public abstract int setLayoutResourceID();

    protected abstract void init();
}
