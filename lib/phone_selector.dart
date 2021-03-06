import 'dart:async';

import 'package:flutter/services.dart';

class PhoneSelector {
  /// MethodChannel to invoke native methods.
  static const MethodChannel _channel =
      const MethodChannel('luciferx86.flutter.plugins.phoneselector');

  /// Method used to call the Phone Selector API in Android
  static Future<String?> getPhoneNumber() async {
    final String? version = await _channel.invokeMethod('callPhoneSelector');
    return version;
  }
}
