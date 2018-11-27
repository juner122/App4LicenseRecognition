package com.frank.plate.api;

import com.frank.plate.bean.BaseBean;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.bean.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {


    @GET("login")
    Observable<ResponseBody> login();




    @POST("user/getInfo")
    @FormUrlEncoded
    Observable<BaseBean<UserInfo>> getUserInfo(@FieldMap Map<String, Object> maps);


    //余额信息
    @POST("userbalance/getInfo")
    @FormUrlEncoded
    Observable<BaseBean<MyBalanceEntity>> getUserBalanceInfo(@FieldMap Map<String, Object> maps);



    //账单列表
    @POST("userbalancedetail/list")
    @FormUrlEncoded
    Observable<BaseBean<BillEntity>> getUserBillList(@FieldMap Map<String, Object> maps);




    //拍照接单自动查找订单或车况
    @POST("order/queryByCar")
    @FormUrlEncoded
    Observable<BaseBean<QueryByCarEntity>> queryByCar(@FieldMap Map<String, Object> maps);


    //会员录入
    @POST("user/saveUserAndCar")
    @FormUrlEncoded
    Observable<BaseBean<SaveUserAndCarEntity>> saveUserAndCar(@FieldMap Map<String, Object> maps);


    //更新车况
    @POST("usercarcondition/save")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> saveCarInfo(@FieldMap Map<String, Object> maps);


    //新增车况
    @POST("usercarcondition/save")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> addCarInfo(@FieldMap Map<String, Object> maps);


    //查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
    @POST("goods/queryAnyGoods")
    @FormUrlEncoded
    Observable<BaseBean<GoodsListEntity>> queryAnyGoods(@FieldMap Map<String, Object> maps);



    //分类下品牌列表加第一个品牌第一页下商品
    @POST("brand/categoryBrandList")
    @FormUrlEncoded
    Observable<BaseBean<CategoryBrandList>> categoryBrandList(@FieldMap Map<String, Object> maps);





}
