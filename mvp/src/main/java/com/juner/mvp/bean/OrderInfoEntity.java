package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import java.util.List;

public class OrderInfoEntity extends SelectedBean implements Parcelable {

    Integer id;
    Integer quotation_id;

    String order_sn;
    Integer user_id;
    Integer order_status;
    String shipping_status;
    Integer pay_status;
    String pay_name;
    String pay_status_text;
    double actual_price;//实际支付
    double coupon_price;
    Integer coupon_id;

    String consignee;//
    String mobile;
    Integer car_id;
    String postscript;
    String order_status_text;//未付款
    String add_time;//
    String pay_time;//
    String car_no;//
    String confirm_time;//确认时间
    Long planfinishi_time;//预计完成时间
    Long planInformTime;//检修单预计完成时间
    double order_price;//
    int pay_type;// 1嗨卡 11微信 21 掌贝 22 现金
    String discount_price;//自定义折扣
    String custom_cut_price;//自定义减免

    String goods_unit;//单位
    String province;//卡号

    String district = "";//客户签名图片地址 七牛

    List<GoodsEntity> goodsList;
    List<Technician> sysUserList;
    List<GoodsEntity> userActivityList;

    ArrayList<Server> skillList;

    Integer os_type;//0=销售单 1=服务单

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

    public String getPay_name() {
        if (pay_type == 11)
            return "微信";
        else
            return null == pay_name || pay_name.equals("") ? "-" : pay_name;
    }

    public Long getPlanInformTime() {
        return planInformTime;
    }

    public void setPlanInformTime(Long planInformTime) {
        this.planInformTime = planInformTime;
    }

    public Integer getQuotation_id() {
        return quotation_id;
    }

    public void setQuotation_id(Integer quotation_id) {
        this.quotation_id = quotation_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return null == district ? "" : district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(Integer coupon_id) {
        this.coupon_id = coupon_id;
    }

    public ArrayList<Server> getSkillList() {
        return skillList;
    }

    public void setSkillList(ArrayList<Server> skillList) {
        this.skillList = skillList;
    }

    public List<GoodsEntity> getUserActivityList() {
        return userActivityList;
    }

    public void setUserActivityList(List<GoodsEntity> userActivityList) {
        this.userActivityList = userActivityList;
    }


    public double getYouweijie_price() {
        if (discount_price.equals("0.00")) {
            if (custom_cut_price.equals("0.00")) return 0.00d;
            else return Double.parseDouble(custom_cut_price);
        } else return Double.parseDouble(discount_price);
    }

    public String getGoods_unit() {
        return goods_unit;
    }

    public void setGoods_unit(String goods_unit) {
        this.goods_unit = goods_unit;
    }

    public double getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(double coupon_price) {
        this.coupon_price = coupon_price;
    }

    public double getActual_price() {
        return actual_price;
    }

    public void setActual_price(double actual_price) {
        this.actual_price = actual_price;
    }

    public String getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(String confirm_time) {
        this.confirm_time = confirm_time;
    }

    public Integer getPay_type() {
        return pay_type;
    }

    public String getPay_time() {
        return null != pay_time ? pay_time : "-";
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getCustom_cut_price() {
        return custom_cut_price;

    }

    public void setCustom_cut_price(String custom_cut_price) {
        this.custom_cut_price = custom_cut_price;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }


    public List<GoodsEntity> getGoodsList() {
        return goodsList;
    }

    public List<GoodsEntity> getGoodsAndUserActivityList() {

        List<GoodsEntity> list = new ArrayList<>();
        list.addAll(getGoodsList());
        list.addAll(getUserActivityList());
        return list;
    }


    public void setGoodsList(List<GoodsEntity> orderGoods) {
        this.goodsList = orderGoods;
    }

    public List<Technician> getSysUserList() {
        return sysUserList;
    }

    public void setSysUserList(List<Technician> sysUserList) {
        this.sysUserList = sysUserList;
    }

    public Long getPlanfinishi_time() {
        return planfinishi_time;
    }

    public void setPlanfinishi_time(Long planfinishi_time) {
        this.planfinishi_time = planfinishi_time;
    }

    public String getPay_status_text() {
        return pay_status_text;
    }

    public void setPay_status_text(String pay_status_text) {
        this.pay_status_text = pay_status_text;
    }


    public OrderInfoEntity(Integer user_id, String moblie, Integer car_id, String car_number, String consignee) {
        this.user_id = user_id;
        this.mobile = moblie;
        this.car_id = car_id;
        this.car_no = car_number;
        this.consignee = consignee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public Integer getPay_status() {
        return pay_status;
    }

    public void setPay_status(Integer pay_status) {
        this.pay_status = pay_status;
    }

    public String getConsignee() {
        return null == consignee || "".equals(consignee) ? "匿名" : consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public String getPostscript() {
        return null == postscript || postscript.equals("") ? "暂无备注" : postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getOrder_status_text() {
        return order_status_text;
    }

    public void setOrder_status_text(String order_status_text) {
        this.order_status_text = order_status_text;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public OrderInfoEntity() {
    }

    public Integer getOs_type() {
        return os_type;
    }

    public void setOs_type(Integer os_type) {
        this.os_type = os_type;
    }

    @Override
    public String toString() {
        return "OrderInfoEntity{" +
                "id=" + id +
                ", quotation_id=" + quotation_id +
                ", order_sn='" + order_sn + '\'' +
                ", user_id=" + user_id +
                ", order_status=" + order_status +
                ", shipping_status='" + shipping_status + '\'' +
                ", pay_status=" + pay_status +
                ", pay_name='" + pay_name + '\'' +
                ", pay_status_text='" + pay_status_text + '\'' +
                ", actual_price=" + actual_price +
                ", coupon_price=" + coupon_price +
                ", coupon_id=" + coupon_id +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", car_id=" + car_id +
                ", postscript='" + postscript + '\'' +
                ", order_status_text='" + order_status_text + '\'' +
                ", add_time='" + add_time + '\'' +
                ", pay_time='" + pay_time + '\'' +
                ", car_no='" + car_no + '\'' +
                ", confirm_time='" + confirm_time + '\'' +
                ", planfinishi_time=" + planfinishi_time +
                ", order_price=" + order_price +
                ", pay_type=" + pay_type +
                ", discount_price='" + discount_price + '\'' +
                ", custom_cut_price='" + custom_cut_price + '\'' +
                ", goods_unit='" + goods_unit + '\'' +
                ", province='" + province + '\'' +
                ", district='" + district + '\'' +
                ", goodsList=" + goodsList +
                ", sysUserList=" + sysUserList +
                ", userActivityList=" + userActivityList +
                ", skillList=" + skillList +
                ", os_type=" + os_type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.quotation_id);
        dest.writeString(this.order_sn);
        dest.writeValue(this.user_id);
        dest.writeValue(this.order_status);
        dest.writeString(this.shipping_status);
        dest.writeValue(this.pay_status);
        dest.writeString(this.pay_name);
        dest.writeString(this.pay_status_text);
        dest.writeDouble(this.actual_price);
        dest.writeDouble(this.coupon_price);
        dest.writeValue(this.coupon_id);
        dest.writeString(this.consignee);
        dest.writeString(this.mobile);
        dest.writeValue(this.car_id);
        dest.writeString(this.postscript);
        dest.writeString(this.order_status_text);
        dest.writeString(this.add_time);
        dest.writeString(this.pay_time);
        dest.writeString(this.car_no);
        dest.writeString(this.confirm_time);
        dest.writeValue(this.planfinishi_time);
        dest.writeValue(this.planInformTime);
        dest.writeDouble(this.order_price);
        dest.writeInt(this.pay_type);
        dest.writeString(this.discount_price);
        dest.writeString(this.custom_cut_price);
        dest.writeString(this.goods_unit);
        dest.writeString(this.province);
        dest.writeString(this.district);
        dest.writeTypedList(this.goodsList);
        dest.writeTypedList(this.sysUserList);
        dest.writeTypedList(this.userActivityList);
        dest.writeTypedList(this.skillList);
        dest.writeValue(this.os_type);
        dest.writeString(this.deputy);
        dest.writeString(this.deputy_mobile);
    }

    protected OrderInfoEntity(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.quotation_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.order_sn = in.readString();
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.order_status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shipping_status = in.readString();
        this.pay_status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pay_name = in.readString();
        this.pay_status_text = in.readString();
        this.actual_price = in.readDouble();
        this.coupon_price = in.readDouble();
        this.coupon_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.consignee = in.readString();
        this.mobile = in.readString();
        this.car_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.postscript = in.readString();
        this.order_status_text = in.readString();
        this.add_time = in.readString();
        this.pay_time = in.readString();
        this.car_no = in.readString();
        this.confirm_time = in.readString();
        this.planfinishi_time = (Long) in.readValue(Long.class.getClassLoader());
        this.planInformTime = (Long) in.readValue(Long.class.getClassLoader());
        this.order_price = in.readDouble();
        this.pay_type = in.readInt();
        this.discount_price = in.readString();
        this.custom_cut_price = in.readString();
        this.goods_unit = in.readString();
        this.province = in.readString();
        this.district = in.readString();
        this.goodsList = in.createTypedArrayList(GoodsEntity.CREATOR);
        this.sysUserList = in.createTypedArrayList(Technician.CREATOR);
        this.userActivityList = in.createTypedArrayList(GoodsEntity.CREATOR);
        this.skillList = in.createTypedArrayList(Server.CREATOR);
        this.os_type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.deputy = in.readString();
        this.deputy_mobile = in.readString();
    }

    public static final Creator<OrderInfoEntity> CREATOR = new Creator<OrderInfoEntity>() {
        @Override
        public OrderInfoEntity createFromParcel(Parcel source) {
            return new OrderInfoEntity(source);
        }

        @Override
        public OrderInfoEntity[] newArray(int size) {
            return new OrderInfoEntity[size];
        }
    };
}
