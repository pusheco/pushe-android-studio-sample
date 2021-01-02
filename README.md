# Pushe for Android native

Simple implementation of [Pushe](http://pushe.co) SDK using Android studio and Java.

#### Run the example

* Install git if you don't have it.
* Run:
`git clone https://github.com/pusheco/android-studio-sample.git`
* Open it with Android studio and run it on your device.

#### Installation on your project


```groovy
dependencies {
   implementation 'co.pushe.plus:base:2.4.1' // Core module + essential
   implementation 'co.pushe.plus:hms:2.4.1' // Huawei PushKit support
   implementation 'co.pushe.plus:inappmessaging:2.4.1' // InAppMessaging
}
```

```groovy
allprojects {
    repositories {
        maven { url 'https://developer.huawei.com/repo/' }
        jcenter()
        // ...
    }
}
```

#### AndroidManifest.xml

In order to run this application follow bellow steps:
1. Signup to [Pushe Console](https://console.pushe.co)
2. Create application
3. Get the manifest content and add it to your project `AndroidManifest.xml`

The manifest content will be a tag like this:

```xml
<meta-data android:name="pushe_token"
            android:value="PUSHE_TOKEN" />
```
If you need location-based features, add `Location permissions` to the manifest as well.


#### More features

All features are added to the sample. You can check them out.

Now run and install your app on a device or emulator that has google-play-service installed.
Pushe needs minimum android api=15 and google-play-service version >= 3 to run.

## More Info
For detailed documentations visit https://docs.pushe.co/docs/android/intro


## Contribution

Feel free to add anything you think is suitable to be in this sample.<br>
It does not follow any specific code style. So just read the code a little bit and send a pull request at anytime. We'll be happy :D.

## Support 
#### Email:
If you have any problem, please contact us using this email, we will get back to you right away:
`hi [at] pushe.co`


