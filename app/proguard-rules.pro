# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep line numbers and source file attributes for debugging crash reports
-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,EnclosingMethod,*Annotation*

# For Kotlin Coroutines & Reflection (If used)
-keepclassmembers class * {
    @dalvik.annotation.optimization.FastNative <methods>;
    @dalvik.annotation.optimization.CriticalNative <methods>;
}

# Ignore ProGuard warnings from external libraries like textmate/gson if any
-dontwarn io.github.rosemoe.language.**
-dontwarn json.**
-dontwarn org.json.**

# Maintain full class structures for main Android components
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
