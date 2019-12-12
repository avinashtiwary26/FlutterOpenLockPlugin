package com.example.flutterpluginupdate.Utilities;

/**
 * @author OpenKey Inc.
 * <p>
 * This will hold all the constants strings for the SDK
 */

public class Constants {

    // SharedPreference keys below
    // Manufacturer for identification purpose to use which SDK needs to be started
    public static final String UUID = "openkey_sdk_uuid";
    //    public static String UUID = "";
    public static final String MANUFACTURER = "manufacturer";
    public static final String AUTH_SIGNATURE = "auth_signature";
    public static final String BOOKING_ID = "booking_id";

    public static final String MOBILE_KEY = "mobile_key";


    public static final String BOOKING = "booking";
    public static final String UNIQUE_NUMBER = "unique_number";
    public static final String MOBILE_KEY_STATUS = "mobile_key_status";
    public static final String BASE_URL = "base_url";
    public static final String TOKEN = "Token ";
    public static final String ENVIRONMENT_TYPE = "environment_type";
    public static final long SCANNING_TIME = 12000;

    // KEY_DELIVERED=0;
    // PENDING_KEY_SERVER_REQUEST=1;
    // KEY_SERVER_REQUESTED=2;
    public static final String KEY_DELIVERED = "KEY DELIVERED";
    public static final String PENDING_KEY_SERVER_REQUEST = "PENDING KEY SERVER REQUEST";


    //LIVE BASE URL
    //  public static final String BASE_URL_LIVE = "https://developer.openkey.co/";
    // public static final String BASE_URL_LIVE = "https://betadeveloper.openkey.co/";

    //DEV BASE URL
//    public static String BASE_URL_DEV = "https://connector.openkey.co/";
    //    public static  String BASE_URL_DEV = "https://partner.openkey.co/";
    public static String BASE_URL_DEV = "https://apidev.openkey.co/";
    //Live
    public static String BASE_URL_LIVE = "https://connector.openkey.co/";


}
