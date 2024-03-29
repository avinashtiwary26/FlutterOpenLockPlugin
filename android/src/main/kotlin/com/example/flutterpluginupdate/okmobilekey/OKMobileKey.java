package com.example.flutterpluginupdate.okmobilekey;

import android.app.Application;
import android.util.Log;

import com.example.flutterpluginupdate.Utilities.Constants;
import com.example.flutterpluginupdate.Utilities.Response;
import com.example.flutterpluginupdate.Utilities.Utilities;
import com.example.flutterpluginupdate.api.request.Api;
import com.example.flutterpluginupdate.interfaces.OpenKeyCallBack;
import com.openkey.okmobilekeysdk.callbackmodule.OKMobileKeyCallBack;
import com.openkey.okmobilekeysdk.ok_manager.OKMobileKeyManager;

import java.util.ArrayList;

public class OKMobileKey implements OKMobileKeyCallBack {
    private Application mApplication;
    private OpenKeyCallBack openKeyCallBack;


    //-----------------------------------------------------------------------------------------------------------------|
    public OKMobileKey(Application application, OpenKeyCallBack OpenKeyCallBack) {
        this.openKeyCallBack = OpenKeyCallBack;
        this.mApplication = application;
        initialize();
    }


    //-----------------------------------------------------------------------------------------------------------------|

    /*
     * initialize  sdk for OKModule
     *
     * */
    private void initialize() {
        okMobileKeySDKInitialize();
        int mobileKeyStatusId = Utilities.getInstance().getValue(Constants.MOBILE_KEY_STATUS, 0, mApplication);
        Log.e("mobileKeyStatusId", ":" + mobileKeyStatusId);
        Log.e("haveKey()", ":" + haveKey());
        if (haveKey() && mobileKeyStatusId == 3) {
            Log.e("Keystatus ", ":" + mobileKeyStatusId);
            Log.e("mobileKeyStatusId ", "haveKey:" + mobileKeyStatusId);
            openKeyCallBack.isKeyAvailable(true, Response.FETCH_KEY_SUCCESS);
        } else {
            if (mobileKeyStatusId == 1) {
                Log.e("Keystatus ", ":" + mobileKeyStatusId);
                Log.e("mobileKeyStatusId", "is: " + haveKey());
                Api.setPeronalizationComplete(mApplication, openKeyCallBack);
            } else {
                Log.e("Keystatus ", ":" + mobileKeyStatusId);
                openKeyCallBack.initializationSuccess();
            }
        }
    }

    private void okMobileKeySDKInitialize() {
        boolean environmentType = Utilities.getInstance().getValue(Constants.ENVIRONMENT_TYPE, false, mApplication);
        OKMobileKeyManager.Companion.getInstance(mApplication).registerOKMobileKeyModuleCallback(this);
        OKMobileKeyManager.Companion.getInstance(mApplication).OKInit(environmentType);
    }

    /**
     * if device has a key for okmodule
     */
    public boolean haveKey() {
        String key = Utilities.getInstance().getValue(Constants.MOBILE_KEY, "", mApplication);
        return key != null && key.length() > 0;

    }


    /* fetch roomlist from server*/
    public void fetchOkMobileKeyRoomList() {
        String keyToken = Utilities.getInstance().getValue(Constants.MOBILE_KEY, "", mApplication);
        OKMobileKeyManager.Companion.getInstance(mApplication).fetchKeys(keyToken);

    }


    /**
     * start OKModule scanning for open lock when scanning animation on going
     */
    public void startScanning(String title) {
        Log.e("OKModule startScanning", "true");

        try {
            OKMobileKeyManager.Companion.getInstance(mApplication).scanDevices(title);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void scanResult(String msg) {

    }


    @Override
    public void openDoorSuccess(String msg) {
        Log.e("OpenSucess", "called");
        Api.logSDK(mApplication, 1);
        openKeyCallBack.stopScan(true, "");
    }

    @Override
    public void openDoorFailure(String msg) {
        openKeyCallBack.stopScan(false, "");

    }

    @Override
    public void fetchKeySuccess(ArrayList<String> roomList) {

        openKeyCallBack.getOKCandOkModuleMobileKeysResponse(roomList);

    }

    @Override
    public void fetchKeyFailure(String msg) {
//        openKeyCallBack.initializationFailure(msg);
    }


    @Override
    public void initializationFailure() {

    }

    @Override
    public void initializationSuccess() {

    }
}
