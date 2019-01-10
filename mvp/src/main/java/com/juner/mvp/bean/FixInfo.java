package com.juner.mvp.bean;

import java.util.List;

/**
 * 维修单
 */
public class FixInfo {

    /**
     * "quotationSn": 报价单号 【不需录入】
     * "carId": 车况id
     * "carNo":车牌
     * "userId":会员id
     * "userName":会员名
     * "mobile": 会员手机号
     * "sysuserId":接单员 【不需录入】
     * "describe": "完好无损，纯属测试",
     * "status": 状态 【不需录入】
     * //-1 删除   0初始单1未确认2已确认3已形成定单
     * "servePrice":服务总价
     * "goodsPrice": 商品总价
     * "actualPrice": 最终总定价,
     * "addTime": 新建时间 【不需录入】,
     * "planInformTime": 预计报价时间,
     * "informTime": 实际报价时间【不需录入】,
     * "confirmTime": 客户确认时间【不需录入】,
     * "submitTime": 提交到订单时间,【不需录入】
     * "sysUserList<AppSysUser>": 技师列表
     */

    int carId;//车况id
    String carNo;//车牌
    int userId;//会员id
    int sysuserId;//接单员 【不需录入】
    String userName;
    String mobile;
    String describe;
    int status;
    String servePrice;
    String goodsPrice;
    String actualPrice;
    String addTime;
    String planInformTime;
    String informTime;
    String quotationSn;

    public String getQuotationSn() {
        return quotationSn;
    }

    public void setQuotationSn(String quotationSn) {
        this.quotationSn = quotationSn;
    }

    public String getCarNo() {
        return carNo;
    }

    String confirmTime;
    String submitTime;
    List<Technician> sysUserList;

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setSysUserList(List<Technician> sysUserList) {
        this.sysUserList = sysUserList;
    }

    public int getCarId() {
        return carId;
    }


    public int getUserId() {
        return userId;
    }

    public int getSysuserId() {
        return sysuserId;
    }

    public void setSysuserId(int sysuserId) {
        this.sysuserId = sysuserId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getStatusText() {
        switch (status) {
            case 0:
                return "初始单";
            case 1:
                return "未确认";
            case 2:
                return "已确认";
            case 3:
                return "已形成定单";
            case -1:
                return "删除";
        }

        return "";
    }

    public String getServePrice() {
        return servePrice;
    }

    public void setServePrice(String servePrice) {
        this.servePrice = servePrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPlanInformTime() {
        return planInformTime;
    }

    public void setPlanInformTime(String planInformTime) {
        this.planInformTime = planInformTime;
    }

    public String getInformTime() {
        return informTime;
    }

    public void setInformTime(String informTime) {
        this.informTime = informTime;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public List<Technician> getSysUserList() {
        return sysUserList;
    }
}
