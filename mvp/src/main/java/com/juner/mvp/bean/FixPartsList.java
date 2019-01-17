package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FixPartsList implements Parcelable {

    List<FixPartsSeek> firstComponent;


    List<FixPartsListEntity> categoryList;

    public List<FixPartsSeek> getFirstComponent() {
        return firstComponent;
    }

    public void setFirstComponent(List<FixPartsSeek> firstComponent) {
        this.firstComponent = firstComponent;
    }

    public List<FixPartsListEntity> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<FixPartsListEntity> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.firstComponent);
        dest.writeTypedList(this.categoryList);
    }



    protected FixPartsList(Parcel in) {
        this.firstComponent = in.createTypedArrayList(FixPartsSeek.CREATOR);
        this.categoryList = in.createTypedArrayList(FixPartsListEntity.CREATOR);
    }

    public static final Parcelable.Creator<FixPartsList> CREATOR = new Parcelable.Creator<FixPartsList>() {
        @Override
        public FixPartsList createFromParcel(Parcel source) {
            return new FixPartsList(source);
        }

        @Override
        public FixPartsList[] newArray(int size) {
            return new FixPartsList[size];
        }
    };
}
