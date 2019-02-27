package com.tspolice.htplive.network;

import android.net.Uri;

public class URLs {

    private static final String rootUrl = "http://117.239.149.90:8080/HTP/rest/htpService/";

    private static final String testUrl = "http://61.95.168.181:8282/";

    private static final String sectionsUrl = "http://61.95.168.181:8080/echallan/";

    public static final String saveCapturedImage = testUrl + "saveCapturedImage";

    public static final String getAutoFares = rootUrl + "getAutoFares";

    private static final String getAutoFaresByDistance = rootUrl + "getAutoFaresByDistance";

    public static String getAutoFaresByDistance(String distance) {
        return getAutoFaresByDistance + "?" + URLParams.distance + "=" + distance;
    }

    //2.3
    /*private static final String googleDirectionsApi = "http://maps.google.com/maps/api/directions/xml";
    public static String getGoogleDirectionsApi(double latitude, double longitude, double dest_latitude, double dest_longitude) {
        return googleDirectionsApi + "?origin=" + latitude + "," + longitude + "&destination="
                + dest_latitude + "," + dest_longitude + "&sensor=false&units=metric";
    }*/

    public static Uri getUri(double latitude, double longitude, double destLatitude, double destLongitude) {
        return Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                + "&daddr=" + destLatitude + "," + destLongitude);
    }

    private static final String googleDirectionsApi = "https://maps.googleapis.com/maps/api/distancematrix/json";

    private static final String googleApiKey = "AIzaSyAdJ3Z4n1oMYfyViKxK2G1rjCf1pUYpY6Y";

    private static final String googleApiKey2 = "AIzaSyDL4r9owIihPTqBSW13hUWYeJ2B9Qaaxy4";

    public static String getGoogleDirectionsApi(double latitude, double longitude, double destLatitude, double destLongitude) {
        return googleDirectionsApi + "?" + URLParams.origins + "=" + latitude + "," + longitude
                + "&" + URLParams.destinations + "=" + destLatitude + "," + destLongitude
                + "&" + URLParams.departure_time + "=now"
                + "&" + URLParams.key + "=" + googleApiKey;
    }

    //https://maps.googleapis.com/maps/api/distancematrix/json?origins=17.4014434,78.4765565&destinations=17.439929499999998,78.4982741&departure_time=now&key=AIzaSyAdJ3Z4n1oMYfyViKxK2G1rjCf1pUYpY6Y

    public static final String getHydPoliceStations = rootUrl + "getHydPoliceStations"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    public static final String getZones = rootUrl + "getZones";

    public static final String getParkingDetails = rootUrl + "getParkingDetails";

    public static final String eChallanStatus = "https://www.echallan.org/publicview/";

    public static final String getCaptchaForVehicleDetails = rootUrl + "getCaptchaForVehicleDetails";

    private static final String getVehicleDetails = rootUrl + "getVehicleDetails";

    public static String getVehicleDetails(String vehicleNo, String ctrl) {
        return getVehicleDetails + "?" + URLParams.regNo + "=" + vehicleNo
                + "&" + URLParams.ctrl + "=" + ctrl;
    }

    public static final String saveAutocomplainData = testUrl + "saveAutocomplainData";

    public static final String saveSuggestions = rootUrl + "saveSuggestions";

    public static final String getPublicAdvisaryData = rootUrl + "getPublicAdvisaryData";

    private static final String loadPubAdvNextRec = rootUrl + "loadPubAdvNextRec";

    public static String loadPubAdvNextRec(String id) {
        return loadPubAdvNextRec + "?" + URLParams.id + "=" + id;
    }

    public static final String getTrafficOfficers = rootUrl + "getTrafficOfficers"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    public static final String getHtpWebsites = rootUrl + "getHtpWebsites"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    public static final String getHtpQuestions = rootUrl + "getHtpQuestions"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    public static final String htpOnFacebook = "https://www.facebook.com/HYDTP?sk=wall";

    public static final String getAboutHtpApp = rootUrl + "getAboutHtpApp"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    private static final String getEmergencyContacts = rootUrl + "getEmergencyContacts";

    public static String getEmergencyContacts(String serviceType) {
        return getEmergencyContacts + "?" + URLParams.serviceType + "=" + serviceType
                + "&" + URLParams.updatedDate + "=" + URLParams.updatedDate
                + "&" + URLParams.language + "=" + URLParams.ENG;
    }

    private static final String saveRegIds = testUrl + "saveRegIds";
    public static String saveRegIds(String regId, String deviceType, String deviceId) {
        return saveRegIds + "?" + URLParams.regId + "=" + regId
                + "&" + URLParams.deviceType + "=" + deviceType
                + "&" + URLParams.deviceId + "=" + deviceId;
    }

    private static final String sectionMasterByWheeler = sectionsUrl + "sectionMasterByWheeler";
    public static String sectionMasterByWheeler(String wheelerCd) {
        return sectionMasterByWheeler + "?" + URLParams.wheelerCd + "=" + wheelerCd;
    }

    public static final String contentType = "application/json; charset=utf-8";

    public static final String utf_8 = "utf-8";

    public static final String unSupportedEncodingException = "Unsupported Encoding while trying to get the bytes of %s using %s";
}
