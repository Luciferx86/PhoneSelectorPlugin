# phone_selector

### A flutter plugin to invoke the PhoneSelector API for Android

 ![pub package](https://img.shields.io/pub/v/phone_selector.svg)  ![pub points](https://badges.bar/phone_selector/pub%20points)  ![popularity](https://badges.bar/phone_selector/popularity) ![Issues](https://img.shields.io/github/issues/Luciferx86/PhoneSelectorPlugin)  ![Stars](https://img.shields.io/github/stars/Luciferx86/PhoneSelectorPlugin) ![License](https://img.shields.io/github/license/Luciferx86/PhoneSelectorPlugin) 


This Library can be used to invoke the Phone selector API on Android Devices.

This library does not support IOS.

<img src="https://raw.githubusercontent.com/Luciferx86/PhoneSelectorPlugin/main/screenshot.png"/>

## Installation

To use this package:

Add the following to your `pubspec.yaml` file:

```yaml
dependencies:
    phone_selector: ^2.0.7
```

## How to Use

Import the library
```dart
import 'package:phone_selector/phone_selector.dart';
```

Call the `getPhoneNumber` method of `PhoneSelector` class.

```dart
.
.
_getPhoneNumber() async {
    String phoneNumber;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      phoneNumber = await PhoneSelector.getPhoneNumber();
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
