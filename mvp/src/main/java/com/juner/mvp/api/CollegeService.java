package com.juner.mvp.api;


import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.Course;
import com.juner.mvp.bean.Courses;
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

//学院模块相关api
public interface CollegeService {

    //课程列表
    @POST("course/list")
    @FormUrlEncoded
    Observable<BaseBean<List<Courses>>> courseList(@Header("X-Nideshop-Token") String token, @Field("name") String name, @Field("course_type") String course_type);

}
