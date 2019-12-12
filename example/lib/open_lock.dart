import 'package:flutter/material.dart';
import 'package:flutter_plugin_update/flutter_plugin_update.dart';

class OpenLock extends StatefulWidget {
  @override
  _OpenLockState createState() => _OpenLockState();
}

Future<void> initialize() async {
  String platformVersion =
      await FlutterPluginUpdate.init("45144534-f181-4011-b142-5d53162a95c8");
}

Future<void> openDoor() async {
  String platformVersion = await FlutterPluginUpdate.openLock("103");
}

Future<void> autheticate() async {
  String platformVersion = await FlutterPluginUpdate.authenticate(
      "bvs4nukfand5j4aorsanz4543mfr6u5opavjote5zb3ncqpxxt4c3ofbdtgfmcdq");
}

class _OpenLockState extends State<OpenLock> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: new Container(
        alignment: Alignment.center,
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            new SizedBox(
              height: 30.0,
            ),
            InkWell(
              onTap: () {
                initialize();
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
            )
          ],
        ),
      ),
    );
  }
}
