# Android Studio Sample for Pushe.co

This is a sample application (in Android Studio) powered by [Pushe](http://pushe.co) push notification service.

You can see other samples:
* [Eclipse](https://github.com/ronashco/pushe-eclipse-sample)
* [Unity (coming soon)](https://github.com/ronashco/pushe-unity-sample)
* [Basic4Android (coming soon)](https://github.com/ronashco/pushe-ba4-sample)

For a complete demo app and Wiki you may **Pushe Android Demo**
* [pushe-android-demo on github](https://github.com/pusheco/pushe)
* [Pushe Demo App on Google Play](http://pushe.co/static/images/googleplay-logo.png)

## Installation 
You have to add some lines to these files:

* build.gradle
* AndroidManifest.xml
* MainActivity.java

#### build.gradle
```
dependencies {
   compile 'co.ronash.android:pushe-base:1.3.0'
   compile 'com.google.android.gms:play-services-gcm:10.0.+'
}

```
#### AndroidManifest.xml
You need to make an account in our site at `http://panel.pushe.co` and create an application using your
applicationId (or packagename) of your app. After creating your application, copy the content of manifest file
and paste it in your `AndroidManifest.xml`.


#### MainActivity.java

By MainActivity.java, we mean your launcher activity. It may has another name.
`import co.ronash.pushe.Pushe`

Add the following in your onCreate() method

`Pushe.initialize(this, true);`

Now run and install your app on a device or emulator that has google-play-service installed.
Pushe needs minimum android api=9 and google-play-service version >= 3 to run.

## More Info
For detailed documentations visit http://docs.pushe.co


## Support 
#### Email:
If you have any problem, please contact us using this email, we will get back to you right away:
`support [at] pushe.co`
#### Phone:
+98-21-44668276 (8:00 to 17:00 Sat-Thu)




