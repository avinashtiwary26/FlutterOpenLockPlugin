package com.example.flutterpluginupdate.Utilities;

/**
 * Response constants for showing the Specific error occurred
 **/
public class Response {


    // if any failure occur while initialization
    public static final String INITIALIZATION_FAILED = "INITIALIZATION FAILED";

    // if getting key operation is failed
    public static final String FETCH_KEY_FAILED = "FAILED GETTING KEYS";

    // if getting key operation is success
    public static final String FETCH_KEY_SUCCESS = "SUCCESS GETTING KEYS";

    // If Unknown error occurred.
    public static final String UNKNOWN = "Unknown error occurred, please contact administrator";

    // IF device has not been setup
    public static final String NOT_INITIALIZED = "SDK NOT INITIALIZED";

    // IF device has failed authentication
    public static final String AUTHENTICATION_FAILED = "AUTHENTICATION FAILED";

    // IF booking not exist
    public static final String BOOKING_NOT_FOUNT = "BOOKING NOT FOUND";


    // IF null context passed
    public static final String NULL_CONTEXT = "CONTEXT NULL";

    // if auth signature is not valid
    public static final String INVALID_AUTH_SIGNATURE = "AUTH SIGNATURE IS INVALID";

    // if device has no keys
    public static final String NO_KEY_FOUND = "NO KEY FOUND";


}