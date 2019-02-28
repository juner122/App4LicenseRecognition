package com.eb.new_line_seller.mvp.model;

import android.content.Context;

import com.aliyun.vodplayer.BuildConfig;
import com.eb.new_line_seller.util.SystemUtil;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Token;
import com.eb.new_line_seller.mvp.contacts.LoginContacts;


import net.grandcentrix.tray.AppPreferences;

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
        map.put("version", SystemUtil.packaGetName());

        sendRequest(HttpUtils.getLoginApi().login(map).compose(RxHelper.<Token>observe()), rxSubscribe);
    }

    @Override
    public void smsSendSms(String mobile, RxSubscribe<NullDataEntity> rxSubscribe) {

        int type = 1;
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("type", type);//1登陆2体现3银行卡验证

        sendRequest(HttpUtils.getLoginApi().smsSendSms(map).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);

    }

    @Override
    public void saveToken(Token token, Context context) {

        new AppPreferences(context).put(Configure.Token, token.getToken().getToken());

    }

    @Override
    public void savePhone(String phone, Context context) {
        new AppPreferences(context).put(Configure.moblie_s, phone);
    }


}

