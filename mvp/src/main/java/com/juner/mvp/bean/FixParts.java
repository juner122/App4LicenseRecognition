package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

//检修单配件 显示，提交
public class FixParts extends FixInfoItem implements Parcelable {


    int order_id;
    int goods_id;
    int product_id;
    int number;
    int quotation_id;
    int component_id;
    String goods_name;
    String goods_sn;
    String market_price;
    String retail_price;
    String goods_specifition_name_value;
    String type;
    String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getQuotation_id() {
        return quotation_id;
    }

    public void setQuotation_id(int quotation_id) {
        this.quotation_id = quotation_id;
    }

    public int getComponent_id() {
        return component_id;
    }

    public void setComponent_id(int component_id) {
        this.component_id = component_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getMarket_price() {
        return market_price;
    }

    public Double getMarket_priceD() {
        return Double.parseDouble(market_price);
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getRetail_price() {
        return null == retail_price ? "0.00" : retail_price;
    }

    public Double getRetail_priceD() {
        return Double.parseDouble(getRetail_price());
    }

    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }

    public String getGoods_specifition_name_value() {
        return goods_specifition_name_value;
    }

    public void setGoods_specifition_name_value(String goods_specifition_name_value) {
        this.goods_specifition_name_value = goods_specifition_name_value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.order_id);
        dest.writeInt(this.goods_id);
        dest.writeInt(this.product_id);
        dest.writeInt(this.number);
        dest.writeInt(this.quotation_id);
        dest.writeInt(this.component_id);
        dest.writeString(this.goods_name);
        dest.writeString(this.goods_sn);
        dest.writeString(this.market_price);
        dest.writeString(this.retail_price);
        dest.writeString(this.goods_specifition_name_value);
        dest.writeString(this.type);
        dest.writeString(this.status);
    }

    public FixParts() {
    }

    protected FixParts(Parcel in) {
        this.order_id = in.readInt();
        this.goods_id = in.readInt();
        this.product_id = in.readInt();
        this.number = in.readInt();
        this.quotation_id = in.readInt();
        this.component_id = in.readInt();
        this.goods_name = in.readString();
        this.goods_sn = in.readString();
        this.market_price = in.readString();
        this.retail_price = in.readString();
        this.goods_specifition_name_value = in.readString();
        this.type = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<FixParts> CREATOR = new Parcelable.Creator<FixParts>() {
        @Override
        public FixParts createFromParcel(Parcel source) {
            return new FixParts(source);
        }

        @Override
        public FixParts[] newArray(int size) {
            return new FixParts[size];
        }
    };
}
