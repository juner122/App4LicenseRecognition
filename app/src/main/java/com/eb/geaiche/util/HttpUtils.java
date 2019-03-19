package com.eb.geaiche.util;

import com.eb.geaiche.api.ApiService;
import com.eb.geaiche.api.FixService;
import com.juner.mvp.api.http.RetrofitServiceManager;

public class HttpUtils {


    public static ApiService getApi() {
        return RetrofitServiceManager.getInstance().create(ApiService.class);


    }

    public static FixService getFix() {
        return RetrofitServiceManager.getInstance().create(FixService.class);
    }
}
