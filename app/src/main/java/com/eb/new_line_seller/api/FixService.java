package com.eb.new_line_seller.api;

import com.eb.new_line_seller.bean.AutoBrand;
import com.eb.new_line_seller.bean.Meal;
import com.eb.new_line_seller.bean.Meal2;
import com.juner.mvp.bean.ActivityEntity;
import com.juner.mvp.bean.ActivityPage;
import com.juner.mvp.bean.AutoModel;
import com.juner.mvp.bean.BankList;
import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.BaseBean2;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.BillEntity;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.Card;
import com.juner.mvp.bean.CategoryBrandList;
import com.juner.mvp.bean.Coupon;
import com.juner.mvp.bean.Course;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;
import com.juner.mvp.bean.FixServiceList;
import com.juner.mvp.bean.FixServieEntity;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsListEntity;
import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.MyBalanceEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.ProductList;
import com.juner.mvp.bean.QueryByCarEntity;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.juner.mvp.bean.ServerList;
import com.juner.mvp.bean.Shop;
import com.juner.mvp.bean.Technician;
import com.juner.mvp.bean.Token;
import com.juner.mvp.bean.UserBalanceAuthPojo;
import com.juner.mvp.bean.UserInfo;
import com.juner.mvp.bean.WeixinCode;
import com.juner.mvp.bean.WorkIndex;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;


//维修功能版块Api
public interface FixService {


    //报价单列表条件查询
    @POST("quotation/list")
    @FormUrlEncoded
    Observable<BaseBean<FixInfoList>> quotationList(@Header("X-Nideshop-Token") String token, @Field("status") int status);

    //报价单列表条件查询
    @POST("quotation/list")
    Observable<BaseBean<FixInfoList>> quotationList(@Header("X-Nideshop-Token") String token);


    //报价单详情
    @POST("quotation/info")
    @FormUrlEncoded
    Observable<BaseBean<FixInfo>> info(@Header("X-Nideshop-Token") String token, @Field("id") int id);

    //初次报价（状态将由0->2）
    @POST("quotation/inform")
    Observable<BaseBean<NullDataEntity>> inform(@Header("X-Nideshop-Token") String token, @Body() FixInfoEntity infoEntity);


    //分类下服务列表 获取所有数据
    @POST("goods/serveHourList")
    Observable<BaseBean<FixServiceList>> serveHourList(@Header("X-Nideshop-Token") String token);

    //追加项目（报价单status=1状态下才可调用）
    @POST("quotation/addGoodsOrProject")
    Observable<BaseBean<NullDataEntity>> addGoodsOrProject(@Header("X-Nideshop-Token") String token, @Body() FixInfoEntity infoEntity);

    //重新提交勾选后的各个项目（报价单status=1状态下才可调用，将由status1->2）
    @POST("quotation/remakeSelected")
    Observable<BaseBean<NullDataEntity>> remakeSelected(@Header("X-Nideshop-Token") String token, @Body() FixInfoEntity infoEntity);

    //报价单去生成订单
    @POST("quotation/submit")
    Observable<BaseBean<NullDataEntity>> submit(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity infoEntity);


    //配件分类列表
    @POST("component/list")
    Observable<BaseBean<FixPartsList>> componentList(@Header("X-Nideshop-Token") String token);

    //搜索配件接口
    @POST("component/searchComponent")
    @FormUrlEncoded
    Observable<BaseBean<FixPartsEntityList>> seekParts(@Header("X-Nideshop-Token") String token, @Field("category_id") int id, @Field("name") String key);

   //搜索服务接口
    @POST("goods/searchServer")
    @FormUrlEncoded
    Observable<BaseBean<FixServieEntity>> searchServer(@Header("X-Nideshop-Token") String token, @Field("service_id") int id, @Field("name") String key);


}
