package com.juner.mvp.bean;

import java.util.List;

public class XgxPurchaseOrderPojo {
    int payType = 1;//支付方式 1是微信支付 2是支付宝支付 3是货到付款
    String shopId;//门店id
    String orderPrice;//订单价格
    String discountPrice;//优惠金额
    String realPrice;//实付金额

    List<XgxPurchaseOrderGoodsPojo> xgxPurchaseOrderGoodsPojoList;//下单商品List

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }

    public List<XgxPurchaseOrderGoodsPojo> getXgxPurchaseOrderGoodsPojoList() {
        return xgxPurchaseOrderGoodsPojoList;
    }

    public void setXgxPurchaseOrderGoodsPojoList(List<XgxPurchaseOrderGoodsPojo> xgxPurchaseOrderGoodsPojoList) {
        this.xgxPurchaseOrderGoodsPojoList = xgxPurchaseOrderGoodsPojoList;
    }
}
