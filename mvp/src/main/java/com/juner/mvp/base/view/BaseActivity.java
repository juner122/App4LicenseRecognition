package com.juner.mvp.base.view;

import android.app.ProgressDialog;

import com.juner.mvp.base.BaseXActivity;
import com.juner.mvp.base.presenter.IBasePresenter;


/**
 * View的实现
 * 实现IBaseView中方法，并继承BaseXActivity
 */
public abstract class BaseActivity<P extends IBasePresenter> extends BaseXActivity<P> implements IBaseView {
    // 加载进度框
    private ProgressDialog mProgressDialog;


    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }


}
