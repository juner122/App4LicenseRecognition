package com.juner.mvp.bean;

import java.util.List;

public class Token {


    TokenEntity token;
    List<AppMenu> appMenuList;
    Integer shop_type;   //0直营1是加盟  默认为1
    Integer user_role;   //0店员 1 店长


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


    public Integer getUser_role() {
        return user_role;
    }

    public void setUser_role(Integer user_role) {
        this.user_role = user_role;
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
