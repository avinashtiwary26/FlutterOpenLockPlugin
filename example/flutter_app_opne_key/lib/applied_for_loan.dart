import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ApplyForLoan extends StatefulWidget {
  @override
  _ApplyForLoanState createState() => _ApplyForLoanState();
}

class _ApplyForLoanState extends State<ApplyForLoan> {
  TextEditingController _AmountController = new TextEditingController();
  TextEditingController _SecurityController = new TextEditingController();

  ValueNotifier<bool> monthNotifier = new ValueNotifier(false);
  ValueNotifier<bool> dayNotifier = new ValueNotifier(false);
  ValueNotifier<bool> weekNotifier = new ValueNotifier(false);
  ValueNotifier<bool> yearNotifier = new ValueNotifier(false);
  ValueNotifier<bool> marriedNotifier = new ValueNotifier(false);
  ValueNotifier<bool> singleNotifier = new ValueNotifier(false);

  FocusNode _AmountField = new FocusNode();
  FocusNode _SecurityField = new FocusNode();
  LinearGradient linearGradient = new LinearGradient(
    colors: [Colors.grey, Colors.grey],
  );

  LinearGradient linearGradientChecked = new LinearGradient(
    colors: [Colors.blue, Colors.green],
  );
  var _valueDays = false;
  var _valueWeeks = false;
  var _valueMonths = false;
  var _valueYear = false;
  var _valueSingle = false;
  var _valueMarried = false;

  List<TextInputFormatter> listNormal = new List<TextInputFormatter>();

  @override
  Widget build(BuildContext context) {
    var screenSize = MediaQuery.of(context).size;
    return Scaffold(
      appBar: new AppBar(
        leading: new Icon(
          Icons.keyboard_backspace,
          color: Colors.black,
          size: 30.0,
        ),
        backgroundColor: Colors.white,
        centerTitle: true,
        title: new Text(
          "Sample App",
          style: new TextStyle(color: Colors.black, fontSize: 20.0),
        ),
      ),
      body: new SingleChildScrollView(
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
                margin: new EdgeInsets.only(left: 50.0, right: 50.0, top: 20.0),
                alignment: Alignment.center,
                child: new Text(
                  "Get the most competitive rates for loans and the most secured money",
                  style: new TextStyle(color: Colors.black54),
                  textAlign: TextAlign.center,
                )),
            new SizedBox(
              height: 20.0,
            ),
            Container(
              margin: new EdgeInsets.only(left: 20.0, right: 20.0),
              child: getTextField(
                  "Amount Desired",
                  validatorAmount,
                  _AmountController,
                  _AmountField,
                  _AmountField,
                  false,
                  TextInputType.text,
                  listNormal),
            ),
            new SizedBox(
              height: 30.0,
            ),
            Container(
                margin: new EdgeInsets.only(left: 20.0),
                child: new Text(
                  "Payback Time",
                  style: new TextStyle(
                      color: Colors.grey,
                      fontSize: 19.0,
                      fontWeight: FontWeight.bold),
                )),
            new SizedBox(
              height: 10.0,
            ),
            Container(
              margin: new EdgeInsets.only(top: 20, left: 20.0, right: 20.0),
              child: new Wrap(
                children: <Widget>[
                  buildCheckBox(
                      title: "Days",
                      offstage: false,
                      type: 0,
                      changeNotifier: dayNotifier),
                  buildCheckBox(
                      title: "Weeks",
                      offstage: false,
                      type: 1,
                      changeNotifier: weekNotifier),
                  buildCheckBox(
                      title: "Months",
                      offstage: false,
                      type: 2,
                      changeNotifier: monthNotifier),
                  buildCheckBox(
                      title: "Years",
                      offstage: false,
                      type: 3,
                      changeNotifier: yearNotifier),
                ],
              ),
            ),
            new SizedBox(
              height: 35.0,
            ),
            Container(
                margin: new EdgeInsets.only(left: 20.0),
                child: new Text(
                  "Maritial Status",
                  style: new TextStyle(
                      color: Colors.grey,
                      fontSize: 19.0,
                      fontWeight: FontWeight.bold),
                )),
            new SizedBox(
              height: 10.0,
            ),
            Container(
              margin: new EdgeInsets.only(top: 10, left: 20.0, right: 20.0),
              child: new Wrap(
                children: <Widget>[
                  buildCheckBox(
                      title: "Single",
                      offstage: false,
                      type: 4,
                      changeNotifier: singleNotifier),
                  buildCheckBox(
                      title: "Married",
                      offstage: false,
                      type: 5,
                      changeNotifier: marriedNotifier),

                  /*     getCheckBox("Single",_valueSingle,true,4),
                  getCheckBox("Married",_valueMarried,true,5)*/
                ],
              ),
            ),
            new SizedBox(
              height: 20.0,
            ),
            Container(
              margin: new EdgeInsets.only(left: 20.0, right: 20.0),
              child: getTextField(
                  "Collateral Security",
                  validatorSecurity,
                  _SecurityController,
                  _SecurityField,
                  _SecurityField,
                  false,
                  TextInputType.text,
                  listNormal),
            ),
            new SizedBox(
              height: 20.0,
            ),
            new Container(
                height: 100.0,
                width: screenSize.width,
                margin: new EdgeInsets.only(left: 45.0, right: 45.0),
                decoration: new BoxDecoration(
                    shape: BoxShape.rectangle,
                    borderRadius: new BorderRadius.circular(4.0),
                    border: new Border.all(
                        color: Colors.grey.withOpacity(0.5), width: 1.0),
                    color: Colors.black12),
                child: new Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    new Icon(
                      Icons.image,
                      size: 40.0,
                      color: Colors.black38,
                    ),
                    new SizedBox(
                      height: 10.0,
                    ),
                    new Text("Upload Picture of Collateral Security")
                  ],
                )),
            new SizedBox(
              height: 20.0,
            ),
          ],
        ),
      ),
    );
  }

  Widget buildCheckBox({
    String title,
    bool offstage,
    int type,
    ValueNotifier<bool> changeNotifier,
  }) {
    return Container(
      width: MediaQuery.of(context).size.width * .44,
      padding: EdgeInsets.all(8.0),
      child: new Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Container(
              child: InkWell(
            onTap: () {
              bool currentStatus = changeNotifier.value;
              currentStatus = !currentStatus;
              changeNotifier.value = currentStatus;

              if (changeNotifier == singleNotifier) {
                if (singleNotifier.value) {
                  marriedNotifier.value = false;
                }
              } else if (changeNotifier == marriedNotifier) {
                if (marriedNotifier.value) {
                  singleNotifier.value = false;
                }
              }
            },
            child: ValueListenableBuilder(
              valueListenable: changeNotifier,
              builder: (context, value, _) {
                return Container(
                  decoration: BoxDecoration(
                      shape: BoxShape.rectangle,
                      gradient: value ? linearGradientChecked : linearGradient,
                      borderRadius: new BorderRadius.circular(5.0)),
                  child: Padding(
                    padding: const EdgeInsets.all(3.0),
                    child: value
                        ? Icon(
                            Icons.check,
                            size: 22.0,
                            color: Colors.white,
                          )
                        : Icon(
                            Icons.check_box_outline_blank,
                            size: 22.0,
                            color: Colors.transparent,
                          ),
                  ),
                );
              },
            ),
          )),
          new SizedBox(
            width: 8.0,
          ),
          Expanded(
            child: Container(
                alignment: Alignment.centerLeft,
                child: new Text(
                  title,
                  style: new TextStyle(
                      color: Colors.grey,
                      fontWeight: FontWeight.bold,
                      fontSize: 18.0),
                )),
          ),
          new SizedBox(
            width: 12.0,
          ),
          Offstage(
            offstage: offstage,
            child: new Container(
              width: 30.0,
              height: 30.0,
              decoration: new BoxDecoration(
                  borderRadius: new BorderRadius.circular(5.0),
                  shape: BoxShape.rectangle,
                  border: new Border.all(color: Colors.grey, width: 2.0)),
            ),
          )
        ],
      ),
    );
  }

  Widget getTextField(
      String labelText,
      Function validators,
      TextEditingController controller,
      FocusNode focusNodeCurrent,
      FocusNode focusNodeNext,
      bool obsectextType,
      TextInputType textType,
      List<TextInputFormatter> list) {
    return new TextFormField(
        validator: validators,
        controller: controller,
        maxLines: 1,
        keyboardType: textType,
        obscureText: obsectextType,
        focusNode: focusNodeCurrent,
        inputFormatters: list,
        style: new TextStyle(fontWeight: FontWeight.bold, color: Colors.black),
        onFieldSubmitted: (value) {},
        decoration: InputDecoration(
            labelText: labelText,
            labelStyle: new TextStyle(
                color: Colors.grey,
                fontWeight: FontWeight.bold,
                fontSize: 14.0),
            border: new UnderlineInputBorder(
                borderSide: new BorderSide(
              color: Colors.black,
            ))));
  }

  String validatorAmount(String value) {
    if (value.isEmpty) {
      return 'Please enter your amout';
    }
  }

  String validatorSecurity(String value) {
    if (value.isEmpty) {
      return 'Please enter your security';
    }
  }
}
