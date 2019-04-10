package com.eb.geaiche.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.juner.mvp.bean.SelectedBean;

//开卡记录
public class MealEntity2 extends SelectedBean implements MultiItemEntity, Parcelable {

    int id;
    String activityId;
    String goodsId;
    String shopName;
    String shopId;
    String mobile;
    String userName;
    String userId;
    String activityName;
    String goodsName;
    String endTime;
    String carNo;
    String dealUserName;
    Integer goodsNum = 0;
    String activitySn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getActivitySn() {
        return activitySn;
    }

    public void setActivitySn(String activitySn) {
        this.activitySn = activitySn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.activityId);
        dest.writeString(this.goodsId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopId);
        dest.writeString(this.mobile);
        dest.writeString(this.userName);
        dest.writeString(this.userId);
        dest.writeString(this.activityName);
        dest.writeString(this.goodsName);
        dest.writeString(this.endTime);
        dest.writeString(this.carNo);
        dest.writeString(this.dealUserName);
        dest.writeValue(this.goodsNum);
        dest.writeString(this.activitySn);
    }

    public MealEntity2() {
    }

    protected MealEntity2(Parcel in) {
        this.id = in.readInt();
        this.activityId = in.readString();
        this.goodsId = in.readString();
        this.shopName = in.readString();
        this.shopId = in.readString();
        this.mobile = in.readString();
        this.userName = in.readString();
        this.userId = in.readString();
        this.activityName = in.readString();
        this.goodsName = in.readString();
        this.endTime = in.readString();
        this.carNo = in.readString();
        this.dealUserName = in.readString();
        this.goodsNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.activitySn = in.readString();
    }

    public static final Creator<MealEntity2> CREATOR = new Creator<MealEntity2>() {
        @Override
        public MealEntity2 createFromParcel(Parcel source) {
            return new MealEntity2(source);
        }

        @Override
        public MealEntity2[] newArray(int size) {
            return new MealEntity2[size];
        }
    };

    @Override
    public int getItemType() {
        return 1;
    }
}
