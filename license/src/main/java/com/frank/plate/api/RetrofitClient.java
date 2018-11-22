package com.frank.plate.api;

import android.content.Context;
import android.util.Log;

import com.frank.plate.Configure;
import com.frank.plate.bean.BaseBean;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.UserInfo;
import com.tamic.novate.Novate;
import com.tamic.novate.exception.NovateException;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Func1;

public class RetrofitClient {

    private ApiService apiService;

    private Novate novate;

    public static String baseUrl = Configure.BaseUrl;

    private static Context mContext;


    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            Log.v("RetrofitClient", "getInstance" + "");
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }


    private RetrofitClient(Context context) {

        novate = new Novate.Builder(context)
                .addLog(true)
                .baseUrl(baseUrl).build();
        apiService = novate.create(ApiService.class);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(MySubscriber<UserInfo> bodyBaseSubscriber, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", token);


        novate.call(apiService.getUserInfo(map).map(new HttpResultFunc<UserInfo>()), bodyBaseSubscriber);
    }


    public void getPhoneCode(MySubscriber<BaseBean<UserInfo>> bodyBaseSubscriber, String phoneNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phoneNumber);
        novate.call(apiService.getUserInfo(map), bodyBaseSubscriber);
    }

    /**
     * 获取用户余额信息
     */
    public void getUserBalanceInfo(MySubscriber<MyBalanceEntity> bodyBaseSubscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");

        novate.call(apiService.getUserBalanceInfo(map).map(new HttpResultFunc<MyBalanceEntity>()), bodyBaseSubscriber);

    }

    /**
     * 获取用户账单列表
     *
     * @param limit 页数
     */
    public void getUserBillList(MySubscriber<BillEntity> bodyBaseSubscriber, int page, int limit, String sidx, String order) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("page", page);
        map.put("limit", limit);
        map.put("sidx", sidx);
        map.put("order", order);

        novate.call(apiService.getUserBillList(map).map(new HttpResultFunc<BillEntity>()), bodyBaseSubscriber);
    }


    /**
     * 拍照接单自动查找订单或车况
     *
     */
    public void queryByCar(MySubscriber<QueryByCarEntity> bodyBaseSubscriber, int car_no) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("car_no", car_no);

        novate.call(apiService.queryByCar(map).map(new HttpResultFunc<QueryByCarEntity>()), bodyBaseSubscriber);
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<BaseBean<T>, T> {

        @Override
        public T call(BaseBean<T> httpResult) {
            if (httpResult.getErrno() != 0) {

                throw new ApiException(httpResult.getErrmsg());
            }
            return httpResult.getData();
        }
    }


}
