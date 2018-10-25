package com.tspolice.htplive.models;

public class TrPSInfoModel {

    private int id;
    private String id1, createdDate, updatedDate, stationName, mobileNumber, language, latitude, longitude,
            mobileAppSimNo, inspectorNo, acpNo, constableNo2, constableNo3, constableNo4;

    public TrPSInfoModel() {
        //default constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMobileAppSimNo() {
        return mobileAppSimNo;
    }

    public void setMobileAppSimNo(String mobileAppSimNo) {
        this.mobileAppSimNo = mobileAppSimNo;
    }

    public String getInspectorNo() {
        return inspectorNo;
    }

    public void setInspectorNo(String inspectorNo) {
        this.inspectorNo = inspectorNo;
    }

    public String getAcpNo() {
        return acpNo;
    }

    public void setAcpNo(String acpNo) {
        this.acpNo = acpNo;
    }

    public String getConstableNo2() {
        return constableNo2;
    }

    public void setConstableNo2(String constableNo2) {
        this.constableNo2 = constableNo2;
    }

    public String getConstableNo3() {
        return constableNo3;
    }

    public void setConstableNo3(String constableNo3) {
        this.constableNo3 = constableNo3;
    }

    public String getConstableNo4() {
        return constableNo4;
    }

    public void setConstableNo4(String constableNo4) {
        this.constableNo4 = constableNo4;
    }
}
