package com.juner.mvp.bean;

import java.util.List;

public class Token {


    TokenEntity token;
    List<AppMenu> appMenuList;


    public List<AppMenu> getAppMenuList() {
        return appMenuList;
    }

    public void setAppMenuList(List<AppMenu> appMenuList) {
        this.appMenuList = appMenuList;
    }

    public TokenEntity getToken() {
        return token;
    }

    public void setToken(TokenEntity token) {
        this.token = token;
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

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }
    }
}
