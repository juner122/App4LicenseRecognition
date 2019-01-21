package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

//检修单工时服务
public class FixServie extends FixInfoItem implements Parcelable {

    int orderId;
    int quotationId;
    int projectId;
    int serviceId;
    int type;
    String name;//标准洗车
    String explain;//五座轿车
    String price;
    String marketPrice;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(int quotationId) {
        this.quotationId = quotationId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Double getPriceD() {
        return Double.parseDouble(price);
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public Double getMarketPriceD() {
        return Double.parseDouble(marketPrice);
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderId);
        dest.writeInt(this.quotationId);
        dest.writeInt(this.projectId);
        dest.writeInt(this.serviceId);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.explain);
        dest.writeString(this.price);
        dest.writeString(this.marketPrice);
    }

    public FixServie() {
    }

    protected FixServie(Parcel in) {
        this.orderId = in.readInt();
        this.quotationId = in.readInt();
        this.projectId = in.readInt();
        this.serviceId = in.readInt();
        this.type = in.readInt();
        this.name = in.readString();
        this.explain = in.readString();
        this.price = in.readString();
        this.marketPrice = in.readString();
    }

    public static final Parcelable.Creator<FixServie> CREATOR = new Parcelable.Creator<FixServie>() {
        @Override
        public FixServie createFromParcel(Parcel source) {
            return new FixServie(source);
        }

        @Override
        public FixServie[] newArray(int size) {
            return new FixServie[size];
        }
    };
}
