package com.juner.mvp.bean;


//商品品牌
public class GoodsBrand {

    String name;
    String src;

    public GoodsBrand(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
