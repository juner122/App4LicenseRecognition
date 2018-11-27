package com.frank.plate.api;

import com.frank.plate.bean.BaseBean;

import io.reactivex.functions.Function;

public class PayLoad<T> implements Function<BaseBean<T>, T> {
    @Override
    public T apply(BaseBean<T> o) {
        if (o.getErrno() != 0)
            throw new ApiException(o.getErrmsg());
        return o.getData();
    }
}
