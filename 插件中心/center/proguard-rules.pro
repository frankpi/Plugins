

-repackageclasses 'com.gameassist.plugin'

-optimizationpasses 5
-dontusemixedcaseclassnames
-ignorewarnings                # 抑制警告
#-dontobfuscate
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable


#############    保留入口，不要混淆     #############
-keep class com.gameassist.plugin.center.PluginEntry { public *;}
-keep class com.gameassist.plugin.** { public *;}

#############    保留需要替换的类，不要混淆     #############
-keep class com.ndkdemo.** { *;}
-keep class com.tapjoy.** { *;}

#Log相关全部移除
#-assumenosideeffects class android.util.Log { public *; }


#
##############    标准系统接口类，不用混淆     ###################
-keep public class * extends android.view.View {*;}
-keep class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *;}
-keepclasseswithmembers class * {public <init>(android.content.Context, android.util.AttributeSet, int);}
#-keepclassmembers class * extends java.lang.Enum {
#    <fields>;
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep public class * extends android.app.Activity
-keep public class * extends android.content.ContextWrapper
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.support.v4

