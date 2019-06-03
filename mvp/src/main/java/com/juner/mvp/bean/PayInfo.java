package com.juner.mvp.bean;

public class PayInfo {

    PayInfoE payInfo;

    public PayInfoE getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfoE payInfo) {
        this.payInfo = payInfo;
    }

    public class PayInfoE {
        String appId;
        String nonceStr;

        String partnerId;
        String paySign;
        String prepayId;
        Long timeStamp;
        String totalFee;//订单价格，单位为分
        String orderSn;//订单价格，单位为分

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public String getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(String totalFee) {
            this.totalFee = totalFee;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppid(String appid) {
            this.appId = appid;
        }

        public String getNoncestr() {
            return nonceStr;
        }

        public void setNoncestr(String noncestr) {
            this.nonceStr = noncestr;
        }

        public String getPartnerid() {
            return partnerId;
        }

        public void setPartnerid(String partnerid) {
            this.partnerId = partnerid;
        }

        public String getPaySign() {
            return paySign;
        }

        public void setPaySign(String paySign) {
            this.paySign = paySign;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public Long getTimestamp() {
            return timeStamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timeStamp = timestamp;
        }
    }
}
