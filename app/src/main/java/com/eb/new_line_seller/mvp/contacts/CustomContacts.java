package com.eb.new_line_seller.mvp.contacts;


import android.view.View;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.Component;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixPartsListEntity;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.FixServiceListEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.ShopProject;

import java.util.List;

//自定义配件
public class CustomContacts {


    /**
     * view 层接口
     */
    public interface CustomUI extends IBaseView {

        void onChangeView(int type);//0 工时  1 配件

        void setNumber(int num);//数量

        void setType1String(String name);//设置一级分类name

        void setType2String(String name);//设置二级分类name

        void confirm(List list, int type);//确认添加 返回工时还是配件
        void onContinue();

    }

    /**
     * presenter 层接口
     */
    public interface CustomPtr extends IBasePresenter {

        void changeView();//变更页面，配件或工时

        void confirm(String dec, String name, String price, int number, boolean isContinue);//确认  isContinue 是否继续添加



        void numberUp(int num);//加数量

        void numberDown(int num);//减数量


        void pickType1(View view);//选择一级分类

        void pickType2(View view);//选择二级分类


    }

    /**
     * model 层接口
     */
    public interface CustomMdl {
        void componentSave(Component component, RxSubscribe<Component> rxSubscribe);//自定义配件

        void componentFirstCategory(RxSubscribe<List<FixParts2item>> rxSubscribe);//自定义零件 一级分类下拉框

        void componentSecondCategory(int parent_id, RxSubscribe<List<FixParts2item>> rxSubscribe);//自定义零件 二级分类下拉框


        void addShopService(ShopProject shopProject, RxSubscribe<ShopProject> rxSubscribe);//自定义工时

        void firstService(RxSubscribe<List<FixService2item>> rxSubscribe);//自定义工时  一级分类下拉框

        void secondService(int parent_id, RxSubscribe<List<FixService2item>> rxSubscribe);//自定义工时 二级分类下拉框


    }


}
