package com.frank.plate.bean;

public class BaseBean<T> {


    int errno;
    String errmsg;

    T data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public boolean ifSuccess() {
        return errno == 0;
    }


    @Override
    public String toString() {
        return "BaseBea{" +
                "errno='" + errno + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
