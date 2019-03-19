package com.eb.geaiche.mvp.contacts;

import android.support.v7.widget.RecyclerView;

import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;

/**
 * 登录页面契约类Contacts
 * <p>
 * 将View、Presenter、Model中的实现接口写在一起，看起来更加规范清晰，方便查找
 */
public class MessageMarketingContacts {
    /**
     * view 层接口
     */
    public interface MessageMarketingUI extends IBaseView {


    }

    /**
     * presenter 层接口
     */
    public interface MessageMarketingPtr extends IBasePresenter {
        void getRecordInfo( RecyclerView rv2);//

        void getModleInfo( RecyclerView rv3);//
    }

    /**
     * model 层接口
     */
    public interface MessageMarketingMdl {


    }

}
