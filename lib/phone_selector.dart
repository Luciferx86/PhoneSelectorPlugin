
import 'dart:async';

import 'package:flutter/services.dart';

class PhoneSelector {
  static const MethodChannel _channel =
      const MethodChannel('phone_selector');

  static Future<String> get callPhoneSelector async {
    final String version = await _channel.invokeMethod('callPhoneSelector');
    return version;
  }
}
