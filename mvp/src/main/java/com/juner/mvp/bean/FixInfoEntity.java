package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 维修单
 */
public class FixInfoEntity implements Parcelable {


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
    String signPic;


    List<Technician> sysUserList;//技师
    List<FixServie> orderProjectList;//工时
    List<FixParts> orderGoodsList;//配件

    //客户签名
    private String replaceSignPic;
    //其他凭证图片
    private String replaceOterPic;
    String deputy;
    String deputy_mobile;

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getDeputy_mobile() {
        return deputy_mobile;
    }

    public void setDeputy_mobile(String deputy_mobile) {
        this.deputy_mobile = deputy_mobile;
    }
    public String getReplaceSignPic() {
        return replaceSignPic;
    }

    public void setReplaceSignPic(String replaceSignPic) {
        this.replaceSignPic = replaceSignPic;
    }

    public String getReplaceOterPic() {
        return replaceOterPic;
    }

    public void setReplaceOterPic(String replaceOterPic) {
        this.replaceOterPic = replaceOterPic;
    }

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
                return "已取消";
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



    public void setSignPic(String signPic) {
        this.signPic = signPic;
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
                d = d + fs.getPriceD() * fs.getNumber();
        }

        return d;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.carId);
        dest.writeString(this.carNo);
        dest.writeInt(this.userId);
        dest.writeInt(this.sysuserId);
        dest.writeString(this.userName);
        dest.writeString(this.mobile);
        dest.writeString(this.describe);
        dest.writeInt(this.status);
        dest.writeString(this.servePrice);
        dest.writeString(this.goodsPrice);
        dest.writeString(this.actualPrice);
        dest.writeString(this.addTime);
        dest.writeString(this.planInformTime);
        dest.writeString(this.informTime);
        dest.writeString(this.quotationSn);
        dest.writeString(this.confirmTime);
        dest.writeString(this.submitTime);
        dest.writeString(this.signPic);
        dest.writeTypedList(this.sysUserList);
        dest.writeTypedList(this.orderProjectList);
        dest.writeTypedList(this.orderGoodsList);
        dest.writeString(this.replaceSignPic);
        dest.writeString(this.replaceOterPic);
        dest.writeString(this.deputy);
        dest.writeString(this.deputy_mobile);
    }

    public FixInfoEntity() {
    }

    protected FixInfoEntity(Parcel in) {
        this.id = in.readInt();
        this.carId = in.readInt();
        this.carNo = in.readString();
        this.userId = in.readInt();
        this.sysuserId = in.readInt();
        this.userName = in.readString();
        this.mobile = in.readString();
        this.describe = in.readString();
        this.status = in.readInt();
        this.servePrice = in.readString();
        this.goodsPrice = in.readString();
        this.actualPrice = in.readString();
        this.addTime = in.readString();
        this.planInformTime = in.readString();
        this.informTime = in.readString();
        this.quotationSn = in.readString();
        this.confirmTime = in.readString();
        this.submitTime = in.readString();
        this.signPic = in.readString();
        this.sysUserList = in.createTypedArrayList(Technician.CREATOR);
        this.orderProjectList = in.createTypedArrayList(FixServie.CREATOR);
        this.orderGoodsList = in.createTypedArrayList(FixParts.CREATOR);
        this.replaceSignPic = in.readString();
        this.replaceOterPic = in.readString();
        this.deputy = in.readString();
        this.deputy_mobile = in.readString();
    }

    public static final Parcelable.Creator<FixInfoEntity> CREATOR = new Parcelable.Creator<FixInfoEntity>() {
        @Override
        public FixInfoEntity createFromParcel(Parcel source) {
            return new FixInfoEntity(source);
        }

        @Override
        public FixInfoEntity[] newArray(int size) {
            return new FixInfoEntity[size];
        }
    };
}
