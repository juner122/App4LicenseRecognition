package com.juner.mvp.bean;

public class Ask {

    String id;
    String shopName;
    String askContent;//内容
    String createTime;
    String unityId;
    String unityName;
    String unityImg;
    String shopId;
    String userId;
    String userName;
    String answerContent;
    String status;//报名状态

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnityId() {
        return unityId;
    }

    public void setUnityId(String unityId) {
        this.unityId = unityId;
    }

    public String getUnityName() {
        return unityName;
    }

    public void setUnityName(String unityName) {
        this.unityName = unityName;
    }

    public String getUnityImg() {
        return unityImg;
    }

    public void setUnityImg(String unityImg) {
        this.unityImg = unityImg;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAskContent() {
        return askContent;
    }

    public void setAskContent(String askContent) {
        this.askContent = askContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
