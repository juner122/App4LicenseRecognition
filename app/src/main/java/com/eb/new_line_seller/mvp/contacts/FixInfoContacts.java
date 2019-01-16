package com.eb.new_line_seller.mvp.contacts;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixInfoItem;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;
import com.juner.mvp.bean.SaveUserAndCarEntity;

import java.util.List;


//汽车检修单信息页面
public class FixInfoContacts {


    /**
     * view 层接口
     */
    public interface FixInfoUI extends IBaseView {


        void setInfo(FixInfoEntity fixInfo);//设置页面信息


        void createOrderSuccess();//生成估价单成功

//        void setServicePrice();//设置工时金额
//
//        void setPartsPrice();//设置配件金额


        void showAddButton();//显示增加按钮

        void hideAddButton();//隐藏增加按钮

        void setButtonText(String text);//设置文字


    }

    /**
     * presenter 层接口
     */
    public interface FixInfoPtr extends IBasePresenter {

        void getInfo();//获取页面数据

        void upServiceDataList(List<FixServie> list);//更新工时列表

        void upPartsDataList(List<FixParts> list);//更新配件列表

        void initRecyclerView(RecyclerView rv_service, RecyclerView rv_parts);

        void onInform();////初次报价（状态将由0->2）      重新提交勾选后的各个项目    两种情况

        void upDataServicePrice(TextView tv_price);//更新工时金额

        void upDataPartsPrice(TextView tv_price);//更新配件金额


        void handleCallback(Intent intent);//处理从选择工时页面或选择配件页面返回的结果

    }

    /**
     * model 层接口
     */
    public interface FixInfoMdl {


        void getInfo(int id, RxSubscribe<FixInfo> rxSubscribe);//页面数据接口

        void inform(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//初次报价（状态将由0->2）

        void addGoodsOrProject(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//追加项目（报价单status=1状态下才可调用）


        void remakeSelected(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//重新提交勾选后的各个项目

        void submit(OrderInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//报价单去生成订单

    }


}
