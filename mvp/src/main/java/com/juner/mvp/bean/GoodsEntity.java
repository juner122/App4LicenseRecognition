package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsEntity implements Parcelable {

    int type = -1;//服务商品类型  4 项目，非4为其他
    boolean isSet = true;


    String id;
    String name;
    String goods_sn;
    String couponSn;//劵号
    String goodsCode;//商品编码


    String goodsSn;//新的
    String brand_id;


    String keywords;
    String goods_brief;
    String goods_desc;
    String category_id;
    String list_pic_url;

    int easy_id;//设置项目id
    int number;//商品数量


    String price;//工时服务价格
    int is_hot;

    String activitySn;
    int activityId;
    String activityName;
    int goodsNum;//套餐商品剩余数量
    int goodsId;//套餐商品id
    String goodsName;//套餐商品名

    //商品对象
    String goods_specifition_name_value;
    int goods_specifition_ids;


    //主推项目对象
    String goodsSpecifitionNameValue;
    int goodsSpecifitionIds;

    int product_id;
    String retail_price;
    String market_price;
    String retailPrice;
    String marketPrice;

    Goods.GoodsStandard goodsStandard;
    int goods_id;

    //5.30新加字段
    String standardId;//
    String goodsStandardTitle;//

    public String getCouponSn() {
        return couponSn;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getStandarId() {
        return standardId;
    }

    public void setStandarId(String standarId) {
        this.standardId = standarId;
    }

    public String getGoodsStandardTitle() {
        return goodsStandardTitle;
    }

    public void setGoodsStandardTitle(String goodsStandardTitle) {
        this.goodsStandardTitle = goodsStandardTitle;
    }

    String firstCategoryId;

    public String getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(String firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }


    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    //以下是新商品接口
    String goods_name;//新商品接口

    public Goods.GoodsStandard getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(Goods.GoodsStandard goodsStandard) {
        this.goodsStandard = goodsStandard;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public GoodsEntity(String name, boolean isSet) {
        this.name = name;
        this.isSet = isSet;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public String getGoods_specifition_name_value() {
        return product_id == 0 ? "选择规格" : goods_specifition_name_value;
    }

    public void setGoods_specifition_name_value(String goods_specifition_name_value) {
        this.goods_specifition_name_value = goods_specifition_name_value;
    }

    public int getGoods_specifition_ids() {
        return goods_specifition_ids;
    }

    public void setGoods_specifition_ids(int goods_specifition_ids) {
        this.goods_specifition_ids = goods_specifition_ids;
    }

    public String getGoodsSpecifitionNameValue() {
        return goodsSpecifitionNameValue;
    }

    public void setGoodsSpecifitionNameValue(String goodsSpecifitionNameValue) {
        this.goodsSpecifitionNameValue = goodsSpecifitionNameValue;
    }

    public int getGoodsSpecifitionIds() {
        return goodsSpecifitionIds;
    }

    public void setGoodsSpecifitionIds(int goodsSpecifitionIds) {
        this.goodsSpecifitionIds = goodsSpecifitionIds;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getPriceTodouble() {


        return Double.parseDouble(null != price ? price : "0");
    }

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

    public String getNumberString() {
        return number == 0 ? "" : String.valueOf(number);

    }

    public String getNumberStringX() {
        return number == 0 ? "" : String.valueOf("x" + number);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrimary_pic_url() {
        return list_pic_url;
    }

    public void setPrimary_pic_url(String primary_pic_url) {
        this.list_pic_url = primary_pic_url;
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
    public String toString() {
        return "GoodsEntity{" +
                "type=" + type +
                ", isSet=" + isSet +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", goods_sn='" + goods_sn + '\'' +
                ", brand_id='" + brand_id + '\'' +
                ", keywords='" + keywords + '\'' +
                ", goods_brief='" + goods_brief + '\'' +
                ", goods_desc='" + goods_desc + '\'' +
                ", category_id='" + category_id + '\'' +
                ", list_pic_url='" + list_pic_url + '\'' +
                ", easy_id=" + easy_id +
                ", number=" + number +
                ", price='" + price + '\'' +
                ", is_hot=" + is_hot +
                ", activitySn='" + activitySn + '\'' +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", goodsNum=" + goodsNum +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goods_specifition_name_value='" + goods_specifition_name_value + '\'' +
                ", goods_specifition_ids='" + goods_specifition_ids + '\'' +
                ", product_id=" + product_id +
                ", retail_price='" + retail_price + '\'' +
                ", market_price='" + market_price + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeByte(this.isSet ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.goods_sn);
        dest.writeString(this.goodsSn);
        dest.writeString(this.brand_id);
        dest.writeString(this.keywords);
        dest.writeString(this.goods_brief);
        dest.writeString(this.goods_desc);
        dest.writeString(this.category_id);
        dest.writeString(this.list_pic_url);
        dest.writeInt(this.easy_id);
        dest.writeInt(this.number);
        dest.writeString(this.price);
        dest.writeInt(this.is_hot);
        dest.writeString(this.activitySn);
        dest.writeInt(this.activityId);
        dest.writeString(this.activityName);
        dest.writeInt(this.goodsNum);
        dest.writeInt(this.goodsId);
        dest.writeString(this.goodsName);
        dest.writeString(this.goods_specifition_name_value);
        dest.writeInt(this.goods_specifition_ids);
        dest.writeString(this.goodsSpecifitionNameValue);
        dest.writeInt(this.goodsSpecifitionIds);
        dest.writeInt(this.product_id);
        dest.writeString(this.retail_price);
        dest.writeString(this.market_price);
        dest.writeString(this.retailPrice);
        dest.writeString(this.marketPrice);
        dest.writeParcelable(this.goodsStandard, flags);
        dest.writeInt(this.goods_id);
        dest.writeString(this.standardId);
        dest.writeString(this.goodsStandardTitle);
        dest.writeString(this.firstCategoryId);
        dest.writeString(this.goods_name);
    }

    protected GoodsEntity(Parcel in) {
        this.type = in.readInt();
        this.isSet = in.readByte() != 0;
        this.id = in.readString();
        this.name = in.readString();
        this.goods_sn = in.readString();
        this.goodsSn = in.readString();
        this.brand_id = in.readString();
        this.keywords = in.readString();
        this.goods_brief = in.readString();
        this.goods_desc = in.readString();
        this.category_id = in.readString();
        this.list_pic_url = in.readString();
        this.easy_id = in.readInt();
        this.number = in.readInt();
        this.price = in.readString();
        this.is_hot = in.readInt();
        this.activitySn = in.readString();
        this.activityId = in.readInt();
        this.activityName = in.readString();
        this.goodsNum = in.readInt();
        this.goodsId = in.readInt();
        this.goodsName = in.readString();
        this.goods_specifition_name_value = in.readString();
        this.goods_specifition_ids = in.readInt();
        this.goodsSpecifitionNameValue = in.readString();
        this.goodsSpecifitionIds = in.readInt();
        this.product_id = in.readInt();
        this.retail_price = in.readString();
        this.market_price = in.readString();
        this.retailPrice = in.readString();
        this.marketPrice = in.readString();
        this.goodsStandard = in.readParcelable(Goods.GoodsStandard.class.getClassLoader());
        this.goods_id = in.readInt();
        this.standardId = in.readString();
        this.goodsStandardTitle = in.readString();
        this.firstCategoryId = in.readString();
        this.goods_name = in.readString();
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
