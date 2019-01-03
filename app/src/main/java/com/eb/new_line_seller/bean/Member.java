package com.eb.new_line_seller.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Member implements Parcelable {
    int dayMember;
    int monthMember;
    List<MemberEntity> memberList;

    public int getDayMember() {
        return dayMember;
    }

    public void setDayMember(int dayMember) {
        this.dayMember = dayMember;
    }

    public int getMonthMember() {
        return monthMember;
    }

    public void setMonthMember(int monthMember) {
        this.monthMember = monthMember;
    }

    public List<MemberEntity> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberEntity> memberList) {
        this.memberList = memberList;
    }

    @Override
    public String toString() {
        return "Member{" +
                "dayMember=" + dayMember +
                ", monthMember=" + monthMember +
                ", memberList=" + memberList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dayMember);
        dest.writeInt(this.monthMember);
        dest.writeTypedList(this.memberList);
    }

    public Member() {
    }

    protected Member(Parcel in) {
        this.dayMember = in.readInt();
        this.monthMember = in.readInt();
        this.memberList = in.createTypedArrayList(MemberEntity.CREATOR);
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel source) {
            return new Member(source);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
