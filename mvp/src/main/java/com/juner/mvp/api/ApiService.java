package com.juner.mvp.api;


import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.RemakeActCard;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.juner.mvp.bean.UserEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    //会员手机号快捷录入或老会员车况查询列表
    @POST("user/addUser")
    @FormUrlEncoded
    Observable<BaseBean<SaveUserAndCarEntity>> addUser(@FieldMap Map<String, Object> maps);


    //纸质卡补录
    @POST("activity/remakeActCard")
    Observable<BaseBean<NullDataEntity>> remakeActCard(@Header("X-Nideshop-Token") String token, @Body List<RemakeActCard> list);


    //获取当前登录用户
    @POST("/app/sysuser/getInfo")
    Observable<BaseBean<UserEntity>> getInfo(@Header("X-Nideshop-Token") String token);


    //新建维修报价单
    @POST("quotation/save")
    Observable<BaseBean<NullDataEntity>> quotationSave(@Header("X-Nideshop-Token") String token, @Body FixInfoEntity fixInfo);


}
