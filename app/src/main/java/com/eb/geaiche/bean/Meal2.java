package com.eb.geaiche.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

//门店可录套卡
public class Meal2 extends AbstractExpandableItem<MealEntity> implements Parcelable ,MultiItemEntity {


    int id;
    int activityId;
    String actName;
    String createTime;
    String price;
    List<MealEntity> goodsList;


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public List<MealEntity> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<MealEntity> goodsList) {
        this.goodsList = goodsList;
    }

    public Meal2() {
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.activityId);
        dest.writeString(this.actName);
        dest.writeString(this.createTime);
        dest.writeString(this.price);
        dest.writeTypedList(this.goodsList);
    }

    protected Meal2(Parcel in) {
        this.id = in.readInt();
        this.activityId = in.readInt();
        this.actName = in.readString();
        this.createTime = in.readString();
        this.price = in.readString();
        this.goodsList = in.createTypedArrayList(MealEntity.CREATOR);
    }

    public static final Creator<Meal2> CREATOR = new Creator<Meal2>() {
        @Override
        public Meal2 createFromParcel(Parcel source) {
            return new Meal2(source);
        }

        @Override
        public Meal2[] newArray(int size) {
            return new Meal2[size];
        }
    };
}
