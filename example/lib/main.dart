import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:phone_selector/phone_selector.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _phoneNumber = '';

  @override
  void initState() {
    super.initState();
  }

  _getPhoneNumber() async {
    String phoneNumber;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      phoneNumber = await PhoneSelector.callPhoneSelector;
      print(phoneNumber);
    } on PlatformException {
      phoneNumber = 'Failed to get Phone Number.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (mounted) {
      setState(() {
        _phoneNumber = phoneNumber;
      });
    }
  }

  // Platform messages are asynchronous, so we initialize in an async method.

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SizedBox(
                height: 40,
              ),
              Text(
                "Phone Number",
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 30),
              ),
              SizedBox(
                height: 20,
              ),
              Text(
                _phoneNumber,
                style: TextStyle(fontSize: 20),
              ),
              SizedBox(
                height: 40,
              ),
              RaisedButton(
                onPressed: () {
                  _getPhoneNumber();
                },
                child: Text("Get Phone Number"),
              )
            ],
          ),
        ),
      ),
    );
  }
}
