package com.juner.mvp.base;


import android.app.Activity;

/**
 * View层的基类实现
 *
 * View中通过IBaseXPresenter，来实现View对Presenter的依赖，
 * 同时做了内存泄漏的预防处理。Activity通过getPresenter()来调用Presenter。
 * 另外，对于Fragment也可以仿照这样写。
 */
public abstract class BaseXActivity<P extends IBaseXPresenter> extends Activity implements IBaseXView {
    private P mPresenter;

    /**
     * 创建 Presenter
     *
     * @return
     */
    public abstract P onBindPresenter();

    /**
     * 获取 Presenter 对象，在需要获取时才创建`Presenter`，起到懒加载作用
     */
    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }

    @Override
    public Activity getSelfActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束时，将 presenter 与 view 之间的联系断开，防止出现内存泄露
         */
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
