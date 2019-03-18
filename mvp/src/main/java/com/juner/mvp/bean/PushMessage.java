package com.juner.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PushMessage implements Parcelable {
    int type;
    String text;

    String title;

    int pushId;//消息id
    int valueId;//订单id
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return pushId;
    }

    public void setId(int id) {
        this.pushId = id;
    }

    public int getOrderId() {
        return valueId;
    }

    public void setOrderId(int orderId) {
        this.valueId = orderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.text);
        dest.writeString(this.title);
        dest.writeInt(this.pushId);
        dest.writeInt(this.valueId);
    }

    public PushMessage() {
    }

    protected PushMessage(Parcel in) {
        this.type = in.readInt();
        this.text = in.readString();
        this.title = in.readString();
        this.pushId = in.readInt();
        this.valueId = in.readInt();
    }

    public static final Parcelable.Creator<PushMessage> CREATOR = new Parcelable.Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel source) {
            return new PushMessage(source);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };
}
