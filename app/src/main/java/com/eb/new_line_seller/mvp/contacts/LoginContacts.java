package com.eb.new_line_seller.mvp.contacts;


import android.content.Context;

import com.juner.mvp.api.http.RxSubscribe;

import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.AppMenu;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Token;

import java.util.List;

/**
 * 登录页面契约类Contacts
 * <p>
 * 将View、Presenter、Model中的实现接口写在一起，看起来更加规范清晰，方便查找
 */
public class LoginContacts {
    /**
     * view 层接口
     */
    public interface LoginUI extends IBaseView {
        /**
         * 登录成功
         */
        void loginSuccess(List<AppMenu> list);//权限

        /**
         * 登录失败
         */
        void loginFailure(String masage);


        //设置倒计时
        void countDown(String cou);

        //设置发送验证码按钮是否可点击
        void setCodeViewClickable(boolean isClickable);


    }

    /**
     * presenter 层接口
     */
    public interface LoginPtr extends IBasePresenter {
        void login(String mobile, String authCode, String cid);//cid 个推cid

        void smsSendSms(String mobile);//发送短信

        void stopCountDown();//停止倒计时
    }

    /**
     * model 层接口
     */
    public interface LoginMdl {

        void login(String mobile, String authCode, String cid, RxSubscribe<Token> rxSubscribe);

        void saveToken(Token token, Context context);

        void savePhone(String phone, Context context);

        void smsSendSms(String mobile, RxSubscribe<NullDataEntity> rxSubscribe);//发送短信
    }

}
