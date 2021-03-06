package com.eb.geaiche.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.eb.geaiche.bean.RecordMeal;
import com.eb.geaiche.stockControl.bean.StockGoods;
import com.eb.geaiche.stockControl.bean.StockInOrOut;
import com.eb.geaiche.stockControl.bean.Supplier;
import com.eb.geaiche.util.BitmapUtil;
import com.eb.geaiche.util.CameraThreadPool;
import com.eb.geaiche.util.DateUtil;
import com.eb.geaiche.util.FileUtil;
import com.eb.geaiche.util.MyAppPreferences;
import com.eb.geaiche.util.SystemUtil;
import com.eb.geaiche.vehicleQueue.VehicleQueue;
import com.juner.mvp.Configure;
import com.eb.geaiche.MyApplication;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.bean.ActivityEntity;
import com.juner.mvp.bean.ActivityPage;
import com.eb.geaiche.bean.AutoBrand;
import com.juner.mvp.bean.Ask;
import com.juner.mvp.bean.AutoModel;
import com.juner.mvp.bean.BankList;
import com.juner.mvp.bean.Banner;
import com.juner.mvp.bean.BasePage;
import com.juner.mvp.bean.BillEntity;
import com.juner.mvp.bean.CarCheckResul;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.CarNumberRecogResult;
import com.juner.mvp.bean.CarVin;
import com.juner.mvp.bean.CarVin2;
import com.juner.mvp.bean.CarVinInfo2;
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
import com.juner.mvp.bean.StaffPerformance;
import com.juner.mvp.bean.Technician;
import com.juner.mvp.bean.TechnicianInfo;
import com.juner.mvp.bean.Token;
import com.juner.mvp.bean.UserBalanceAuthPojo;
import com.juner.mvp.bean.UserEntity;
import com.juner.mvp.bean.VersionInfo;
import com.juner.mvp.bean.Vin;
import com.juner.mvp.bean.VinImageBody;
import com.juner.mvp.bean.WeixinCode;
import com.juner.mvp.bean.WorkIndex;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.XgxPurchaseOrderPojo;

import net.grandcentrix.tray.AppPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.graphics.Bitmap.Config.RGB_565;

public class ApiLoader {

    private ApiService apiService;

    Map<String, Object> map = new HashMap<>();
    String token;
    Context context;

    public ApiLoader(Context context) {
        this.context = context;
        token = new AppPreferences(context).getString(Configure.Token, "");
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
    public Observable<BillEntity> getUserBillList(int isShowAll, int page) {

        //如需分页则添加	分页参数（int page，int limit，String sidx，String order）
        map.put("page", page);
        map.put("limit", Configure.limit_page);
        List<Integer> list = new ArrayList<>();
        if (isShowAll == 0) {
            list.add(3);
            list.add(4);

        } else {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
        }

        map.put("type", list);
        return apiService.getUserBillList(map).compose(RxHelper.<BillEntity>observe());

    }

    /**
     * 账单统计加列表 收入账单 与我的账单列表一个接口，多一个参数
     *
     * @param isdate 是否用时间
     * @return
     */
    public Observable<BillEntity> getUserBillList(Date after, Date before, boolean isdate, int isShowAll, int page) {


        //选日期需要添加，不添加默认取本月	Date before, Date after
        if (isdate) {
            map.put("before", before.getTime());
            map.put("after", after.getTime());
        }

        return getUserBillList(isShowAll, page);

    }

    /**
     * 2.会员快捷录入
     *
     * @return
     */
    public Observable<SaveUserAndCarEntity> addUser(String mobile, String username) {

        map.put("mobile", mobile);
        map.put("username", username);

        return apiService.addUser(map).compose(RxHelper.observe());
    }

    /**
     * 7.新增车况
     *
     * @return
     */
    public Observable<Integer> addCarInfo(CarInfoRequestParameters parameters) {

        return apiService.addCarInfo(token, parameters).compose(RxHelper.observe());
    }


    /**
     * 8.修改车况
     *
     * @return
     */
    public Observable<NullDataEntity> fixCarInfo(CarInfoRequestParameters parameters) {

        return apiService.fixCarInfo(token, parameters).compose(RxHelper.observe());
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


        return apiService.submit(token, infoEntity).compose(RxHelper.observe());
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
    public Observable<NullDataEntity> beginServe(int order_id, String order_sn, String district) {

        map.put("order_id", order_id);
        map.put("order_sn", order_sn);
        map.put("district", district);
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
        map.put("limit", 100);//页数
        return apiService.sysuserList(map).compose(RxHelper.<BasePage<Technician>>observe());
    }


    /**
     * 服务工时列表 ps：与商品分类一样，初始返回了第一种类的显示服务
     */
    public Observable<CategoryBrandList> categoryServeList() {
        return apiService.categoryServeList(map).compose(RxHelper.<CategoryBrandList>observe());
    }

    /**
     * 门店服务列表
     */
    public Observable<ServerList> goodsServeList() {
        return apiService.goodsServeList(token).compose(RxHelper.<ServerList>observe());
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


        map.put("attribute_category", category_id); //商品类别
        map.put("brand_id", brand_id);//品牌
        map.put("name", name);//查询关键字

        return apiService.queryAnyGoods(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 10.查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     *
     * @return
     */
    public Observable<GoodsListEntity> queryAnyGoods(String name) {


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

        return apiService.orderList(map).compose(RxHelper.observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList(int position, int page) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("page", page);
        map.put("limit", Configure.limit_page);
        switch (position) {
            case 0:
                break;
            case 1:
                map.put("order_status", "0");
                map.put("pay_status", "0");
                break;
            case 2:
                map.put("order_status", "0");
                map.put("pay_status", "2");
                break;
            case 3:
                map.put("order_status", "1");
                break;
            case 4:
                map.put("order_status", "2");
                map.put("pay_status", "2");

                break;


        }

        return apiService.orderList(map).compose(RxHelper.observe());
    }

    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @param before 开始时间  毫秒数小
     * @param after  结束时间  毫秒数大
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList(Date before, Date after, boolean isdate, String name, int page) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        if (isdate)
            map.put("limit", 100);
        else
            map.put("limit", Configure.limit_page);
        if (!TextUtils.isEmpty(name))
            map.put("name", name);
        map.put("page", page);

        //选日期需要添加，不添加默认取本月	Date before, Date after
        if (isdate) {
            map.put("dateStart", before.getTime());
            map.put("dateEnd", after.getTime());
        }

        return apiService.orderList(map).compose(RxHelper.observe());
    }

    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList(int page, String user_id) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("limit", Configure.limit_page);
        map.put("page", page);
        map.put("user_id", user_id);
        return apiService.orderList(map).compose(RxHelper.observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @param deduction_status 是否分配过业绩，1是0否
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderStatusList(String deduction_status, int page) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("limit", Configure.limit_page);
//        map.put("limit", 100);
        map.put("page", page);
        if (null != deduction_status)
            map.put("deduction_status", deduction_status);


        map.put("order_type", "2");//服务完成后的订单


        return apiService.orderList(map).compose(RxHelper.observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList4DayOfMoon(int page, int type) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("page", page);
        map.put("limit", Configure.limit_page);

        if (type == 0) {
            map.put("dateStart", System.currentTimeMillis() / 1000 * 1000);//当前
            map.put("dateEnd", System.currentTimeMillis() / 1000 * 1000 + 60 * 60 * 24 * 1000);
        } else {
            Calendar startDate = Calendar.getInstance();
            startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 1, 0, 0, 0);
            map.put("dateStart", startDate.getTime().getTime() / 1000 * 1000);//当前
//            map.put("dateEnd", System.currentTimeMillis() / 1000 * 1000 + 60 * 60 * 24 * 1000);
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
        return apiService.orderDetail(map).compose(RxHelper.observe());
    }


    /**
     * 删除订单
     *
     * @returnD
     */
    public Observable<NullDataEntity> orderDelete(int id, String postscript) {


        OrderInfoEntity infoEntity = new OrderInfoEntity(id, postscript);
        return apiService.orderDelete(token, infoEntity).compose(RxHelper.observe());
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
    public Observable<ActivityPage> activityList(int activity_type) {

//        map.put("activity_type", activity_type);//=1.平台活动 =3.门店活动
//        map.put("activity_name", activity_name);//查询关键字

        return apiService.activityList(map).compose(RxHelper.<ActivityPage>observe());
    }

    /**
     * 活动详情
     */
    public Observable<ActivityEntity> activityDetail(int id) {

        map.put("id", id);

        return apiService.activityDetail(map).compose(RxHelper.<ActivityEntity>observe());
    }

//    /**
//     * 活动详情
//     */
//    public Observable<ActivityEntityItem> activityDetail(int id) {
//
//        map.put("id", id);
//
//        return apiService.activityDetail(map).compose(RxHelper.<ActivityEntityItem>observe());
//    }

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
    public Observable<Member> memberList(int page, String name) {

        map.put("page", page);
        map.put("limit", Configure.limit_page);
        map.put("name", name);
        return apiService.memberList(map).compose(RxHelper.<Member>observe());
    }

    /**
     * 会员管理页面数据
     */
    public Observable<Member> memberList(int page, int limit) {

        map.put("page", page);
        map.put("limit", limit);
        return apiService.memberList(map).compose(RxHelper.<Member>observe());
    }

    /**
     * 会员管理页面数据
     */
    public Observable<Member> memberList(int page, String name, int limit) {

        map.put("page", page);
        map.put("limit", limit);
        map.put("name", name);
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
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        return apiService.shopInfo(map).compose(RxHelper.<Shop>observe());
    }

    /**
     * 工作台信息
     */
    public Observable<List<Banner>> getWorkHeaderAd() {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        return apiService.getWorkHeaderAd(map).compose(RxHelper.<List<Banner>>observe());
    }

    /**
     * 未读新消息数量
     */
    public Observable<Integer> needRead() {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        return apiService.needRead(map).compose(RxHelper.<NumberBean>observeNub());
    }


    /**
     * 未读新消息list
     */
    public Observable<List<OrderNews>> pushlogList(int page) {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        map.put("page", page);
        return apiService.pushlogList(map).compose(RxHelper.<List<OrderNews>>observe());
    }

    /**
     * 标为已读
     */
    public Observable<NullDataEntity> updateRead(int id) {
        map.put("X-Nideshop-Token", new AppPreferences(MyApplication.getInstance()).getString(Configure.Token, ""));
        if (id != 0)
            map.put("id", id);
        return apiService.updateRead(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<List<Coupon>> couponList(String order_price, int user_id) {

        map.put("order_price", order_price);
        map.put("user_id", user_id);

        return apiService.couponList(map).compose(RxHelper.observe());
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
        return apiService.courseList(map).compose(RxHelper.observe());
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

                ToastUtils.showToast("验证码已发送");
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
                ToastUtils.showToast(message);
            }
        });
        return disposable[0];
    }


    /**
     * 更改客户信息发送验证码
     */
    public Disposable updateUserSms(String mobile, final TextView tv, final View v, final Context context) {
        final String TAG = "短信验证码:";
        final int con = 60;

        map.put("mobile", mobile);
        final Disposable[] disposable = new Disposable[1];

        apiService.updateUserSms(map).compose(RxHelper.<NullDataEntity>observe()).subscribe(new RxSubscribe<NullDataEntity>(context, false) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                v.setVisibility(View.VISIBLE);
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
                ToastUtils.showToast(message);
            }
        });
        return disposable[0];
    }

    /**
     * 银行卡验证短信
     */
    public Observable<NullDataEntity> sendBankSms() {
        return apiService.sendBankSms(map).compose(RxHelper.observe());
    }

    /**
     * 登录
     */
    public Observable<Token> login(String mobile, String et_car_code) {
        map.put("mobile", mobile);
        map.put("authCode", et_car_code);
        return apiService.login(map).compose(RxHelper.observe());
    }


    /**
     * 添加银行卡
     */
    public Observable<NullDataEntity> bankSave(Card card) {
        return apiService.bankSave(token, card).compose(RxHelper.<NullDataEntity>observe());
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
    public Observable<NullDataEntity> shopeasySave(GoodsEntity setProject) {
        setProject.setType(1);
        return apiService.shopeasySave(token, setProject).compose(RxHelper.observe());
    }

    /**
     * 修改快捷主推项目
     */
    public Observable<Integer> shopeasyUpdate(GoodsEntity setProject) {
        return apiService.shopeasyUpdate(token, setProject).compose(RxHelper.observe());
    }

    /**
     * 用户可用套餐列表
     */
    public Observable<Meal> queryUserAct(int user_id, String car_no) {
        map.put("user_id", user_id);
        map.put("car_no", car_no);
        return apiService.queryUserAct(map).compose(RxHelper.<Meal>observe());
    }

    /**
     * 申请提现
     */
    public Observable<NullDataEntity> ask(UserBalanceAuthPojo maps) {
        return apiService.ask(token, maps).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 申请提现
     */
    public Observable<ProductList> sku(int id) {
        map.put("id", id);
        return apiService.sku(map).compose(RxHelper.<ProductList>observe());
    }

    /**
     * 查看银行卡 type=1申请中 2申请成功3申请失败
     */
    public Observable<BankList> bankList() {

        return apiService.bankList(token).compose(RxHelper.<BankList>observe());
    }


    /**
     * 车牌识别
     */
    public Observable<CarNumberRecogResult> carLicense(byte[] bytes) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = RGB_565;
        String pic = BitmapUtil.bitmapToString(BitmapUtil.createBitmapThumbnail(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options), true, 0.1f));

        Log.d("扫描识别", "车牌图片\n" + pic);
        return apiService.carLicense(Configure.carNumberRecognition, pic).compose(RxHelper.observe2());
    }

    /**
     * 车牌识别
     */
    public Observable<CarNumberRecogResult> carLicense(byte[] bytes, int vh) throws IOException {

        String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "car_no.jpg";//存入到DOWNLOADS
        File file = FileUtil.getFileFromBytes(bytes, outputFile);

        Bitmap bitmap = new Compressor(context)
                .setQuality(70)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmap(file);

        String pic = BitmapUtil.bitmapToString(BitmapUtil.cropBitmap(bitmap, vh));

        Log.d("扫描识别", "车牌图片\n" + pic);
        return apiService.carLicense(Configure.carNumberRecognition, pic).compose(RxHelper.observe2());
    }

    /**
     * 车牌识别 Baidu
     */
    public void carLicenseBaidu(byte[] bytes, int vh, OnResultListener<OcrResponseResult> result) {

        CameraThreadPool.execute(() -> {
            String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "car_no.jpg";
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                Bitmap bitmap = BitmapUtil.cropBitmap(new Compressor(context)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(FileUtil.getFileFromBytes(bytes, outputFile)), vh);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);

                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // 车牌识别参数设置
            OcrRequestParams param = new OcrRequestParams();
            // 设置image参数
            param.setImageFile(new File(outputFile));
            // 调用车牌识别服务
            OCR.getInstance(context).recognizeLicensePlate(param, result);

        });
    }

    /**
     * 车辆vin识别 baidu
     */
    public void carVinLicenseBaidu(byte[] bytes, int vh, OnResultListener<GeneralResult> result) {

        CameraThreadPool.execute(() -> {
            String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "car_vin.jpg";
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                Bitmap bitmap = BitmapUtil.cropBitmap(new Compressor(context)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(FileUtil.getFileFromBytes(bytes, outputFile)), vh);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);

                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // 车牌识别参数设置
            GeneralBasicParams param = new GeneralBasicParams();
            // 设置image参数
            param.setImageFile(new File(outputFile));
            // 调用通用文字识别服务
            OCR.getInstance(context).recognizeGeneralBasic(param, result);

        });
    }


    /**
     * 车辆vin识别
     */
    public void carVinLicenseBaiDu(byte[] bytes, int vh) throws IOException {

        CameraThreadPool.execute(() -> {
            String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "car_no.jpg";
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                Bitmap bitmap = BitmapUtil.cropBitmap(new Compressor(context)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(FileUtil.getFileFromBytes(bytes, outputFile)), vh);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }


    /**
     * 车牌识别
     */
    public Observable<Carsinfo> carLicense2(byte[] bytes, int vh) throws IOException {

        String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "car_no.jpg";//存入到DOWNLOADS
        File file = FileUtil.getFileFromBytes(bytes, outputFile);

        Bitmap bitmap = new Compressor(context)
                .setQuality(70)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToBitmap(file);

        String pic = BitmapUtil.bitmapToString(BitmapUtil.cropBitmap(bitmap, vh));

        Log.d("扫描识别", "车牌图片\n" + pic);
        return apiService.carLicense2(Configure.carNumberRecognition2, pic).compose(RxHelper.observe3());
    }


    /**
     * 车辆vin识别
     */
    public Observable<CarNumberRecogResult> carVinLicense(byte[] bytes, int vh) throws IOException {

        String outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "vin.webp";//存入到DOWNLOADS
        File file = FileUtil.getFileFromBytes(bytes, outputFile);

        Bitmap bitmap = new Compressor(context)
                .setQuality(50)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmap(file);

        String svin = BitmapUtil.bitmapToString(BitmapUtil.cropBitmap(bitmap, vh));


        Log.d("VIN", "车架号图片长度：" + "文件大小:" + file.length() + "压缩后裁剪：" + svin.length() + "，压缩不剪：");
        return apiService.carVinLicense(Configure.carVinRecognition, new VinImageBody(svin)).compose(RxHelper.observeVin());
    }


    /**
     * 车辆vin信息查询
     */
    public Observable<CarVin> carVinInfoQuery(String vin) {

        return apiService.carVinInfoQuery(Configure.carVinInfo, vin).compose(RxHelper.observeVin());
    }


    /**
     * 车辆vin信息查询
     */
    public Observable<CarVinInfo2> carVinInfoQuery2(String vin) {


        return apiService.carVinInfoQuery2(Configure.carVinInfo3, new Vin(vin)).compose(RxHelper.observeVin());
    }


    /**
     * 报价单列表条件查询
     *
     * @param status 订单状态
     */
    public Observable<FixInfoList> quotationList(int status, int page) {


        if (status == -1)
            return apiService.quotationList(token, page, Configure.limit_page).compose(RxHelper.<FixInfoList>observe());
        else
            return apiService.quotationList(token, status, page, Configure.limit_page).compose(RxHelper.<FixInfoList>observe());
    }

    /**
     * 报价单列表条件查询
     *
     * @param name 关键字
     */
    public Observable<FixInfoList> quotationList(Date before, Date after, boolean isdate, String name, int page) {

        map.clear();
        map.put("X-Nideshop-Token", token);
        if (isdate)
            map.put("limit", 100);
        else
            map.put("limit", Configure.limit_page);
        if (!TextUtils.isEmpty(name))
            map.put("name", name);
        map.put("page", page);

        //选日期需要添加，不添加默认取本月	Date before, Date after
        if (isdate) {
            map.put("dateStart", before.getTime());
            map.put("dateEnd", after.getTime());
        }


        return apiService.quotationList(map).compose(RxHelper.<FixInfoList>observe());
    }

    /**
     * 报价单列表条件查询
     */
    public Observable<FixInfoList> quotationList(int page, String user_id) {
        map.clear();
        map.put("X-Nideshop-Token", token);
        map.put("limit", Configure.limit_page);
        map.put("page", page);
        map.put("user_id", user_id);
        return apiService.quotationList(map).compose(RxHelper.observe());
    }

    /**
     * 报价单取消
     */
    public Observable<NullDataEntity> quotationCancle(int id) {


        return apiService.quotationCancle(token, id).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
     */
    public Observable<RecordMeal> queryConnectAct(String name) {
        String user_id = MyAppPreferences.getString(Configure.user_id);
        if (!"".equals(user_id))
            return apiService.queryConnectAct(token, name, user_id).compose(RxHelper.observe());

        return apiService.queryConnectAct(token, name).compose(RxHelper.observe());

    }

    /**
     * 纸卡录入历史记录（与用户可用套餐查询返回的格式相仿）
     */
    public Observable<RecordMeal> queryConnectAct(int page) {
        return apiService.queryConnectAct(token, page).compose(RxHelper.<RecordMeal>observe());

    }

    /**
     * 课程列表
     */
    public Observable<List<Courses>> courseList2(String name, String course_type) {


        if (course_type.equals("0"))
            return apiService.courseList2(token, 1000).compose(RxHelper.<List<Courses>>observe());
        else
            return apiService.courseList2(token, name, course_type, 1000).compose(RxHelper.<List<Courses>>observe());
    }

    /**
     * 课程分类
     */
    public Observable<List<CategoryType>> queryCategory() {

        return apiService.queryCategory(token).compose(RxHelper.<List<CategoryType>>observe());
    }

    /**
     * 课程学习记录列表
     */
    public Observable<List<CourseRecord>> courseRecord() {
        return apiService.courseRecord(token, 1000).compose(RxHelper.<List<CourseRecord>>observe());
    }

    /**
     * 课程列表
     */
    public Observable<List<Courses>> courseListSearch(String name) {
        return apiService.courseList2(token, name, null, 1000).compose(RxHelper.<List<Courses>>observe());
    }

    /**
     * 用户点击视频观看退出时访问，用来增加记录
     */
    public Observable<NullDataEntity> addWatchLog(CourseRecord courseRecord) {
        return apiService.addWatchLog(token, courseRecord).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 检查版本更新
     */
    public Observable<VersionInfo> checkVersionUpDate() {
        return apiService.checkVersionUpDate(token).compose(RxHelper.observe());
    }

    /**
     * 记录用户更新
     *
     * @param version 版本号 ，用户看的
     */
    public Observable<String> updateAppLog(String version) {
        return apiService.updateAppLog(token, version).compose(RxHelper.observe());
    }


    /**
     * 获取员工详情
     */
    public Observable<TechnicianInfo> sysuserDetail(int id) {
        return apiService.sysuserDetail(token, id).compose(RxHelper.observe());

    }

    /**
     * 修改员工
     */
    public Observable<NullDataEntity> sysuserUpdate(Technician technicianInfo) {
        return apiService.sysuserUpdate(token, technicianInfo).compose(RxHelper.<NullDataEntity>observe());

    }


    /**
     * 删除员工
     */
    public Observable<NullDataEntity> sysuserDelete(int id) {
        return apiService.sysuserDelete(token, id).compose(RxHelper.observe());

    }

    /**
     * 添加员工
     */
    public Observable<NullDataEntity> sysuserSave(Technician technicianInfo) {
        return apiService.sysuserSave(token, technicianInfo).compose(RxHelper.<NullDataEntity>observe());

    }

    /**
     * 供选择的角色列表
     */
    public Observable<List<Roles>> queryRoles() {
        return apiService.queryRoles(token).compose(RxHelper.<List<Roles>>observe());

    }

    /**
     * 获取员工销售订单
     */
    public Observable<List<OrderInfoEntity>> saleList(int id) {
        map.put("limit", 100);
        map.put("sysuser_id", id);

        return apiService.saleList(map).compose(RxHelper.<List<OrderInfoEntity>>observe());
    }

    /**
     * 获取员工服务订单
     */
    public Observable<List<OrderInfoEntity>> sysOrderList(int id) {
        map.put("limit", 100);
        map.put("user_id", id);
        return apiService.sysOrderList(map).compose(RxHelper.<List<OrderInfoEntity>>observe());
    }


    /**
     * 更改客户信息发送验证码
     */
    public Observable<NullDataEntity> updateUserSms(String mobile) {

        map.put("mobile", mobile);
        return apiService.updateUserSms(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 更改客户信息
     */
    public Observable<NullDataEntity> remakeUserName(int user_id, String user_name, String authCode, String mobile) {

        map.put("mobile", mobile);
        map.put("user_id", user_id);
        map.put("user_name", user_name);
        map.put("authCode", authCode);
        return apiService.remakeUserName(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 更改客户信息2
     */
    public Observable<NullDataEntity> remakeName(int user_id, String user_name, String mobile) {

        map.put("mobile", mobile);
        map.put("user_id", user_id);
        map.put("user_name", user_name);
        return apiService.remakeName(map).compose(RxHelper.<NullDataEntity>observe());
    }

    /**
     * 门店检修报告选项列表
     */
    public Observable<List<CheckOptions>> queryCheckOptions() {

        return apiService.queryCheckOptions(token).compose(RxHelper.observe());
    }

    /**
     * 添加门店检修选项；
     */
    public Observable<NullDataEntity> addCheckOptions(CheckOptions checkOptions) {

        return apiService.addCheckOptions(token,checkOptions).compose(RxHelper.observe());
    }

    /**
     * 修改门店检修选项
     */
    public Observable<NullDataEntity> updateCheckOptions(CheckOptions checkOptions) {

        return apiService.updateCheckOptions(token,checkOptions).compose(RxHelper.observe());
    }


    /**
     * 暂存或者生成检测报告
     */
    public Observable<NullDataEntity> checkOutResult(CarCheckResul checkResul) {

        return apiService.checkOutResult(token, checkResul).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 检测报告单列表
     */
    public Observable<List<CarCheckResul>> checkResultList(int car_id) {
        if (car_id != -1)
            map.put("car_id", car_id);
        map.put("limit", 100);//页数
        return apiService.checkResultList(map).compose(RxHelper.<List<CarCheckResul>>observe());
    }


    /**
     * 检测报告单结果详情
     */
    public Observable<CarCheckResul> checkResultDetail(int id) {
        map.put("id", id);
        return apiService.checkResultDetail(map).compose(RxHelper.<CarCheckResul>observe());
    }


    /**
     * 修改暂存的门店检测报告 type为1的不可访问此接口
     */
    public Observable<NullDataEntity> updateCheckResult(CarCheckResul checkResul) {

        return apiService.updateCheckResult(token, checkResul).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 门店服务过的车辆管理
     */

    public Observable<ShopCarBane> shopCarList(String key, int page) {
        if (!key.equals(""))
            map.put("name", key);
        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        return apiService.shopCarList(map).compose(RxHelper.<ShopCarBane>observe());
    }


    /**
     * @param type 1 ：8个分类   2：所有 3:服务  4:配件
     *             获取商品分类
     */
    public Observable<List<GoodsCategory>> queryShopcategoryAll(String type) {
        if (null != type)
            map.put("type", type);
        return apiService.queryShopcategoryAll(map).compose(RxHelper.<List<GoodsCategory>>observe());
    }

    /**
     * @param goodsTitle   商品名称
     * @param goodsBrandId 商品品牌Id
     * @param categoryId   商品分类Id
     *                     查询商品（分页） 无参
     * @param type         1是汽配商品,3是工时，4是配件
     */
    public Observable<GoodsList> xgxshopgoodsList(String goodsTitle, String goodsBrandId, String categoryId, int page, int type) {
        if (null != goodsTitle)
            map.put("goodsTitle", goodsTitle);
        if (null != goodsBrandId)
            map.put("goodsBrandId", goodsBrandId);
        if (null != categoryId)
            map.put("categoryId", categoryId);

        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        map.put("type", type);

        return apiService.xgxshopgoodsList(map).compose(RxHelper.<GoodsList>observe());
    }

    /**
     * @param goodsTitle   商品名称
     * @param goodsBrandId 商品品牌Id
     * @param categoryId   商品分类Id
     *                     查询商品（分页） 无参
     * @param type         1是汽配商品,3是工时，4是配件
     */
    public Observable<GoodsList> xgxshopgoodsList(String goodsTitle, String goodsBrandId, String categoryId, int page, int type, String vin) {
        if (null != goodsTitle)
            map.put("goodsTitle", goodsTitle);
        if (null != goodsBrandId)
            map.put("goodsBrandId", goodsBrandId);
        if (null != categoryId)
            map.put("categoryId", categoryId);

        if (null != vin && !vin.equals(""))
            map.put("vin", vin);

        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        map.put("type", type);


        return apiService.xgxshopgoodsList(map).compose(RxHelper.<GoodsList>observe());
    }


    /**
     * @param goodsTitle   商品名称
     * @param goodsBrandId 商品品牌Id
     * @param categoryId   商品分类Id
     *                     查询商品（分页） 无参
     * @param type         1是汽配商品,3是工时，4是配件
     */
    public Observable<GoodsList> xgxshopgoodsList(String goodsTitle, String goodsBrandId, String categoryId, int page, int type, int limit) {
        if (null != goodsTitle)
            map.put("goodsTitle", goodsTitle);
        if (null != goodsBrandId)
            map.put("goodsBrandId", goodsBrandId);
        if (null != categoryId)
            map.put("categoryId", categoryId);

        map.put("limit", limit);//页数
        map.put("page", page);
        map.put("type", type);
        return apiService.xgxshopgoodsList(map).compose(RxHelper.<GoodsList>observe());
    }


    /**
     * @param id 商品分类id
     *           根据商品分类查询品牌
     */
    public Observable<List<GoodsBrand>> shopcategoryInfo(String id) {

        map.put("id", id);
        map.put("limit", "1000");//页数


        return apiService.shopcategoryInfo(map).compose(RxHelper.observe());
    }

    /**
     * 库存管理新增商品里的查找所有品牌
     */
    public Observable<List<GoodsBrand>> brandGoodsList() {

        return apiService.brandGoodsList(map).compose(RxHelper.observe());
    }


    /**
     * @param id 商品id
     *           id
     */
    public Observable<Goods> xgxshopgoodsInfo(int id) {

        map.put("id", id);
        return apiService.xgxshopgoodsInfo(map).compose(RxHelper.observe());
    }

    /**
     * 获取购物车信息
     */
    public Observable<CartList> getShoppingCart() {

        return apiService.getShoppingCart(token, "1", "1").compose(RxHelper.<CartList>observe());
    }


    /**
     * 添加购物车
     *
     * @param goodsId   商品id
     * @param productId 商品规格id
     */
    public Observable<CartList> addToShoppingCart(int goodsId, int productId, int number) {

        map.put("goodsId", goodsId);
        map.put("productId", productId);
        map.put("number", number);
        return apiService.addToShoppingCart(map).compose(RxHelper.<CartList>observe());
    }

    /**
     * 购物车商品下单
     */
    public Observable<Integer> mallMakeOrder(XgxPurchaseOrderPojo purchaseOrderPojo) {

        return apiService.mallMakeOrder(token, purchaseOrderPojo).compose(RxHelper.<Integer>observe());
    }


    /**
     * 获取商城订单详情
     */
    public Observable<XgxPurchaseOrderPojo> mallOrderInfo(int id) {

        return apiService.mallOrderInfo(token, id).compose(RxHelper.<XgxPurchaseOrderPojo>observe());
    }

    /**
     * 调用微信支付
     */
    public Observable<PayInfo> prepay(int orderId) {

        return apiService.prepay(token, orderId).compose(RxHelper.observe());
    }

    /**
     * 获取商城订单详情
     * shipStatus    发货状态  1为未发货 2为已发货  订单生成后默认未发货
     * payStatus  支付状态  1为未付款 2为已付款 订单生成默认未付款
     *
     * @param status 1 未付款 2待发货 3待收货 4已收货 0 全部
     */
    public Observable<BasePage<XgxPurchaseOrderPojo>> purchaseorderList(int page, int status) {
        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);


        switch (status) {

            case 1:
                map.put("payStatus", 1);
                map.put("shipStatus", 1);
                break;
            case 2:
                map.put("payStatus", 2);
                map.put("shipStatus", 1);

                break;
            case 3:
                map.put("shipStatus", 2);
                break;
            case 4:
                map.put("shipStatus", 3);
                break;
        }


        return apiService.purchaseorderList(map).compose(RxHelper.<BasePage<XgxPurchaseOrderPojo>>observe());
    }

    /**
     * app运行异常记录
     */
    public Observable<NullDataEntity> saveError(String error) {

        map.put("error", error);


        return apiService.saveError(map).compose(RxHelper.observe());
    }


    /**
     * 出入库操作
     */
    public Observable<NullDataEntity> inOrOut(StockInOrOut stockInOrOut) {
        return apiService.inOrOut(token, stockInOrOut).compose(RxHelper.observe());
    }


    /**
     * 出入库记录详情
     */
    public Observable<StockInOrOut> stockInfo(String id) {
        map.put("id", id);
        return apiService.info(map).compose(RxHelper.observe());
    }


    /**
     * 添加库存商品的规格
     */
    public Observable<NullDataEntity> addStandard(Goods.GoodsStandard goodsStandard) {

        return apiService.addStandard(token, goodsStandard).compose(RxHelper.observe());
    }

    /**
     * 修改某个规格详情
     */
    public Observable<NullDataEntity> updateStandard(Goods.GoodsStandard goodsStandard) {

        return apiService.updateStandard(token, goodsStandard).compose(RxHelper.observe());
    }

    /**
     * 库存商品删除某个规格
     */
    public Observable<NullDataEntity> deleteStandard(Goods.GoodsStandard goodsStandard) {

        return apiService.deleteStandard(token, goodsStandard).compose(RxHelper.observe());
    }

    /**
     * 查看每个规格详情
     */
    public Observable<Goods.GoodsStandard> standardInfo(int id) {

        return apiService.standardInfo(token, id).compose(RxHelper.observe());
    }


    /**
     * 添加库存商品
     */
    public Observable<Integer> addGoods(Goods goods) {

        return apiService.addGoods(token, goods).compose(RxHelper.observe());
    }

    /**
     * 编辑库存商品
     */
    public Observable<NullDataEntity> fixGoods(Goods goods) {

        return apiService.fixGoods(token, goods).compose(RxHelper.observe());
    }

    /**
     * 添加商品图片
     */
    public Observable<NullDataEntity> addDetails(Goods.GoodsPic goodsPic) {

        return apiService.addDetails(token, goodsPic).compose(RxHelper.observe());
    }


    /**
     * 删除商品图
     */
    public Observable<NullDataEntity> deleteDetails(String id) {

        return apiService.deleteDetails(token, id).compose(RxHelper.observe());
    }


    /**
     * 添加供应商
     */
    public Observable<NullDataEntity> addSupplier(Supplier supplier) {

        return apiService.addSupplier(token, supplier).compose(RxHelper.observe());
    }

    /**
     * 修改供应商
     */
    public Observable<NullDataEntity> fixSupplier(Supplier supplier) {

        return apiService.fixSupplier(token, supplier).compose(RxHelper.observe());
    }

    /**
     * 供应商详情
     */
    public Observable<Supplier> infoSupplier(String id) {

        return apiService.infoSupplier(token, id).compose(RxHelper.observe());
    }

    /**
     * 供应商列表
     */
    public Observable<List<Supplier>> listSupplier() {

        return apiService.listSupplier(token).compose(RxHelper.observe());
    }


    /**
     * 活动列表
     */
    public Observable<List<Maneuver>> listShopunity() {

        return apiService.listShopunity(token).compose(RxHelper.observe());
    }

    /**
     * 活动详情页
     */
    public Observable<Maneuver> infoShopunity(String id) {

        return apiService.infoShopunity(token, id).compose(RxHelper.observe());
    }

    /**
     * 反馈列表
     */
    public Observable<List<Ask>> askList(String unity_id) {
        return apiService.askList(token, unity_id).compose(RxHelper.observe());
    }

    /**
     * 反馈列表
     */
    public Observable<List<Ask>> askList(String unity_id, String user_id) {

        return apiService.askList(token, unity_id, user_id).compose(RxHelper.observe());
    }

    /**
     * 报名列表
     */
    public Observable<List<Joiner>> joinList(String shopId) {

        return apiService.joinList(token, shopId).compose(RxHelper.observe());
    }


    /**
     * 提交反馈
     */
    public Observable<NullDataEntity> askTo(Ask ask) {

        return apiService.askTo(token, ask).compose(RxHelper.observe());
    }

    /**
     * 出入库记录列表
     */
    public Observable<List<StockInOrOut>> stockInOrOutRecordList(String name, int type, int page) {

        map.put("limit", Configure.limit_page);//页数
        map.put("page", page);
        map.put("type", type);
        if (null != name)
            map.put("name", name);

        return apiService.stockInOrOutRecordList(token, map).compose(RxHelper.observe());
    }


    /**
     * 报名参加
     */
    public Observable<NullDataEntity> joinIn(Ask ask) {

        return apiService.joinIn(token, ask).compose(RxHelper.observe());
    }

    /**
     * 出库时匹配订单
     */
    public Observable<List<StockGoods>> matchOrder(int orderId) {

        return apiService.matchOrder(token, orderId).compose(RxHelper.observe());
    }


    /**
     * 查看已完成订单技师绩效分配
     */
    public Observable<List<Technician>> getOrderDeduction(int orderId) {

        return apiService.getOrderDeduction(token, orderId).compose(RxHelper.observe());
    }

    /**
     * 设置员工绩效分配
     */
    public Observable<NullDataEntity> setDeduction(List<Technician> list) {

        return apiService.setDeduction(token, list).compose(RxHelper.observe());
    }

    /**
     * 删除购物车商品
     */
    public Observable<NullDataEntity> deleteCard(int[] cartIds) {

        return apiService.delete(token, cartIds).compose(RxHelper.observe());
    }

    /**
     * 更新商品购物车数据
     */
    public Observable<NullDataEntity> shoppingCartUpdate(Integer goodsId, Integer productId, int number) {


        if (null != goodsId)
            map.put("goodsId", goodsId);
        if (null != productId)
            map.put("productId", productId);

        map.put("number", number);

        return apiService.shoppingCartUpdate(map).compose(RxHelper.observe());
    }


    /**
     * 获取购物车信息
     */
    public Observable<CartList> getShoppingCartInfo() {


        return apiService.getShoppingCart(token, "1", "1").compose(RxHelper.observe());
    }


    /**
     * 门店优惠券模板列表
     */
    public Observable<List<Coupon2>> shopCouponList() {

        return apiService.shopCouponList(token).compose(RxHelper.observe());
    }

    /**
     * 添加优惠模板
     */
    public Observable<NullDataEntity> addShopCoupon(Coupon2 coupon2) {

        return apiService.addShopCoupon(token, coupon2).compose(RxHelper.observe());
    }

    /**
     * 修改优惠模板
     */
    public Observable<NullDataEntity> fixShopCoupon(Coupon2 coupon2) {

        return apiService.fixShopCoupon(token, coupon2).compose(RxHelper.observe());
    }

    /**
     * 优惠券派发记录列表
     */
    public Observable<List<CouponRecode>> couponPostRecode() {

        return apiService.couponPostRecode(token).compose(RxHelper.observe());
    }

    /**
     * 提现记录
     */
    public Observable<DisRecordList> disRecode() {

        return apiService.ceauthList(token).compose(RxHelper.observe());
    }

    /**
     * 派发优惠券记录详情
     */
    public Observable<CouponRecode> pushLogInfo(String id) {

        return apiService.pushLogInfo(token, id).compose(RxHelper.observe());
    }


    /**
     * 派发优惠券
     */
    public Observable<NullDataEntity> pushCoupon(CouponRecode couponRecode) {

        return apiService.pushCoupon(token, couponRecode).compose(RxHelper.observe());
    }

    /**
     * 查看优惠券模板详情
     */
    public Observable<Coupon2> shopCouponInfo(String id) {

        return apiService.shopCouponInfo(token, id).compose(RxHelper.observe());
    }


    /**
     * 查看优惠券模板详情
     */
    public Observable<UserEntity> getInfo() {

        return apiService.getInfo(token).compose(RxHelper.observe());
    }


    /**
     * 添加扫描车辆池
     */
    public Observable<NullDataEntity> savePlate(String plate) {
        VehicleQueue vq = new VehicleQueue();
        vq.setPlateNumber(plate);

        return apiService.savePlate(token, vq).compose(RxHelper.observe());
    }

    /**
     * 车辆池
     */
    public Observable<List<VehicleQueue>> platelist() {

        return apiService.platelist(token).compose(RxHelper.observe());
    }


    /**
     * 扫描车辆池改变接车状态
     */
    public Observable<NullDataEntity> plateUpdate(String id) {

        return apiService.plateUpdate(token, id).compose(RxHelper.observe());
    }

    /**
     * 车辆入店误扫操作
     */
    public Observable<NullDataEntity> updateUnable(String id, String changeReason) {

        return apiService.updateUnable(token, id, changeReason).compose(RxHelper.observe());
    }

    /**
     * 券码查券
     */
    public Observable<Coupon> queryByNumber(String couponNumber) {

        return apiService.queryByNumber(token, couponNumber).compose(RxHelper.observe());
    }


    /**
     * 套卡扫码
     */
    public Observable<GoodsEntity> activityqueryByNumber(String couponNumber, String orderId) {

        return apiService.activityqueryByNumber(token, couponNumber, orderId).compose(RxHelper.observe());
    }

    /**
     * 核销券
     */
    public Observable<NullDataEntity> convertCoupon(String couponNumber) {

        return apiService.convertCoupon(token, couponNumber).compose(RxHelper.observe());
    }

    /**
     * 核销记录
     */
    public Observable<List<Coupon>> convertCouponLog() {

        return apiService.convertCouponLog(token).compose(RxHelper.observe());
    }


}
