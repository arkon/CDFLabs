# http://developer.android.com/guide/developing/tools/proguard.html

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson-specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class me.echeung.cdflabs.labs.** { *; }
-keep class me.echeung.cdflabs.printers.** { *; }

##---------------End:   proguard configuration for Gson  ----------


##---------------Begin: proguard configuration for jsoup  ----------
-keeppackagenames org.jsoup.nodes
##---------------End:   proguard configuration for jsoup  ----------


##---------------Begin: proguard configuration for support lib  ----------
-keep class android.support.v7.widget.** {*;}
##---------------End:   proguard configuration for support lib  ----------