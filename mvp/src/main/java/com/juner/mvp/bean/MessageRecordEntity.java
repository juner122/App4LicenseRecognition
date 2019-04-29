package com.juner.mvp.bean;


//发送记录
public class MessageRecordEntity {

    String id;
    String shopId;
    String content;
    String addTime;
    int suc;
    int fail;
    String usersString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getSuc() {
        return suc;
    }

    public void setSuc(int suc) {
        this.suc = suc;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public String getUsersString() {
        return usersString;
    }

    public void setUsersString(String usersString) {
        this.usersString = usersString;
    }
}
