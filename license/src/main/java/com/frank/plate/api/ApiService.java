package com.frank.plate.api;

import com.frank.plate.bean.ActivityEntity;
import com.frank.plate.bean.ActivityEntityItem;
import com.frank.plate.bean.ActivityPage;
import com.frank.plate.bean.AutoBrand;
import com.frank.plate.bean.AutoModel;
import com.frank.plate.bean.BankList;
import com.frank.plate.bean.BaseBean;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.CarInfoEntity;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.Card;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.Coupon;
import com.frank.plate.bean.Course;
import com.frank.plate.bean.GoodsEntity;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.Meal;

import com.frank.plate.bean.Member;
import com.frank.plate.bean.MemberOrder;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.ProductList;
import com.frank.plate.bean.ProductValue;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.bean.SetProject;
import com.frank.plate.bean.Shop;

import com.frank.plate.bean.Technician;
import com.frank.plate.bean.Token;
import com.frank.plate.bean.UserBalanceAuthPojo;
import com.frank.plate.bean.UserInfo;
import com.frank.plate.bean.WeixinCode;
import com.frank.plate.bean.WorkIndex;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
    Observable<BaseBean<NullDataEntity>> addCarInfo(@Header("X-Nideshop-Token") String token, @Body CarInfoRequestParameters event);

    //修改车况
    @POST("usercarcondition/update")
    Observable<BaseBean<NullDataEntity>> fixCarInfo(@Header("X-Nideshop-Token") String token, @Body CarInfoRequestParameters event);


    //门店信息
    @POST("shop/info")
    @FormUrlEncoded
    Observable<BaseBean<Shop>> shopInfo(@FieldMap Map<String, Object> maps);

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
    Observable<BaseBean<NullDataEntity>> bankSave(@Header("X-Nideshop-Token") String token,@Body Card maps);

    //查看银行卡
    @POST("bank/list")
    Observable<BaseBean<BankList>> bankList(@Header("X-Nideshop-Token") String token);


    //服务工时列表 ps：与商品分类一样，初始返回了第一种类的显示服务
    @POST("catalog/categoryServeList")
    @FormUrlEncoded
    Observable<BaseBean<CategoryBrandList>> categoryServeList(@FieldMap Map<String, Object> maps);


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
    Observable<BaseBean<NullDataEntity>> shopeasyUpdate(@Header("X-Nideshop-Token") String token, @Body GoodsEntity setProject);


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


}
