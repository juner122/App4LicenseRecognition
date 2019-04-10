package com.eb.geaiche.api;

import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.Component;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.FixPartsList;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.FixServiceList;
import com.juner.mvp.bean.FixServieEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.ShopProject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


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


    //保存退出（报价单status=1状态下才可调用）
    @POST("quotation/remakeSave")
    Observable<BaseBean<NullDataEntity>> remakeSave(@Header("X-Nideshop-Token") String token, @Body() FixInfoEntity infoEntity);



    //店长跨客户回撤 （不需要凭证图片，报价单status=3状态下才可调用，将由status3->2）
    @POST("quotation/replaceReback")
    Observable<BaseBean<NullDataEntity>> replaceReback(@Header("X-Nideshop-Token") String token, @Body() FixInfoEntity infoEntity);


    //店长跨客户确认（需要凭证图片才能提。报价单status=2状态下才可调用，将由status2->3）
    @POST("quotation/replaceConfirm")
    Observable<BaseBean<NullDataEntity>> replaceConfirm(@Header("X-Nideshop-Token") String token, @Body() FixInfoEntity infoEntity);



    //报价单去生成订单
    @POST("quotation/submit")
    Observable<BaseBean<NullDataEntity>> submit(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity infoEntity);


    //配件分类列表
    @POST("component/list")
    Observable<BaseBean<FixPartsList>> componentList(@Header("X-Nideshop-Token") String token);

    //搜索配件接口
    @POST("component/searchComponent")
    @FormUrlEncoded
    Observable<BaseBean<FixPartsEntityList>> seekParts(@Header("X-Nideshop-Token") String token, @Field("category_id") int id, @Field("name") String key);    //搜索配件接口
    @POST("component/searchComponent")
    @FormUrlEncoded
    Observable<BaseBean<FixPartsEntityList>> seekParts(@Header("X-Nideshop-Token") String token, @Field("name") String key);

    //搜索服务接口
    @POST("goods/searchServer")
    @FormUrlEncoded
    Observable<BaseBean<FixServieEntity>> searchServer(@Header("X-Nideshop-Token") String token, @Field("service_id") int id, @Field("name") String key);
    //搜索服务接口
    @POST("goods/searchServer")
    @FormUrlEncoded
    Observable<BaseBean<FixServieEntity>> searchServer(@Header("X-Nideshop-Token") String token, @Field("name") String key);

    //添加自定义零件
    @POST("component/save")
    Observable<BaseBean<Component>> componentSave(@Header("X-Nideshop-Token") String token, @Body Component component);


    //自定义零件 一级分类下拉框
    @POST("component/firstCategory")
    Observable<BaseBean<List<FixParts2item>>> componentFirstCategory(@Header("X-Nideshop-Token") String token);

    //自定义零件 二级分类下拉框
    @POST("component/secondCategory")
    @FormUrlEncoded
    Observable<BaseBean<List<FixParts2item>>> componentSecondCategory(@Header("X-Nideshop-Token") String token, @Field("parent_id") int id);

    //添加自定义服务
    @POST("goods/addShopService")
    Observable<BaseBean<ShopProject>> addShopService(@Header("X-Nideshop-Token") String token, @Body ShopProject shopProject);

    //自定义服务 服务分类第一级下拉框
    @POST("goods/firstService")
    Observable<BaseBean<List<FixService2item>>> firstService(@Header("X-Nideshop-Token") String tokenct);


    //自定义服务 服务分类第二级下拉框
    @POST("goods/secondService")
    @FormUrlEncoded
    Observable<BaseBean<List<FixService2item>>> secondService(@Header("X-Nideshop-Token") String tokenct, @Field("parent_id") int id);

    //添加自定义服务或商品
    @POST("xgxshopgoods/save")
    Observable<BaseBean<ShopProject>> save(@Header("X-Nideshop-Token") String token, @Body ShopProject shopProject);

}
