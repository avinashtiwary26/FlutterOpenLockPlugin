#import "FlutterPluginUpdatePlugin.h"
#import <flutter_plugin_update/flutter_plugin_update-Swift.h>

@implementation FlutterPluginUpdatePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterPluginUpdatePlugin registerWithRegistrar:registrar];
}
@end
