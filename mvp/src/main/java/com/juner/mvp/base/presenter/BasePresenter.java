package com.juner.mvp.base.presenter;

import android.support.annotation.NonNull;

import com.juner.mvp.base.BaseXPresenter;
import com.juner.mvp.base.view.IBaseView;


/**
 * Presenter的实现
 * <p>
 * 这里不止实现了IBasePresenter,，还实现了HttpResponseListener,网络请求响应接口
 */
public abstract class BasePresenter<V extends IBaseView> extends BaseXPresenter<V> implements IBasePresenter {

    public BasePresenter(@NonNull V view) {
        super(view);
    }

    @Override
    public void cancel(@NonNull Object tag) {
    }

    @Override
    public void cancelAll() {


    }


    public void finish() {

        getView().getSelfActivity().finish();
    }


}