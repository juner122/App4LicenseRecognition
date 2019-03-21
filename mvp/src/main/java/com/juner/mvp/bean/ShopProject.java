package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

//	服务pojo类
public class ShopProject implements Parcelable {

    //第二级分类主键id
    private int serviceId;
    //商品名
    private String name;
    //描述，备注
    private String explain;
    //价格
    private String price;
    private int openStatus;

    public int getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(int openStatus) {
        this.openStatus = openStatus;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ShopProject() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.serviceId);
        dest.writeString(this.name);
        dest.writeString(this.explain);
        dest.writeString(this.price);
        dest.writeInt(this.openStatus);
    }

    protected ShopProject(Parcel in) {
        this.serviceId = in.readInt();
        this.name = in.readString();
        this.explain = in.readString();
        this.price = in.readString();
        this.openStatus = in.readInt();
    }

    public static final Creator<ShopProject> CREATOR = new Creator<ShopProject>() {
        @Override
        public ShopProject createFromParcel(Parcel source) {
            return new ShopProject(source);
        }

        @Override
        public ShopProject[] newArray(int size) {
            return new ShopProject[size];
        }
    };
}
