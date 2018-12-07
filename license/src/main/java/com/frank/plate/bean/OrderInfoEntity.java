package com.frank.plate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderInfoEntity extends SelectedBean implements Parcelable {

    int id;
    String order_sn;
    int user_id;
    int order_status;
    String shipping_status;
    int pay_status;
    String  pay_status_text;
    double actual_price;
    double coupon_price;

    String consignee;//
    String mobile;
    int car_id;
    String postscript;
    String order_status_text;//未付款
    String add_time;//
    String pay_time;//
    String car_no;//
    String confirm_time;//确认时间
    Long planfinishi_time;//预计完成时间
    double order_price;//预计完成时间
    int pay_type;
    String discount_price;//自定义折扣
    String custom_cut_price;//自定义减免

    List<GoodsEntity> goodsList;
    List<Technician> sysUserList;

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

    public int getPay_type() {
        return pay_type;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public void setPay_type(int pay_type) {
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

    public void setGoodsList(List<GoodsEntity> orderGoods) {
        this.goodsList = orderGoods;
    }

    public List<Technician> getSysUserList() {
        return sysUserList;
    }

    @Override
    public String toString() {
        return "OrderInfoEntity{" +
                "id='" + id + '\'' +
                ", order_sn='" + order_sn + '\'' +
                ", user_id='" + user_id + '\'' +
                ", order_status='" + order_status + '\'' +
                ", shipping_status='" + shipping_status + '\'' +
                ", pay_status='" + pay_status + '\'' +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", car_id='" + car_id + '\'' +
                ", postscript='" + postscript + '\'' +
                ", order_status_text='" + order_status_text + '\'' +
                ", add_time='" + add_time + '\'' +
                ", car_no='" + car_no + '\'' +
                ", planfinishi_time=" + planfinishi_time +
                ", goodsList=" + goodsList +
                ", sysUserList=" + sysUserList +
                '}';
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


    public OrderInfoEntity(int user_id, String moblie, int car_id, String car_number, String consignee) {
        this.user_id = user_id;
        this.mobile = moblie;
        this.car_id = car_id;
        this.car_no = car_number;
        this.consignee = consignee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
    }

    public String getConsignee() {
        return consignee;
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

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getPostscript() {
        return postscript;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.order_sn);
        dest.writeInt(this.user_id);
        dest.writeInt(this.order_status);
        dest.writeString(this.shipping_status);
        dest.writeInt(this.pay_status);
        dest.writeString(this.pay_status_text);
        dest.writeDouble(this.actual_price);
        dest.writeDouble(this.coupon_price);
        dest.writeString(this.consignee);
        dest.writeString(this.mobile);
        dest.writeInt(this.car_id);
        dest.writeString(this.postscript);
        dest.writeString(this.order_status_text);
        dest.writeString(this.add_time);
        dest.writeString(this.pay_time);
        dest.writeString(this.car_no);
        dest.writeString(this.confirm_time);
        dest.writeValue(this.planfinishi_time);
        dest.writeDouble(this.order_price);
        dest.writeInt(this.pay_type);
        dest.writeString(this.discount_price);
        dest.writeString(this.custom_cut_price);
        dest.writeTypedList(this.goodsList);
        dest.writeTypedList(this.sysUserList);
    }

    protected OrderInfoEntity(Parcel in) {
        this.id = in.readInt();
        this.order_sn = in.readString();
        this.user_id = in.readInt();
        this.order_status = in.readInt();
        this.shipping_status = in.readString();
        this.pay_status = in.readInt();
        this.pay_status_text = in.readString();
        this.actual_price = in.readDouble();
        this.coupon_price = in.readDouble();
        this.consignee = in.readString();
        this.mobile = in.readString();
        this.car_id = in.readInt();
        this.postscript = in.readString();
        this.order_status_text = in.readString();
        this.add_time = in.readString();
        this.pay_time = in.readString();
        this.car_no = in.readString();
        this.confirm_time = in.readString();
        this.planfinishi_time = (Long) in.readValue(Long.class.getClassLoader());
        this.order_price = in.readDouble();
        this.pay_type = in.readInt();
        this.discount_price = in.readString();
        this.custom_cut_price = in.readString();
        this.goodsList = in.createTypedArrayList(GoodsEntity.CREATOR);
        this.sysUserList = in.createTypedArrayList(Technician.CREATOR);
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
