package com.frank.plate.bean;


import android.os.Parcel;
import android.os.Parcelable;

//技师
public class Technician extends SelectedBean implements Parcelable {



    int userId;
    int paremId;
    int status;
    int deptId;
    int createUserId;
    String username;
    String password;
    String email;
    String mobile;
    String roleName;

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

    @Override
    public String toString() {
        return "Technician{" +
                "userId=" + userId +
                ", paremId=" + paremId +
                ", status=" + status +
                ", deptId=" + deptId +
                ", createUserId=" + createUserId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }

    public Technician() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.paremId);
        dest.writeInt(this.status);
        dest.writeInt(this.deptId);
        dest.writeInt(this.createUserId);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.roleName);
    }

    protected Technician(Parcel in) {
        this.userId = in.readInt();
        this.paremId = in.readInt();
        this.status = in.readInt();
        this.deptId = in.readInt();
        this.createUserId = in.readInt();
        this.username = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.roleName = in.readString();
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
