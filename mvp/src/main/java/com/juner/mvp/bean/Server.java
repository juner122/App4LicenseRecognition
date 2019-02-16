package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Server extends SelectedBean implements Parcelable {

    int id;
    int shopId;
    String name;
    String explain;
    int type;
    double price;
    String marketPrice;//市场价 显示用
    int serviceId;
    int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", shopId=" + shopId +
                ", name='" + name + '\'' +
                ", explain='" + explain + '\'' +
                ", type=" + type +
                ", price='" + price + '\'' +
                ", marketPrice='" + marketPrice + '\'' +
                ", serviceId=" + serviceId +
                '}';
    }

    public Server() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.shopId);
        dest.writeString(this.name);
        dest.writeString(this.explain);
        dest.writeInt(this.type);
        dest.writeDouble(this.price);
        dest.writeString(this.marketPrice);
        dest.writeInt(this.serviceId);
        dest.writeInt(this.number);
    }

    protected Server(Parcel in) {
        this.id = in.readInt();
        this.shopId = in.readInt();
        this.name = in.readString();
        this.explain = in.readString();
        this.type = in.readInt();
        this.price = in.readDouble();
        this.marketPrice = in.readString();
        this.serviceId = in.readInt();
        this.number = in.readInt();
    }

    public static final Creator<Server> CREATOR = new Creator<Server>() {
        @Override
        public Server createFromParcel(Parcel source) {
            return new Server(source);
        }

        @Override
        public Server[] newArray(int size) {
            return new Server[size];
        }
    };
}
