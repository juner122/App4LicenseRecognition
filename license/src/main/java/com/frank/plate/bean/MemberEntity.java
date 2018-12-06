package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MemberEntity implements Parcelable {



    int userId;
    String username;
    String mobile;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.username);
        dest.writeString(this.mobile);
    }

    public MemberEntity() {
    }

    protected MemberEntity(Parcel in) {
        this.userId = in.readInt();
        this.username = in.readString();
        this.mobile = in.readString();
    }

    public static final Parcelable.Creator<MemberEntity> CREATOR = new Parcelable.Creator<MemberEntity>() {
        @Override
        public MemberEntity createFromParcel(Parcel source) {
            return new MemberEntity(source);
        }

        @Override
        public MemberEntity[] newArray(int size) {
            return new MemberEntity[size];
        }
    };
}
