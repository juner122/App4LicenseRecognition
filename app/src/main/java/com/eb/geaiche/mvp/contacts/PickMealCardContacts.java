package com.eb.geaiche.mvp.contacts;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.geaiche.adapter.MealPickListAdapter;
import com.eb.geaiche.bean.Meal2;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;

import java.util.List;


//纸卡录入 选择套卡
public class PickMealCardContacts {


    /**
     * view 层接口
     */
    public interface PickMealCardUI extends IBaseView {

        //设置车辆列表
        void setMealList(List<MultiItemEntity> mealList);

        void setPickMealName(String name);//设置选择的套卡名

        void onConfirmPick(Meal2 meal2);//确认选择套卡 返回上个页面 返回一个套卡对象
    }

    /**
     * presenter 层接口
     */
    public interface PickMealCardPtr extends IBasePresenter {


        void getMealList();//门店可录入套卡列表 不包含商品

        void setOnItemChildClickListener(MealPickListAdapter adapter);//设置套卡项点击监听器



        void getMeal2(List<MultiItemEntity> data);//获取选择中的Meal2对象
    }

    /**
     * model 层接口
     */
    public interface PickMealCardMdl {
        void getMealList(RxSubscribe<List<Meal2>> rxSubscribe);


    }


}
