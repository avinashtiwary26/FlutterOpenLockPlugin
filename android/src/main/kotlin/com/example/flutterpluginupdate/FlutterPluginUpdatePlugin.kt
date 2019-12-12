package com.example.flutterpluginupdate

import android.app.Application
import android.util.Log
import com.example.flutterpluginupdate.api.response.session.SessionResponse
import com.example.flutterpluginupdate.interfaces.OpenKeyCallBack
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

class FlutterPluginUpdatePlugin(private var openKeyCallBack: OpenKeyCallBack, private var activity: Application) : MethodCallHandler {


    companion object : OpenKeyCallBack {
        override fun sessionResponse(sessionResponse: SessionResponse?) {
            Log.e("successSessionResponse", "successSessionResponse")
            OpenKeyManager.getInstance().initialize(this)
        }

        override fun initializationSuccess() {
            Log.e("initializeSuceess", "initializeSuceess")
            OpenKeyManager.getInstance().getKey(this)

        }

        override fun sessionFailure(errorDescription: String?, errorCode: String?) {
            Log.e("sessionfailure", "sessionfailure");
        }

        override fun initializationFailure(errorDescription: String?) {

        }

        override fun stopScan(isLockOpened: Boolean, description: String?) {
            Log.e("stop", "stopscan");
        }

        override fun isKeyAvailable(haveKey: Boolean, description: String?) {
            Log.e("keyAvaioable", "keyAvaioable")
        }

        override fun getOKCandOkModuleMobileKeysResponse(availableRooms: ArrayList<String>?) {
            Log.e("getOkOkc", "getOkOkc")
        }

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_plugin_update")
            channel.setMethodCallHandler(FlutterPluginUpdatePlugin(this, registrar.activity().application))
        }
    }


    override fun onMethodCall(call: MethodCall, result: Result) {
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
            OpenKeyManager.getInstance().authenticate(token, openKeyCallBack, false)

        } else if (call.method == "openkeysdkinitialize") {
            OpenKeyManager.getInstance().getKey(openKeyCallBack)
        } else if (call.method == "openkeysdkgetkey") {

        } else if (call.method == "openkeysdkstartscanning") {
            val roomTitle = call.argument<String>("room")
            Log.e("roomTitle", roomTitle)
            OpenKeyManager.getInstance().startScanning(openKeyCallBack, roomTitle)
        } else {
            result.notImplemented()
        }
    }

}
