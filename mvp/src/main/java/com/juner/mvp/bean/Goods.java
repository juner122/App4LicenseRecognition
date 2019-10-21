package com.juner.mvp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

//商品
public class Goods extends AbstractExpandableItem<Goods.GoodsStandard> implements MultiItemEntity, Parcelable {

    int id;
    String goodsCode;
    String goodsTitle;
    String goodsBrandId;
    String goodsBrandTitle;
    String firstCategoryId;
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

    //是否微信上架1是2否
    private Integer wxType;
    //库存提醒设置数字
    private Integer remindLimit;

    //库存备注
    private String remarks;

    List<GoodsPic> goodsDetailsPojoList;//图片Banner
    List<GoodsStandard> xgxGoodsStandardPojoList;//规格
    List<GoodsPic> goodsDetailsType2PojoList;//商品详情图片
    GoodsStandard goodsStandard;

    int selected;

    public boolean selectde() {

        return selected != 0;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


    public GoodsStandard getGoodsStandard() {
        return goodsStandard;
    }

    public void setGoodsStandard(GoodsStandard goodsStandard) {
        this.goodsStandard = goodsStandard;
    }

    public List<GoodsPic> getGoodsInfoPicList() {
        return goodsDetailsType2PojoList;
    }

    public void setGoodsInfoPicList(List<GoodsPic> goodsInfoPicList) {
        this.goodsDetailsType2PojoList = goodsInfoPicList;
    }

    public Integer getWxType() {
        return wxType;
    }

    public void setWxType(Integer wxType) {
        this.wxType = wxType;
    }

    public Integer getRemindLimit() {
        return remindLimit;
    }

    public void setRemindLimit(Integer remindLimit) {
        this.remindLimit = remindLimit;
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

    public String getGoodsBrandId() {
        return goodsBrandId;
    }

    public void setGoodsBrandId(String goodsBrandId) {
        this.goodsBrandId = goodsBrandId;
    }

    public String getGoodsBrandTitle() {
        return goodsBrandTitle;
    }

    public void setGoodsBrandTitle(String goodsBrandTitle) {
        this.goodsBrandTitle = goodsBrandTitle;
    }

    public String getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(String firstCategoryId) {
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

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }


    public static class GoodsStandard extends SelectedBean implements MultiItemEntity, Parcelable {


        public GoodsStandard() {
        }
        List<GoodsPic> goodsDetailsPojoList;//图片Banner
        int id;
        Integer goodsStandardId;
        String goodsStandardTitle;
        int goodsId;
        String goodsTitle;
        String goodsStandardPrice;//价钱
        int num;
        String stockPrice;//成本入库价

        //供应商
        private  String supplierId;

        //库存数量
        private  String stock;


        //供应商名
        private String supplierName;

        private int isShowItem;//是否显示子项

        public int getIsShowItem() {
            return isShowItem;
        }

        public void setIsShowItem(int isShowItem) {
            this.isShowItem = isShowItem;
        }

        public List<GoodsPic> getGoodsDetailsPojoList() {
            return goodsDetailsPojoList;
        }

        public void setGoodsDetailsPojoList(List<GoodsPic> goodsDetailsPojoList) {
            this.goodsDetailsPojoList = goodsDetailsPojoList;
        }

        public String getGoodsTitle() {
            return goodsTitle;
        }

        public void setGoodsTitle(String goodsTitle) {
            this.goodsTitle = goodsTitle;
        }

        public String getStockPrice() {
            return stockPrice;
        }

        public void setStockPrice(String stockPrice) {
            this.stockPrice = stockPrice;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
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

        public int getGoodsStandardId() {
            return null == goodsStandardId ? id : goodsStandardId;// 自定义商品goodsStandardId为null
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
        public int getItemType() {
            return 1;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.goodsDetailsPojoList);
            dest.writeInt(this.id);
            dest.writeValue(this.goodsStandardId);
            dest.writeString(this.goodsStandardTitle);
            dest.writeInt(this.goodsId);
            dest.writeString(this.goodsTitle);
            dest.writeString(this.goodsStandardPrice);
            dest.writeInt(this.num);
            dest.writeString(this.stockPrice);
            dest.writeString(this.supplierId);
            dest.writeString(this.stock);
            dest.writeString(this.supplierName);
        }

        protected GoodsStandard(Parcel in) {
            this.goodsDetailsPojoList = new ArrayList<GoodsPic>();
            in.readList(this.goodsDetailsPojoList, GoodsPic.class.getClassLoader());
            this.id = in.readInt();
            this.goodsStandardId = (Integer) in.readValue(Integer.class.getClassLoader());
            this.goodsStandardTitle = in.readString();
            this.goodsId = in.readInt();
            this.goodsTitle = in.readString();
            this.goodsStandardPrice = in.readString();
            this.num = in.readInt();
            this.stockPrice = in.readString();
            this.supplierId = in.readString();
            this.stock = in.readString();
            this.supplierName = in.readString();
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


    public static class GoodsPic {

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

    public static class GoodsInfoPic {

        String image;
        int id;
        String goodsId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public Goods() {
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
        dest.writeString(this.goodsBrandId);
        dest.writeString(this.goodsBrandTitle);
        dest.writeString(this.firstCategoryId);
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
        dest.writeValue(this.wxType);
        dest.writeValue(this.remindLimit);
        dest.writeString(this.remarks);
        dest.writeList(this.goodsDetailsPojoList);
        dest.writeTypedList(this.xgxGoodsStandardPojoList);
        dest.writeList(this.goodsDetailsType2PojoList);
        dest.writeParcelable(this.goodsStandard, flags);
        dest.writeInt(this.selected);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected Goods(Parcel in) {
        this.id = in.readInt();
        this.goodsCode = in.readString();
        this.goodsTitle = in.readString();
        this.goodsBrandId = in.readString();
        this.goodsBrandTitle = in.readString();
        this.firstCategoryId = in.readString();
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
        this.wxType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.remindLimit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.remarks = in.readString();
        this.goodsDetailsPojoList = new ArrayList<GoodsPic>();
        in.readList(this.goodsDetailsPojoList, GoodsPic.class.getClassLoader());
        this.xgxGoodsStandardPojoList = in.createTypedArrayList(GoodsStandard.CREATOR);
        this.goodsDetailsType2PojoList = new ArrayList<GoodsPic>();
        in.readList(this.goodsDetailsType2PojoList, GoodsInfoPic.class.getClassLoader());
        this.goodsStandard = in.readParcelable(GoodsStandard.class.getClassLoader());
        this.selected = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
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
