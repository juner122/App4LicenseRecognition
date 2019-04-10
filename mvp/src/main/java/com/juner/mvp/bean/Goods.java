package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//商品
public class Goods extends SelectedBean implements Parcelable {

    int id;
    String goodsCode;
    String goodsTitle;
    int goodsBrandId;
    String goodsBrandTitle;
    int firstCategoryId;
    String firstCategoryTitle;
    int secondCategoryId;
    String secondCategoryTitle;
    Long createTime;
    String goodsUnitTitle;
    int type;
    int hotStatus;
    int newStatus;
    int activityStatus;
    int num;


    int selected;

    public boolean selectde() {

        return selected != 0;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    List<GoodsPic> goodsDetailsPojoList;//图片Banner
    List<GoodsStandard> xgxGoodsStandardPojoList;//规格
    List<GoodsInfoPic> goodsDetailsType2PojoList;//商品详情图片
    GoodsStandard goodsStandard;

    public GoodsStandard getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(GoodsStandard goodsStandard) {
        this.goodsStandard = goodsStandard;
    }

    public List<GoodsInfoPic> getGoodsInfoPicList() {
        return goodsDetailsType2PojoList;
    }

    public void setGoodsInfoPicList(List<GoodsInfoPic> goodsInfoPicList) {
        this.goodsDetailsType2PojoList = goodsInfoPicList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public int getGoodsBrandId() {
        return goodsBrandId;
    }

    public void setGoodsBrandId(int goodsBrandId) {
        this.goodsBrandId = goodsBrandId;
    }

    public String getGoodsBrandTitle() {
        return goodsBrandTitle;
    }

    public void setGoodsBrandTitle(String goodsBrandTitle) {
        this.goodsBrandTitle = goodsBrandTitle;
    }

    public int getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(int firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public String getFirstCategoryTitle() {
        return firstCategoryTitle;
    }

    public void setFirstCategoryTitle(String firstCategoryTitle) {
        this.firstCategoryTitle = firstCategoryTitle;
    }

    public int getSecondCategoryId() {
        return secondCategoryId;
    }

    public void setSecondCategoryId(int secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }

    public String getSecondCategoryTitle() {
        return secondCategoryTitle;
    }

    public void setSecondCategoryTitle(String secondCategoryTitle) {
        this.secondCategoryTitle = secondCategoryTitle;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getGoodsUnitTitle() {
        return goodsUnitTitle;
    }

    public void setGoodsUnitTitle(String goodsUnitTitle) {
        this.goodsUnitTitle = goodsUnitTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(int hotStatus) {
        this.hotStatus = hotStatus;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public List<GoodsPic> getGoodsDetailsPojoList() {
        return goodsDetailsPojoList;
    }

    public void setGoodsDetailsPojoList(List<GoodsPic> goodsDetailsPojoList) {
        this.goodsDetailsPojoList = goodsDetailsPojoList;
    }


    public static class GoodsStandard extends SelectedBean implements Parcelable {


        public GoodsStandard() {
        }

        int id;
        int goodsStandardId;
        String goodsStandardTitle;
        int goodsId;
        String goodsStandardPrice;//价钱
        int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsStandardId() {
            return goodsStandardId;
        }

        public void setGoodsStandardId(int goodsStandardId) {
            this.goodsStandardId = goodsStandardId;
        }

        public String getGoodsStandardTitle() {
            return goodsStandardTitle;
        }

        public void setGoodsStandardTitle(String goodsStandardTitle) {
            this.goodsStandardTitle = goodsStandardTitle;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsStandardPrice() {
            return goodsStandardPrice;
        }

        public void setGoodsStandardPrice(String goodsStandardPrice) {
            this.goodsStandardPrice = goodsStandardPrice;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.goodsStandardId);
            dest.writeString(this.goodsStandardTitle);
            dest.writeInt(this.goodsId);
            dest.writeString(this.goodsStandardPrice);
            dest.writeInt(this.num);
        }

        protected GoodsStandard(Parcel in) {
            this.id = in.readInt();
            this.goodsStandardId = in.readInt();
            this.goodsStandardTitle = in.readString();
            this.goodsId = in.readInt();
            this.goodsStandardPrice = in.readString();
            this.num = in.readInt();
        }

        public static final Creator<GoodsStandard> CREATOR = new Creator<GoodsStandard>() {
            @Override
            public GoodsStandard createFromParcel(Parcel source) {
                return new GoodsStandard(source);
            }

            @Override
            public GoodsStandard[] newArray(int size) {
                return new GoodsStandard[size];
            }
        };
    }


    public class GoodsPic {

        int id;
        int goodsId;
        String image;
        int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public List<GoodsStandard> getXgxGoodsStandardPojoList() {
        return xgxGoodsStandardPojoList;
    }

    public void setXgxGoodsStandardPojoList(List<GoodsStandard> xgxGoodsStandardPojoList) {
        this.xgxGoodsStandardPojoList = xgxGoodsStandardPojoList;
    }

    public class GoodsInfoPic {

        String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.goodsCode);
        dest.writeString(this.goodsTitle);
        dest.writeInt(this.goodsBrandId);
        dest.writeString(this.goodsBrandTitle);
        dest.writeInt(this.firstCategoryId);
        dest.writeString(this.firstCategoryTitle);
        dest.writeInt(this.secondCategoryId);
        dest.writeString(this.secondCategoryTitle);
        dest.writeValue(this.createTime);
        dest.writeString(this.goodsUnitTitle);
        dest.writeInt(this.type);
        dest.writeInt(this.hotStatus);
        dest.writeInt(this.newStatus);
        dest.writeInt(this.activityStatus);
        dest.writeInt(this.num);
        dest.writeInt(this.selected);
        dest.writeList(this.goodsDetailsPojoList);
        dest.writeList(this.xgxGoodsStandardPojoList);
        dest.writeList(this.goodsDetailsType2PojoList);
        dest.writeParcelable((Parcelable) this.goodsStandard, flags);
    }

    public Goods() {
    }

    protected Goods(Parcel in) {
        this.id = in.readInt();
        this.goodsCode = in.readString();
        this.goodsTitle = in.readString();
        this.goodsBrandId = in.readInt();
        this.goodsBrandTitle = in.readString();
        this.firstCategoryId = in.readInt();
        this.firstCategoryTitle = in.readString();
        this.secondCategoryId = in.readInt();
        this.secondCategoryTitle = in.readString();
        this.createTime = (Long) in.readValue(Long.class.getClassLoader());
        this.goodsUnitTitle = in.readString();
        this.type = in.readInt();
        this.hotStatus = in.readInt();
        this.newStatus = in.readInt();
        this.activityStatus = in.readInt();
        this.num = in.readInt();
        this.selected = in.readInt();
        this.goodsDetailsPojoList = new ArrayList<GoodsPic>();
        in.readList(this.goodsDetailsPojoList, GoodsPic.class.getClassLoader());
        this.xgxGoodsStandardPojoList = new ArrayList<GoodsStandard>();
        in.readList(this.xgxGoodsStandardPojoList, GoodsStandard.class.getClassLoader());
        this.goodsDetailsType2PojoList = new ArrayList<GoodsInfoPic>();
        in.readList(this.goodsDetailsType2PojoList, GoodsInfoPic.class.getClassLoader());
        this.goodsStandard = in.readParcelable(GoodsStandard.class.getClassLoader());
    }

    public static final Parcelable.Creator<Goods> CREATOR = new Parcelable.Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
