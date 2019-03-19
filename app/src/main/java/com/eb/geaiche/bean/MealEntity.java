package com.eb.geaiche.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.juner.mvp.bean.SelectedBean;

//套餐里的商品
public class MealEntity extends SelectedBean implements MultiItemEntity, Parcelable {

    int id;
    int goodsId;
    int goodsNum;
    String activitySn;
    int activityId;
    String activityName;
    String goodsName;

    long endTime;//有效时间
    String carNo;
    String mobile;

    //门店可用套卡字段
    int number;
    String name;
    int maxNum;//最大数量

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static Creator<MealEntity> getCREATOR() {
        return CREATOR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getActivitySn() {
        return activitySn;
    }

    public void setActivitySn(String activitySn) {
        this.activitySn = activitySn;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
        dest.writeInt(this.goodsId);
        dest.writeInt(this.goodsNum);
        dest.writeString(this.activitySn);
        dest.writeInt(this.activityId);
        dest.writeString(this.activityName);
        dest.writeString(this.goodsName);
        dest.writeLong(this.endTime);
        dest.writeString(this.carNo);
        dest.writeString(this.mobile);
        dest.writeInt(this.number);
        dest.writeString(this.name);
        dest.writeInt(this.maxNum);
    }

    protected MealEntity(Parcel in) {
        this.id = in.readInt();
        this.goodsId = in.readInt();
        this.goodsNum = in.readInt();
        this.activitySn = in.readString();
        this.activityId = in.readInt();
        this.activityName = in.readString();
        this.goodsName = in.readString();
        this.endTime = in.readLong();
        this.carNo = in.readString();
        this.mobile = in.readString();
        this.number = in.readInt();
        this.name = in.readString();
        this.maxNum = in.readInt();
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
