package com.eb.geaiche.mvp.contacts;


import android.view.View;

import com.eb.geaiche.bean.Meal2;
import com.eb.geaiche.bean.MealEntity;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.RemakeActCard;
import com.juner.mvp.bean.SaveUserAndCarEntity;
import com.juner.mvp.bean.UserEntity;

import java.util.List;


//纸卡录入
public class ActivityCardContacts {


    /**
     * view 层接口
     */
    public interface ActivityCardUI extends IBaseView {

        //设置车辆列表
        void setCarList(List<CarInfoRequestParameters> carList);

        //套卡商品列表和选择的套卡名
        void setMealInfoList(List<MealEntity> mealInfoList, String mealName);


        //显示录入信息页面
        void showView();
        //
        void showCheckView();
        void hideCheckView();

        void onShowConfirmDialog();//显示确认Dialog

        void setUserName(String userName);//设置录卡人

        void setCarName(String userName);//设置车主姓名

        void etsetFocusable(boolean b);//
    }

    /**
     * presenter 层接口
     */
    public interface ActivityCardPtr extends IBasePresenter {

        void checkMember(String phone, String name);//会员检测

        void confirmInput(int position);//确认录入

        void setMealList(Meal2 meal2);//处理从选择套卡页面返回的套卡对象 变为录入接口的请求对象

        void setCarNo(String carNo);//设置车牌号

        void setCardSn(String cardSn);//设置套卡编号

        void setStartData(View view);//设置开始时间

        void setEndData(View view);//设置结束时间

        void showConfirmDialog(int position);//显示确认Dialog

        void getInfo();//获取当前登录用户

    }

    /**
     * model 层接口
     */
    public interface ActivityCardMdl {


        void checkMember(String phone, String name, RxSubscribe<SaveUserAndCarEntity> rxSubscribe);//检测用户

        void confirmInput(List<RemakeActCard> list, RxSubscribe<NullDataEntity> rxSubscribe);//确认录入
        void confirmAdd(List<RemakeActCard> list, RxSubscribe<NullDataEntity> rxSubscribe);//新增套卡

        void getInfo(RxSubscribe<UserEntity> rxSubscribe);//获取当前登录用户

    }


}
