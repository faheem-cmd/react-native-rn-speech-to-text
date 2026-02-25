#import "SpeechToTextAndroid.h"

@implementation SpeechToTextAndroid
- (NSNumber *)multiply:(double)a b:(double)b {
    NSNumber *result = @(a * b);

    return result;
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeSpeechToTextAndroidSpecJSI>(params);
}

+ (NSString *)moduleName
{
  return @"SpeechToTextAndroid";
}

@end
