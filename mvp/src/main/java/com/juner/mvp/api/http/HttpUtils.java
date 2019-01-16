package com.juner.mvp.api.http;

import com.juner.mvp.api.ApiService;
import com.juner.mvp.api.LoginService;
import com.juner.mvp.api.OrderService;

public class HttpUtils {

    public static LoginService getLoginApi() {
        return RetrofitServiceManager.getInstance().create(LoginService.class);


    }

    public static OrderService getOrderApi() {
        return RetrofitServiceManager.getInstance().create(OrderService.class);
    }

    public static ApiService getApi() {
        return RetrofitServiceManager.getInstance().create(ApiService.class);


    }
}
