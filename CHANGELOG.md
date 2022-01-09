# Change log

## 2.5.1-rc02
- Add `AD_ID` permission to ensure `Pushe.getAdvertisingId` works for Google since the change in [AdvertisingId policy](https://support.google.com/googleplay/android-developer/answer/6048248?hl=en)

> **NOTE**: In order to remove `AD_ID` permission (which you may need to due to Google Play restrictions) use this code to remove the permission:
> 
> ```xml
> <uses-permission android:name="com.google.android.gms.permission.AD_ID" tools:node="remove"/>
> 
> <application ... />
> ```

- Fix warning for `missing class co.pushe.plus.messaging.ParcelSendException` when minifying.

## 2.5.1-rc01
- Fix `android:exported` not present in some `<service>` tags (targetSDK=31 only)
- Updated targetSDKVersion to **31**

## 2.5.0
- **New**: Location APIs are now provided by `fcm` and `hms` independently. Geofence and Location-based notifications are supported both in Huawei and Google devices.
- **New**: Ability to disable display of notification when app is in Foreground state. 
  You can just pass `show_foreground: false` when sending using RESTful API (Console not supported yet).  
  Or Use one of provided APIs:

  ```java
    PusheNotification notification = Pushe.getPusheService(PusheNotification.class);
    if(notification != null) {
        // فعال‌کردن این قابلیت برای همه‌ی اعلانها
        notification.enableNotificationForceForegroundAware();
    
        // حالت پیش‌فرض
        notification.disableNotificationForceForegroundAware();
    
        // بررسی فعال‌بودن یا نبودن  
        notification.isForegroundAwareByForce();
    }
  ```

  More info at [documentation](https://docs.pushe.co/docs/android/notification/foreground)

- Deprecated `UserNotification.withAndroidId`. Use `UserNotification.withDeviceId` instead

## 2.4.0
- **NEW**: Added support for Huawei Mobile services push notification service.
  Huawei devices and other devices that have HMS core will now be supported by pushe
- Support for Huawei OAID as a replacement for Google Ad id
- Update `firebase-messaging` dependency to `21.0.0`
- Added support for default firebase. In order to fix `FIS_AUTH` happened for any reason, manifest flag `use_default_firebase` can be used

**NOTE**: You need `huawei` maven repo to sync `hms` module of pushe:

```java
allprojects {
    repositories {
        maven {url 'https://developer.huawei.com/repo/'}
        jcenter()
    }
}
```

```xml
<meta-data android:name="pushe_use_default_firebase" android:value="true" />
```


## 2.2.1
- Fix: Error `addObserver must be called on mainThread`

## 2.2.0
- Add support for **InAppMessaging**. [More Info](https://docs.pushe.co/docs/android/inappmessaging/intro/)

## 2.1.1
- **GDPR** compliancy support
- `getAndroidId()` is deprecated. Use `getDeviceId()` instead.
- Remove `getPusheId()`. Use [other IDs specified](https://docs.pushe.co/docs/android/unification)

## 2.1.0 (18 Apr, 2020)
**Breaking change**

- Migrate to AndroidX. App using legacy android support, MUST migrate to SDK in order to be able to use Pushe SDK.
If an still insists on android.support, it might not get some key features. Further releases and features will only be supported for androidX artifact.

- Update library dependencies to highest.
- Support for notification badge back to API 19 (Optional shortcutBadger library)
- Fix issue with `Firebase-Messaging:20.1.1` throwing `FIS_AUTH_ERROR` and updated dependency to `firebase-messaging:20.1.5`.
- Fix bug with R8 generating duplicate classes.
- Fix bug with Pushe:rxjava duplicate issue even when no rxjava2 was added.

## 2.0.5 (1 Apr, 2020)
- Support for disabling data collection manually using manifest meta data (With awareness of feature loss)
   Location, Wifi and cellular data can be disabled using manifest key. So they will not be collected even if the permission is granted.
- Schedules notifications can now be canceled. Just send the messageId in a `type 34` downstream message.
- Minor bug fixes

## 2.0.4 (19 Jan, 2020)   
- Tags now support callback to let the developer know if the process is done.
- Geofence can now be optional.
- Fix issue with `Firebase-messaging:20.1.0` while initializing a `FirebaseMessaging` class.

## 2.0.1 (21 Sep, 2019)
- Instead of having a list of strings as user tag, now tag is a set of key/values (aka a json) that can be set for a user.
- Improvements on Token string of `pushe_token`
- Added API method `getSubscribedTags`, so the developer can see and remove all tags.

## 2.0.0 (1 Sep, 2019)
- First stable version to release