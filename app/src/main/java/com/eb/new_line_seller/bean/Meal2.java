package com.eb.new_line_seller.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//门店可录套卡
public class Meal2 implements Parcelable {


    int id;
    int activityId;
    String actName;

    List<MealEntity> goodsList;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.activityId);
        dest.writeString(this.actName);
        dest.writeList(this.goodsList);
    }

    public Meal2() {
    }

    protected Meal2(Parcel in) {
        this.id = in.readInt();
        this.activityId = in.readInt();
        this.actName = in.readString();
        this.goodsList = new ArrayList<MealEntity>();
        in.readList(this.goodsList, MealEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<Meal2> CREATOR = new Parcelable.Creator<Meal2>() {
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
