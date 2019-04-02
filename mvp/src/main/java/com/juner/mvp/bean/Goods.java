package com.juner.mvp.bean;


import java.util.List;

//商品
public class Goods {

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

    List<GoodsPic> goodsDetailsPojoList;//图片Banner
    List<GoodsStandard> xgxGoodsStandardPojoList;//规格
    List<GoodsInfoPic> goodsDetailsType2PojoList;//商品详情图片

    public List<GoodsInfoPic> getGoodsInfoPicList() {
        return goodsDetailsType2PojoList;
    }

    public void setGoodsInfoPicList(List<GoodsInfoPic> goodsInfoPicList) {
        this.goodsDetailsType2PojoList = goodsInfoPicList;
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


    public class GoodsStandard {


        int id;
        int goodsStandardId;
        String goodsStandardTitle;
        int goodsId;
        String goodsStandardPrice;//价钱

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
}
