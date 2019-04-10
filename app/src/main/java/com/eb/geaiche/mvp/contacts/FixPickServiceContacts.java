package com.eb.geaiche.mvp.contacts;


import android.support.v7.widget.RecyclerView;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.FixServiceList;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.FixServieEntity;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import java.util.List;


//选择服务工时
public class FixPickServiceContacts {


    /**
     * view 层接口
     */
    public interface FixPickServiceUI extends IBaseView {

        void showServiceList();//显示工时商品列表

        void showService2List();//显示工时2级类别列表

        void setPickAllPrice(String prices);//更新已选择的总价格

        void onConfirm(List<FixServie> fixServies);//确认选择

        String getKey();//获取搜索字

    }

    /**
     * presenter 层接口
     */
    public interface FixPickServicePtr extends IBasePresenter {


        void onGetData(RadioGroup rg);//获取数据后 直接创建RadioGroup

        void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_service, EasyRefreshLayout easylayout);//

        void confirm();//确认选择

        void seekServerforKey(String key);//搜索


    }

    /**
     * model 层接口
     */
    public interface FixPickServiceMdl {

        void getServiceData(RxSubscribe<List<GoodsCategory>> rxSubscribe);//获取大分类数据

        void searchServer(int id, String key, RxSubscribe<FixServieEntity> rxSubscribe);//搜索

        void getGoodList(RxSubscribe<GoodsList> rxSubscribe, String goodsTitle, int page,String categoryId);
    }


}
