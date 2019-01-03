package com.eb.new_line_seller.bean;

public class UserInfo extends BaseBean {


    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
