package com.eb.geaiche.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.juner.mvp.bean.SelectedBean;

//套餐里的商品
public class MealEntity extends SelectedBean implements MultiItemEntity, Parcelable {

    int id;
    String goodsTitle;
    Integer goodsNum = 0;
    Integer type;
    String goodsCode;
    Integer maxNum = 0;
    Integer wxType;
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
    String activitySn;


    //5.30号新增字段
    String standardId;
    String goodsStandardTitle;



    public String getStandarId() {
        return standardId;
    }

    public void setStandarId(String standarId) {
        this.standardId = standarId;
    }

    public String getGoodsStandardTitle() {
        return goodsStandardTitle;
    }

    public void setGoodsStandardTitle(String goodsStandardTitle) {
        this.goodsStandardTitle = goodsStandardTitle;
    }

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

    public Integer getWxType() {
        return wxType;
    }

    public void setWxType(Integer wxType) {
        this.wxType = wxType;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public String getGoods_sn() {
        return goodsCode;
    }

    public void setGoods_sn(String goods_sn) {
        this.goodsCode = goods_sn;
    }


    public String getName() {
        return goodsTitle;
    }

    public void setName(String name) {
        this.goodsTitle = name;
    }

    public Integer getNumber() {
        return null == goodsNum ? 0 : goodsNum;
    }

    public void setNumber(Integer number) {
        this.goodsNum = number;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return 1;
    }

    public MealEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.goodsTitle);
        dest.writeValue(this.goodsNum);
        dest.writeValue(this.type);
        dest.writeString(this.goodsCode);
        dest.writeValue(this.maxNum);
        dest.writeValue(this.wxType);
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
        dest.writeString(this.activitySn);
        dest.writeString(this.standardId);
        dest.writeString(this.goodsStandardTitle);
    }

    protected MealEntity(Parcel in) {
        this.id = in.readInt();
        this.goodsTitle = in.readString();
        this.goodsNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.goodsCode = in.readString();
        this.maxNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.wxType = (Integer) in.readValue(Integer.class.getClassLoader());
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
        this.activitySn = in.readString();
        this.standardId = in.readString();
        this.goodsStandardTitle = in.readString();
    }

    public static final Creator<MealEntity> CREATOR = new Creator<MealEntity>() {
        @Override
        public MealEntity createFromParcel(Parcel source) {
            return new MealEntity(source);
        }

        @Override
        public MealEntity[] newArray(int size) {
            return new MealEntity[size];
        }
    };
}
