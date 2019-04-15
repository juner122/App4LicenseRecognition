package com.eb.geaiche.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;


import com.juner.mvp.bean.GoodsEntity;
import com.eb.geaiche.bean.MealEntity;
import com.juner.mvp.bean.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.juner.mvp.Configure.Goods_TYPE_3;
import static com.juner.mvp.Configure.Goods_TYPE_4;
import static com.juner.mvp.Configure.Goods_TYPE_5;
import static com.juner.mvp.Configure.JSON_CART;

public class CartUtils {

    private static CartUtils instance = null;

    SparseArray data;


    Context context;

    public static synchronized CartUtils getInstance(Context context) {

        //这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        //使用时生成实例，提高了效率！
        if (instance == null)
            instance = new CartUtils(context);
        return instance;
    }

    public CartUtils(Context context) {

        data = new SparseArray<>(100);
        new AppPreferences(context).remove(JSON_CART);
        this.context = context;
    }



    //本地获取json数据，并且通过Gson解析成list列表数据
    public List<GoodsEntity> getDataFromLocal() {
        List<GoodsEntity> carts = new ArrayList<>();
        //从本地获取缓存数据
        String savaJson = new AppPreferences(context).getString(JSON_CART, "");
        if (!TextUtils.isEmpty(savaJson)) {
            //把数据转换成列表
            carts = new Gson().fromJson(savaJson, new TypeToken<List<GoodsEntity>>() {
            }.getType());
        }
        return carts;

    }


    public List<GoodsEntity> getProductList() {

        List<GoodsEntity> carts = new ArrayList<>();
        List<GoodsEntity> list = getDataFromLocal();
        for (GoodsEntity c : list) {
            if (c.getType() == Goods_TYPE_4)
                carts.add(c);
        }
        return carts;
    }


    public List<GoodsEntity> getServerList() {

        List<GoodsEntity> carts = new ArrayList<>();
        List<GoodsEntity> list = getDataFromLocal();
        for (GoodsEntity c : list) {
            if (c.getType() == Goods_TYPE_3)
                carts.add(c);
        }
        return carts;
    }


    public List<GoodsEntity> getAllGoods() {

        List<GoodsEntity> carts = new ArrayList<>();
        carts.addAll(getProductList());
        carts.addAll(getServerList());
        return carts;
    }


    public List<GoodsEntity> getMealList() {

        List<GoodsEntity> carts = new ArrayList<>();
        List<GoodsEntity> list = getDataFromLocal();
        for (GoodsEntity c : list) {
            if (c.getType() == Goods_TYPE_5)
                carts.add(c);
        }
        return carts;
    }


    public void addProductData(GoodsEntity good) {


        addData(good);

    }


    //套餐商品
    public void addMeal(MealEntity entity) {

        GoodsEntity good = new GoodsEntity();

        good.setId(entity.getId());
        good.setGoodsId(Integer.parseInt(entity.getGoodsId()));
        good.setGoods_id(Integer.parseInt(entity.getGoodsId()));
        good.setName(entity.getGoodsName());
        good.setGoodsNum(entity.getNumber());
        good.setActivityId(Integer.parseInt(entity.getActivityId()));
        good.setActivitySn(entity.getActivitySn());
        good.setActivityName(entity.getActivityName());
        good.setGoodsName(entity.getGoodsName());

        good.setType(Goods_TYPE_5);
        addData(good);
    }

    //套餐商品
    public void reduceMeal(MealEntity entity) {

        GoodsEntity good = new GoodsEntity();
        good.setGoods_id(entity.getId());
        good.setType(5);
        reduceData(good);
    }
    public void addDataNoCommit(GoodsEntity good) {


        //添加数据
        GoodsEntity tempCart = (GoodsEntity) data.get(good.getGoods_id());
        if (tempCart != null) {
            tempCart.setNumber(tempCart.getNumber() + 1);
        } else {
            tempCart = good;
            tempCart.setNumber(1);
        }
        data.put(good.getGoods_id(), tempCart);

    }

    public void addData(GoodsEntity good) {

        //添加数据
        GoodsEntity tempCart = (GoodsEntity) data.get(good.getGoods_id());
        if (tempCart != null) {
            if (good.getType() == 4)
                tempCart.setNumber(tempCart.getNumber() + 1);
            else
                tempCart.setRetail_price(good.getRetail_price());
        } else {
            tempCart = good;
            tempCart.setNumber(1);
        }
        data.put(good.getGoods_id(), tempCart);

        commit();

    }

    public void reduceData(GoodsEntity good) {

        //减数据
        GoodsEntity tempCart = (GoodsEntity) data.get(good.getGoods_id());
        if (tempCart != null) {
            tempCart.setNumber(tempCart.getNumber() - 1);

            if (tempCart.getNumber() == 0) {
                data.remove(good.getGoods_id());
            } else {
                data.put(good.getGoods_id(), tempCart);
            }
            commit();
        }
    }


    public void reduceDataNoCommit(GoodsEntity good) {
        //减数据
        GoodsEntity tempCart = (GoodsEntity) data.get(good.getGoods_id());
        if (tempCart != null) {
            tempCart.setNumber(tempCart.getNumber() - 1);
            if (tempCart.getNumber() == 0) {
                data.remove(good.getGoods_id());
            } else {
                data.put(good.getGoods_id(), tempCart);
            }

        }

    }

    public void commit() {


        //把parseArray转换成list
        List<GoodsEntity> carts = sparsesToList();

        Log.d("购物车：", "商品总数:" + getTotalGoodsNumber() + "商品种型数为：" + carts.size() + "商品总价格:" + getProductPrice() + getServerPrice());


        //把转换成String
        String json = new Gson().toJson(carts);
        // 保存
        new AppPreferences(context).put(JSON_CART, json);


    }

    //计算商品总价格
    public double getProductPrice() {
        List<GoodsEntity> carts = getProductList();
        double totalPrice = 0d;
        for (GoodsEntity g : carts) {
            totalPrice = g.getNumber() * g.getRetail_priceTodouble() + totalPrice;
        }
        return totalPrice;
    }

    //计算服务总价格
    public double getServerPrice() {
        List<GoodsEntity> carts = getServerList();
        double totalPrice = 0d;
        for (GoodsEntity g : carts) {
            totalPrice = g.getPriceTodouble() + totalPrice;
        }
        return totalPrice;
    }


    //商品总数
    private int getTotalGoodsNumber() {
        List<GoodsEntity> carts = sparsesToList();
        int i = 0;
        for (GoodsEntity g : carts) {

            i = g.getNumber() + i;
        }
        return i;
    }


    //商品
    private List<GoodsEntity> sparsesToList() {
        List<GoodsEntity> carts = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                GoodsEntity shoppingCart = (GoodsEntity) data.valueAt(i);
                carts.add(shoppingCart);
            }
        }
        return carts;
    }


    public void deleteAllData() {
        data.clear();
        new AppPreferences(context).remove(JSON_CART);

    }


    public boolean isNull() {

        if (sparsesToList().size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
