package com.frank.plate.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.plate.Configure;
import com.frank.plate.MyApplication;
import com.frank.plate.activity.WeiXinPayCodeActivity;
import com.frank.plate.bean.ActivityEntity;
import com.frank.plate.bean.ActivityEntityItem;
import com.frank.plate.bean.AutoBrand;
import com.frank.plate.bean.AutoModel;
import com.frank.plate.bean.BaseBean;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.Card;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.Coupon;
import com.frank.plate.bean.Course;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.Meal;

import com.frank.plate.bean.Member;
import com.frank.plate.bean.MemberOrder;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.bean.SetProject;
import com.frank.plate.bean.Shop;
import com.frank.plate.bean.ShopEntity;
import com.frank.plate.bean.Technician;
import com.frank.plate.bean.Token;
import com.frank.plate.bean.WeixinCode;
import com.frank.plate.bean.WorkIndex;

import net.grandcentrix.tray.AppPreferences;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ApiLoader {

    private ApiService apiService;

    Map<String, Object> map = new HashMap<>();
    String token;

    public ApiLoader(Context context) {

        token = new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, "");
        Log.i("apiService", "X-Nideshop-Token:  " + token);
        apiService = RetrofitServiceManager.getInstance().create(ApiService.class);
        map.put("X-Nideshop-Token", new AppPreferences(context).getString(Configure.Token, ""));
    }

    /**
     * 1.拍照接单自动查找订单或车况
     *
     * @return
     */
    public Observable<QueryByCarEntity> queryByCar(String car_no) {
        map.put("car_no", car_no);
        return apiService.queryByCar(map).compose(RxHelper.<QueryByCarEntity>observe());

    }

    /**
     * 账单统计加列表
     *
     * @return
     */
    public Observable<BillEntity> getUserBillList(int isShowAll) {


        //选日期需要添加，不添加默认取本月	Date before, Date after


        //如需分页则添加	分页参数（int page，int limit，String sidx，String order）
//        map.put("page", page);
//        map.put("limit", limit);
//        map.put("sidx", sidx);
//        map.put("order", order);
//        map.put("type", type);

        if (isShowAll == 0) {
            List<Integer> list = new ArrayList<>();
            list.add(3);
            list.add(4);
            return apiService.getUserBillList(token, list).compose(RxHelper.<BillEntity>observe());
        } else
            return apiService.getUserBillList(map).compose(RxHelper.<BillEntity>observe());


    }

    /**
     * 账单统计加列表 收入账单 与我的账单列表一个接口，多一个参数
     *
     * @return
     */
    public Observable<BillEntity> getUserBillList(Date before, Date after) {


        //选日期需要添加，不添加默认取本月	Date before, Date after


        map.put("before", before.getTime());
        map.put("after", after.getTime());


        return apiService.getUserBillList(map).compose(RxHelper.<BillEntity>observe());

    }

    /**
     * 会员录入
     *
     * @return
     */
    public Observable<SaveUserAndCarEntity> saveUserAndCar(String car_no, String mobile, String username) {

        map.put("car_no", car_no);
        map.put("mobile", mobile);
        map.put("username", username);
        return apiService.saveUserAndCar(map).compose(RxHelper.<SaveUserAndCarEntity>observe());
    }

    /**
     * 2.会员快捷录入
     *
     * @return
     */
    public Observable<SaveUserAndCarEntity> addUser(String mobile, String username) {

        map.put("mobile", mobile);
        map.put("username", username);

        return apiService.addUser(map).compose(RxHelper.<SaveUserAndCarEntity>observe());
    }

    /**
     * 7.新增车况
     *
     * @return
     */
    public Observable<NullDataEntity> addCarInfo(CarInfoRequestParameters parameters) {

        return apiService.addCarInfo(token, parameters).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 8.修改车况
     *
     * @return
     */
    public Observable<NullDataEntity> fixCarInfo(CarInfoRequestParameters parameters) {

        return apiService.fixCarInfo(token, parameters).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 4.批量删除车况图片
     *
     * @return
     */
    public Observable<NullDataEntity> delete(List<Integer> integers) {

        return apiService.delete(token, integers).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 4.批量删除车况图片
     *
     * @return
     */
    public Observable<NullDataEntity> delete(int id) {


        List<Integer> integers = new ArrayList<>();
        integers.add(id);


        return delete(integers);


    }


    /**
     * 确认下单
     *
     * @return
     */
    public Observable<OrderInfo> submit(OrderInfoEntity infoEntity) {


        return apiService.submit(token, infoEntity).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 订单修改 orderInfo类
     *
     * @return
     */
    public Observable<OrderInfo> remake(OrderInfoEntity infoEntity) {


        return apiService.remake(token, infoEntity).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 4.开始服务(修改订单状态为服务中)
     *
     * @return
     */
    public Observable<NullDataEntity> beginServe(int order_id, String order_sn) {

        map.put("order_id", order_id);
        map.put("order_sn", order_sn);
        return apiService.beginServe(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 14.车况详情显示
     *
     * @param id 车况主键
     * @return
     */
    public Observable<CarInfoRequestParameters> showCarInfo(int id) {

        map.put("id", id);

        return apiService.showCarInfo(map).compose(RxHelper.<CarInfoRequestParameters>observe());
    }


    /**
     * 15.当前门店用户（技师）列表
     *
     * @return
     */
    public Observable<BasePage<Technician>> sysuserList() {

        return apiService.sysuserList(map).compose(RxHelper.<BasePage<Technician>>observe());
    }


    /**
     * 服务工时列表 ps：与商品分类一样，初始返回了第一种类的显示服务
     */
    public Observable<CategoryBrandList> categoryServeList() {
        return apiService.categoryServeList(map).compose(RxHelper.<CategoryBrandList>observe());
    }

    /**
     * 9.分类下品牌列表加第一个品牌第一页下商品
     *
     * @return
     */
    public Observable<CategoryBrandList> categoryBrandList() {
        return apiService.categoryBrandList(map).compose(RxHelper.<CategoryBrandList>observe());
    }

    /**
     * 10.查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     *
     * @return
     */
    public Observable<GoodsListEntity> queryAnyGoods(String category_id, String brand_id, String name) {


        map.put("category_id", category_id); //商品类别
        map.put("brand_id", brand_id);//品牌
        map.put("name", name);//查询关键字

        return apiService.queryAnyGoods(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 10.四个火热商品
     *
     * @return
     */
    public Observable<GoodsListEntity> shopeasyList() {

        return apiService.shopeasyList(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 确认订单最后完成
     *
     * @return
     */
    public Observable<NullDataEntity> confirmFinish(int order_id) {

        map.put("order_id", order_id);
        return apiService.confirmFinish(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 确认支付
     *
     * @return
     */
    public Observable<NullDataEntity> confirmPay(OrderInfoEntity infoEntity) {

        return apiService.confirmPay(token, infoEntity).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList() {

        return apiService.orderList(map).compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }

    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList(String order_status) {


        if (order_status.equals("00"))
            order_status = "0";

        if (!order_status.equals("")) {
            map.put("order_status", order_status);
        }

        return apiService.orderList(map).compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }


    /**
     * 订单详情页
     *
     * @returnD
     */
    public Observable<OrderInfo> orderDetail(int id) {

        map.put("id", id);
        return apiService.orderDetail(map).compose(RxHelper.<OrderInfo>observe());
    }

    /**
     * 账单详情（同订单详情一个接口，入参不同）
     *
     * @returnD
     */
    public Observable<OrderInfo> orderDetail(String order_sn) {

        map.put("order_sn", order_sn);
        return apiService.orderDetail(map).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 活动列表
     */
    public Observable<ActivityEntity> activityList(int activity_type, String activity_name) {

        map.put("activity_type", activity_type);//=1.平台活动 =3.门店活动
        map.put("activity_name", activity_name);//查询关键字

        return apiService.activityList(map).compose(RxHelper.<ActivityEntity>observe());
    }

    /**
     * 活动详情
     */
    public Observable<ActivityEntityItem> activityDetail(int id) {

        map.put("id", id);

        return apiService.activityDetail(map).compose(RxHelper.<ActivityEntityItem>observe());
    }

    /**
     * 品牌查询列表
     */
    public Observable<List<AutoBrand>> listByName() {

        return apiService.listByName(token).compose(RxHelper.<List<AutoBrand>>observe());
    }

    /**
     * 通过品牌查车型列表
     */
    public Observable<List<AutoModel>> listByBrand(int brand_id) {

        map.put("brand_id", brand_id);
        return apiService.listByBrand(map).compose(RxHelper.<List<AutoModel>>observe());
    }


    /**
     * 工作台首页
     */
    public Observable<WorkIndex> workIndex() {

        return apiService.workIndex(map).compose(RxHelper.<WorkIndex>observe());
    }


    /**
     * 会员管理页面数据
     */
    public Observable<Member> memberList() {

        return apiService.memberList(map).compose(RxHelper.<Member>observe());
    }


    /**
     * 查看会员信息及订单记录
     */
    public Observable<MemberOrder> memberOrderList(int user_id) {

        map.put("user_id", user_id);

        return apiService.memberOrderList(map).compose(RxHelper.<MemberOrder>observe());
    }

    /**
     * 门店信息
     */
    public Observable<Shop> shopInfo() {

        return apiService.shopInfo(map).compose(RxHelper.<Shop>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<List<Coupon>> couponList(String order_price, int user_id) {

        map.put("order_price", order_price);
        map.put("user_id", user_id);

        return apiService.couponList(map).compose(RxHelper.<List<Coupon>>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<MyBalanceEntity> balanceInfo() {

        return apiService.balanceInfo(map).compose(RxHelper.<MyBalanceEntity>observe());
    }

    /**
     * 课程列表
     */
    public Observable<List<Course>> courseList(int course_type) {
        // course_type	1为线下 2为线上
        map.put("course_type", course_type);
        return apiService.courseList(map).compose(RxHelper.<List<Course>>observe());
    }

    /**
     * 课程报名
     */
    public Observable<NullDataEntity> coursejoinnameSave(String name, String mobile) {

        map.put("name", name);
        map.put("mobile", mobile);
        return apiService.coursejoinnameSave(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 添加反馈
     */
    public Observable<String> feedbackSave(String content) {

        map.put("content", content);
        return apiService.feedbackSave(map).compose(RxHelper.<String>observe());
    }


    /**
     * 短信验证码
     */
    public Observable<NullDataEntity> smsSendSms(String mobile, int type) {

        map.put("mobile", mobile);
        map.put("type", type);//1登陆2体现3银行卡验证
        return apiService.smsSendSms(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 短信验证码
     */
    public Disposable smsSendSms(String mobile, int type, final TextView tv, final Context context) {
        final String TAG = "短信验证码:";
        final int con = 60;

        map.put("mobile", mobile);
        map.put("type", type);//1登陆2体现3银行卡验证
        final Disposable[] disposable = new Disposable[1];

        apiService.smsSendSms(map).compose(RxHelper.<NullDataEntity>observe()).subscribe(new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                disposable[0] = Observable //计时器
                        .interval(0, 1, TimeUnit.SECONDS)
                        .take(con)//次数
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) {
                                Log.e(TAG, "onNext");
                                Long l = con - aLong;
                                tv.setText(l + "s");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                Log.e(TAG, "onError");
                                tv.setClickable(true);
                                throwable.printStackTrace();
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                Log.e(TAG, "onComplete");
                                tv.setText("获取验证码");
                                tv.setClickable(true);
                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) {
                                Log.e(TAG, "onSubscribe");
                                tv.setClickable(false);
                            }
                        });
            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
        return disposable[0];
    }

    /**
     * 登录
     */
    public Observable<Token> login(String mobile, String et_car_code) {
        map.put("mobile", mobile);
        map.put("authCode", et_car_code);
        return apiService.login(map).compose(RxHelper.<Token>observe());
    }


    /**
     * 添加银行卡
     */
    public Observable<NullDataEntity> bankSave(String authCode, Card card) {
        map.put("authCode", authCode);
        map.put("cardNumber", card.getCardNumber());
        map.put("bankName", card.getBankName());
        map.put("bankAddr", card.getBankAddr());
        map.put("cardholder", card.getCardholder());
        return apiService.bankSave(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 微信收款码支付
     */
    public Observable<WeixinCode> prepay(OrderInfoEntity infoEntity) {


        return apiService.prepay(token, infoEntity).compose(RxHelper.<WeixinCode>observe());
    }

    /**
     * 查微信支付成功通知
     */
    public Observable<NullDataEntity> payQuery(int orderId) {
        map.put("id", orderId);
        return apiService.payQuery(map).compose(RxHelper.<NullDataEntity>observe());

    }


    /**
     * 添加快捷主推项目
     */
    public Observable<NullDataEntity> shopeasySave(SetProject setProject) {
        return apiService.shopeasySave(token, setProject).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 修改快捷主推项目
     */
    public Observable<NullDataEntity> shopeasyUpdate(SetProject setProject) {

        return apiService.shopeasyUpdate(token, setProject).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 用户可用套餐列表
     */
    public Observable<Meal> queryUserAct(int user_id) {
        map.put("user_id", user_id);
        return apiService.queryUserAct(map).compose(RxHelper.<Meal>observe());
    }


}
