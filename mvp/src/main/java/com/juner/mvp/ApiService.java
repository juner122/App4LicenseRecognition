package com.juner.mvp;


import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.Token;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {


    //登陆账号
    @POST("auth/login")
    @FormUrlEncoded
    Observable<BaseBean<Token>> login(@FieldMap Map<String, Object> maps);


}
