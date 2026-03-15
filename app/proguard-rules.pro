# Keep all Xposed hook entry points
-keep class dev.simspoof.tiktok.hook.** { *; }
-keep class dev.simspoof.tiktok.config.** { *; }

# Keep Xposed API
-keep class de.robv.android.xposed.** { *; }

# Keep Gson model fields
-keepclassmembers class dev.simspoof.tiktok.config.SpoofConfig {
    <fields>;
    <init>(...);
}

# Generic rules
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn de.robv.android.xposed.**
