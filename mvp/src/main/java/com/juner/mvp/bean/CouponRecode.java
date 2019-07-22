package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CouponRecode implements Parcelable {
    String id;
    String couponId;
    String deptId;
    String userId;
    String userName;
    String couponName;
    String typeMoney;
    String minAmount;
    String couponType;
    String superposition;
    String cycle;
    String dealNum;//收券人数
    String createTime;//时间
    String remark;
    String withMsg;// 0,是否短信提醒0不短信1用短信
    List<MemberEntity> xgxCouponPushUsersList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getTypeMoney() {
        return typeMoney;
    }

    public void setTypeMoney(String typeMoney) {
        this.typeMoney = typeMoney;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getSuperposition() {
        return superposition;
    }

    public void setSuperposition(String superposition) {
        this.superposition = superposition;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getDealNum() {
        return dealNum;
    }

    public void setDealNum(String dealNum) {
        this.dealNum = dealNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWithMsg() {
        return withMsg;
    }

    public void setWithMsg(String withMsg) {
        this.withMsg = withMsg;
    }

    public List<MemberEntity> getXgxCouponPushUsersList() {
        return xgxCouponPushUsersList;
    }

    public void setXgxCouponPushUsersList(List<MemberEntity> xgxCouponPushUsersList) {
        this.xgxCouponPushUsersList = xgxCouponPushUsersList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.couponId);
        dest.writeString(this.deptId);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.couponName);
        dest.writeString(this.typeMoney);
        dest.writeString(this.minAmount);
        dest.writeString(this.couponType);
        dest.writeString(this.superposition);
        dest.writeString(this.cycle);
        dest.writeString(this.dealNum);
        dest.writeString(this.createTime);
        dest.writeString(this.remark);
        dest.writeString(this.withMsg);
        dest.writeTypedList(this.xgxCouponPushUsersList);
    }

    public CouponRecode() {
    }

    protected CouponRecode(Parcel in) {
        this.id = in.readString();
        this.couponId = in.readString();
        this.deptId = in.readString();
        this.userId = in.readString();
        this.userName = in.readString();
        this.couponName = in.readString();
        this.typeMoney = in.readString();
        this.minAmount = in.readString();
        this.couponType = in.readString();
        this.superposition = in.readString();
        this.cycle = in.readString();
        this.dealNum = in.readString();
        this.createTime = in.readString();
        this.remark = in.readString();
        this.withMsg = in.readString();
        this.xgxCouponPushUsersList = in.createTypedArrayList(MemberEntity.CREATOR);
    }

    public static final Parcelable.Creator<CouponRecode> CREATOR = new Parcelable.Creator<CouponRecode>() {
        @Override
        public CouponRecode createFromParcel(Parcel source) {
            return new CouponRecode(source);
        }

        @Override
        public CouponRecode[] newArray(int size) {
            return new CouponRecode[size];
        }
    };
}
