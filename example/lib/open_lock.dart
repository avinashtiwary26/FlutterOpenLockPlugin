import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_plugin_update/flutter_plugin_update.dart';

class OpenLock extends StatefulWidget {

  @override
  _OpenLockState createState() => _OpenLockState();
}


class _OpenLockState extends State<OpenLock> {

  var buffer = new StringBuffer();
  bool sdkInitialize = false;

  Future<void> initialize() async {
    String platformVersion =
    await FlutterPluginUpdate.init("45144534-f181-4011-b142-5d53162a95c8");
  }

  Future<dynamic> openDoor() async {
    buffer.write("Open Door Process......");
    buffer.write("\n");
    _StreamController.add(buffer.toString());
    var openDoorData = await FlutterPluginUpdate.openLock("103");

    if (openDoorData is Map) {
      if (openDoorData.containsKey("isLockOpened")) {
        bool opnDoor = openDoorData["isLockOpened"];

        if (opnDoor) {
          buffer.write("Open Door suceess");
          buffer.write("\n");
          _StreamController.add(buffer.toString());
        }
        else {
          buffer.write("Open Door failed");
          buffer.write("\n");
          _StreamController.add(buffer.toString());
        }
      }
      else if (openDoorData.containsKey("Initialize")) {
        bool initialize = openDoorData["Initialize"];
        if (!initialize) {
          buffer.write("SDK Initialization Failure.....Try Again for get key");
          buffer.write("\n");
          _StreamController.add(buffer.toString());
        }
        else {
          buffer.write(
              "Something went wrong,Please try again all process for get key");
          buffer.write("\n");
          _StreamController.add(buffer.toString());
        }
      }
      else {
        buffer.write(
            "Something went wrong,Please try again all process for get key");
        buffer.write("\n");
        _StreamController.add(buffer.toString());
      }
    }
  }


  Future<dynamic> GetKey() async {
    var isKeyAvailabeData = await FlutterPluginUpdate.isKeyAvailabe();

    if (isKeyAvailabeData is Map) {
      if (isKeyAvailabeData.containsKey("isKeyAvailabe")) {
        bool isKey = isKeyAvailabeData["isKeyAvailabe"];
        if (isKey) {
          var getKeyData = await FlutterPluginUpdate.getKey();
          if (getKeyData is Map) {
            if (getKeyData.containsKey("keyavailable")) {
              if (getKeyData.containsKey("roomList")) {
                buffer.write("Get Room List suceess");
                buffer.write("\n");
                _StreamController.add(buffer.toString());
              }
              bool fetchKey = getKeyData["keyavailable"];
              if (fetchKey) {
                buffer.write("fetch Key suceess");
                buffer.write("\n");
                _StreamController.add(buffer.toString());
              }
              else {
                buffer.write("fetch Key failed");
                buffer.write("\n");
                _StreamController.add(buffer.toString());
              }
            } else if (getKeyData.containsKey("Initialize")) {
              bool initialize = getKeyData["Initialize"];
              if (!initialize) {
                buffer.write(
                    "SDK Initialization Failure.....Try Again for get key");
                buffer.write("\n");
                _StreamController.add(buffer.toString());
              }
              else {
                buffer.write(
                    "Something went wrong,Please try again all process for get key");
                buffer.write("\n");
                _StreamController.add(buffer.toString());
              }
            }
            else {
              buffer.write(
                  "Something went wrong,Please try again all process for get key");
              buffer.write("\n");
              _StreamController.add(buffer.toString());
            }
          }
        } else {
          buffer.write(
              "Key not available");
          buffer.write("\n");
          _StreamController.add(buffer.toString());
        }
      }
    }
  }

  Future<dynamic> autheticate() async {
    buffer.write("Authentication....");
    buffer.write("\n");
    _StreamController.add(buffer.toString());
    bool authSucess = false;
    var data = await FlutterPluginUpdate.authenticate(
        "hon3kaoih6qt3kijy2xlqnovtqsa74cxdh6yymchz6vgoafnoxl6vd56skurtzsu");
    if (data is Map) {
      print("calll ${data.containsKey("authentication")}");

      if (data.containsKey("authentication")) {
        authSucess = data["authentication"];
        print("authresult $authSucess");
      }
      else {
        authSucess = false;
        print("authfalse");
      }

      if (authSucess) {
        buffer.write("authetication suceess");
        buffer.write("\n");
        _StreamController.add(buffer.toString());
        buffer.write("SDK Initialization...");
        buffer.write("\n");
        _StreamController.add(buffer.toString());

        var initializeData = await FlutterPluginUpdate.sdkInitialize();

        if (initializeData is Map) {
          sdkInitialize = false;
          if (initializeData.containsKey("Initialize")) {
            var
            sdkInitialize = initializeData["Initialize"];
            if (sdkInitialize) {
              buffer.write("SDK Initialization Success");
              buffer.write("\n");
              _StreamController.add(buffer.toString());
              buffer.write("Please Start Get Key Process");
              buffer.write("\n");
              _StreamController.add(buffer.toString());
            }
            else {
              buffer.write(
                  "SDK Initialization Failure.....Try Again for get key");
              buffer.write("\n");
              _StreamController.add(buffer.toString());
            }
          }
          else if (initializeData.containsKey("keyavailable")) {
            bool keyAvailable = data["keyavailable"];
            if (keyAvailable) {
              buffer.write(
                  "You have already a key for open door, please open door with this key");
              buffer.write("\n");
              _StreamController.add(buffer.toString());
            }
            else {
              buffer.write("Something went wrong, please try again later");
              buffer.write("\n");
              _StreamController.add(buffer.toString());
            }
          }
          else {
            sdkInitialize = false;
            buffer.write(
                "SDK Initialization Failure.....Try Again for get key");
            buffer.write("\n");
            _StreamController.add(buffer.toString());
          }
        }
      }
      else {
        buffer.write("authetication failure");
        buffer.write("\n");
        _StreamController.add(buffer.toString());
      }
      print(data.values);
    }
    else {
      print("answerr");
    }
  }

  final StreamController<String> _StreamController =
  new StreamController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: new Container(
        alignment: Alignment.topCenter,
        child: new Column(

          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            new SizedBox(
              height: 25.0,
            ),

            StreamBuilder<String>(
              stream: _StreamController.stream,
              builder: (context, snapshot) {
                return Expanded(
                  flex: 1,
                  child: SingleChildScrollView(
                    child: Text("${snapshot.hasData ? snapshot.data : ""}",
                      style: new TextStyle(
                          color: Colors.black,
                          fontSize: 18.0,
                          fontWeight: FontWeight.bold
                      ),),
                  ),
                );
              },

            ),
            new SizedBox(
              height: 15.0,
            ),

            InkWell(
              onTap: () {
                buffer.write("Initialize...");
                buffer.write("\n");
                _StreamController.add(buffer.toString());
                initialize();

                var duration = new Duration(
                  milliseconds: 800,
                );
                new Timer(duration, () {
                  buffer.write("Initialize Success");
                  buffer.write("\n");
                  _StreamController.add(buffer.toString());
                });
              },

              child: new Container(
                width: 100.0,
                height: 50.0,
                color: Colors.black,
                alignment: Alignment.center,
                child: new Text(
                  "Initilaize",
                  style: new TextStyle(color: Colors.white),
                ),
              ),
            ),
            new SizedBox(
              height: 30.0,
            ),
            InkWell(

              onTap: () {
                autheticate();
              },
              child: new Container(
                width: 100.0,
                height: 50.0,
                color: Colors.black,
                alignment: Alignment.center,
                child: new Text(
                  "Authenticate",
                  style: new TextStyle(color: Colors.white),
                ),
              ),
            ),
            new SizedBox(
              height: 30.0,
            ),
            InkWell(
              onTap: () {
                if (sdkInitialize) {
                  buffer.write("Fetch Key...");
                  buffer.write("\n");
                  _StreamController.add(buffer.toString());
                  GetKey();
                }
                else {
                  buffer.write("SDK Initialization Failed");
                  buffer.write("\n");
                  _StreamController.add(buffer.toString());
                }
              },
              child: new Container(
                width: 100.0,
                height: 50.0,
                color: Colors.black,
                alignment: Alignment.center,
                child: new Text(
                  "Get Key",
                  style: new TextStyle(color: Colors.white),
                ),
              ),
            ),


            new SizedBox(
              height: 30.0,
            ),
            InkWell(
              onTap: () {
                openDoor();
              },
              child: new Container(
                width: 100.0,
                height: 50.0,
                color: Colors.black,
                alignment: Alignment.center,
                child: new Text(
                  "OpenLock",
                  style: new TextStyle(color: Colors.white),
                ),
              ),
            ),

            new SizedBox(
              height: 30.0,
            ),
          ],
        ),
      ),
    );
  }
}
