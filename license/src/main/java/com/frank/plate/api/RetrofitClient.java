package com.frank.plate.api;

import android.content.Context;
import android.util.Log;

import com.frank.plate.Configure;
import com.frank.plate.bean.BaseBean;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.bean.UserInfo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private ApiService apiService;

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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }


    /**
     * 获取用户信息
     */
    public void getUserInfo(MySubscriber<UserInfo> bodyBaseSubscriber, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", token);


    }

    /**
     * 获取用户余额信息
     */
    public void getUserBalanceInfo(MySubscriber<MyBalanceEntity> bodyBaseSubscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");


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


    }


    /**
     * 拍照接单自动查找订单或车况
     */
    public void queryByCar(MySubscriber<QueryByCarEntity> bodyBaseSubscriber, int car_no) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("car_no", car_no);


    }


    /**
     * 会员录入
     */
    public void saveUserAndCar(MySubscriber<SaveUserAndCarEntity> bodyBaseSubscriber, String car_no, String mobile, String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("car_no", car_no);
        map.put("mobile", mobile);
        map.put("username", username);


    }

    /**
     * 车况录入
     */
    public void saveCarInfo(MySubscriber<NullDataEntity> bodyBaseSubscriber, String car_no, String userId, String carModel, String postscript) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("car_no", car_no);
        map.put("userId", userId);
        map.put("carModel", carModel);
        map.put("postscript", postscript);


    }

    /**
     * 查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     */
    public void queryAnyGoods(MySubscriber<GoodsListEntity> bodyBaseSubscriber, String category_id, String brand_id, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("category_id", category_id); //商品类别
        map.put("brand_id", brand_id);//品牌
        map.put("name", name);//查询关键字

    }


    /**
     * 分类下品牌列表加第一个品牌第一页下商品
     */
    public void categoryBrandList(MySubscriber<CategoryBrandList> bodyBaseSubscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
    }


    /**
     * 分类下品牌列表加第一个品牌第一页下商品
     */
    public void categoryBrandList2(Callback<BaseBean<CategoryBrandList>> callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");


        apiService.categoryBrandList(map).subscribe();

    }


//    /**
//     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
//     *
//     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
//     */
//    private class HttpResultFunc<T> implements Func1<BaseBean<T>, T> {
//
//        @Override
//        public T call(BaseBean<T> httpResult) {
//            if (httpResult.getErrno() != 0) {
//
//                throw new ApiException(httpResult.getErrmsg());
//            }
//            return httpResult.getData();
//        }
//    }


}
