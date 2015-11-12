# Android Studio Sample for Pushe.co

This is a sample application (in Android Studio) powered by [Pushe](http://pushe.co) push notification service.

You can see other samples:
* [Eclipse](https://github.com/ronashco/pushe-eclipse-sample)
* [Unity (coming soon)](https://github.com/ronashco/pushe-unity-sample)
* [Basic4Android (coming soon)](https://github.com/ronashco/pushe-ba4-sample)

For a complete demo app and Wiki you may **Pushe Android Demo**
* [pushe-android-demo on github](https://github.com/ronashco/pushe-android-demo)
* [Pushe Demo App on Google Play](http://pushe.co/static/images/googleplay-logo.png)

## Installation 
You have to add some lines to these files:

* build.gradle
* AndroidManifest.xml
* MainActivity.java

#### build.gradle
```
dependencies {
   compile 'co.ronash.pushe:android-lib:0.8.1'
   compile 'com.google.android.gms:play-services-gcm:7.5.0'
}

```
#### AndroidManifest.xml

Add contants of your downloaded `CopyToManifestForAndroidStudio.xml` to your `AndroidManifest.xml`.


#### MainActivity.java

`import co.ronash.pushe.Pushe`

Add the folowing in your onCreate() method

`Pushe.initialize(this, true);`

## More Info
For detailed documentations visit http://docs.pushe.co


## Support 
#### Email:
support [at] pushe.co
#### Phone:
+98-21-44668276 (8:00 to 17:00 Sat-Thu)




