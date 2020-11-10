import 'dart:async';

import 'package:flutter/services.dart';

class PhoneSelector {
  /// MethodChannel to invoke native methods.
  static const MethodChannel _channel = const MethodChannel('phone_selector');

  /// Method used to call the Phone Selector API in Android
  static Future<String> callPhoneSelector() async {
    final String version = await _channel.invokeMethod('callPhoneSelector');
    return version;
  }
}
