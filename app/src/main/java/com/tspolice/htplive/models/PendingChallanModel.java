package com.tspolice.htplive.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PendingChallanModel implements Serializable {

    private int id;

    String UnitName,ChallanNo,Date,PointName,PSName,CompoundingAmount,violations;

    private Boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getChallanNo() {
        return ChallanNo;
    }

    public void setChallanNo(String challanNo) {
        ChallanNo = challanNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public String getPSName() {
        return PSName;
    }

    public void setPSName(String PSName) {
        this.PSName = PSName;
    }

    public String getCompoundingAmount() {
        return CompoundingAmount;
    }

    public void setCompoundingAmount(String compoundingAmount) {
        CompoundingAmount = compoundingAmount;
    }

    public String getViolations() {
        return violations;
    }

    public void setViolations(String violations) {
        this.violations = violations;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
