package com.tspolice.htplive.models;

public class ViolationsModel {

    private int offenceCd;
    private String section, offenceDesc, fineMin, fineMax, penaltyPoints;

    public ViolationsModel() {
    }

    public int getOffenceCd() {
        return offenceCd;
    }

    public void setOffenceCd(int offenceCd) {
        this.offenceCd = offenceCd;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getOffenceDesc() {
        return offenceDesc;
    }

    public void setOffenceDesc(String offenceDesc) {
        this.offenceDesc = offenceDesc;
    }

    public String getFineMin() {
        return fineMin;
    }

    public void setFineMin(String fineMin) {
        this.fineMin = fineMin;
    }

    public String getFineMax() {
        return fineMax;
    }

    public void setFineMax(String fineMax) {
        this.fineMax = fineMax;
    }

    public String getPenaltyPoints() {
        return penaltyPoints;
    }

    public void setPenaltyPoints(String penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }
}
