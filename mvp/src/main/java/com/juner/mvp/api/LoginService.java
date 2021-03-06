package com.juner.mvp.api;


import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Token;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {



    //登陆账号
    @POST("auth/login")
    @FormUrlEncoded
    Observable<BaseBean<Token>> login(@FieldMap Map<String, Object> maps);


    //短信验证码
    @POST("sms/sendSms")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> smsSendSms(@FieldMap Map<String, Object> maps);
}
