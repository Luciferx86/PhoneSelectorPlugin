import 'package:flutter/material.dart';
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
      phoneNumber = await PhoneSelector.getPhoneNumber();
      print(phoneNumber);
    } catch(e){
      print(e);
      phoneNumber = 'Failed to get Phone Number.';
    }
    if (mounted) {
      setState(() {
        _phoneNumber = phoneNumber;
      });
    }
  }

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
              ElevatedButton(
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
