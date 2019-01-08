package com.juner.mvp.api;

import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.OrderInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderService {



    //2.订单详情页
    @POST("order/detail")
    @FormUrlEncoded
    Observable<BaseBean<OrderInfo>> orderDetail(@FieldMap Map<String, Object> maps);

}
