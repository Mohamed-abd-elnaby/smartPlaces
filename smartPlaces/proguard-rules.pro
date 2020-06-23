
 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --
-dontwarn com.google.android.gms.internal.zzhu

 -dontwarn org.apache.commons.logging.**

 -dontwarn okhttp3.internal.platform.*

 -dontwarn okio.**

 -dontwarn retrofit2.Platform$Java8

-dontwarn android.support.**

-keep class com.google.android.gms.internal.** { *; }


# Add this global rule
-keepattributes Signature

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class fahmy.smartplaces.entities.** {
  *;
}
-keepattributes Exceptions, Signature, InnerClasses
