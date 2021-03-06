package com.juner.mvp.api.http;

import android.util.Log;

import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.BaseBean2;
import com.juner.mvp.bean.BaseBean3;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.NumberBean;
import com.juner.mvp.utils.ToastUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static final <T> ObservableTransformer<BaseBean<T>, T> observe() {
        return new ObservableTransformer<BaseBean<T>, T>() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.flatMap(new Function<BaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(BaseBean<T> result) {

                        if (result.ifSuccess()) {//检查是否掉接口成功了
                            return createData(result.getData());//成功，剥取我们要的数据，把BaseModel丢掉
                        } else {
                            Log.e("json", result.toString());
                            return Observable.error(new Exception(result.getErrno() + ":" + result.getErrmsg()));//出错就返回服务器错误
                        }

                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果进行预处理
     *
     * @return
     */
    public static final ObservableTransformer<NumberBean, Integer> observeNub() {
        return new ObservableTransformer<NumberBean, Integer>() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.flatMap(new Function<NumberBean, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(NumberBean result) {

                        if (result.ifSuccess()) {//检查是否掉接口成功了
                            return createData(result.getData());//成功，剥取我们要的数据，把BaseModel丢掉
                        } else {

                            return Observable.error(new Exception(result.getErrno() + ":" + result.getErrmsg()));//出错就返回服务器错误
                        }

                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static final <T> ObservableTransformer<T, T> observevid() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.flatMap(new Function<T, Observable<T>>() {
                    @Override
                    public Observable<T> apply(T result) {

                        return createData(result);//成功，剥取我们要的数据，把BaseModel丢掉


                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static final <T> ObservableTransformer<BaseBean2<T>, T> observe2() {
        return new ObservableTransformer<BaseBean2<T>, T>() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.flatMap(new Function<BaseBean2<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(BaseBean2<T> result) {
                        if (result.ifSuccess()) {//检查是否掉接口成功了
                            return createData(result.getData());//成功，剥取我们要的数据，把BaseModel丢掉
                        } else {
                            return Observable.error(new Exception(result.getMsg()));//出错就返回服务器错误
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 对结果进行预处理
     * <p>
     * 处理第三方识别车牌
     *
     * @param <T>
     * @return
     */
    public static final <T> ObservableTransformer<BaseBean3<T>, T> observe3() {
        return new ObservableTransformer<BaseBean3<T>, T>() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.flatMap(new Function<BaseBean3<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(BaseBean3<T> result) {
                        if (result.getMessage().equals("0")) {//检查是否掉接口成功了
                            return createData(result.getCardsinfo());//成功，剥取我们要的数据，把BaseModel丢掉
                        } else {
                            return Observable.error(new Exception(result.getMessage().getValue()));//出错就返回服务器错误
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static final <T> ObservableTransformer<T, T> observeVin() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.flatMap(new Function<T, Observable<T>>() {
                    @Override
                    public Observable<T> apply(T result) {
                        try {
                            String t = result.toString();
                            return createData(result);//成功，剥取我们要的数据，把BaseModel丢掉
                        } catch (Exception e) {
                            e.printStackTrace();
                            return Observable.error(new Exception("识别失败"));//出错就返回服务器错误
                        }

                    }
                }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
                try {
                    subscriber.onNext(data);
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final List<T> data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
                try {
                    subscriber.onNext(data.get(0));
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


}
