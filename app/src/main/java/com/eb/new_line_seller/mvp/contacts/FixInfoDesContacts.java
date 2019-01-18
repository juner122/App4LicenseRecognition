package com.eb.new_line_seller.mvp.contacts;


import android.widget.TextView;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;

import java.util.List;

/**
 * 维修故障描述页面
 */
public class FixInfoDesContacts {

    public interface FixInfoDesUI extends IBaseView {

        void setCarNo(String carNo);//设置车牌

        void setTechnician(String technicians);//设置技师

        void setDate(String date);//设置报价时间

        void setTip(String tip);//添加快捷描述

        void toFixInfoActivity(int id);
        void toMian();

    }

    public interface FixInfoDesPtr extends IBasePresenter {

        void setTipClickListener(List<TextView> textViews);

        void getInfo();

        void getDescribe(String describe);//描述

        void toTechnicianListActivity();//前往技师选择页面

        void quotationSave(boolean isFinish);//是否保存退出


    }

    public interface FixInfoDesMdl {


        void quotationSave(FixInfoEntity fixInfo, RxSubscribe<NullDataEntity> rxSubscribe);//保存退出
    }

}
