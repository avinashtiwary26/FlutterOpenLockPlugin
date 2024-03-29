package com.example.flutterpluginupdate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.flutteropenkeysdkplugin.enums.MANUFACTURER;
import com.example.flutterpluginupdate.Utilities.Constants;
import com.example.flutterpluginupdate.Utilities.Response;
import com.example.flutterpluginupdate.Utilities.Utilities;
import com.example.flutterpluginupdate.api.request.Api;
import com.example.flutterpluginupdate.api.response.session.SessionResponse;
import com.example.flutterpluginupdate.interfaces.OpenKeyCallBack;
import com.example.flutterpluginupdate.okmobilekey.OKMobileKey;
import com.example.flutterpluginupdate.singleton.GetBooking;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author OpenKey Inc.
 * <p>
 * This class is responsible for the  SDK task management and Exception handling.
 * This class is build upon the Singleton pattern to prevent the multiple Object
 * creation.
 */
public final class OpenKeyManager {

    @SuppressLint("StaticFieldLeak")
    private static volatile OpenKeyManager instance;
    private static Application mContext;
    private MANUFACTURER manufacturer;
    private OKMobileKey okMobileKey;
    private OpenKeyCallBack mOpenKeyCallBack;


    //-----------------------------------------------------------------------------------------------------------------|
    //-----------------------------------------------------------------------------------------------------------------|
    /*
     * Getting mobile key from server, If the key is issued from backend then start syncing
     * process
     * */
    private Callback getKeyCallback = new Callback() {
        @Override
        public void onResponse(Call call, retrofit2.Response response) {
            if (response.isSuccessful()) {
                startSync();
            } else {
                if (mOpenKeyCallBack != null)
                    mOpenKeyCallBack.isKeyAvailable(false, Response.FETCH_KEY_FAILED);
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            if (mOpenKeyCallBack != null)
                mOpenKeyCallBack.isKeyAvailable(false, Response.FETCH_KEY_FAILED);
        }
    };


    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * This will return the instance of this class and provide a
     * mContext to this class.
     *
     * @return Instance of the {@link OpenKeyManager} class
     */
    public static synchronized OpenKeyManager getInstance() {
        if (instance == null) {
            instance = new OpenKeyManager();
        }
        return instance;
    }

    /**
     * Access to this class can only be provided by this class
     * so object creation is limited to this only.
     *
     * @param context
     */
    public void init(Application context, String UUID) throws NullPointerException {
        if (context == null) throw new NullPointerException(Response.NULL_CONTEXT);

        mContext = context;
        Utilities.getInstance(mContext);
        Utilities.getInstance().saveValue(Constants.UUID, UUID, mContext);
        SessionResponse sessionResponse = Utilities.getInstance().getBookingFromLocal(mContext);
        if (sessionResponse != null) GetBooking.getInstance().setBooking(sessionResponse);
    }


    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * @param authToken
     * @param openKeyCallBack Call back for response purpose
     */
    public void authenticate(String authToken, OpenKeyCallBack openKeyCallBack, boolean environmentType) {

        //Set configuration
        setConfiguration(environmentType);

        if (authToken != null && authToken.length() > 0 && mContext != null)
            Api.getSession(mContext, authToken, openKeyCallBack);
        else openKeyCallBack.sessionFailure(Response.INVALID_AUTH_SIGNATURE, "");
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void setConfiguration(boolean environmentType) {
        if (mContext != null) {
            Utilities.getInstance().saveValue(Constants.ENVIRONMENT_TYPE, environmentType, mContext);
            if (environmentType)
                Utilities.getInstance().saveValue(Constants.BASE_URL, Constants.BASE_URL_LIVE, mContext);
            else
                Utilities.getInstance().saveValue(Constants.BASE_URL, Constants.BASE_URL_DEV, mContext);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|

    public void getSession(String authToken, final Callback callback) {
        //Set configuration
        if (mContext != null && authToken != null) Api.getBooking(authToken, mContext, callback);
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * Initialize SDK with unique number.
     * <p>
     * the unique identification number for setting up device with
     *
     * @param openKeyCallBack Call back for response purpose
     */
    public synchronized void initialize(@NonNull OpenKeyCallBack openKeyCallBack) {
        Log.e("initialize", "called");
        if (mContext == null) {
            Log.e("Context", "null");
            openKeyCallBack.initializationFailure(Response.INITIALIZATION_FAILED);
            return;
        }

        final String manufacturerStr = Utilities.getInstance().getValue(Constants.MANUFACTURER, "", mContext);
        if (manufacturerStr.isEmpty()) {
            openKeyCallBack.initializationFailure(Response.BOOKING_NOT_FOUNT);
            return;
        }

        manufacturer = Utilities.getInstance().getManufacturer(mContext, openKeyCallBack);
        switch (manufacturer) {

            case OKMOBILEKEY:
                okMobileKey = new OKMobileKey(mContext, openKeyCallBack);
                break;

        }
    }

    /**
     * If the user is successfully authenticated
     * and initialization is also successful, can
     * get keys via this method
     *
     * @param openKeyCallBack Call back for response purpose
     */
    public synchronized void getKey(@NonNull final OpenKeyCallBack openKeyCallBack) {
        if (mContext == null && okMobileKey == null) {
            openKeyCallBack.isKeyAvailable(false, Response.FETCH_KEY_FAILED);
            return;
        }

        mOpenKeyCallBack = openKeyCallBack;

        //if mContext null then it returned callback with null mContext description
        if (mContext == null) openKeyCallBack.isKeyAvailable(false, Response.NULL_CONTEXT);


        //Getting key from server
        Api.getMobileKey(mContext, getKeyCallback);
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * If the user is successfully
     * get keys then start sync process via this method
     */
    private void startSync() {
        if (mOpenKeyCallBack == null || mContext == null) return;

        manufacturer = Utilities.getInstance().getManufacturer(mContext, mOpenKeyCallBack);
        switch (manufacturer) {

            case OKMOBILEKEY:
                okMobileKey.fetchOkMobileKeyRoomList();
                updateKeyStatus(true);
                mOpenKeyCallBack.isKeyAvailable(true, Response.FETCH_KEY_SUCCESS);
                break;
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * If device has a key available
     *
     * @param openKeyCallBack Call back for response purpose
     * @return boolean
     */
    public synchronized boolean isKeyAvailable(OpenKeyCallBack openKeyCallBack) {
        if (okMobileKey == null) {
            Log.e("Started", "INITIALIZATION_FAILED");
            openKeyCallBack.initializationFailure(Response.INITIALIZATION_FAILED);
            initialize(openKeyCallBack);

        }
        boolean haveKey = false;
        manufacturer = Utilities.getInstance().getManufacturer(mContext, openKeyCallBack);
        switch (manufacturer) {

            case OKMOBILEKEY:
                haveKey = okMobileKey.haveKey();
                break;
        }
        return haveKey;
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * start scanning if passes the initial checks
     * and device have a key
     *
     * @param openKeyCallBack Call back for response purpose
     */
    public synchronized void startScanning(@NonNull OpenKeyCallBack openKeyCallBack, String roomNumber) {
        manufacturer = Utilities.getInstance().getManufacturer(mContext, openKeyCallBack);

        if (mContext == null) {
            Log.e("Context", "null");
            openKeyCallBack.initializationFailure(Response.NULL_CONTEXT);
        }
//
//        if (manufacturer == MANUFACTURER.OKC && !BleHelper.getInstance().isBleOpend()) {
//            okc.okcSDKInitialize();
//        }

        if (isKeyAvailable(openKeyCallBack)) {
            switch (manufacturer) {


                case OKMOBILEKEY:
                    okMobileKey.startScanning(roomNumber);
                    break;

            }
        } else {
            Log.e("startScanning", "key not available");
            openKeyCallBack.stopScan(false, Response.NO_KEY_FOUND);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * * This method is used to update the key status on server.
     * 1 identify the device have key
     * 0 identify the device have not key
     *
     * @param haveKey Device have key or not
     */
    public void updateKeyStatus(boolean haveKey) {

        if (haveKey) Api.setKeyStatus(mContext, Constants.KEY_DELIVERED);
        else Api.setKeyStatus(mContext, Constants.PENDING_KEY_SERVER_REQUEST);
    }
    //-----------------------------------------------------------------------------------------------------------------|

}