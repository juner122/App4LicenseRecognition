package com.eb.geaiche.mvp.contacts;


import androidx.recyclerview.widget.RecyclerView;
import android.widget.RadioGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixPartsEntityList;
import com.juner.mvp.bean.GoodsCategory;
import com.juner.mvp.bean.GoodsList;

import java.util.List;


//选择配件
public class FixPickPartsContacts {


    /**
     * view 层接口
     */
    public interface FixPickPartsUI extends IBaseView {

        void showPartsList();//显示配件列表

        void showParts2List();//显示配件2级类别列表

        void setPickAllPrice(String prices);//更新已选择的总价格

        void onConfirm(List<FixParts> fixPartsList);//确认选择
        String getKey();//获取搜索字
    }

    /**
     * presenter 层接口
     */
    public interface FixPickPartsPtr extends IBasePresenter {


        void onGetData(RadioGroup rg);//获取数据后 直接创建RadioGroup

        void initRecyclerView(RecyclerView rv_2item, RecyclerView rv_service, EasyRefreshLayout easylayout);//

        void confirm();//确认选择

//        void seekParts(int id);//查找配件 根据id

        void seekPartsforKey(String key);
        void loadMoreData();
        void getData();

    }

    /**
     * model 层接口
     */
    public interface FixPickPartsMdl {

        void getPartsData(RxSubscribe<List<GoodsCategory>> rxSubscribe);//获取一级二级数据

        void seekParts(int id, RxSubscribe<FixPartsEntityList> rxSubscribe);//查找配件 根据id

        void seekPartsforKey(int id, String key, RxSubscribe<FixPartsEntityList> rxSubscribe);//查找配件

        void getGoodList(RxSubscribe<GoodsList> rxSubscribe, String goodsTitle, int page,String categoryId);


    }


}
