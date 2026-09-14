# Keep OkHttp, Retrofit, and JSON classes
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep our data transfer objects and API model classes
-keep class com.example.data.api.ChatRequest { *; }
-keep class com.example.data.api.ChatResponse { *; }
-keep class com.example.data.model.** { *; }
-keep class org.json.** { *; }


# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
