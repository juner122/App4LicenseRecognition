package com.juner.mvp.base.model;


import android.content.Context;
import androidx.annotation.NonNull;

import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;

import net.grandcentrix.tray.AppPreferences;

import io.reactivex.Observable;

/**
 * Model的实现
 * 在项目中实现常用的发送网络请求的方法，本人在项目中使用的是Retrofit + RxJava
 */
public class BaseModel {
    /**
     * 发送网络请求
     *
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>
     */
    protected <T> void sendRequest(@NonNull Observable<T> observable, @NonNull RxSubscribe<T> observer) {
        observable.subscribe(observer);
    }

    /**
     * 获取token
     */
    protected String getToken(Context context) {
        return new AppPreferences(context).getString(Configure.Token, "");

    }


}
