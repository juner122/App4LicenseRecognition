package com.juner.mvp.bean;

import java.util.List;

public class Token {


    TokenEntity token;
    List<AppMenu> appMenuList;
    Integer shop_type;   //0直营1是加盟  默认为1

    public List<AppMenu> getAppMenuList() {
        return appMenuList;
    }


    public TokenEntity getToken() {
        return token;
    }

    public void setToken(TokenEntity token) {
        this.token = token;
    }

    public int getShop_type() {
        return null == shop_type ? 1 : shop_type;
    }



    public class TokenEntity {

        String token;
        String expire;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


    }
}
