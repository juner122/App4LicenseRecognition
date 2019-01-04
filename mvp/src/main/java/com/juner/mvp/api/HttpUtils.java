package com.juner.mvp.api;

public class HttpUtils {

    public static LoginService getLoginApi() {
        return RetrofitServiceManager.getInstance().create(LoginService.class);
    }

    public static ApiService getApi() {
        return RetrofitServiceManager.getInstance().create(ApiService.class);
    }
}
