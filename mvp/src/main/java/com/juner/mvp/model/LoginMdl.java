package com.juner.mvp.model;

import com.juner.mvp.api.HttpUtils;
import com.juner.mvp.api.RxHelper;
import com.juner.mvp.api.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.Token;
import com.juner.mvp.contacts.LoginContacts;


import java.util.HashMap;
import java.util.Map;


public class LoginMdl extends BaseModel implements LoginContacts.LoginMdl {


    /**
     * 登录
     *
     * @param mobile      手机号
     * @param authCode    验证码
     * @param rxSubscribe 网络请求回调
     */
    @Override
    public void login(String mobile, String authCode, RxSubscribe<Token> rxSubscribe) {

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("authCode", authCode);

        sendRequest(HttpUtils.getLoginApi().login(map).compose(RxHelper.<Token>observe()), rxSubscribe);
    }
}

