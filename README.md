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

The manifest will be a tag like this:

```xml
<meta-data android:name="co.ronash.pushe.token"
            android:value="PUSHE_76583046756" />
```

The value `PUSHE_76583046756` is for demo panel. Replace it with your own token.

And if you need location-based features, add `Location permission` to the manifest as well.


#### MainActivity.java

In your activity add this to `onCreate()` method:

```java
Context context = this.getApplicationContext(); // This is optional. The `initialize` needs a context. Provide it from anywhere you want.
Pushe.initialize(context, true);
```

#### More features

All features are added to the sample. You can check them out.

Now run and install your app on a device or emulator that has google-play-service installed.
Pushe needs minimum android api=15 and google-play-service version >= 3 to run.

## More Info
For detailed documentations visit https://pushe.co/docs/android-studio/


## Contribution

Feel free to add anything you think is suitable to be in this sample.<br>
It does not follow any specific code style. So just read the code a little bit and send a pull request at anytime. We'll be happy :D.

## Support 
#### Email:
If you have any problem, please contact us using this email, we will get back to you right away:
`support [at] pushe.co`


