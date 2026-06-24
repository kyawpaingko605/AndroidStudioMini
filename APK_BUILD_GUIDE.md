# Android Studio Mini - APK Build Guide

## Overview

This guide explains how to build the Android Studio Mini IDE into a production-ready APK file for installation on Android devices.

## Prerequisites

### System Requirements
- **Java**: OpenJDK 11 or higher
- **Gradle**: 7.0 or higher
- **Android SDK**: API 26 (Android 8.0) or higher
- **Build Tools**: 34.0.0 or higher
- **RAM**: 4GB minimum
- **Disk Space**: 2GB minimum

### Installation

#### Ubuntu/Debian
```bash
# Install Java
sudo apt-get install openjdk-11-jdk

# Install Gradle
sudo apt-get install gradle

# Install Android SDK
sudo apt-get install android-sdk-build-tools android-sdk-platform-tools
```

#### macOS
```bash
# Install Java
brew install openjdk@11

# Install Gradle
brew install gradle

# Install Android SDK
brew install android-sdk
```

#### Windows
1. Download Java from https://adoptopenjdk.net/
2. Download Gradle from https://gradle.org/releases/
3. Download Android SDK from https://developer.android.com/studio

## Project Structure

```
AndroidStudioMini/
├── build.gradle.kts          # Root build configuration
├── settings.gradle.kts       # Project settings
├── app/
│   ├── build.gradle.kts      # App build configuration
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/         # Kotlin source files
│   │   │   └── res/          # Resources
│   │   └── test/
│   │       └── java/         # Test files
│   └── build/                # Build output
├── build.sh                  # Build script
└── README.md                 # Project documentation
```

## Build Methods

### Method 1: Using Build Script (Recommended)

The easiest way to build the APK:

```bash
cd AndroidStudioMini
chmod +x build.sh
./build.sh
```

**What the script does:**
1. Validates prerequisites
2. Cleans previous builds
3. Validates project structure
4. Runs Gradle build
5. Generates APK file
6. Signs with debug key
7. Outputs build information

**Output:**
```
APK File Information:
  Name:     AndroidStudioMini-debug-20260624_093412.apk
  Path:     /path/to/app/build/outputs/apk/
  Size:     ~25 MB
  Created:  [timestamp]
```

### Method 2: Using Gradle Directly

```bash
cd AndroidStudioMini

# Build debug APK
gradle assembleDebug

# Build release APK
gradle assembleRelease

# Build and run tests
gradle build

# Clean build
gradle clean build
```

### Method 3: Using Android Studio

1. Open Android Studio
2. File → Open → Select AndroidStudioMini directory
3. Build → Build Bundle(s) / APK(s) → Build APK(s)
4. APK appears in `app/build/outputs/apk/debug/`

## Build Configuration

### Debug Build

**File:** `app/build.gradle.kts`

```kotlin
android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.androidstudiomini"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    buildTypes {
        debug {
            debuggable = true
            minifyEnabled = false
        }
    }
}
```

**Characteristics:**
- Debuggable on device
- No code obfuscation
- Larger file size
- Faster build time
- Good for development

### Release Build

```kotlin
buildTypes {
    release {
        debuggable = false
        minifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

**Characteristics:**
- Not debuggable
- Code obfuscated
- Smaller file size
- Slower build time
- For production deployment

## Build Output

### Location
```
AndroidStudioMini/app/build/outputs/apk/
├── debug/
│   ├── app-debug.apk
│   └── output-metadata.json
└── release/
    ├── app-release.apk
    └── output-metadata.json
```

### File Information

**Debug APK:**
- Size: ~25-30 MB
- Debuggable: Yes
- Signed: Debug key
- Use: Development, testing

**Release APK:**
- Size: ~15-20 MB
- Debuggable: No
- Signed: Release key (requires keystore)
- Use: Production, distribution

## Signing APK

### Debug Signing (Automatic)

Debug APKs are automatically signed with the debug key:

```bash
# Debug keystore location
~/.android/debug.keystore

# Debug key credentials
Alias: androiddebugkey
Password: android
```

### Release Signing (Manual)

Create a keystore for release builds:

```bash
# Create keystore
keytool -genkey -v -keystore my-release-key.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias my-key-alias

# Sign APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
    -keystore my-release-key.jks \
    app/build/outputs/apk/release/app-release.apk \
    my-key-alias

# Verify signature
jarsigner -verify -verbose -certs \
    app/build/outputs/apk/release/app-release.apk
```

### Configure in Gradle

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.jks")
            storePassword = "my-store-password"
            keyAlias = "my-key-alias"
            keyPassword = "my-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

## Installation

### Method 1: Using ADB (Android Debug Bridge)

```bash
# Connect device via USB
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Install and run
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.example.androidstudiomini/.MainActivity

# Uninstall
adb uninstall com.example.androidstudiomini
```

### Method 2: Manual Installation

1. Transfer APK to Android device
2. Enable "Unknown Sources" in Settings
3. Open file manager
4. Tap APK file
5. Follow installation prompts

### Method 3: Using Installation Script

```bash
cd app/build/outputs/apk/
chmod +x install.sh
./install.sh app-debug.apk
```

## Build Troubleshooting

### Issue: "Unable to start the daemon process"

**Solution:**
```bash
# Clear Gradle cache
rm -rf ~/.gradle

# Rebuild
gradle clean build
```

### Issue: "Unrecognized VM option 'MaxPermSize'"

**Solution:**
Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2048m
```

### Issue: "SDK location not found"

**Solution:**
Create `local.properties`:
```properties
sdk.dir=/path/to/android/sdk
```

### Issue: "Compilation failed"

**Solution:**
```bash
# Clean and rebuild
gradle clean
gradle build -x test
```

## Build Optimization

### Reduce APK Size

```kotlin
android {
    bundle {
        density {
            enableSplit = true
        }
        language {
            enableSplit = true
        }
    }
}
```

### Enable ProGuard

```kotlin
buildTypes {
    release {
        minifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

### Parallel Build

```bash
gradle build --parallel --max-workers=4
```

## Build Variants

### Debug Build
```bash
gradle assembleDebug
```

### Release Build
```bash
gradle assembleRelease
```

### All Variants
```bash
gradle assemble
```

### Build and Test
```bash
gradle build
```

## Continuous Integration

### GitHub Actions

Create `.github/workflows/build.yml`:

```yaml
name: Build APK

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      
      - name: Build APK
        run: |
          chmod +x ./build.sh
          ./build.sh
      
      - name: Upload APK
        uses: actions/upload-artifact@v2
        with:
          name: app-debug.apk
          path: app/build/outputs/apk/debug/
```

## Performance Metrics

### Build Time
- Clean build: 2-5 minutes
- Incremental build: 30-60 seconds
- Rebuild with cache: 10-20 seconds

### APK Size
- Debug APK: ~25-30 MB
- Release APK: ~15-20 MB
- With ProGuard: ~12-15 MB

### Installation Time
- Via ADB: 10-30 seconds
- Manual: 1-2 minutes

## Distribution

### Play Store

1. Create Google Play Developer Account
2. Create application listing
3. Upload signed APK
4. Fill store listing details
5. Set pricing and distribution
6. Submit for review

### Direct Distribution

1. Host APK on website
2. Generate QR code
3. Share download link
4. Users enable "Unknown Sources"
5. Install manually

### Enterprise Distribution

1. Use Mobile Device Management (MDM)
2. Deploy via corporate app store
3. Automatic updates via MDM

## Security Considerations

### Code Obfuscation
```gradle
minifyEnabled = true
```

### Secure Keystore
```bash
# Use strong passwords
# Store securely
# Backup keystore
# Never commit to version control
```

### Manifest Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## Next Steps

1. **Test on Device**: Install and test on Android device
2. **Gather Feedback**: Get user feedback
3. **Fix Issues**: Address any bugs
4. **Optimize**: Improve performance
5. **Release**: Publish to Play Store or distribute

## Resources

- [Android Developers - Build Guide](https://developer.android.com/studio/build)
- [Gradle Documentation](https://docs.gradle.org/)
- [Android SDK Documentation](https://developer.android.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/)

## Support

For issues or questions:
1. Check the README.md
2. Review phase documentation
3. Check Android developer documentation
4. Search Stack Overflow

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Status**: Production Ready
