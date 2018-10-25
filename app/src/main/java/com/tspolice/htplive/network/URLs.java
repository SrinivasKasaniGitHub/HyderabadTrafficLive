package com.tspolice.htplive.network;

public class URLs {

    // rootUrl
    private static final String rootUrl = "http://117.239.149.90:8080/HTP/rest/htpService/";

    //2.1
    public static final String getAutoFares = rootUrl + "getAutoFares";
    //2.2
    private static final String getAutoFaresByDistance = rootUrl + "getAutoFaresByDistance";
    public static String getAutoFaresByDistance(String distance) {
        return getAutoFaresByDistance + "?distance=" + distance;
    }
    //2.3
    private static final String googleDirectionsApi = "http://maps.google.com/maps/api/directions/xml";
    public static String getGoogleDirectionsApi(double latitude, double longitude, double dest_latitude, double dest_longitude) {
        return googleDirectionsApi + "?origin=" + latitude + "," + longitude + "&destination="
                + dest_latitude + "," + dest_longitude + "&sensor=false&units=metric";
    }

    //4.1
    public static final String getHydPoliceStations = rootUrl + "getHydPoliceStations?updatedDate=&language=ENG";
    //4.2
    public static final String getZones = rootUrl + "getZones";
    //4.3
    public static final String getParkingDetails = rootUrl + "getParkingDetails";
    //4.4
    public static final String contentType = "application/json; charset=utf-8";
    //4.5
    public static final String utf_8 = "utf-8";

    //5
    public static final String eChallanStatus = "https://www.echallan.org/publicview/";

    //6.1, 7.1
    public static final String getCaptchaForVehicleDetails = rootUrl + "getCaptchaForVehicleDetails";
    //6.2, 7.2
    private static final String getVehicleDetails = rootUrl + "getVehicleDetails";
    public static String getVehicleDetails(String vehicleNo, String ctrl) {
        return getVehicleDetails + "?regNo=" + vehicleNo + "&ctrl=" + ctrl;
    }

    public static final String getTrafficOfficers = rootUrl + "getTrafficOfficers?updatedDate=\"\"&language=ENG";

    //9.3.2
    public static final String getHtpWebsites = rootUrl + "getHtpWebsites?updatedDate=\"\"&language=ENG";
    //9.3.3
    public static final String getHtpQuestions = rootUrl + "getHtpQuestions?updatedDate=\"\"&language=ENG";
    //9.3.6
    public static final String htpOnFacebook = "https://www.facebook.com/HYDTP?sk=wall";
    //9.4.1
    public static final String getAboutHtpApp = rootUrl + "getAboutHtpApp?updatedDate=\"\"&language=ENG";

    private static final String getEmergencyContacts = rootUrl + "getEmergencyContacts";
    public static String getEmergencyContacts(String serviceType) {
        return getEmergencyContacts + "?serviceType=" + serviceType + "&updatedDate=updatedDate" + "&language=ENG";
    }
}
