package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;


//  "id": 7,
//                "goods_id": 1115029,
//                "specification_id": 2,
//                "value": "205/55R/17",
//                "name": "规格尺寸",
//                "pic_url": null
//商品规格
public class GoodsSpec implements Parcelable {


    int id;
    int goods_id;
    int specification_id;
    String value;
    String name;
    String pic_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getSpecification_id() {
        return specification_id;
    }

    public void setSpecification_id(int specification_id) {
        this.specification_id = specification_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.goods_id);
        dest.writeInt(this.specification_id);
        dest.writeString(this.value);
        dest.writeString(this.name);
        dest.writeString(this.pic_url);
    }

    public GoodsSpec() {
    }

    protected GoodsSpec(Parcel in) {
        this.id = in.readInt();
        this.goods_id = in.readInt();
        this.specification_id = in.readInt();
        this.value = in.readString();
        this.name = in.readString();
        this.pic_url = in.readString();
    }

    public static final Parcelable.Creator<GoodsSpec> CREATOR = new Parcelable.Creator<GoodsSpec>() {
        @Override
        public GoodsSpec createFromParcel(Parcel source) {
            return new GoodsSpec(source);
        }

        @Override
        public GoodsSpec[] newArray(int size) {
            return new GoodsSpec[size];
        }
    };
}
