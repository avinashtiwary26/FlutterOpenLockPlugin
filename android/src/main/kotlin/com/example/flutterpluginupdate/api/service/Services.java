package com.example.flutterpluginupdate.api.service;


import com.example.flutterpluginupdate.api.model.KeyStatusRequest;
import com.example.flutterpluginupdate.api.model.SdkLogRequest;
import com.example.flutterpluginupdate.api.response.key_status.KeyStatusResponse;
import com.example.flutterpluginupdate.api.response.logaction.LogActionResponse;
import com.example.flutterpluginupdate.api.response.mobile_key_response.MobileKeyResponse;
import com.example.flutterpluginupdate.api.response.personlization.PersonlizationResponse;
import com.example.flutterpluginupdate.api.response.session.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * @author OpenKey Inc.
 * <p>
 * This will provide the methods to call a web service via retrofit 2.3.0,
 * with proper Api models and responses
 */
public interface Services {

    //V5
//    String key = "45144534-f181-4011-b142-5d53162a95c8";
//    String key = Utilities.getInstance().getValue(Constants.UUID,"",)


    //-----------------------------------------------------------------------------------------------------------------|

    @GET("sdk/v5/sessions")
    Call<SessionResponse> getSession(@Header("Authorization") String Authorization);
    //-----------------------------------------------------------------------------------------------------------------|

    @GET("sdk/v5/sessions/9/session_mobile_keys")
    Call<MobileKeyResponse> getMobileKey(@Header("Authorization") String Authorization);
    //-----------------------------------------------------------------------------------------------------------------|

    @GET("sdk/v5/sessions/setPersonalization.json")
    Call<PersonlizationResponse> setPeronalizationComplete(@Header("Authorization") String Authorization);
    //-----------------------------------------------------------------------------------------------------------------|

    @POST("sdk/v5/sessions/setMobileKeyStatus")
    Call<KeyStatusResponse> setKeyStatus(@Header("Authorization") String Authorization, @Body KeyStatusRequest keyStatusRequest);
    //-----------------------------------------------------------------------------------------------------------------|

    @POST("/sdk/v5/sessions/logAction")
    Call<LogActionResponse> logSDK(@Header("Authorization") String Authorization, @Body SdkLogRequest sdkLogRequest);
    //-----------------------------------------------------------------------------------------------------------------|
}