package com.juner.mvp.base;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Presenter中的基类实现
 * Presenter中通过IBaseXView，来实现Presenter对View的依赖，当每次对View进行调用时，
 * 先使用isViewAttach判断一下，Presenter与View之间的联系是否还在，防止内存泄漏。
 * Presenter通过View暴露的接口IBaseXView，来控制View
 */

public class BaseXPresenter<V extends IBaseXView> implements IBaseXPresenter {
    // 防止 Activity 不走 onDestory() 方法，所以采用弱引用来防止内存泄漏
    private WeakReference<V> mViewRef;

    public BaseXPresenter(@NonNull V view) {
        attachView(view);
    }

    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public V getView() {
        return mViewRef.get();
    }

    @Override
    public boolean isViewAttach() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}

