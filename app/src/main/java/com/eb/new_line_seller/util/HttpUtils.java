package com.eb.new_line_seller.util;

import com.eb.new_line_seller.api.ApiService;
import com.juner.mvp.api.http.RetrofitServiceManager;

public class HttpUtils {



    public static ApiService getApi() {
        return RetrofitServiceManager.getInstance().create(ApiService.class);
    }
}
