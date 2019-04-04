package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.FixInfoContacts;
import com.eb.geaiche.util.HttpUtils;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class FixInfoMdl extends BaseModel implements FixInfoContacts.FixInfoMdl {
    Context context;

    public FixInfoMdl(Context context) {
        this.context = context;
    }


    @Override
    public void getInfo(int id, RxSubscribe<FixInfo> rxSubscribe) {


        sendRequest(HttpUtils.getFix().info(getToken(context), id).compose(RxHelper.<FixInfo>observe()), rxSubscribe);
    }


    //追加项目（报价单status=1状态下才可调用）
    @Override
    public void addGoodsOrProject(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {
        sendRequest(HttpUtils.getFix().addGoodsOrProject(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }


    //重新提交勾选后的各个项目（报价单status=1状态下才可调用，将由status1->2）
    @Override
    public void remakeSelected(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {
        sendRequest(HttpUtils.getFix().remakeSelected(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }

    @Override
    public void remakeSave(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {

        if (infoEntity.getOrderGoodsList().size() == 0 && null == infoEntity.getDescribe() || "".equals(infoEntity.getDescribe())) {
            ToastUtils.showToast("未作任何修改，不能保存退出！");
            return;
        }
        sendRequest(HttpUtils.getFix().remakeSave(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);


    }

    //店长跨客户回撤 （不需要凭证图片，报价单status=3状态下才可调用，将由status3->2）
    @Override
    public void replaceReback(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {

        sendRequest(HttpUtils.getFix().replaceReback(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }

    //店长跨客户确认（需要凭证图片才能提。报价单status=2状态下才可调用，将由status2->3）
    @Override
    public void replaceConfirm(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {



        sendRequest(HttpUtils.getFix().replaceConfirm(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);

    }


    //报价单去生成订单
    @Override
    public void submit(OrderInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {
        sendRequest(HttpUtils.getFix().submit(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }


}
