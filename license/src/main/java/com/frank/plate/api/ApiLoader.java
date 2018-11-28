package com.frank.plate.api;

import com.frank.plate.bean.ActivityEntity;
import com.frank.plate.bean.BaseBean;
import com.frank.plate.bean.CarInfoEntity;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.CategoryBrandList;
import com.frank.plate.bean.GoodsListEntity;
import com.frank.plate.bean.NullDataEntity;
import com.frank.plate.bean.QueryByCarEntity;
import com.frank.plate.bean.SaveUserAndCarEntity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class ApiLoader extends ObjectLoader {

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
        return observe(apiService.queryByCar(map)).map(new PayLoad<QueryByCarEntity>());
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
        return observe(apiService.saveUserAndCar(map)).map(new PayLoad<SaveUserAndCarEntity>());
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
        return observe(apiService.addUser(map)).map(new PayLoad<SaveUserAndCarEntity>());
    }

    /**
     * 7.新增车况
     *
     * @return
     */
    public Observable<NullDataEntity> addCarInfo(CarInfoRequestParameters parameters) {
        return observe(apiService.addCarInfo(parameters)).map(new PayLoad<NullDataEntity>());
    }

    /**
     * 8.修改车况
     *
     * @return
     */
    public Observable<NullDataEntity> fixCarInfo(CarInfoRequestParameters parameters) {
        return observe(apiService.fixCarInfo(parameters)).map(new PayLoad<NullDataEntity>());
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
        return observe(apiService.showCarInfo(map)).map(new PayLoad<CarInfoRequestParameters>());
    }


    /**
     * 9.分类下品牌列表加第一个品牌第一页下商品
     *
     * @return
     */
    public Observable<CategoryBrandList> categoryBrandList() {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");

        return observe(apiService.categoryBrandList(map)).map(new PayLoad<CategoryBrandList>());

    }

    /**
     * 10.查询任意条件商品 目前主要存brand_id品牌，category_id类型，name商品名
     *
     * @return
     */
    public Observable<GoodsListEntity> queryAnyGoods(String category_id, String brand_id, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("category_id", category_id); //商品类别
        map.put("brand_id", brand_id);//品牌
        map.put("name", name);//查询关键字

        return observe(apiService.queryAnyGoods(map)).map(new PayLoad<GoodsListEntity>());
    }


    /**
     * 活动列表
     */
    public Observable<ActivityEntity> activityList(String activity_type, String activity_name) {
        Map<String, Object> map = new HashMap<>();
        map.put("X-Nideshop-Token", "1");
        map.put("activity_type", activity_type);//=1.平台活动 =3.门店活动
        map.put("activity_name", activity_name);//查询关键字

        return observe(apiService.activityList(map)).map(new PayLoad<ActivityEntity>());
    }


}
