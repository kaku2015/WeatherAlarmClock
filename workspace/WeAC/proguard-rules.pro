# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\ProgramFiles\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#在proguard-rules.pro中加入以下代码，基本可以涵盖所有

#-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
#-dontskipnonpubliclibraryclasses    # 是否混淆第三方jar/不去忽略非公共的库类
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
#优化  不优化输入的类文件
#-dontoptimize
#保护注解/会保留Activity的被@Override注释的方法onCreate、onDestroy方法等。
#-keepattributes *Annotation*

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

# 将.class信息中的类名重新定义为"Proguard"字符串
-renamesourcefileattribute Proguard
# 并保留源文件名为"Proguard"字符串，而非原始的类名 并保留行号 // blog from sodino.com
-keepattributes SourceFile,LineNumberTable

# 只保留类名，类名不会混淆重命名，但是其成员和方法会混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#     <init>;#匹配所有构造函数
#     <fields>;#匹配所有成员
#     <methods>;#匹配所有方法

# -keepclasseswithmembers 保护指定的类和类的成员
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# -keepclassmembers 保护指定类的成员
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

#声明第三方jar包,不用管第三方jar包中的.so文件(如果有)
#-libraryjars libs/litepal-1.3.0.jar

#不混淆第三方jar包中的类 保留第三方jar包的所有类及其成员和方法
-keep class org.litepal.** {*;}
-keep class com.baidu.location.** {*;}
-keep class u.upd.** {*;}
-keep class com.google.zxing.** {*;}

-dontwarn org.apache.**
-keep class okio.**{*;}
-dontwarn okio.**
-keep class com.makeramen.roundedimageview.**{*;}
-dontwarn com.makeramen.roundedimageview.**
-keep class com.handmark.pulltorefresh.**{*;}
-dontwarn com.handmark.pulltorefresh.**
-dontwarn com.squareup.leakcanary.**

# litepal
-keep public class * extends org.litepal.crud.DataSupport{*;}     # 保留类及其所有成员不被混淆

# otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# 友盟
-keep class com.umeng.**{*;}
-dontwarn com.umeng.**
-keep class org.android.agoo.* {*;}
-dontwarn org.android.agoo.**

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.kaku.weac.R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}