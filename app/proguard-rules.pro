# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\echeung\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }

-keep public class com.google.code.gson.** {
    public *;
}

-keep public class org.jsoup.** {
    public *;
}
