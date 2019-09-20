package com.eb.geaiche.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juner.mvp.bean.Goods;


import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

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
        new AppPreferences(context).remove(JSON_STOCK_CART);
        this.context = context;
    }


    //本地获取json数据，并且通过Gson解析成list列表数据
    public List<Goods.GoodsStandard> getDataFromLocal() {
        List<Goods.GoodsStandard> carts = new ArrayList<>();
        //从本地获取缓存数据
        String savaJson = new AppPreferences(context).getString(JSON_STOCK_CART, "");
        if (!TextUtils.isEmpty(savaJson)) {
            //把数据转换成列表
            carts = new Gson().fromJson(savaJson, new TypeToken<List<Goods.GoodsStandard>>() {
            }.getType());
        }
        return carts;

    }

    /**
     * 添加一个商品规格
     *
     * @param gs 要添加的规格
     */
    public void addGoodsStandard(Goods.GoodsStandard gs) {

        int gsId = gs.getId();

        //添加数据
        Goods.GoodsStandard tempCart = (Goods.GoodsStandard) data.get(gsId);
        if (null == tempCart) {//不等于空
            tempCart = gs;
//            tempCart.setNum(1);//设置默认数量为1
            data.put(gsId, tempCart);
        }
        commit();
    }

    /**
     * 去掉一个商品规格
     *
     * @param gs 要添加的规格
     */
    public void deleteGoodsStandard(Goods.GoodsStandard gs) {

        int gsId = gs.getId();

        //添加数据
        Goods.GoodsStandard tempCart = (Goods.GoodsStandard) data.get(gsId);
        if (null != tempCart) {//不等于空
            data.remove(gsId);
        }
        commit();
    }




    public void commit() {


        //把parseArray转换成list
        List<Goods.GoodsStandard> carts = sparsesToList();


        //把转换成String
        String json = new Gson().toJson(carts);
        // 保存
        new AppPreferences(context).put(JSON_STOCK_CART, json);


    }


    //商品总数
    private int getTotalGoodsNumber() {
        List<Goods.GoodsStandard> carts = sparsesToList();
        int i = 0;
        for (Goods.GoodsStandard g : carts) {

            i = g.getNum() + i;
        }
        return i;
    }


    //商品
    private List<Goods.GoodsStandard> sparsesToList() {
        List<Goods.GoodsStandard> carts = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Goods.GoodsStandard shoppingCart = (Goods.GoodsStandard) data.valueAt(i);
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


}
