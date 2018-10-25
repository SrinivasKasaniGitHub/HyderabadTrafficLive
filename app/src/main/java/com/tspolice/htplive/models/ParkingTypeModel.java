package com.tspolice.htplive.models;

public class ParkingTypeModel {

    private int id;
    private String name, icon, bigicon;

    public ParkingTypeModel() {
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBigicon() {
        return bigicon;
    }

    public void setBigicon(String bigicon) {
        this.bigicon = bigicon;
    }
}
