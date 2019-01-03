package com.eb.new_line_seller.bean;

import java.util.List;

public class ServerList {

    List<Server> list;

    public List<Server> getList() {
        return list;
    }

    public void setList(List<Server> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ServerList{" +
                "list=" + list +
                '}';
    }
}
