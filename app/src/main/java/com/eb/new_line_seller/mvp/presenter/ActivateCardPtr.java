package com.eb.new_line_seller.mvp.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.eb.new_line_seller.bean.Meal2;
import com.eb.new_line_seller.bean.MealEntity;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.RemakeActCard;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.eb.new_line_seller.mvp.contacts.ActivityCardContacts;
import com.eb.new_line_seller.mvp.model.ActivateCardMdl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivateCardPtr extends BasePresenter<ActivityCardContacts.ActivityCardUI> implements ActivityCardContacts.ActivityCardPtr {

    private ActivityCardContacts.ActivityCardMdl mdl;
    private Activity context;

    List<RemakeActCard> list;//接口请求参数对象

    private int user_id;//用户id
    private String car_no;//车牌
    private String card_sn;//卡号

    public ActivateCardPtr(@NonNull ActivityCardContacts.ActivityCardUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new ActivateCardMdl(context);
        list = new ArrayList<>();
    }

    @Override
    public void checkMember(String phone, String name) {

        if (phone.equals("")) {
            getView().showToast("手机号码不能为空");
            return;
        }
        if (name.equals("")) {
            getView().showToast("车主姓名不能为空");
            return;
        }

        mdl.checkMember(phone, name, new RxSubscribe<SaveUserAndCarEntity>(context, true) {
            @Override
            protected void _onNext(SaveUserAndCarEntity entity) {
                user_id = entity.getUser_id();

                getView().setCarList(entity.getCarList());//车辆列表

                getView().showView();
            }

            @Override
            protected void _onError(String message) {
                getView().showToast(message);
            }
        });

    }

    @Override
    public void confirmInput() {
        if (!refreshList()) return;
        mdl.confirmInput(list, new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity n) {

                getView().showToast("录入成功！");

                context.finish();
            }

            @Override
            protected void _onError(String message) {
                getView().showToast("录入失败:" + message);
            }
        });

    }

    @Override
    public void setMealList(Meal2 meal2) {

        List<MealEntity> goodsList = meal2.getGoodsList();
        getView().setMealInfoList(goodsList, meal2.getActName());
        for (MealEntity m : goodsList) {
            RemakeActCard rac = new RemakeActCard();
            rac.setActivityId(meal2.getActivityId());
            rac.setActivityName(meal2.getActName());
            rac.setDetailsId(meal2.getId());
            rac.setGoodsId(m.getId());
            rac.setGoodsName(m.getName());
            rac.setGoodsNum(m.getNumber());
            list.add(rac);
        }

    }

    private boolean refreshList() {
        if (car_no.equals("")) {
            getView().showToast("请选择车辆！");
            return false;
        }

        if (card_sn.equals("")) {
            getView().showToast("卡号不能为空！");
            return false;
        }


        for (int i = 0; i < list.size(); i++) {
            list.get(i).setActivitySn(card_sn);
            list.get(i).setAddTime(new Date().getTime());
            list.get(i).setCarNo(car_no);
            list.get(i).setEndTime(new Date().getTime());
            list.get(i).setUserId(user_id);
        }
        return true;
    }


    @Override
    public void setCarNo(String carNo) {
        car_no = carNo;
    }

    @Override
    public void setCardSn(String cardSn) {
        card_sn = cardSn;
    }

}
