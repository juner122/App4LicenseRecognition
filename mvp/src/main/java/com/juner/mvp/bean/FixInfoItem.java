package com.juner.mvp.bean;

/**
 * 检修单  工时和配件的父对象
 */
public class FixInfoItem {

    int id;
    int selected;//选择 0 ,1,2

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }


    public boolean selectde() {

        return selected != 0;

    }
}
