package com.eb.geaiche.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.eb.geaiche.bean.MealEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.Goods;
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
        new AppPreferences(context).remove(JSON_STOCK_CART);
        this.context = context;
    }


    //本地获取json数据，并且通过Gson解析成list列表数据
    public List<Goods> getDataFromLocal() {
        List<Goods> carts = new ArrayList<>();
        //从本地获取缓存数据
        String savaJson = new AppPreferences(context).getString(JSON_STOCK_CART, "");
        if (!TextUtils.isEmpty(savaJson)) {
            //把数据转换成列表
            carts = new Gson().fromJson(savaJson, new TypeToken<List<Goods>>() {
            }.getType());
        }
        return carts;

    }

    /**
     * 添加一个商品规格
     *
     * @param good 要添加规格的商品
     * @param gs   要添加的规格
     */
    public void addDataNoCommit(Goods good, Goods.GoodsStandard gs) {

        int cart_id = good.getId();


        //添加数据
        Goods tempCart = (Goods) data.get(cart_id);
        if (tempCart != null) {//不等于空

            List<Goods.GoodsStandard> gsl = tempCart.getXgxGoodsStandardPojoList();

            gsl.add(gs);
            tempCart.setXgxGoodsStandardPojoList(gsl);
        } else {
            List<Goods.GoodsStandard> gsl = new ArrayList<>();
            gsl.add(gs);
            good.setXgxGoodsStandardPojoList(gsl);

            tempCart = good;


        }
        data.put(cart_id, tempCart);
    }


    /**
     * 设置商品规格数量
     *
     * @param gs     要设置规格
     * @param num    设置数量
     * @param goodId 要设置的商品
     */
    public void setNum(int goodId, Goods.GoodsStandard gs, int num) {

        if (num <= 0) {

            ToastUtils.showToast("数量不能为0！");
            return;
        }


        //添加数据
        Goods tempCart = (Goods) data.get(goodId);
        if (tempCart != null) {//不等于空

            List<Goods.GoodsStandard> gsl = tempCart.getXgxGoodsStandardPojoList();


            for (int i = 0; i < gsl.size(); i++) {
                if (gsl.get(i).getId() == gs.getId()) {
                    tempCart.getXgxGoodsStandardPojoList().get(i).setNum(num);
                    break;
                }
            }


            data.put(goodId, tempCart);
        }


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


}
