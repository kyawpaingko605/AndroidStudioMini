# Android Studio Mini - APK Build Instructions

## အကျဉ်းချုပ်

ဤ guide သည် Android Studio Mini IDE ကို APK ဖိုင်အဖြစ် ဖန်တီးရန် အသေးစိတ် လမ်းညွှန်ပြထားသည်။

---

## 1. Prerequisites (အစိတ်အပိုင်းများ)

### 1.1 System Requirements

- **Operating System**: Linux, macOS, or Windows
- **Java**: JDK 11 or higher
- **Gradle**: 8.1.0 or higher
- **Android SDK**: API 34 (Android 14)
- **Memory**: 4GB RAM minimum
- **Storage**: 2GB free space

### 1.2 Installation

**Ubuntu/Debian:**
```bash
# Install Java
sudo apt-get update
sudo apt-get install -y openjdk-11-jdk

# Install Gradle
sudo apt-get install -y gradle

# Verify installation
java -version
gradle --version
```

**macOS:**
```bash
# Install Java (using Homebrew)
brew install openjdk@11

# Install Gradle
brew install gradle

# Verify installation
java -version
gradle --version
```

**Windows:**
- Download JDK 11 from [Oracle](https://www.oracle.com/java/technologies/downloads/)
- Download Gradle from [gradle.org](https://gradle.org/releases/)
- Add both to PATH environment variable

---

## 2. Project Setup

### 2.1 Clone/Download Project

```bash
# Clone from GitHub
git clone https://github.com/your-username/AndroidStudioMini.git
cd AndroidStudioMini

# Or extract ZIP file
unzip AndroidStudioMini-Complete.zip
cd AndroidStudioMini
```

### 2.2 Verify Project Structure

```bash
# Check directory structure
ls -la

# Expected output:
# .github/              (GitHub Actions workflows)
# app/                  (Android app module)
# gradle/               (Gradle wrapper)
# build.gradle.kts      (Root build configuration)
# settings.gradle.kts   (Project settings)
# gradle.properties     (Gradle properties)
# gradlew              (Gradle wrapper script)
```

---

## 3. Build Configuration

### 3.1 Update gradle.properties

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx2048m -XX:+UseG1GC

# Android settings
android.compileSdkVersion=34
android.buildToolsVersion=34.0.0
android.minSdkVersion=26
android.targetSdkVersion=34

# Gradle daemon
org.gradle.daemon=true
org.gradle.parallel=true
```

### 3.2 Verify build.gradle.kts

```kotlin
// Root build.gradle.kts
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

// app/build.gradle.kts
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.androidstudiomini"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
```

---

## 4. Building APK

### 4.1 Debug APK Build

**Using Gradle Wrapper (Recommended):**
```bash
cd AndroidStudioMini

# Make gradlew executable (Linux/macOS)
chmod +x gradlew

# Build debug APK
./gradlew app:assembleDebug

# Output location:
# app/build/outputs/apk/debug/app-debug.apk
```

**Using System Gradle:**
```bash
gradle app:assembleDebug
```

### 4.2 Release APK Build

```bash
# Build release APK (unsigned)
./gradlew app:assembleRelease

# Output location:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

### 4.3 Build with Specific Tasks

```bash
# Clean build
./gradlew clean

# Build with dependencies
./gradlew build

# Build specific module
./gradlew app:build

# Check build tasks
./gradlew tasks
```

---

## 5. Signing APK

### 5.1 Create Keystore

```bash
# Generate debug keystore (if not exists)
keytool -genkey -v -keystore ~/.android/debug.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias debug -storepass android -keypass android \
  -dname "CN=Android Studio Mini, O=Development, C=MM"
```

### 5.2 Sign Release APK

```bash
# Sign APK with apksigner
apksigner sign \
  --ks ~/.android/debug.keystore \
  --ks-pass pass:android \
  --ks-key-alias debug \
  --key-pass pass:android \
  --out app-signed.apk \
  app/build/outputs/apk/release/app-release-unsigned.apk
```

### 5.3 Align APK

```bash
# Align APK for optimization
zipalign -v 4 app-signed.apk app-aligned.apk

# Verify alignment
zipalign -c -v 4 app-aligned.apk
```

---

## 6. APK Installation

### 6.1 Install on Device

```bash
# Connect Android device via USB
adb devices

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Install and run
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.example.androidstudiomini/.MainActivity
```

### 6.2 Install on Emulator

```bash
# List emulators
emulator -list-avds

# Start emulator
emulator -avd <emulator_name>

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 7. Troubleshooting

### Issue 1: "Gradle not found"

**Solution:**
```bash
# Install Gradle
sudo apt-get install gradle

# Or set GRADLE_HOME
export GRADLE_HOME=/usr/share/gradle
export PATH=$GRADLE_HOME/bin:$PATH
```

### Issue 2: "Android SDK not found"

**Solution:**
```bash
# Set ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$ANDROID_HOME/tools:$PATH

# Or install Android SDK
sudo apt-get install android-sdk
```

### Issue 3: "Build failed - Java version"

**Solution:**
```bash
# Check Java version
java -version

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

### Issue 4: "Out of memory"

**Solution:**
```bash
# Increase Gradle heap size
export GRADLE_OPTS="-Xmx2048m"

# Or update gradle.properties
org.gradle.jvmargs=-Xmx2048m
```

### Issue 5: "Dependency not found"

**Solution:**
```bash
# Clear Gradle cache
./gradlew clean

# Update dependencies
./gradlew build --refresh-dependencies
```

---

## 8. Build Outputs

### 8.1 APK Locations

**Debug APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

**Release APK:**
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

**Signed APK:**
```
app-signed.apk
```

**Aligned APK:**
```
app-aligned.apk
```

### 8.2 Build Reports

**Build report:**
```
app/build/reports/
```

**Test results:**
```
app/build/test-results/
```

**Lint report:**
```
app/build/reports/lint-results.html
```

---

## 9. Continuous Integration

### 9.1 GitHub Actions

**Automated builds:**
```yaml
# .github/workflows/build-apk.yml
name: Build APK
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v3
      - run: ./gradlew app:assembleDebug
```

### 9.2 Local CI/CD

```bash
# Create build script
cat > build.sh << 'EOF'
#!/bin/bash
./gradlew clean
./gradlew app:assembleDebug
./gradlew app:assembleRelease
EOF

chmod +x build.sh
./build.sh
```

---

## 10. Advanced Build Options

### 10.1 Build Variants

```bash
# Build specific variant
./gradlew app:assembleDebug
./gradlew app:assembleRelease

# Build all variants
./gradlew app:assemble
```

### 10.2 ProGuard/R8 Obfuscation

```gradle
// app/build.gradle.kts
android {
    buildTypes {
        release {
            minifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 10.3 Multi-Dex Support

```gradle
android {
    defaultConfig {
        multiDexEnabled = true
    }
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
}
```

---

## 11. Performance Optimization

### 11.1 Build Speed

```bash
# Parallel builds
./gradlew app:assembleDebug --parallel

# Incremental builds
./gradlew app:assembleDebug --incremental

# Daemon
./gradlew app:assembleDebug --daemon
```

### 11.2 APK Size Optimization

```gradle
android {
    buildTypes {
        release {
            minifyEnabled = true
            shrinkResources = true
        }
    }
}
```

---

## 12. Testing Before Release

### 12.1 Unit Tests

```bash
./gradlew test
```

### 12.2 Lint Checks

```bash
./gradlew lint
```

### 12.3 APK Verification

```bash
# Verify APK signature
jarsigner -verify -verbose app-signed.apk

# Check APK contents
unzip -l app-signed.apk | head -20
```

---

## 13. Distribution

### 13.1 Release to Google Play

```bash
# Create signed APK
./gradlew app:bundleRelease

# Upload to Play Console
# https://play.google.com/console
```

### 13.2 Direct Distribution

```bash
# Share APK file
# 1. Generate signed APK
# 2. Share via email, cloud storage, or QR code
# 3. Users install via "Install from file"
```

---

## 14. Complete Build Script

```bash
#!/bin/bash
# build-apk.sh - Complete APK build script

set -e  # Exit on error

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo "=== Android Studio Mini - APK Build ==="
echo ""

# Step 1: Clean
echo "[1/5] Cleaning build artifacts..."
./gradlew clean

# Step 2: Build
echo "[2/5] Building APK..."
./gradlew app:assembleDebug

# Step 3: Verify
echo "[3/5] Verifying APK..."
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "✓ APK created successfully"
    ls -lh app/build/outputs/apk/debug/app-debug.apk
else
    echo "✗ APK build failed"
    exit 1
fi

# Step 4: Sign (optional)
echo "[4/5] Signing APK..."
apksigner sign \
  --ks ~/.android/debug.keystore \
  --ks-pass pass:android \
  --out app-debug-signed.apk \
  app/build/outputs/apk/debug/app-debug.apk

# Step 5: Align (optional)
echo "[5/5] Aligning APK..."
zipalign -v 4 app-debug-signed.apk app-debug-aligned.apk

echo ""
echo "=== Build Complete ==="
echo "APK Location: app/build/outputs/apk/debug/app-debug.apk"
echo "Signed APK: app-debug-signed.apk"
echo "Aligned APK: app-debug-aligned.apk"
```

---

## အကျဉ်းချုပ်

### Quick Start

```bash
# 1. Clone project
git clone <repo-url>
cd AndroidStudioMini

# 2. Build APK
./gradlew app:assembleDebug

# 3. Install on device
adb install app/build/outputs/apk/debug/app-debug.apk

# 4. Run app
adb shell am start -n com.example.androidstudiomini/.MainActivity
```

### Build Commands

| Command | Purpose |
|---------|---------|
| `./gradlew app:assembleDebug` | Build debug APK |
| `./gradlew app:assembleRelease` | Build release APK |
| `./gradlew clean` | Clean build artifacts |
| `./gradlew build` | Full build |
| `./gradlew test` | Run tests |
| `./gradlew lint` | Run lint checks |

### APK Installation

```bash
# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall app
adb uninstall com.example.androidstudiomini

# View installed apps
adb shell pm list packages
```

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Language**: Myanmar (Burmese)
