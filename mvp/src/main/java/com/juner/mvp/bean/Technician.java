package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//技师
public class Technician extends SelectedBean implements Parcelable {


    int userId;
    private Integer id;
    //订单id
    private Integer orderId;
    //技师id
    private Integer sysuserId;
    int paremId;
    int status;
    int deptId;
    int createUserId;
    String username;//赋值mobile的同时把同样值给此字段
    String userName;//赋值mobile的同时把同样值给此字段
    String nickName;//代替了以前的姓名
    String percentage;////提成比

    String password;
    String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String mobile;
    String roleName;
    List<Integer> roleList;
    String userSn;

    String deduction;
    String deductionBase;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getSysuserId() {
        return sysuserId;
    }

    public void setSysuserId(Integer sysuserId) {
        this.sysuserId = sysuserId;
    }

    public String getDeduction() {

        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getDeductionBase() {
        return deductionBase;
    }

    public void setDeductionBase(String deductionBase) {
        this.deductionBase = deductionBase;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
    public String getNickName() {
        return null == nickName || nickName.equals("") ? "匿名" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList) {
        this.roleList = roleList;
    }

    public String getUserSn() {
        return userSn;
    }

    public void setUserSn(String userSn) {
        this.userSn = userSn;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParemId() {
        return paremId;
    }

    public void setParemId(int paremId) {
        this.paremId = paremId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(int createUserId) {
        this.createUserId = createUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Technician() {
    }

    @Override
    public String toString() {
        return "Technician{" +
                "userId=" + userId +
                ", paremId=" + paremId +
                ", status=" + status +
                ", deptId=" + deptId +
                ", createUserId=" + createUserId +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleList=" + roleList +
                ", userSn='" + userSn + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeValue(this.id);
        dest.writeValue(this.orderId);
        dest.writeValue(this.sysuserId);
        dest.writeInt(this.paremId);
        dest.writeInt(this.status);
        dest.writeInt(this.deptId);
        dest.writeInt(this.createUserId);
        dest.writeString(this.username);
        dest.writeString(this.userName);
        dest.writeString(this.nickName);
        dest.writeString(this.percentage);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.roleName);
        dest.writeList(this.roleList);
        dest.writeString(this.userSn);
        dest.writeString(this.deduction);
        dest.writeString(this.deductionBase);
    }

    protected Technician(Parcel in) {
        this.userId = in.readInt();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sysuserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paremId = in.readInt();
        this.status = in.readInt();
        this.deptId = in.readInt();
        this.createUserId = in.readInt();
        this.username = in.readString();
        this.userName = in.readString();
        this.nickName = in.readString();
        this.percentage = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.roleName = in.readString();
        this.roleList = new ArrayList<Integer>();
        in.readList(this.roleList, Integer.class.getClassLoader());
        this.userSn = in.readString();
        this.deduction = in.readString();
        this.deductionBase = in.readString();
    }

    public static final Creator<Technician> CREATOR = new Creator<Technician>() {
        @Override
        public Technician createFromParcel(Parcel source) {
            return new Technician(source);
        }

        @Override
        public Technician[] newArray(int size) {
            return new Technician[size];
        }
    };
}
