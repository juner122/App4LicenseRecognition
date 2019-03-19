package com.eb.geaiche.mvp.contacts;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;


//汽车检修单信息页面
public class FixInfoContacts {


    /**
     * view 层接口
     */
    public interface FixInfoUI extends IBaseView {


        void setInfo(FixInfoEntity fixInfo);//设置页面信息


        void createOrderSuccess(int i);//生成估价单成功

        void setServicePrice(String price);//设置工时金额

        void setPartsPrice(String price);//设置配件金额

        void setAllPrice(String price);//设置总价


        void showAddButton();//显示增加按钮

        void hideAddButton();//隐藏增加按钮


        void setButtonText(String text);//设置文字

        void onToCarInfoActivity(int car_id);//到车况信息页面

        void showSaveButton();//显示保存退出按钮

        void showPostFixButton();//显示提交修改按钮


        void setRTitle();

        String getDec();//
    }

    /**
     * presenter 层接口
     */
    public interface FixInfoPtr extends IBasePresenter {

        void getInfo();//获取页面数据


        void initRecyclerView(RecyclerView rv_service, RecyclerView rv_parts);

        void onInform();////初次报价（状态将由0->2）      重新提交勾选后的各个项目    两种情况

        void handleCallback(Intent intent);//处理从选择工时页面或选择配件页面返回的结果

        void toCarInfoActivity();//到车况信息页面

        void remakeSave(int type);//保存修改 // type = 0 保存退出 ，1添加维修工时，2更换材料

        void remakeSelected();//提交修改

        void changeDec();//修改备注

        void setlpvUrl(String url);//设置签名图片 七牛云url

        void toAuthorizeActivity();

        void notice();//通知客户

    }

    /**
     * model 层接口
     */
    public interface FixInfoMdl {


        void getInfo(int id, RxSubscribe<FixInfo> rxSubscribe);//页面数据接口

//        void inform(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//初次报价（状态将由0->2）

        void addGoodsOrProject(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//追加项目（报价单status=1状态下才可调用）


        void remakeSelected(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//重新提交勾选后的各个项目

        void remakeSave(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//保存修改

        void replaceReback(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//店长跨客户回撤 （不需要凭证图片，报价单status=3状态下才可调用，将由status3->2）

        void replaceConfirm(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//店长跨客户确认（需要凭证图片才能提。报价单status=2状态下才可调用，将由status2->3）




        void submit(OrderInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe);//报价单去生成订单


    }


}
