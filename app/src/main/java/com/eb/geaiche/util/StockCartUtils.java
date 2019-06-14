package com.eb.geaiche.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.eb.geaiche.bean.MealEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.GoodsEntity;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.juner.mvp.Configure.Goods_TYPE_3;
import static com.juner.mvp.Configure.Goods_TYPE_4;
import static com.juner.mvp.Configure.Goods_TYPE_5;
import static com.juner.mvp.Configure.JSON_CART;
import static com.juner.mvp.Configure.JSON_STOCK_CART;

public class StockCartUtils {

    private static StockCartUtils instance = null;

    SparseArray data;


    Context context;

    public static synchronized StockCartUtils getInstance(Context context) {

        //这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        //使用时生成实例，提高了效率！
        if (instance == null)
            instance = new StockCartUtils(context);
        return instance;
    }

    public StockCartUtils(Context context) {

        data = new SparseArray<>(100);
        new AppPreferences(context).remove(JSON_CART);
        this.context = context;
    }


    //本地获取json数据，并且通过Gson解析成list列表数据
    public List<GoodsEntity> getDataFromLocal() {
        List<GoodsEntity> carts = new ArrayList<>();
        //从本地获取缓存数据
        String savaJson = new AppPreferences(context).getString(JSON_STOCK_CART, "");
        if (!TextUtils.isEmpty(savaJson)) {
            //把数据转换成列表
            carts = new Gson().fromJson(savaJson, new TypeToken<List<GoodsEntity>>() {
            }.getType());
        }
        return carts;

    }







    public void commit() {


        //把parseArray转换成list
        List<GoodsEntity> carts = sparsesToList();



        //把转换成String
        String json = new Gson().toJson(carts);
        // 保存
        new AppPreferences(context).put(JSON_CART, json);


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
        new AppPreferences(context).remove(JSON_STOCK_CART);

    }


    public boolean isNull() {

        if (sparsesToList().size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private int getCartId(GoodsEntity good) {
        //购物车商品id 有两种情况 一，只有商品没规格 cart_id =  good_id;   二，有规格   cart_id = goodsStandardId;
        if (null != good.getId() && !good.getId().equals("")) {
            return Integer.valueOf(good.getId());
        }
        if (null == good.getGoodsStandard())
            return good.getGoods_id();
        else {
            return good.getGoodsStandard().getId();
        }
    }
}
