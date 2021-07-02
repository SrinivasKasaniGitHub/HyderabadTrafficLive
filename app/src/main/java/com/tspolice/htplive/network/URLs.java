package com.tspolice.htplive.network;

import android.net.Uri;

public class URLs {

    /*getTrafficOfficersDetails ---
            getAboutApp
    getAutoFares
            getDivisions
    getZones----
            getVehicleType-----
    getUserType
            getHtpWebsites ---
    getFAQ ---
            getPoliceStations ***
    getParkingDetails
            getParkingType*/

    private static final String rootUrl = "http://117.239.149.90:8080/HTP/rest/htpService/";

    //public static final String rootLocalUrl = "http://125.16.1.70:8080/HydLive/Traffic/service/";

    public static final String rootLocalUrl = "https://echallan.tspolice.gov.in/TSTrafficLiveRS/Traffic/service/";

    public static final String pendingChalnsUrl="https://echallan.tspolice.gov.in/publicviewRS/tspolice/pendChallansByRegn?regnNo=";

    //public static final String pendingChalnsUrl = "http://61.95.168.181:8080/HackEyeService/tspolice/pendChallansByRegn?regnNo=";

    public static final String paymentChalnsUrl = "https://echallan.tspolice.gov.in/publicview/?regnNumber=";

    private static final String testUrl = "http://61.95.168.181:8282/";

    private static final String sectionsUrl = "http://61.95.168.181:8080/echallan/";

    public static final String saveCapturedImage = rootLocalUrl + "publicInterface";

    public static final String getAutoFares = rootLocalUrl + "getAutoFaresList";

    private static final String getAutoFaresByDistance = rootLocalUrl + "getAutoFares";

    public static final String getTrafficOficersList = rootLocalUrl + "getTrafficOfficersDetails";

    public static final String getHtpWebsites = rootLocalUrl + "getHtpWebsites";

    public static final String getHtpQuestions = rootLocalUrl + "getFAQ";

    public static final String getHydPoliceStations = rootLocalUrl + "getPoliceStations";

    public static String getAutoFaresByDistance(String distance) {
        return getAutoFaresByDistance + "?" + URLParams.distance + "=" + distance;
    }

    // http://localhost:8080/HydLive/Traffic/service/getAutoFares?distMtrs=12

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

    public static final String googleApiKey = "AIzaSyAdJ3Z4n1oMYfyViKxK2G1rjCf1pUYpY6Y";

    public static final String googleApiKey2 = "AIzaSyDL4r9owIihPTqBSW13hUWYeJ2B9Qaaxy4";

    public static String getGoogleDirectionsApi(double latitude, double longitude, double destLatitude, double destLongitude) {
        return googleDirectionsApi + "?" + URLParams.origins + "=" + latitude + "," + longitude
                + "&" + URLParams.destinations + "=" + destLatitude + "," + destLongitude
                + "&" + URLParams.departure_time + "=now"
                + "&" + URLParams.key + "=" + googleApiKey;
    }

    //https://maps.googleapis.com/maps/api/distancematrix/json?origins=17.4014434,78.4765565&destinations=17.439929499999998,78.4982741&departure_time=now&key=AIzaSyAdJ3Z4n1oMYfyViKxK2G1rjCf1pUYpY6Y

    public static final String getZones = rootLocalUrl + "getZones";

    public static final String getVehicleType = rootLocalUrl + "getVehicleType";

    public static final String getParkingDetails = rootLocalUrl + "getParkingDetails";

    public static String getParkingDetails(String psID, String vehID, String parkingID) {
        return getParkingDetails + "?psID=" + psID + "&vehID=" + vehID + "&parkingID=" + parkingID;
    }

    public static final String eChallanStatus = "https://echallan.tspolice.gov.in/publicview/";

    public static final String getCaptchaForVehicleDetails = rootLocalUrl + "getCaptchaForVehicleDetails";

    private static final String getVehicleDetails = rootLocalUrl + "getVehicleDetails";

    public static String getVehicleDetails(String vehicleNo, String ctrl) {
        return getVehicleDetails + "?" + URLParams.regNo + "=" + vehicleNo
                + "&" + URLParams.ctrl + "=" + ctrl;
    }

    public static final String saveAutocomplainData = rootLocalUrl + "getPublicComplaints";

    public static final String saveSuggestions = rootLocalUrl + "getSuggestions";

    public static String saveSuggestions(String sugGESTION, String emaIL, String mobILE_NO, String namE) {
        return saveSuggestions + "?sugGESTION=" + sugGESTION
                + "&emaIL=" + emaIL
                + "&mobILE_NO=" + mobILE_NO
                + "&namE=" + namE;
    }


    public static final String getPublicAdvisaryData = rootLocalUrl + "getPublicAdvisaryData";

    private static final String loadPubAdvNextRec = rootUrl + "loadPubAdvNextRec";

    public static String loadPubAdvNextRec(String id) {
        return loadPubAdvNextRec + "?" + URLParams.id + "=" + id;
    }

    public static final String getTrafficOfficers = rootUrl + "getTrafficOfficers"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    public static final String htpOnFacebook = "https://www.facebook.com/HYDTP?sk=wall";

    public static final String getAboutHtpApp = rootUrl + "getAboutHtpApp"
            + "?" + URLParams.updatedDate + "=" + URLParams.updatedDate
            + "&" + URLParams.language + "=" + URLParams.ENG;

    private static final String getEmergencyContacts = rootLocalUrl + "getEmergencyContacts";

    public static String getEmergencyContacts(String serviceType) {
        return getEmergencyContacts + "?" + URLParams.serviceType + "=" + serviceType;
    }

    private static final String saveRegIds = rootLocalUrl + "saveRegIds";

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


    // Quiz

    public static String getRandomQstns_Url = "http://61.95.168.181:8181/HydLive/Traffic/service/";

    public static String getRandomQuestions(final String langId) {
        return rootLocalUrl + "getRandomQuestions?languageId=" + langId;
    }

    public static String loginUserURL = rootLocalUrl + "loginT20Test";

    public static String testResultUrl = rootLocalUrl + "finalSubmitT20Test";

}
