package com.example.flutterpluginupdate.api.request;

import android.content.Context;
import android.util.Log;

import com.example.flutterpluginupdate.Utilities.Constants;
import com.example.flutterpluginupdate.Utilities.Response;
import com.example.flutterpluginupdate.Utilities.Utilities;
import com.example.flutterpluginupdate.api.model.KeyStatusRequest;
import com.example.flutterpluginupdate.api.model.SdkLogRequest;
import com.example.flutterpluginupdate.api.response.key_status.KeyStatusResponse;
import com.example.flutterpluginupdate.api.response.logaction.LogActionResponse;
import com.example.flutterpluginupdate.api.response.mobile_key_response.MobileKeyResponse;
import com.example.flutterpluginupdate.api.response.personlization.PersonlizationResponse;
import com.example.flutterpluginupdate.api.response.session.SessionResponse;
import com.example.flutterpluginupdate.api.service.Services;
import com.example.flutterpluginupdate.interfaces.OpenKeyCallBack;
import com.example.flutterpluginupdate.singleton.GetBooking;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;
import static com.example.flutterpluginupdate.Utilities.Constants.TOKEN;


/**
 * @author OpenKey Inc.
 * <p>
 * This class will hold all the api calls made from the SDK
 */
public class Api {

    /**
     * Token the user(Third party developer) to use the SDK. This will
     * call OpenKey server to get booking, response will be provided via @{@link OpenKeyCallBack}
     *
     * @param openKeyCallBack Call back for  response
     */
    public static void getSession(final Context context, final String token,
                                  final OpenKeyCallBack openKeyCallBack) {

        if (context == null || token == null)
            return;

        // Get the retrofit instance
        Services services = Utilities.getInstance().getRetrofit(context).create(Services.class);
        services.getSession(TOKEN + token).enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, retrofit2.Response<SessionResponse>
                    response) {
                if (response.isSuccessful()) {
                    Utilities.getInstance().saveValue(Constants.AUTH_SIGNATURE, token, context);
                    saveData(response.body(), context);
                    openKeyCallBack.sessionResponse(response.body());
                } else {
                    // get the error message from the response and return it to the callback
                    openKeyCallBack.sessionFailure(Response.AUTHENTICATION_FAILED, response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                openKeyCallBack.sessionFailure(Response.AUTHENTICATION_FAILED, "");
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private static void saveData(SessionResponse bookingResponse, Context context) {
        if (context == null)
            return;

        if (bookingResponse != null && bookingResponse.getData() != null) {
            Utilities.getInstance(context).saveBookingToLocal(context, bookingResponse);
            GetBooking.getInstance().setBooking(bookingResponse);
            //Saved manufacturer in locally
//            if (bookingResponse.getData().getHotel() != null &&
//                    bookingResponse.getData().getHotel().getLockVendor() != null &&
//                    bookingResponse.getData().getHotel().getLockVendor().getTitle() != null) {
            if (bookingResponse.getData().getHotel() != null &&
                    bookingResponse.getData().getHotel().getLockVendorModel().getLockVendor() != null &&
                    bookingResponse.getData().getHotel().getLockVendorModel().getLockVendor().getTitle() != null) {
                String manufacturer = bookingResponse.getData().getHotel().getLockVendorModel().getLockVendor().getTitle().toUpperCase();
                Utilities.getInstance().saveValue(Constants.MANUFACTURER, manufacturer, context);
            }

            if (bookingResponse.getData().getGuest() != null &&
                    bookingResponse.getData().getGuest().getPhone() != null) {
                // save it locally
                String phoneNumber = bookingResponse.getData().getGuest().getPhone();
                phoneNumber = phoneNumber.replace("+", "");
                Utilities.getInstance().saveValue(Constants.UNIQUE_NUMBER, phoneNumber, context);
            }

            if (bookingResponse.getData().getMobileKeyStatus() != null)
                Utilities.getInstance().saveValue(Constants.MOBILE_KEY_STATUS,
                        bookingResponse.getData().getMobileKeyStatusId(), context);

        }
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /*
     * Getting the key from server
     * */
    @SuppressWarnings("unchecked")
    public static void getMobileKey(final Context context, final Callback callback) {

        if (context == null)
            return;

        Services services = Utilities.getInstance().getRetrofit(context).create(Services.class);
        final String tokenStr = Utilities.getInstance().getValue(Constants.AUTH_SIGNATURE, "", context);
        services.getMobileKey(TOKEN + tokenStr).enqueue(new Callback<MobileKeyResponse>() {
            @Override
            public void onResponse(Call<MobileKeyResponse> call, retrofit2.Response<MobileKeyResponse> response) {

                if (response != null && response.body() != null && response.body().getData() != null
                        && response.body().getData().size() > 0 &&
                        response.body().getData().get(0).getMobileKey() != null) {
                    String key = response.body().getData().get(0).getMobileKey();
                    Utilities.getInstance().saveValue(Constants.MOBILE_KEY, key, context);
                } else {
                    Utilities.getInstance().saveValue(Constants.MOBILE_KEY, "", context);
                }

                Log.e("onResponse", ":MobileKey");
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<MobileKeyResponse> call, Throwable t) {
                Log.e("onFailure", ":MobileKey: " + t.getMessage());

                Utilities.getInstance().saveValue(Constants.MOBILE_KEY, "", context);
                callback.onFailure(call, t);
            }
        });
    }
    //-----------------------------------------------------------------------------------------------------------------|


    /**
     * @param context application's context
     */
    public static void logSDK(final Context context, int isDoorOpened) {

        if (context == null)
            return;

        Services services = Utilities.getInstance().getRetrofit(context).create(Services.class);
        final String tokenStr = Utilities.getInstance().getValue(Constants.AUTH_SIGNATURE, "", context);

        services.logSDK(TOKEN + tokenStr, new SdkLogRequest("door-opened",
                isDoorOpened)).enqueue(new Callback<LogActionResponse>() {
            @Override
            public void onResponse(Call<LogActionResponse> call, retrofit2.Response<LogActionResponse> response) {
                Log.e("OnResponse", "Lock Opened Successfully");
            }

            @Override
            public void onFailure(Call<LogActionResponse> call, Throwable t) {
                Log.e("onFailure", "Lock Opened Failed");
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------|


    /**
     * Update the status on server that Peronalization(Device is ready to get key from server) has been completed
     */
    public static void setPeronalizationComplete(final Context mContext, final OpenKeyCallBack openKeyCallBack) {

        if (mContext == null)
            return;

        Services services = Utilities.getInstance().getRetrofit(mContext).create(Services.class);
        final String tokenStr = Utilities.getInstance().getValue(Constants.AUTH_SIGNATURE, "", mContext);
        services.setPeronalizationComplete(TOKEN + tokenStr).enqueue(new Callback<PersonlizationResponse>() {
            @Override
            public void onResponse(Call<PersonlizationResponse> call, retrofit2.Response<PersonlizationResponse> response) {
                if (response.isSuccessful()) {

                    PersonlizationResponse personlizationResponse = response.body();
                    if (personlizationResponse != null && personlizationResponse.getData() != null
                            && personlizationResponse.getData().getKeyIssued())
                        openKeyCallBack.initializationSuccess();
                    else
                        openKeyCallBack.isKeyAvailable(false, Response.FETCH_KEY_FAILED);

                    Log.e(TAG, "Personalization Status updated on server");
                } else if (response.code() == 403) {
                    openKeyCallBack.initializationFailure("403");
                } else {
                    // tell user, startSetup is success
                    openKeyCallBack.initializationFailure(Response.INITIALIZATION_FAILED);
                    Log.e(TAG, "Personalization failed");
                }
            }

            @Override
            public void onFailure(Call<PersonlizationResponse> call, Throwable t) {
                openKeyCallBack.initializationFailure(Response.INITIALIZATION_FAILED);
                Log.e(TAG, "Personalization Status failed to update on server");
            }
        });
    }


    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * @param context
     * @param callback
     */
    @SuppressWarnings("unchecked")
    public static void getBooking(String authToken, final Context context, final Callback callback) {

        if (!(authToken != null && authToken.length() > 0 && context != null))
            return;

        Services services = Utilities.getInstance().getRetrofit(context).create(Services.class);
        services.getSession(TOKEN + authToken).enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, retrofit2.Response<SessionResponse> response) {
                if (response.isSuccessful())
                    saveData(response.body(), context);
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------|

    /**
     * Update status on server once device get key.
     *
     * @param context
     * @param status
     */

    public static void setKeyStatus(final Context context, String status) {
        if (context == null)
            return;

        Services services = Utilities.getInstance().getRetrofit(context).create(Services.class);
        final String tokenStr = Utilities.getInstance().getValue(Constants.AUTH_SIGNATURE, "", context);
        services.setKeyStatus(TOKEN + tokenStr, new KeyStatusRequest(status)).enqueue(new Callback<KeyStatusResponse>() {
            @Override
            public void onResponse(Call<KeyStatusResponse> call, retrofit2.Response<KeyStatusResponse> response) {
                Log.e("onResponse", "onResponse");
                Utilities.getInstance().saveValue(Constants.MOBILE_KEY_STATUS,
                        3, context);
            }

            @Override
            public void onFailure(Call<KeyStatusResponse> call, Throwable t) {
                Log.e("onFailure", "onFailure" + t.getMessage());
            }
        });
    }
    //-----------------------------------------------------------------------------------------------------------------|

}
