package com.tspolice.htplive.models;

public class ParkingDetailsModel {

    private int id;
    private VehicleTypeModel vehicleType;
    private ParkingTypeModel parkingType;
    private String location, remarks, latitude, longitude;

    public ParkingDetailsModel() {

    }

    public int getId() {
        return id;
    }

    public VehicleTypeModel getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeModel vehicleType) {
        this.vehicleType = vehicleType;
    }

    public ParkingTypeModel getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingTypeModel parkingType) {
        this.parkingType = parkingType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
