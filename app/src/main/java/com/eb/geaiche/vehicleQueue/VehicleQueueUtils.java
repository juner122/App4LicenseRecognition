package com.eb.geaiche.vehicleQueue;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.GoodsEntity;


import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static com.juner.mvp.Configure.JSON_VEHICLEQUEUE;


//车辆进店队列
public class VehicleQueueUtils {

    private static VehicleQueueUtils instance = null;

    //    SparseArray data;
    HashMap<String, VehicleQueue> data;


    Context context;

    public static synchronized VehicleQueueUtils getInstance(Context context) {

        //这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        //使用时生成实例，提高了效率！
        if (instance == null)
            instance = new VehicleQueueUtils(context);
        return instance;
    }

    public VehicleQueueUtils(Context context) {

        data = new LinkedHashMap<>(100);
        new AppPreferences(context).remove(JSON_VEHICLEQUEUE);
        this.context = context;
    }


    //本地获取json数据，并且通过Gson解析成list列表数据
    public List<VehicleQueue> getDataFromLocal() {
        List<VehicleQueue> carts = new ArrayList<>();
        //从本地获取缓存数据
        String savaJson = new AppPreferences(context).getString(JSON_VEHICLEQUEUE, "");
        if (!TextUtils.isEmpty(savaJson)) {
            //把数据转换成列表
            carts = new Gson().fromJson(savaJson, new TypeToken<List<VehicleQueue>>() {
            }.getType());
        }

        //反转排序
        Collections.reverse(carts);
        return carts;

    }


    //添加一个车辆 以车牌号为id
    public void addVehicleData(String number) {

        String plateNumber = number;//以车牌号为id

        VehicleQueue vehicleQueue = new VehicleQueue();
        vehicleQueue.setPlateNumber(number);
        vehicleQueue.setTime(System.currentTimeMillis());


        data.put(plateNumber, vehicleQueue);

        commit();
    }

    //一辆车出队列
    public void reduceData(String number) {

        //减数据
        VehicleQueue vq = data.get(number);
        if (vq != null) {
            data.remove(number);
            commit();
        }
    }


    public void commit() {


        //把parseArray转换成list
        List<VehicleQueue> vehicle = sparsesToList();

        //把转换成String
        String json = new Gson().toJson(vehicle);
        // 保存
        new AppPreferences(context).put(JSON_VEHICLEQUEUE, json);


    }


    private List<VehicleQueue> sparsesToList() {
        List<VehicleQueue> vehicle = new ArrayList<>();
        if (data != null && data.size() > 0) {

            Collection<VehicleQueue> collection = data.values();
            Iterator<VehicleQueue> iterator = collection.iterator();
            while (iterator.hasNext()) {
                VehicleQueue value = iterator.next();
                vehicle.add(value);
            }
        }
        return vehicle;
    }


    public void deleteAllData() {
        data.clear();
        new AppPreferences(context).remove(JSON_VEHICLEQUEUE);
    }


    public boolean isNull() {
        if (sparsesToList().size() == 0) {
            return true;
        } else {
            return false;
        }
    }


}
