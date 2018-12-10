package com.frank.plate.api;

import com.frank.plate.bean.ActivityEntity;
import com.frank.plate.bean.AutoBrand;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.Coupon;
import com.frank.plate.bean.Course;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.Member;
import com.frank.plate.bean.MemberOrder;
import com.frank.plate.bean.MyBalanceEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.bean.Shop;
import com.frank.plate.bean.ShopEntity;
import com.frank.plate.bean.Technician;
import com.frank.plate.bean.WorkIndex;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class ApiLoader {

    private ApiService apiService;

    public ApiLoader() {
        apiService = RetrofitServiceManager.getInstance().create(ApiService.class);
    }

    /**
     * 1.拍照接单自动查找订单或车况
     *
     * @return
     */
    public Observable<QueryByCarEntity> queryByCar(String car_no) {
        Map<String, Object> map = new HashMap<>();
        map.put("car_no", car_no);
        return apiService.queryByCar(map).compose(RxHelper.<QueryByCarEntity>observe());

    }

    /**
     * 账单统计加列表
     *
     * @return
     */
    public Observable<BillEntity> getUserBillList(int page, int limit, int sidx, int order) {
        Map<String, Object> map = new HashMap<>();


        //选日期需要添加，不添加默认取本月	Date before, Date after


        //如需分页则添加	分页参数（int page，int limit，String sidx，String order）
//        map.put("page", page);
//        map.put("limit", limit);
//        map.put("sidx", sidx);
//        map.put("order", order);
        return apiService.getUserBillList(map).compose(RxHelper.<BillEntity>observe());

    }

    /**
     * 账单统计加列表
     *
     * @return
     */
    public Observable<BillEntity> getUserBillList(Date before, Date after) {
        Map<String, Object> map = new HashMap<>();


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
        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
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

        return apiService.addCarInfo(parameters).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 8.修改车况
     *
     * @return
     */
    public Observable<NullDataEntity> fixCarInfo(CarInfoRequestParameters parameters) {

        return apiService.fixCarInfo(parameters).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 4.批量删除车况图片
     *
     * @return
     */
    public Observable<NullDataEntity> delete(Integer[] ids) {

        return apiService.delete(ids).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 确认下单
     *
     * @return
     */
    public Observable<OrderInfo> submit(OrderInfoEntity infoEntity) {


        return apiService.submit(infoEntity).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 4.开始服务(修改订单状态为服务中)
     *
     * @return
     */
    public Observable<NullDataEntity> beginServe(int order_id, String order_sn) {

        Map<String, Object> map = new HashMap<>();
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
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return apiService.showCarInfo(map).compose(RxHelper.<CarInfoRequestParameters>observe());
    }


    /**
     * 15.当前门店用户（技师）列表
     *
     * @return
     */
    public Observable<BasePage<Technician>> sysuserList() {

        return apiService.sysuserList().compose(RxHelper.<BasePage<Technician>>observe());
    }


    /**
     * 9.分类下品牌列表加第一个品牌第一页下商品
     *
     * @return
     */
    public Observable<CategoryBrandList> categoryBrandList() {
        return apiService.categoryBrandList().compose(RxHelper.<CategoryBrandList>observe());
    }

    /**
     * 10.查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     *
     * @return
     */
    public Observable<GoodsListEntity> queryAnyGoods(String category_id, String brand_id, String name) {
        Map<String, Object> map = new HashMap<>();

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
    public Observable<GoodsListEntity> queryAnyGoods() {
        Map<String, Object> map = new HashMap<>();
        int is_hot = 1;
        map.put("is_hot", is_hot);
        return apiService.queryAnyGoods(map).compose(RxHelper.<GoodsListEntity>observe());
    }

    /**
     * 确认订单最后完成
     *
     * @return
     */
    public Observable<NullDataEntity> confirmFinish(int order_id) {

        Map<String, Object> map = new HashMap<>();
        map.put("order_id", order_id);
        return apiService.confirmFinish(map).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 确认支付
     *
     * @return
     */
    public Observable<NullDataEntity> confirmPay(OrderInfoEntity infoEntity) {

        return apiService.confirmPay(infoEntity).compose(RxHelper.<NullDataEntity>observe());
    }


    /**
     * 任意条件订单列表 不同订单查询看备注
     *
     * @return
     */
    public Observable<BasePage<OrderInfoEntity>> orderList() {

        return apiService.orderList().compose(RxHelper.<BasePage<OrderInfoEntity>>observe());
    }


    /**
     * 订单详情页
     *
     * @returnD
     */
    public Observable<OrderInfo> orderDetail(int id) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        return apiService.orderDetail(map).compose(RxHelper.<OrderInfo>observe());
    }

    /**
     * 账单详情（同订单详情一个接口，入参不同）
     *
     * @returnD
     */
    public Observable<OrderInfo> orderDetail(String order_sn) {
        Map<String, Object> map = new HashMap<>();
        map.put("order_sn", order_sn);
        return apiService.orderDetail(map).compose(RxHelper.<OrderInfo>observe());
    }


    /**
     * 活动列表
     */
    public Observable<ActivityEntity> activityList(String activity_type, String activity_name) {
        Map<String, Object> map = new HashMap<>();

        map.put("activity_type", activity_type);//=1.平台活动 =3.门店活动
        map.put("activity_name", activity_name);//查询关键字

        return apiService.activityList(map).compose(RxHelper.<ActivityEntity>observe());
    }

    /**
     * 品牌查询列表
     */
    public Observable<List<AutoBrand>> listByName() {

        return apiService.listByName().compose(RxHelper.<List<AutoBrand>>observe());
    }


    /**
     * 工作台首页
     */
    public Observable<WorkIndex> workIndex() {

        return apiService.workIndex().compose(RxHelper.<WorkIndex>observe());
    }


    /**
     * 会员管理页面数据
     */
    public Observable<Member> memberList() {

        return apiService.memberList().compose(RxHelper.<Member>observe());
    }


    /**
     * 查看会员信息及订单记录
     */
    public Observable<MemberOrder> memberOrderList(int user_id) {
        Map<String, Object> map = new HashMap<>();

        map.put("user_id", user_id);

        return apiService.memberOrderList(map).compose(RxHelper.<MemberOrder>observe());
    }

    /**
     * 门店信息
     */
    public Observable<Shop> shopInfo() {

        return apiService.shopInfo().compose(RxHelper.<Shop>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<List<Coupon>> couponList(String order_price, int user_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("order_price", order_price);
        map.put("user_id", user_id);

        return apiService.couponList(map).compose(RxHelper.<List<Coupon>>observe());
    }


    /**
     * 获取优惠券列表 [达到满减，未到期，未用过]
     */
    public Observable<MyBalanceEntity> balanceInfo() {

        return apiService.balanceInfo().compose(RxHelper.<MyBalanceEntity>observe());
    }

    /**
     * 课程列表
     */
    public Observable<List<Course>> courseList(int course_type) {
        // course_type	1为线下 2为线上
        Map<String, Object> map = new HashMap<>();
        map.put("course_type", course_type);
        return apiService.courseList(map).compose(RxHelper.<List<Course>>observe());
    }

    /**
     * 课程报名
     */
    public Observable<NullDataEntity> coursejoinnameSave(String name, String mobile) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("mobile", mobile);
        return apiService.coursejoinnameSave(map).compose(RxHelper.<NullDataEntity>observe());
    }


}
