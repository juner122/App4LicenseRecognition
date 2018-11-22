package com.frank.plate.api;
import com.tamic.novate.Throwable;

public interface SubscribeOnNextListener<T> {

    void onNext(T t);
    void onError(Throwable  e);

}
