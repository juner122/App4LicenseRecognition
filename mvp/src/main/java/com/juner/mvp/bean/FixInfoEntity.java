package com.juner.mvp.bean;

import java.util.List;

/**
 * 维修单
 */
public class FixInfoEntity {


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
     * //
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
    int id;
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


    String confirmTime;
    String submitTime;


    List<Technician> sysUserList;//技师
    List<FixServie> orderProjectList;//工时
    List<FixParts> orderGoodsList;//配件


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


    public String getQuotationSn() {
        return quotationSn;
    }

    public void setQuotationSn(String quotationSn) {
        this.quotationSn = quotationSn;
    }

    public String getCarNo() {
        return carNo;
    }

    public String getStatusText() {
        switch (status) {
            case 0:
            case 1:
                return "待报价";
            case 2:
                return "待确认";
            case 3:
                return "已确认";
            case 4:
                return "已出单";
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
        return null == actualPrice ? "0.00" : actualPrice;
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

    public List<FixServie> getOrderProjectList() {
        return orderProjectList;
    }

    public void setOrderProjectList(List<FixServie> orderProjectList) {
        this.orderProjectList = orderProjectList;
        this.setServePrice(getServicePrice(orderProjectList).toString());


    }

    public List<FixParts> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<FixParts> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
        this.setGoodsPrice(getPartsPrice(orderGoodsList).toString());
    }


    //计算配件总价格
    private Double getPartsPrice(List<FixParts> fixParts) {

        Double d = 0d;
        if (fixParts == null)
            return d;


        for (FixParts fp : fixParts) {
            if (fp.selectde())
                d = d + Double.parseDouble(fp.getRetail_price()) * fp.getNumber();
        }
        return d;

    }

    //计算工时总价格
    private Double getServicePrice(List<FixServie> fixServies) {
        Double d = 0d;
        if (fixServies == null)
            return d;

        for (FixServie fs : fixServies) {
            if (fs.selectde())
                d = d + fs.getPriceD();
        }

        return d;

    }

}
