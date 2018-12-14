package com.frank.plate.bean;

public class SetProject {
    int id;

    int valueId;
    int type;
    String name;

    public SetProject(int id, int valueId, String name, int type) {
        this.id = id;
        this.valueId = valueId;
        this.name = name;

        this.type = type;
    }

    public SetProject(String name) {
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SetProject(int valueId, int type) {
        this.valueId = valueId;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
