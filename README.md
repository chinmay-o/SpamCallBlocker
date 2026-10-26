# Prefix Call Blocker (Android)

A small Android app that rejects incoming calls whose caller ID begins with `+911409` (also matches equivalent digit formats such as `00911409...`). It does not upload or store call information.

## Requirements
- Android 10 (API 29) or later.
- The user must explicitly approve the app as the device's Call Screening app.
- Some phone manufacturers/carriers may have their own call-screening restrictions.

## Build the APK
1. Open this folder in Android Studio (current stable version).
2. Allow Gradle sync and install Android SDK Platform 35 if prompted.
3. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. The debug APK is usually created at `app/build/outputs/apk/debug/app-debug.apk`.

## Install and enable
1. Install the APK on the phone. If installing manually, allow the browser/file manager to install unknown apps when Android prompts.
2. Open **Prefix Call Blocker**.
3. Tap **Enable call blocking** and approve the Call Screening role.
4. Leave it enabled in Android's role settings.

Android controls which app can screen calls. Calls with hidden/unknown caller ID cannot be matched to this prefix and will not be blocked by this app.
