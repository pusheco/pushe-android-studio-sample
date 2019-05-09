# Android Studio Sample for Pushe.co

Simple implementation of [Pushe](http://pushe.co) SDK using Android studio and Java.

You can see other samples:

* [Unity](https://github.com/ronashco/pushe-unity-sample)
* [Basic4Android](https://github.com/ronashco/pushe-ba4-sample)
* [Eclipse](https://github.com/ronashco/pushe-eclipse-sample)

#### Installation
```groovy
dependencies {
   implementation 'co.ronash.android:pushe-base:1.6.3' // Or compile for lower gradles
}

```
#### AndroidManifest.xml

Go to [Pushe console](https://console.pushe.co) and get the manifest content and add it to your project `AndroidManifest.xml`


#### MainActivity.java

In your activity add this to `onCreate()` method:

```java
Pushe.initialize(this, true);
```

Now run and install your app on a device or emulator that has google-play-service installed.
Pushe needs minimum android api=15 and google-play-service version >= 3 to run.

## More Info
For detailed documentations visit http://pushe.co/docs


## Support 
#### Email:
If you have any problem, please contact us using this email, we will get back to you right away:
`support [at] pushe.co`


