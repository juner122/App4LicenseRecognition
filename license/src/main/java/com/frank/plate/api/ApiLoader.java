package com.frank.plate.api;

import com.frank.plate.bean.ActivityEntity;
import com.frank.plate.bean.AutoBrand;
import com.frank.plate.bean.BasePage;
import com.frank.plate.bean.BillEntity;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.OrderInfo;
import com.frank.plate.bean.OrderInfoEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;
import com.frank.plate.bean.Technician;

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
     * 1.拍照接单自动查找订单或车况
     *
     * @return
     */
    public Observable<BillEntity> getUserBillList(int count, int limit, int sidx, int order) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("limit", limit);
        map.put("sidx", sidx);
        map.put("order", order);
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
     * 14.车况详情显示
     *
     * @param id 车况主键
     * @return
     */
    public Observable<CarInfoRequestParameters> showCarInfo(String id) {
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
     * 确认支付
     *
     * @return
     */
    public Observable<OrderInfo> confirmPay(OrderInfoEntity infoEntity) {

        return apiService.confirmPay(infoEntity).compose(RxHelper.<OrderInfo>observe());
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


}
