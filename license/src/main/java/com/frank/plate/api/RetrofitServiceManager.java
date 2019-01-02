package com.frank.plate.api;

import android.util.Log;

import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.activity.LoginActivity;

import net.grandcentrix.tray.AppPreferences;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    String TAG = "RetrofitService";
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;

    private RetrofitServiceManager() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
        // 添加公共参数拦截器

//        Log.i(TAG, "X-Nideshop-Token==>" + new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
//                .addHeaderParams("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""))
                .build();

        builder.addInterceptor(commonInterceptor);


        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())//Gson
//                .addConverterFactory(FastJsonConverterFactory.create())//Fastjson
                .baseUrl(Configure.BaseUrl)
                .build();
    }

    private static class SingletonHolder {
        private static RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
