# phone_selector

A flutter plugin to invoke the PhoneSelector API for Android

#### A Flutter Widget to make interactive Progress Timeline.

This Library can be used to invoke the Phone selector API on Android Devices.

This library does not support IOS.

![](demo.gif)

## Installation

To use this package:

Add the following to your `pubspec.yaml` file:

```yaml
dependencies:
    phone_selector: ^1.0.5
```

## How to Use

Import the library
```dart
import 'package:phone_selector/phone_selector.dart';
```

Call the `callPhoneSelector` method of `PhoneSelector` class.

```dart
.
.
_getPhoneNumber() async {
    String phoneNumber;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      phoneNumber = await PhoneSelector.callPhoneSelector();
      print(phoneNumber);
    } on PlatformException {
      print('Failed to get Phone Number.');
    }
  }
.
.
.
}
```

## Possible Results

### Non Empty String
Get a Non Empty String if user selects one of the phone numbers.

### Empty String
Get an empty String if the user dismisses the Dialog.

### PlatformException
Get a PlatformException if the user selects "NONE OF THE ABOVE"
