import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginUpdate {
  static const MethodChannel _channel =
      const MethodChannel('flutter_plugin_update');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Null> init(String uuid) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("UUID", () => uuid);
    await _channel.invokeMethod('openkeysdkinit', args);
    return null;
  }

  static Future<Null> authenticate(String token) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("token", () => token);
    await _channel.invokeMethod('openkeysdkauthenticate', args);
    return null;
  }

  static Future<Null> openLock(String roomTitle) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent("room", () => roomTitle);
    await _channel.invokeMethod('openkeysdkstartscanning', args);
    return null;
  }
}
