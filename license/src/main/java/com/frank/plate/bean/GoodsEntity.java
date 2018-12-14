package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsEntity implements Parcelable {

    int type;//服务商品类型  1 商品，2工时服务


    Integer id;
    String name;
    String goods_sn;
    String brand_id;

    String keywords;
    String goods_brief;
    String goods_desc;
    String category_id;
    String primary_pic_url;
    int easy_id;//设置项目id
    int number;//商品数量
    String retail_price;
    int is_hot;

    String activitySn;
    int activityId;
    String activityName;
    int goodsNum;//套餐商品剩余数量
    int goodsId;//套餐商品id
    String goodsName;//套餐商品名




    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getActivitySn() {
        return activitySn;
    }

    public void setActivitySn(String activitySn) {
        this.activitySn = activitySn;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }



    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getEasy_id() {
        return easy_id;
    }

    public void setEasy_id(int easy_id) {
        this.easy_id = easy_id;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public String getRetail_price() {
        return retail_price;
    }

    public double getRetail_priceTodouble() {


        return Double.parseDouble(null != retail_price ? retail_price : "0");
    }


    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrimary_pic_url() {
        return primary_pic_url;
    }

    public void setPrimary_pic_url(String primary_pic_url) {
        this.primary_pic_url = primary_pic_url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }



    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getGoods_brief() {
        return goods_brief;
    }

    public void setGoods_brief(String goods_brief) {
        this.goods_brief = goods_brief;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }



    public GoodsEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.goods_sn);
        dest.writeString(this.brand_id);
        dest.writeString(this.keywords);
        dest.writeString(this.goods_brief);
        dest.writeString(this.goods_desc);
        dest.writeString(this.category_id);
        dest.writeString(this.primary_pic_url);
        dest.writeInt(this.easy_id);
        dest.writeInt(this.number);
        dest.writeString(this.retail_price);
        dest.writeInt(this.is_hot);
        dest.writeString(this.activitySn);
        dest.writeInt(this.activityId);
        dest.writeString(this.activityName);
        dest.writeInt(this.goodsNum);
    }

    protected GoodsEntity(Parcel in) {
        this.type = in.readInt();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.goods_sn = in.readString();
        this.brand_id = in.readString();
        this.keywords = in.readString();
        this.goods_brief = in.readString();
        this.goods_desc = in.readString();
        this.category_id = in.readString();
        this.primary_pic_url = in.readString();
        this.easy_id = in.readInt();
        this.number = in.readInt();
        this.retail_price = in.readString();
        this.is_hot = in.readInt();
        this.activitySn = in.readString();
        this.activityId = in.readInt();
        this.activityName = in.readString();
        this.goodsNum = in.readInt();
    }

    public static final Creator<GoodsEntity> CREATOR = new Creator<GoodsEntity>() {
        @Override
        public GoodsEntity createFromParcel(Parcel source) {
            return new GoodsEntity(source);
        }

        @Override
        public GoodsEntity[] newArray(int size) {
            return new GoodsEntity[size];
        }
    };
}
