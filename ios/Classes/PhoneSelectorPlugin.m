#import "PhoneSelectorPlugin.h"
#if __has_include(<phone_selector/phone_selector-Swift.h>)
#import <phone_selector/phone_selector-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "phone_selector-Swift.h"
#endif

@implementation PhoneSelectorPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPhoneSelectorPlugin registerWithRegistrar:registrar];
}
@end
