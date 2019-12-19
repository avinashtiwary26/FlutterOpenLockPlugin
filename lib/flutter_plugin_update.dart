import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginUpdate {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugin_update');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }


  static Future<dynamic> init(String uuid) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("UUID", () => uuid);
    await _channel.invokeMethod('openkeysdkinit', args);
    return null;
  }

  static Future<dynamic> authenticate(String token) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("token", () => token);
    var data = await _channel.invokeMethod('openkeysdkauthenticate', args);
    return data;
  }

  static Future<dynamic> sdkInitialize() async {
    var data = await _channel.invokeMethod('openkeysdkinitialize');
    return data;
  }


  static Future<dynamic> getKey() async {
    var data = await _channel.invokeMethod('openkeysdkgetkey');
    return data;
  }

  static Future<dynamic> openLock(String roomTitle) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("room", () => roomTitle);
    var data = await _channel.invokeMethod('openkeysdkstartscanning', args);
    return data;
  }


  static Future<dynamic> isKeyAvailabe() async {
    var data = await _channel.invokeMethod('isKeyAvailabe');
    return data;
  }
}
