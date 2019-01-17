package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//配件分类
public class FixPartsListEntity implements Parcelable {

    int id;
    String name;
    String keywords;


    List<FixParts2item> subCategoryList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<FixParts2item> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<FixParts2item> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.keywords);
        dest.writeList(this.subCategoryList);
    }

    public FixPartsListEntity() {
    }

    protected FixPartsListEntity(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.keywords = in.readString();
        this.subCategoryList = new ArrayList<FixParts2item>();
        in.readList(this.subCategoryList, FixParts2item.class.getClassLoader());
    }

    public static final Parcelable.Creator<FixPartsListEntity> CREATOR = new Parcelable.Creator<FixPartsListEntity>() {
        @Override
        public FixPartsListEntity createFromParcel(Parcel source) {
            return new FixPartsListEntity(source);
        }

        @Override
        public FixPartsListEntity[] newArray(int size) {
            return new FixPartsListEntity[size];
        }
    };
}
