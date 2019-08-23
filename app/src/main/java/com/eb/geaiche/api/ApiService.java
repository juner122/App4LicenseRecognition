package com.eb.geaiche.api;

import com.eb.geaiche.bean.Meal2;
import com.eb.geaiche.bean.RecordMeal;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.juner.mvp.bean.ActivityEntity;
import com.juner.mvp.bean.ActivityPage;
import com.eb.geaiche.bean.AutoBrand;
import com.juner.mvp.bean.Ask;
import com.juner.mvp.bean.AutoModel;
import com.juner.mvp.bean.BankList;
import com.juner.mvp.bean.Banner;
import com.juner.mvp.bean.BaseBean;
import com.juner.mvp.bean.BaseBean2;
import com.juner.mvp.bean.BaseBean3;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.BillEntity;
import com.juner.mvp.bean.CarCheckResul;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.CarVin;
import com.juner.mvp.bean.CarVin2;
import com.juner.mvp.bean.CarVinRequest;
import com.juner.mvp.bean.Card;
import com.juner.mvp.bean.Carsinfo;
import com.juner.mvp.bean.CartList;
import com.juner.mvp.bean.CategoryBrandList;
import com.juner.mvp.bean.CategoryType;
import com.juner.mvp.bean.CheckOptions;
import com.juner.mvp.bean.Coupon;
import com.juner.mvp.bean.Coupon2;
import com.juner.mvp.bean.CouponRecode;
import com.juner.mvp.bean.Course;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.Courses;
import com.juner.mvp.bean.DisRecordList;
import com.juner.mvp.bean.FixInfoList;
import com.juner.mvp.bean.Goods;
import com.juner.mvp.bean.GoodsBrand;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsEntity;
import com.juner.mvp.bean.GoodsList;
import com.juner.mvp.bean.GoodsListEntity;
import com.eb.geaiche.bean.Meal;

import com.juner.mvp.bean.Joiner;
import com.juner.mvp.bean.License;
import com.juner.mvp.bean.Maneuver;
import com.juner.mvp.bean.Member;
import com.juner.mvp.bean.MemberOrder;
import com.juner.mvp.bean.MessageRecordEntity;
import com.juner.mvp.bean.MyBalanceEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.NumberBean;
import com.juner.mvp.bean.OrderInfo;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.OrderNews;
import com.juner.mvp.bean.PayInfo;
import com.juner.mvp.bean.ProductList;
import com.juner.mvp.bean.QueryByCarEntity;
import com.juner.mvp.bean.Roles;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.juner.mvp.bean.ServerList;
import com.juner.mvp.bean.Shop;

import com.juner.mvp.bean.ShopCar;
import com.juner.mvp.bean.ShopCarBane;
import com.juner.mvp.bean.SmsTemplates;
import com.juner.mvp.bean.StaffPerformance;
import com.juner.mvp.bean.Technician;
import com.juner.mvp.bean.TechnicianInfo;
import com.juner.mvp.bean.Token;
import com.juner.mvp.bean.UserBalanceAuthPojo;
import com.juner.mvp.bean.UserEntity;
import com.juner.mvp.bean.UserInfo;
import com.juner.mvp.bean.VersionInfo;
import com.juner.mvp.bean.Video;
import com.juner.mvp.bean.VinImageBody;
import com.juner.mvp.bean.WeixinCode;
import com.juner.mvp.bean.WorkIndex;
import com.juner.mvp.bean.XgxPurchaseOrderPojo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {


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
    Observable<BaseBean<BillEntity>> getUserBillList(@FieldMap Map<String, Object> maps, @Field("type") List<Integer> idList);

    //账单列表
    @POST("userbalancedetail/list")
//    @FormUrlEncoded
    Observable<BaseBean<BillEntity>> getUserBillList(@Header("X-Nideshop-Token") String token, @Body List<Integer> integers);

    //账单列表 收入账单 与我的账单列表一个接口，多一个参数
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


    //会员手机号快捷录入或老会员车况查询列表
    @POST("user/addUser")
    @FormUrlEncoded
    Observable<BaseBean<SaveUserAndCarEntity>> addUser(@FieldMap Map<String, Object> maps);


    //更新车况
    @POST("usercarcondition/save")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> saveCarInfo(@FieldMap Map<String, Object> maps);


    //新增车况
    @POST("usercarcondition/save")
    Observable<BaseBean<Integer>> addCarInfo(@Header("X-Nideshop-Token") String token, @Body CarInfoRequestParameters event);

    //修改车况
    @POST("usercarcondition/update")
    Observable<BaseBean<NullDataEntity>> fixCarInfo(@Header("X-Nideshop-Token") String token, @Body CarInfoRequestParameters event);


    //门店信息
    @POST("shop/info")
    @FormUrlEncoded
    Observable<BaseBean<Shop>> shopInfo(@FieldMap Map<String, Object> maps);

    //工作台信息
    @POST("work/getWorkHeaderAd")
    @FormUrlEncoded
    Observable<BaseBean<List<Banner>>> getWorkHeaderAd(@FieldMap Map<String, Object> maps);

    //未读新消息数量
    @POST("pushlog/needRead")
    @FormUrlEncoded
    Observable<NumberBean> needRead(@FieldMap Map<String, Object> maps);

    //未读新消息list
    @POST("pushlog/list")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderNews>>> pushlogList(@FieldMap Map<String, Object> maps);

    //标为已读
    @POST("pushlog/updateRead")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> updateRead(@FieldMap Map<String, Object> maps);

    //4.批量删除车况图片
    @POST("usercarconditionpicture/delete")
    Observable<BaseBean<NullDataEntity>> delete(@Header("X-Nideshop-Token") String token, @Body List<Integer> integers);

    //确认下单
    @POST("order/submit")
    Observable<BaseBean<OrderInfo>> submit(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity infoEntity);


    //订单修改 orderInfo类
    @POST("order/remake")
    Observable<BaseBean<OrderInfo>> remake(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity infoEntity);

    //确认支付
    @POST("order/confirmPay")
    Observable<BaseBean<NullDataEntity>> confirmPay(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity infoEntity);


    //确认订单最后完成
    @POST("order/confirmFinish")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> confirmFinish(@FieldMap Map<String, Object> maps);


    //4.开始服务(修改订单状态为服务中)
    @POST("order/beginServe")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> beginServe(@FieldMap Map<String, Object> maps);


    //任意条件订单列表 不同订单查询看备注
    @POST("order/list")
    @FormUrlEncoded
    Observable<BaseBean<BasePage<OrderInfoEntity>>> orderList(@FieldMap Map<String, Object> maps);

    //2.订单详情页
    @POST("order/detail")
    @FormUrlEncoded
    Observable<BaseBean<OrderInfo>> orderDetail(@FieldMap Map<String, Object> maps);

    //取消订单
    @POST("order/cancle")
    Observable<BaseBean<NullDataEntity>> orderDelete(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity order);


    //车况详情显示
    @POST("usercarcondition/info")
    @FormUrlEncoded
    Observable<BaseBean<CarInfoRequestParameters>> showCarInfo(@FieldMap Map<String, Object> maps);

    //15.当前门店用户（技师）列表
    @POST("sysuser/list")
    @FormUrlEncoded
    Observable<BaseBean<BasePage<Technician>>> sysuserList(@FieldMap Map<String, Object> maps);


    //查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
    @POST("goods/queryAnyGoods")
    @FormUrlEncoded
    Observable<BaseBean<GoodsListEntity>> queryAnyGoods(@FieldMap Map<String, Object> maps);

    //四个火热商品
    @POST("shopeasy/list")
    @FormUrlEncoded
    Observable<BaseBean<GoodsListEntity>> shopeasyList(@FieldMap Map<String, Object> maps);


    //分类下品牌列表加第一个品牌第一页下商品
    @POST("brand/categoryBrandList")
    @FormUrlEncoded
    Observable<BaseBean<CategoryBrandList>> categoryBrandList(@FieldMap Map<String, Object> maps);


    //活动列表
    @POST("activity/list")
    @FormUrlEncoded
    Observable<BaseBean<ActivityPage>> activityList(@FieldMap Map<String, Object> maps);

    //活动详情
    @POST("activity/detail")
    @FormUrlEncoded
    Observable<BaseBean<ActivityEntity>> activityDetail(@FieldMap Map<String, Object> maps);

    //品牌查询列表
    @POST("carbrand/listByName")
    Observable<BaseBean<List<AutoBrand>>> listByName(@Header("X-Nideshop-Token") String token);

    //通过品牌查车型列表
    @POST("carname/listByBrand")
    @FormUrlEncoded
    Observable<BaseBean<List<AutoModel>>> listByBrand(@FieldMap Map<String, Object> maps);


    //工作台首页
    @POST("work/index")
    @FormUrlEncoded
    Observable<BaseBean<WorkIndex>> workIndex(@FieldMap Map<String, Object> maps);


    //会员管理页面数据
    @POST("user/memberList")
    @FormUrlEncoded
    Observable<BaseBean<Member>> memberList(@FieldMap Map<String, Object> maps);

    //查看会员信息及订单记录
    @POST("user/memberOrderList")
    @FormUrlEncoded
    Observable<BaseBean<MemberOrder>> memberOrderList(@FieldMap Map<String, Object> maps);

    //修改用户名
    @POST("user/remakeUserName")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> remakeUserName(@FieldMap Map<String, Object> maps);

    //修改用户名2 不用验证码
    @POST("user/remakeName")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> remakeName(@FieldMap Map<String, Object> maps);

    //获取优惠券列表 [达到满减，未到期，未用过]
    @POST("coupon/list")
    @FormUrlEncoded
    Observable<BaseBean<List<Coupon>>> couponList(@FieldMap Map<String, Object> maps);

    //我的余额
    @POST("userbalance/getInfo")
    @FormUrlEncoded
    Observable<BaseBean<MyBalanceEntity>> balanceInfo(@FieldMap Map<String, Object> maps);


    //课程列表
    @POST("course/list")
    @FormUrlEncoded
    Observable<BaseBean<List<Course>>> courseList(@FieldMap Map<String, Object> maps);


    //课程报名
    @POST("coursejoinname/save")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> coursejoinnameSave(@FieldMap Map<String, Object> maps);


    //添加反馈
    @POST("feedback/save")
    @FormUrlEncoded
    Observable<BaseBean<String>> feedbackSave(@FieldMap Map<String, Object> maps);

    //短信验证码
    @POST("sms/sendSms")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> smsSendSms(@FieldMap Map<String, Object> maps);


    //银行卡验证短信
    @POST("sms/sendBankSms")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> sendBankSms(@FieldMap Map<String, Object> maps);


    //登陆账号
    @POST("auth/login")
    @FormUrlEncoded
    Observable<BaseBean<Token>> login(@FieldMap Map<String, Object> maps);


    //添加银行卡
    @POST("bank/save")
    Observable<BaseBean<NullDataEntity>> bankSave(@Header("X-Nideshop-Token") String token, @Body Card maps);

    //查看银行卡
    @POST("bank/list")
    Observable<BaseBean<BankList>> bankList(@Header("X-Nideshop-Token") String token);


    //服务工时列表 ps：与商品分类一样，初始返回了第一种类的显示服务
    @POST("catalog/categoryServeList")
    @FormUrlEncoded
    Observable<BaseBean<CategoryBrandList>> categoryServeList(@FieldMap Map<String, Object> maps);


    //门店服务列表
    @POST("goods/serveList")
    Observable<BaseBean<ServerList>> goodsServeList(@Header("X-Nideshop-Token") String token);


    //微信收款码支付
    @POST("pay/prepay")
    Observable<BaseBean<WeixinCode>> prepay(@Header("X-Nideshop-Token") String token, @Body OrderInfoEntity infoEntity);

    //查微信支付成功通知
    @POST("pay/query")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> payQuery(@FieldMap Map<String, Object> maps);

    //添加快捷主推项目
    @POST("shopeasy/save")
    Observable<BaseBean<NullDataEntity>> shopeasySave(@Header("X-Nideshop-Token") String token, @Body GoodsEntity setProject);


    //修改快捷主推项目
    @POST("shopeasy/update")
    Observable<BaseBean<Integer>> shopeasyUpdate(@Header("X-Nideshop-Token") String token, @Body GoodsEntity setProject);


    //用户可用套餐列表
    @POST("activity/queryUserAct")
    @FormUrlEncoded
    Observable<BaseBean<Meal>> queryUserAct(@FieldMap Map<String, Object> maps);


    //申请提现
    @POST("userbalanceauth/ask")
    Observable<BaseBean<NullDataEntity>> ask(@Header("X-Nideshop-Token") String token, @Body UserBalanceAuthPojo maps);


    //商品规格
    @POST("goods/sku")
    @FormUrlEncoded
    Observable<BaseBean<ProductList>> sku(@FieldMap Map<String, Object> maps);


    /**
     * 车牌识别
     *
     * @param url https://api03.aliyun.venuscn.com/ocr/car-license
     * @param pic 车牌图像Base64字符串
     */
    @Headers({
            "Authorization:APPCODE 5ae54531c09a4e79a5464422c9c1c907",
            "Content-Type:application/x-www-form-urlencoded;charset=utf-8"
    })
    @POST()
    @FormUrlEncoded
    Observable<BaseBean2<CarNumberRecogResult>> carLicense(@Url String url, @Field("pic") String pic);


    /**
     * 车牌识别2 翔云-车牌识别API https://market.aliyun.com/products/57002002/cmapi025531.html?spm=5176.2020520132.101.8.737a7218kBVloz#sku=yuncode1953100000
     *
     * @param url http://anpr.sinosecu.com.cn/api/recogliu.do
     * @param img 车牌图像Base64字符串
     */
    @Headers({
            "Authorization:APPCODE 5ae54531c09a4e79a5464422c9c1c907",
            "Content-Type:application/x-www-form-urlencoded;charset=utf-8"
    })
    @POST()
    @FormUrlEncoded
    Observable<BaseBean3<Carsinfo>> carLicense2(@Url String url, @Field("img") String img);


    /**
     * 车辆vin识别
     *
     * @param url          https://market.aliyun.com/products/57124001/cmapi023049.html?spm=5176.2020520132.101.14.5d9a7218KamDd0#sku=yuncode1704900000
     * @param vinImageBody 车辆vin图像Base64字符串
     */
    @Headers({
            "Authorization:APPCODE 5ae54531c09a4e79a5464422c9c1c907",
            "Content-Type:application/x-www-form-urlencoded;charset=utf-8"
    })
    @POST()
    Observable<CarNumberRecogResult> carVinLicense(@Url String url, @Body VinImageBody vinImageBody);


    /**
     * 百度 车辆vin识别
     *
     * @param url          https://aip.baidubce.com/rest/2.0/ocr/v1/vin_code
     * @param vinImageBody 车辆vin图像Base64字符串
     */
    @Headers({
            "Content-Type:application/x-www-form-urlencoded;charset=utf-8"
    })
    @POST()
    Observable<CarNumberRecogResult> carVinLicenseBAIDU(@Url String url, @Body VinImageBody vinImageBody);


    /**
     * 车辆vin信息查询
     *
     * @param url https://ali-vin.showapi.com/vin
     * @param vin 车辆vin
     */
    @Headers({
            "Authorization:APPCODE 5ae54531c09a4e79a5464422c9c1c907",
            "Content-Type:application/json; charset=utf-8"
    })
    @GET()
    Observable<CarVin> carVinInfoQuery(@Url String url, @Query("vin") String vin);


    /**
     * 车辆vin信息查询服务商  http://www.easyepc123.com/interface
     *
     * @param url http://www.easyepc123.com/api/111002
     */
    @POST()
    Observable<CarVin2> carVinInfoQuery2(@Url String url, @Body CarVinRequest carVinRequest);


    //门店可录入套卡列表
    @POST("activity/queryAct")
    Observable<BaseBean<List<Meal2>>> queryAct(@Header("X-Nideshop-Token") String token);


    //检修单列表条件查询
    @POST("quotation/list")
    @FormUrlEncoded
    Observable<BaseBean<FixInfoList>> quotationList(@Header("X-Nideshop-Token") String token, @Field("status") int status, @Field("page") int page, @Field("limit") int limit);

    //检修单列表条件查询
    @POST("quotation/list")
    @FormUrlEncoded
    Observable<BaseBean<FixInfoList>> quotationList(@Header("X-Nideshop-Token") String token, @Field("page") int page, @Field("limit") int limit);


    //检修单列表条件查询
    @POST("quotation/list")
    @FormUrlEncoded
    Observable<BaseBean<FixInfoList>> quotationList(@Header("X-Nideshop-Token") String token, @Field("name") String name, @Field("page") int page, @Field("limit") int limit);

    //检修单列表条件查询
    @POST("quotation/list")
    @FormUrlEncoded
    Observable<BaseBean<FixInfoList>> quotationList(@FieldMap Map<String, Object> maps);


    //报价单取消
    @POST("quotation/cancle")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> quotationCancle(@Header("X-Nideshop-Token") String token, @Field("id") int id);

    /**
     * 纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
     */
    @POST("activity/queryConnectAct")
    @FormUrlEncoded
    Observable<BaseBean<RecordMeal>> queryConnectAct(@Header("X-Nideshop-Token") String token, @Field("name") String name);

    /**
     * 纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
     */
    @POST("activity/queryConnectAct")
    @FormUrlEncoded
    Observable<BaseBean<RecordMeal>> queryConnectAct(@Header("X-Nideshop-Token") String token, @Field("name") String name, @Field("user_id") String user_id);


    //纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
    @POST("activity/queryConnectAct")
    @FormUrlEncoded
    Observable<BaseBean<RecordMeal>> queryConnectAct(@Header("X-Nideshop-Token") String token, @Field("page") int page);

    //课程列表
    @POST("course/list")
    @FormUrlEncoded
    Observable<BaseBean<List<Courses>>> courseList2(@Header("X-Nideshop-Token") String token, @Field("name") String name, @Field("course_type") String course_type, @Field("limit") int limit);

    //课程列表
    @POST("course/list")
    @FormUrlEncoded
    Observable<BaseBean<List<Courses>>> courseList2(@Header("X-Nideshop-Token") String token, @Field("limit") int limit);

    //课程分类
    @POST("course/queryCategory")
    @FormUrlEncoded
    Observable<BaseBean<List<CategoryType>>> queryCategory(@Header("X-Nideshop-Token") String token);

    //课程详情
    @POST("course/info")
    @FormUrlEncoded
    Observable<BaseBean<CourseInfo>> courseInfo(@Header("X-Nideshop-Token") String token, @Field("id") int id);

    //课程学习记录列表
    @POST("course/queryWatchLog")
    @FormUrlEncoded
    Observable<BaseBean<List<CourseRecord>>> courseRecord(@Header("X-Nideshop-Token") String token, @Field("limit") int limit);


    //用户点击视频观看退出时访问，用来增加记录
    @POST("course/addWatchLog")
    Observable<BaseBean<NullDataEntity>> addWatchLog(@Header("X-Nideshop-Token") String token, @Body CourseRecord record);


    //检查版本更新
    @POST("work/getAppVersion")
    Observable<BaseBean<VersionInfo>> checkVersionUpDate(@Header("X-Nideshop-Token") String token);

    //根据vid获取视频
    @POST("course/resourceUrl")
    @FormUrlEncoded
    Observable<BaseBean<Video>> resourceUrl(@Header("X-Nideshop-Token") String token, @Field("videoId") String videoId);


    //获取员工详情
    @POST("sysuser/detail")
    @FormUrlEncoded
    Observable<BaseBean<TechnicianInfo>> sysuserDetail(@Header("X-Nideshop-Token") String token, @Field("id") int id);


    //修改员工
    @POST("sysuser/update")
    Observable<BaseBean<NullDataEntity>> sysuserUpdate(@Header("X-Nideshop-Token") String token, @Body Technician technicianInfo);

    //
    //添加员工
    @POST("sysuser/save")
    Observable<BaseBean<NullDataEntity>> sysuserSave(@Header("X-Nideshop-Token") String token, @Body Technician technicianInfo);


    //删除员工
    @POST("sysuser/delete")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> sysuserDelete(@Header("X-Nideshop-Token") String token, @Field("id") int id);

    //供选择的角色列表
    @POST("sysuser/queryRoles")
    Observable<BaseBean<List<Roles>>> queryRoles(@Header("X-Nideshop-Token") String token);


    //获取员工服务订单
    @POST("sysuser/orderList")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderInfoEntity>>> sysOrderList(@FieldMap Map<String, Object> maps);

    //获取员工销售订单
    @POST("sysuser/saleList")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderInfoEntity>>> saleList(@FieldMap Map<String, Object> maps);


    //更改客户信息发送验证码
    @POST("sms/updateUserSms")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> updateUserSms(@FieldMap Map<String, Object> maps);


    //门店检修报告选项列表
    @POST("shop/queryCheckOptions")
    Observable<BaseBean<List<CheckOptions>>> queryCheckOptions(@Header("X-Nideshop-Token") String token);


    //暂存或者生成检测报告
    @POST("shop/checkOutResult")
    Observable<BaseBean<NullDataEntity>> checkOutResult(@Header("X-Nideshop-Token") String token, @Body CarCheckResul checkResul);


    //检测报告单列表
    @POST("shop/checkResultList")
    @FormUrlEncoded
    Observable<BaseBean<List<CarCheckResul>>> checkResultList(@FieldMap Map<String, Object> maps);

    //检测报告单列表
    @POST("shop/checkResultDetail")
    @FormUrlEncoded
    Observable<BaseBean<CarCheckResul>> checkResultDetail(@FieldMap Map<String, Object> maps);

    //修改暂存的门店检测报告 type为1的不可访问此接口
    @POST("shop/updateCheckResult")
    Observable<BaseBean<NullDataEntity>> updateCheckResult(@Header("X-Nideshop-Token") String token, @Body CarCheckResul checkResul);


    //门店服务过的车辆管理
    @POST("user/shopCarList")
    @FormUrlEncoded
    Observable<BaseBean<ShopCarBane>> shopCarList(@FieldMap Map<String, Object> maps);

    //获取商品分类
    @POST("shopcategory/queryAll")
    @FormUrlEncoded
    Observable<BaseBean<List<GoodsCategory>>> queryShopcategoryAll(@FieldMap Map<String, Object> maps);

    //根据商品信息
    @POST("shopcategory/info")
    @FormUrlEncoded
    Observable<BaseBean<List<GoodsBrand>>> shopcategoryInfo(@FieldMap Map<String, Object> maps);

    //库存管理新增商品里的查找所有品牌
    @POST("brandGoods/list")
    @FormUrlEncoded
    Observable<BaseBean<List<GoodsBrand>>> brandGoodsList(@FieldMap Map<String, Object> maps);

    //查询商品（分页）
    @POST("xgxshopgoods/list")
    @FormUrlEncoded
    Observable<BaseBean<GoodsList>> xgxshopgoodsList(@FieldMap Map<String, Object> maps);

    //查询商品详情
    @POST("xgxshopgoods/info")
    @FormUrlEncoded
    Observable<BaseBean<Goods>> xgxshopgoodsInfo(@FieldMap Map<String, Object> maps);

    //获取登录用户购物车数据
    @POST("cart/getCart")
    @FormUrlEncoded
    Observable<BaseBean<CartList>> getShoppingCart(@Header("X-Nideshop-Token") String token, @Field("status") String status, @Field("sessionId") String sessionId);

    //删除购物车商品
    @POST("cart/delete")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> delete(@Header("X-Nideshop-Token") String token, @Field("cartIds") int cartIds[]);

    //添加购物车
    @POST("cart/add")
    @FormUrlEncoded
    Observable<BaseBean<CartList>> addToShoppingCart(@FieldMap Map<String, Object> maps);


    //更新商品购物车数据
    @POST("cart/update")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> shoppingCartUpdate(@FieldMap Map<String, Object> maps);

    //购物车商品下单
    @POST("purchaseorder/save")
    Observable<BaseBean<Integer>> mallMakeOrder(@Header("X-Nideshop-Token") String token, @Body XgxPurchaseOrderPojo purchaseOrderPojo);


    //购物车商品下单
    @POST("purchaseorder/info")
    @FormUrlEncoded
    Observable<BaseBean<XgxPurchaseOrderPojo>> mallOrderInfo(@Header("X-Nideshop-Token") String token, @Field("id") int id);


    //调用微信支付
    @POST("purchasePay/prepay")
    @FormUrlEncoded
    Observable<BaseBean<PayInfo>> prepay(@Header("X-Nideshop-Token") String token, @Field("orderId") int orderId);

    //查看采购订单列表（根据登录用户查询）
    @POST("purchaseorder/list")
    @FormUrlEncoded
    Observable<BaseBean<BasePage<XgxPurchaseOrderPojo>>> purchaseorderList(@FieldMap Map<String, Object> maps);


    //营销短信模板
    @POST("sms/smsTemplates")
    Observable<BaseBean<List<SmsTemplates>>> smsTemplates(@Header("X-Nideshop-Token") String token);

    //发送营销短信
    @POST("sms/sendAdSms")
    Observable<BaseBean<NullDataEntity>> sendAdSms(@Header("X-Nideshop-Token") String token, @Body SmsTemplates smsTemplates);

    //短信推送记录列表
    @POST("sms/marketLogs")
    Observable<BaseBean<List<MessageRecordEntity>>> marketLogs(@Header("X-Nideshop-Token") String token);

    //app运行异常记录
    @POST("work/saveError")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> saveError(@FieldMap Map<String, Object> maps);


    //出入库操作
    @POST("xgxshopstocklog/inOrOut")
    Observable<BaseBean<NullDataEntity>> inOrOut(@Header("X-Nideshop-Token") String token, @Body StockInOrOut stock);

    //出入库记录详情
    @POST("xgxshopstocklog/info")
    @FormUrlEncoded
    Observable<BaseBean<StockInOrOut>> info(@FieldMap Map<String, Object> maps);


    //添加库存商品的规格
    @POST("xgxshopgoods/addStandard")
    Observable<BaseBean<NullDataEntity>> addStandard(@Header("X-Nideshop-Token") String token, @Body Goods.GoodsStandard goodsStandard);

    //修改某个规格详情
    @POST("xgxshopgoods/updateStandard")
    Observable<BaseBean<NullDataEntity>> updateStandard(@Header("X-Nideshop-Token") String token, @Body Goods.GoodsStandard goodsStandard);

    //库存商品删除某个规格
    @POST("xgxshopgoods/deleteStandard")
    Observable<BaseBean<NullDataEntity>> deleteStandard(@Header("X-Nideshop-Token") String token, @Body Goods.GoodsStandard goodsStandard);

    //查看每个规格详情
    @POST("xgxshopgoods/standardInfo")
    @FormUrlEncoded
    Observable<BaseBean<Goods.GoodsStandard>> standardInfo(@Header("X-Nideshop-Token") String token, @Field("id") int id);

    //添加库存商品
    @POST("xgxshopgoods/addStock")
    Observable<BaseBean<Integer>> addGoods(@Header("X-Nideshop-Token") String token, @Body Goods goods);

    //编辑库存商品
    @POST("xgxshopgoods/update")
    Observable<BaseBean<NullDataEntity>> fixGoods(@Header("X-Nideshop-Token") String token, @Body Goods goods);

    //添加商品图片
    @POST("xgxshopgoods/addDetails")
    Observable<BaseBean<NullDataEntity>> addDetails(@Header("X-Nideshop-Token") String token, @Body Goods.GoodsPic goodsPic);


    //删除商品图
    @POST("xgxshopgoods/deleteDetails")
    @FormUrlEncoded
    Observable<BaseBean<NullDataEntity>> deleteDetails(@Header("X-Nideshop-Token") String token, @Field("id") String id);

    //添加供应商
    @POST("xgxshopsupplier/save")
    Observable<BaseBean<NullDataEntity>> addSupplier(@Header("X-Nideshop-Token") String token, @Body Supplier supplier);

    //修改供应商
    @POST("xgxshopsupplier/update")
    Observable<BaseBean<NullDataEntity>> fixSupplier(@Header("X-Nideshop-Token") String token, @Body Supplier supplier);

    //供应商详情
    @POST("xgxshopsupplier/info")
    @FormUrlEncoded
    Observable<BaseBean<Supplier>> infoSupplier(@Header("X-Nideshop-Token") String token, @Field("id") String id);


    //供应商列表
    @POST("xgxshopsupplier/list")
    Observable<BaseBean<List<Supplier>>> listSupplier(@Header("X-Nideshop-Token") String token);


    //活动列表
    @POST("xgxshopunity/list")
    Observable<BaseBean<List<Maneuver>>> listShopunity(@Header("X-Nideshop-Token") String token);


    //
    //活动详情页
    @POST("xgxshopunity/info")
    @FormUrlEncoded
    Observable<BaseBean<Maneuver>> infoShopunity(@Header("X-Nideshop-Token") String token, @Field("id") String id);


    //反馈列表
    @POST("xgxshopunity/askList")
    @FormUrlEncoded
    Observable<BaseBean<List<Ask>>> askList(@Header("X-Nideshop-Token") String token, @Field("unity_id") String unity_id);

    //反馈列表
    @POST("xgxshopunity/askList")
    @FormUrlEncoded
    Observable<BaseBean<List<Ask>>> askList(@Header("X-Nideshop-Token") String token, @Field("unity_id") String unity_id, @Field("user") String user);


    //提交反馈
    @POST("xgxshopunity/askTo")
    Observable<BaseBean<NullDataEntity>> askTo(@Header("X-Nideshop-Token") String token, @Body Ask ask);

    //报名参加
    @POST("xgxshopunity/joinIn")
    Observable<BaseBean<NullDataEntity>> joinIn(@Header("X-Nideshop-Token") String token, @Body Ask ask);


    //出入库记录列表
    @POST("xgxshopstocklog/list")
    @FormUrlEncoded
    Observable<BaseBean<List<StockInOrOut>>> stockInOrOutRecordList(@Header("X-Nideshop-Token") String token, @Field("type") int type);


    //出库时匹配订单
    @POST("xgxshopstocklog/matchOrder")
    @FormUrlEncoded
    Observable<BaseBean<List<StockGoods>>> matchOrder(@Header("X-Nideshop-Token") String token, @Field("order_id") int order_id);

    //查看已完成订单技师绩效分配
    @POST("order/selectOrderDeduction")
    @FormUrlEncoded
    Observable<BaseBean<List<Technician>>> getOrderDeduction(@Header("X-Nideshop-Token") String token, @Field("order_id") int order_id);

    //设置员工绩效分配
    @POST("order/setDeduction")
    Observable<BaseBean<NullDataEntity>> setDeduction(@Header("X-Nideshop-Token") String token, @Body List<Technician> list);


    //门店优惠券模板列表
    @POST("coupon/shopCouponList")
    Observable<BaseBean<List<Coupon2>>> shopCouponList(@Header("X-Nideshop-Token") String token);

    //添加优惠模板
    @POST("coupon/addShopCoupon")
    Observable<BaseBean<NullDataEntity>> addShopCoupon(@Header("X-Nideshop-Token") String token, @Body Coupon2 coupon2);

    //修改优惠模板
    @POST("coupon/updateShopCoupon")
    Observable<BaseBean<NullDataEntity>> fixShopCoupon(@Header("X-Nideshop-Token") String token, @Body Coupon2 coupon2);

    //优惠券派发记录列表 搜索

    @POST("coupon/pushLogList")
    @FormUrlEncoded
    Observable<BaseBean<List<Coupon2>>> couponPostRecode(@Header("X-Nideshop-Token") String token, @Field("dateStart") String dateStart, @Field("dateEnd") String dateEnd, @Field("name") int name);

    //优惠券派发记录列表
    @POST("coupon/pushLogList")
    Observable<BaseBean<List<CouponRecode>>> couponPostRecode(@Header("X-Nideshop-Token") String token);

    //提现申请列表
    @POST("userbalanceauth/list")
    Observable<BaseBean<DisRecordList>> ceauthList(@Header("X-Nideshop-Token") String token);

    //派发优惠券记录详情
    @POST("coupon/pushLogInfo")
    @FormUrlEncoded
    Observable<BaseBean<CouponRecode>> pushLogInfo(@Header("X-Nideshop-Token") String token, @Field("id") String id);

    //派发优惠券
    @POST("coupon/pushCoupon")
    Observable<BaseBean<NullDataEntity>> pushCoupon(@Header("X-Nideshop-Token") String token, @Body CouponRecode couponRecode);

    //查看优惠券模板详情
    @POST("coupon/shopCouponInfo")
    @FormUrlEncoded
    Observable<BaseBean<Coupon2>> shopCouponInfo(@Header("X-Nideshop-Token") String token, @Field("id") String id);

    //获取当前登录用户
    @POST("/app/sysuser/getInfo")
    Observable<BaseBean<UserEntity>> getInfo(@Header("X-Nideshop-Token") String token);

}
