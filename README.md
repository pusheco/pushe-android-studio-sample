# Android Studio Sample for Pushe.co

This is a sample application (made by Android Studio), which is powered by [Pushe](http://pushe.co) push notification service.

You can see other samples:
* [Eclipse](https://github.com/ronashco/pushe-eclipse-sample)
* [Unity](https://github.com/ronashco/pushe-unity-sample)
* [Basic4Android](https://github.com/ronashco/pushe-b4a-sample)

To see our demo app or its wiki page you may take a look at:
* [Download app from Google Play](https://play.google.com/store/apps/details?id=co.ronash.pushesample)
* [Github Repository](https://github.com/ronashco/pushe-sample)

## Pushe Installation in an Android Studio Project
You have to add some codes to these files:

* build.gradle
* AndroidManifest.xml
* MainActivity.java

#### build.gradle
```
dependencies {
   compile 'co.ronash.pushe:android-lib:0.8.3'
   compile 'com.google.android.gms:play-services-gcm:7.5.0'
}
```

#### AndroidManifest.xml

Add contents of your downloaded `CopyToManifestForAndroidStudio.xml` to your `AndroidManifest.xml`.

#### Add it to your Activity (e.g. MainActivity.java)

Add `import co.ronash.pushe.Pushe` to your activity.

Add the folowing in your onCreate() method:

`Pushe.initialize(this, true);`

## More Info
For detailed documentations visit http://docs.pushe.co


## Support 
#### Email:
support [at] pushe.co
#### Phone:
+98-21-44668276 (8:00 to 17:00 Sat-Thu)
