package com.eb.new_line_seller.mvp.model;

import android.content.Context;

import com.eb.new_line_seller.mvp.contacts.FixInfoContacts;
import com.eb.new_line_seller.util.HttpUtils;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.FixParts;
import com.juner.mvp.bean.FixServie;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.OrderInfoEntity;

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


    //初次报价（状态将由0->2）
    @Override
    public void inform(FixInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {
        if (infoEntity.getOrderGoodsList().size() == 0 && infoEntity.getOrderProjectList().size() == 0) {
            ToastUtils.showToast("未选中任何商品！");
            return;
        } else {

            for (FixParts fp : infoEntity.getOrderGoodsList()) {
                if (fp.selectde()) {
                    sendRequest(HttpUtils.getFix().inform(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
                    return;
                }
            }
            for (FixServie fs : infoEntity.getOrderProjectList()) {
                if (fs.selectde()) {
                    sendRequest(HttpUtils.getFix().inform(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
                    return;
                }
            }

            ToastUtils.showToast("未选中任何商品！");
        }


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


    //报价单去生成订单
    @Override
    public void submit(OrderInfoEntity infoEntity, RxSubscribe<NullDataEntity> rxSubscribe) {
        sendRequest(HttpUtils.getFix().submit(getToken(context), infoEntity).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }
}
