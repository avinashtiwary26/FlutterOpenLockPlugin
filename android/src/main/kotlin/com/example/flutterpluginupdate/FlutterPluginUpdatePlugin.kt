package com.example.flutterpluginupdate

import android.app.Activity
import android.app.Application
import android.util.Log
import com.example.flutterpluginupdate.Utilities.Utilities
import com.example.flutterpluginupdate.api.response.session.SessionResponse
import com.example.flutterpluginupdate.enums.MANUFACTURER
import com.example.flutterpluginupdate.interfaces.OpenKeyCallBack
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

class FlutterPluginUpdatePlugin(var activity: Application) : MethodCallHandler, OpenKeyCallBack, Activity() {
    private var result: Result? = null
    private var type: Int = 0
    private var mAlreadyCall = false
    private var manufacturer: MANUFACTURER? = null


    override fun sessionResponse(sessionResponse: SessionResponse?) {
        Log.e("sessionResponse", "sessionResponse success")
        val map = HashMap<String, Any>()
        map.put("authentication", true)
        if (mAlreadyCall) {
            result?.success(map)
            mAlreadyCall = false
        }
    }

    override fun initializationSuccess() {
        Log.e("initializeSuceess", "initializeSuceess")
        val map = HashMap<String, Any>()
        map.put("Initialize", true)
        if (mAlreadyCall) {
            result?.success(map)
            mAlreadyCall = false
        }
    }


    override fun sessionFailure(errorDescription: String?, errorCode: String?) {
        Log.e("sessionfailure", "sessionfailure");
        val map = HashMap<String, Any>()
        map.put("authentication", false)
        if (mAlreadyCall) {
            result?.success(map)
            mAlreadyCall = false
        }
    }

    override fun initializationFailure(errorDescription: String?) {
        Log.e("Initialize", "Initialize failure");
        val map = HashMap<String, Any>()
        map.put("Initialize", false)
        if (mAlreadyCall) {
            result?.success(map)
            mAlreadyCall = false
        }
    }

    override fun stopScan(isLockOpened: Boolean, description: String?) {
        Log.e("isLockOpened", isLockOpened.toString());
        val map = HashMap<String, Any>()
        map.put("isLockOpened", isLockOpened)
        if (mAlreadyCall) {
            result?.success(map)
            mAlreadyCall = false
        }

    }

    override fun isKeyAvailable(haveKey: Boolean, description: String?) {
        Log.e("KEYAVAILBALEPOPLUGIN", haveKey.toString())
        manufacturer = Utilities.getInstance().getManufacturer(activity.applicationContext, this)

        if (manufacturer == MANUFACTURER.OKC || manufacturer == MANUFACTURER.OKMOBILEKEY || manufacturer == MANUFACTURER.MODULE) {
            if (!haveKey) {
                val map = HashMap<String, Any>()
                map.put("keyavailable", haveKey)
                if (mAlreadyCall) {
                    result?.success(map)
                    mAlreadyCall = false
                }
            }
        } else {
            runOnUiThread {

                if (mAlreadyCall) {
                    val map = HashMap<String, Any>()
                    map.put("keyavailable", haveKey)
                    result?.success(map)
                    Log.e("keyavailable", "calllll")
                    mAlreadyCall = false
                }
            }

        }
    }

    override fun getOKCandOkModuleMobileKeysResponse(availableRooms: ArrayList<String>?) {
        Log.e("getOkOkc", "getOkOkc")
        val map = HashMap<String, Any>()
            map.put("keyavailable", true)
            availableRooms?.let {
                if (it.size > 0) {
                    map.let { map.put("roomList", true) }
                }
            }
            if (mAlreadyCall) {
                result?.success(map)
                mAlreadyCall = false
            }

    }



    override fun onMethodCall(call: MethodCall, result: Result) {
        this.result = result
        mAlreadyCall = true
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")

        } else if (call.method == "openkeysdkinit") {
            val UUID = call.argument<String>("UUID")
            Log.e("UUID", UUID)
            activity.let {
                // OpenKeyManager.getInstance().init(it,"45144534-f181-4011-b142-5d53162a95c8")
                OpenKeyManager.getInstance().init(it, UUID)
            }

            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "openkeysdkauthenticate") {
            val token = call.argument<String>("token")
            Log.e("token", token)
            type = 1
            OpenKeyManager.getInstance().authenticate(token, this, false)

        } else if (call.method == "openkeysdkinitialize") {
            type = 2
            OpenKeyManager.getInstance().initialize(this)
        } else if (call.method == "openkeysdkgetkey") {
            type = 3
            OpenKeyManager.getInstance().getKey(this)
        } else if (call.method == "openkeysdkstartscanning") {
            type = 4
            val roomTitle = call.argument<String>("room")
            Log.e("roomTitle", roomTitle)
            OpenKeyManager.getInstance().startScanning(this, roomTitle)
        } else if (call.method == "isKeyAvailabe") {
            type = 5
            val isKeyAvailabe = OpenKeyManager.getInstance().isKeyAvailable(this)
            val map = HashMap<String, Any>()
            map.put("isKeyAvailabe", isKeyAvailabe)
            if (mAlreadyCall) {
                result?.success(map)
                mAlreadyCall = false
            }
        } else {
            result.notImplemented()
        }
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_plugin_update")
            channel.setMethodCallHandler(FlutterPluginUpdatePlugin(registrar.activity().application))
        }
    }

}
