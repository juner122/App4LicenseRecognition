package com.eb.geaiche.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.juner.mvp.bean.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.juner.mvp.Configure.JSON_ServerCART;

public class CartServerUtils {

    private static CartServerUtils instance = null;

    SparseArray data;


    Context context;

    public static synchronized CartServerUtils getInstance(Context context) {

        //这个方法比上面有所改进，不用每次都进行生成对象，只是第一次
        //使用时生成实例，提高了效率！
        if (instance == null)
            instance = new CartServerUtils(context);
        return instance;
    }

    public CartServerUtils(Context context) {

        data = new SparseArray<>(100);
        new AppPreferences(context).remove(JSON_ServerCART);
        this.context = context;
    }


    public void listToSparse(List<Server> servers) {

        //放到sparseArry中
        if (servers != null && servers.size() > 0) {
            for (int i = 0; i < servers.size(); i++) {
                Server server = servers.get(i);
                data.put(server.getId(), server);
            }
        }
        commit();

    }


    //本地获取json数据，并且通过Gson解析成list列表数据
    private ArrayList<Server> getDataFromLocal() {
        ArrayList<Server> carts = new ArrayList<>();
        //从本地获取缓存数据
        String savaJson = new AppPreferences(context).getString(JSON_ServerCART, "");
        if (!TextUtils.isEmpty(savaJson)) {
            //把数据转换成列表
            carts = new Gson().fromJson(savaJson, new TypeToken<List<Server>>() {
            }.getType());
        }
        return carts;

    }


    public ArrayList<Server> getServerList() {

        return getDataFromLocal();


    }


    public void addServieData(Server good) {

        addData(good);
    }

    public void setServieDatas(List<Server> goods) {

        for (Server g : goods) {
            addServieData(g);
        }
    }

    public void addData(Server good) {

        //添加数据
        Server tempCart = (Server) data.get(good.getId());
        if (tempCart == null) {

            tempCart = good;
            tempCart.setNumber(1);
        }
        data.put(good.getId(), tempCart);
        commit();
    }

    public void reduceData(Server good) {
        //减数据
        Server tempCart = (Server) data.get(good.getId());
        if (tempCart != null) {
            data.remove(good.getId());
            commit();
        }

    }

    private void commit() {


        //把parseArray转换成list
        List<Server> carts = sparsesToList();

        //把转换成String
        String json = new Gson().toJson(carts);
        // 保存
        new AppPreferences(context).put(JSON_ServerCART, json);


    }


    //计算服务总价格
    public double getServerPrice() {
        List<Server> carts = getServerList();
        double totalPrice = 0.00d;
        for (Server g : carts) {
            totalPrice = g.getPrice() + totalPrice;
        }
        return totalPrice;
    }

    //商品
    private List<Server> sparsesToList() {
        List<Server> carts = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                Server shoppingCart = (Server) data.valueAt(i);
                carts.add(shoppingCart);
            }
        }
        return carts;
    }


    public void deleteAllData() {
        data.clear();
        new AppPreferences(context).remove(JSON_ServerCART);

    }


    public boolean isNull() {

        if (sparsesToList().size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
