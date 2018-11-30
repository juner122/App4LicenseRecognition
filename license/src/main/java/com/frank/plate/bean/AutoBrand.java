package com.frank.plate.bean;

import android.support.annotation.NonNull;

public class AutoBrand implements Comparable<AutoBrand> {

    int id;
    String name;
    String image;
    String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AutoBrand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull AutoBrand autoBrand) {
        if (autoBrand == null) {
            return -1;
        }
        return type.compareTo(autoBrand.getType());
    }
}
