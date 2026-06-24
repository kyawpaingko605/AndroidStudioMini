# Android Studio Mini - Assets ဖိုင်များ လမ်းညွှန်

## Assets Folder ကို နားလည်ခြင်း

Android project တွင် **assets** folder သည် ကုဒ်မဖန်တီးပဲ အသုံးပြုနိုင်သည့် ဖိုင်များ သိမ်းဆည်းရန် အသုံးပြုသည်။

### Assets Folder အနေအထူး

```
app/src/main/assets/
├── ကုဒ်မဟုတ်သည့် ဖိုင်များ
├── Configuration ဖိုင်များ
├── Data ဖိုင်များ
└── Resource ဖိုင်များ
```

---

## Assets Folder ထဲ ထည့်သွင်းရမည့် ဖိုင်များ

### 1. **Configuration ဖိုင်များ**

#### A. Gradle Configuration
```
assets/config/
├── gradle.properties
├── local.properties
└── gradle-wrapper.properties
```

**ဖိုင်အကြောင်းအရာ:**

**gradle.properties:**
```properties
# Gradle VM options
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m

# Gradle daemon
org.gradle.daemon=true

# Parallel builds
org.gradle.parallel=true

# Build cache
org.gradle.caching=true

# Kotlin compiler options
kotlin.incremental=true
```

**local.properties:**
```properties
# Android SDK location
sdk.dir=/path/to/android/sdk

# NDK location (optional)
ndk.dir=/path/to/android/ndk

# Gradle location
gradle.dir=/path/to/gradle
```

#### B. Build Configuration
```
assets/config/build/
├── debug.properties
├── release.properties
└── build.gradle.template
```

**ဖိုင်အကြောင်းအရာ:**

**debug.properties:**
```properties
# Debug build configuration
debuggable=true
minifyEnabled=false
shrinkResources=false
multiDexEnabled=false
```

**release.properties:**
```properties
# Release build configuration
debuggable=false
minifyEnabled=true
shrinkResources=true
multiDexEnabled=true
```

---

### 2. **Project Templates**

#### Template Structure
```
assets/templates/
├── empty-project/
│   ├── build.gradle.kts
│   ├── AndroidManifest.xml
│   ├── MainActivity.kt
│   └── strings.xml
├── basic-activity/
│   ├── build.gradle.kts
│   ├── AndroidManifest.xml
│   ├── MainActivity.kt
│   ├── activity_main.xml
│   └── strings.xml
├── jetpack-compose/
│   ├── build.gradle.kts
│   ├── AndroidManifest.xml
│   ├── MainActivity.kt
│   └── strings.xml
├── android-library/
│   ├── build.gradle.kts
│   ├── AndroidManifest.xml
│   └── LibraryClass.kt
└── multi-module/
    ├── build.gradle.kts
    ├── settings.gradle.kts
    ├── app/
    └── lib/
```

**Template ဖိုင်များ:**

**empty-project/build.gradle.kts:**
```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
}
```

**empty-project/AndroidManifest.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.app">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.Light.DarkActionBar">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**empty-project/MainActivity.kt:**
```kotlin
package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

**empty-project/strings.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">My App</string>
</resources>
```

---

### 3. **Documentation ဖိုင်များ**

#### Documentation Structure
```
assets/docs/
├── getting-started.md
├── project-structure.md
├── build-process.md
├── testing-guide.md
├── troubleshooting.md
└── api-reference.md
```

**ဖိုင်အကြောင်းအရာ:**

**getting-started.md:**
```markdown
# Getting Started with Android Studio Mini

## Installation
1. Download APK
2. Enable Unknown Sources
3. Install APK
4. Launch app

## First Project
1. Click New
2. Select Template
3. Enter Project Name
4. Click Create

## Build APK
1. Click Run
2. Wait for build
3. Click Install
```

**project-structure.md:**
```markdown
# Project Structure

## Standard Android Project Layout

app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── test/
└── build.gradle.kts
```

---

### 4. **Code Templates & Snippets**

#### Code Templates Structure
```
assets/snippets/
├── activities/
│   ├── basic-activity.kt
│   ├── compose-activity.kt
│   └── fragment-activity.kt
├── fragments/
│   ├── basic-fragment.kt
│   └── compose-fragment.kt
├── services/
│   ├── basic-service.kt
│   └── foreground-service.kt
├── receivers/
│   └── broadcast-receiver.kt
└── utilities/
    ├── extensions.kt
    ├── helpers.kt
    └── utils.kt
```

**ဖိုင်အကြောင်းအရာ:**

**activities/basic-activity.kt:**
```kotlin
package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class BasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        
        button.setOnClickListener {
            textView.text = "Button clicked!"
        }
    }
}
```

**utilities/extensions.kt:**
```kotlin
package com.example.app.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.isValidEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

fun Int.toDp(context: Context): Float {
    return this * context.resources.displayMetrics.density
}
```

---

### 5. **Resource Files**

#### Resource Structure
```
assets/resources/
├── colors.xml
├── strings.xml
├── dimensions.xml
├── styles.xml
└── themes.xml
```

**ဖိုင်အကြောင်းအရာ:**

**colors.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">#2196F3</color>
    <color name="primary_dark">#1976D2</color>
    <color name="accent">#FF4081</color>
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>
    <color name="gray">#808080</color>
</resources>
```

**strings.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Android Studio Mini</string>
    <string name="hello_world">Hello World!</string>
    <string name="button_ok">OK</string>
    <string name="button_cancel">Cancel</string>
</resources>
```

**dimensions.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="text_size_small">12sp</dimen>
    <dimen name="text_size_medium">16sp</dimen>
    <dimen name="text_size_large">20sp</dimen>
    <dimen name="margin_small">8dp</dimen>
    <dimen name="margin_medium">16dp</dimen>
    <dimen name="margin_large">24dp</dimen>
</resources>
```

---

### 6. **Dependencies & Libraries**

#### Dependencies Configuration
```
assets/dependencies/
├── androidx.txt
├── material.txt
├── compose.txt
├── networking.txt
├── database.txt
└── testing.txt
```

**ဖိုင်အကြောင်းအရာ:**

**androidx.txt:**
```
androidx.appcompat:appcompat:1.6.1
androidx.core:core-ktx:1.10.1
androidx.constraintlayout:constraintlayout:2.1.4
androidx.recyclerview:recyclerview:1.3.1
androidx.viewpager2:viewpager2:1.0.0
```

**compose.txt:**
```
androidx.compose.ui:ui:1.5.0
androidx.compose.material3:material3:1.1.0
androidx.compose.foundation:foundation:1.5.0
androidx.lifecycle:lifecycle-runtime-compose:2.6.1
androidx.activity:activity-compose:1.7.2
```

**networking.txt:**
```
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0
com.squareup.okhttp3:okhttp:4.11.0
com.google.code.gson:gson:2.10.1
```

---

### 7. **Build Scripts & Tools**

#### Scripts Structure
```
assets/scripts/
├── build.sh
├── clean.sh
├── test.sh
├── deploy.sh
└── gradlew
```

**ဖိုင်အကြောင်းအရာ:**

**build.sh:**
```bash
#!/bin/bash
echo "Building project..."
gradle clean
gradle build
echo "Build complete!"
```

**test.sh:**
```bash
#!/bin/bash
echo "Running tests..."
gradle test
echo "Tests complete!"
```

---

### 8. **Sample Projects**

#### Sample Projects Structure
```
assets/samples/
├── todo-app/
│   ├── build.gradle.kts
│   ├── MainActivity.kt
│   ├── TodoDatabase.kt
│   └── activity_main.xml
├── weather-app/
│   ├── build.gradle.kts
│   ├── MainActivity.kt
│   ├── WeatherService.kt
│   └── activity_main.xml
└── notes-app/
    ├── build.gradle.kts
    ├── MainActivity.kt
    ├── NotesDatabase.kt
    └── activity_main.xml
```

---

### 9. **Icons & Images**

#### Icons Structure
```
assets/icons/
├── app-icon.png (512x512)
├── launcher-icon.png (192x192)
├── notification-icon.png (24x24)
├── toolbar-icons/
│   ├── ic_new.png
│   ├── ic_save.png
│   ├── ic_run.png
│   ├── ic_settings.png
│   └── ic_help.png
└── ui-icons/
    ├── ic_folder.png
    ├── ic_file.png
    ├── ic_delete.png
    └── ic_edit.png
```

---

### 10. **Localization Files**

#### Localization Structure
```
assets/localization/
├── strings-en.xml
├── strings-my.xml
├── strings-zh.xml
├── strings-ja.xml
└── strings-ko.xml
```

**ဖိုင်အကြောင်းအရာ:**

**strings-my.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Android Studio Mini</string>
    <string name="new_project">နစ်ပ Project</string>
    <string name="save">သိမ်းဆည်းခြင်း</string>
    <string name="run">ဖန်တီးခြင်း</string>
    <string name="settings">ဆက်တင်များ</string>
</resources>
```

---

## Assets Folder ကို Setup လုပ်ခြင်း

### Step 1: Assets Folder ဖန်တီးခြင်း
```bash
mkdir -p app/src/main/assets
```

### Step 2: Subdirectories ဖန်တီးခြင်း
```bash
mkdir -p app/src/main/assets/config
mkdir -p app/src/main/assets/templates
mkdir -p app/src/main/assets/docs
mkdir -p app/src/main/assets/snippets
mkdir -p app/src/main/assets/resources
mkdir -p app/src/main/assets/dependencies
mkdir -p app/src/main/assets/scripts
mkdir -p app/src/main/assets/samples
mkdir -p app/src/main/assets/icons
mkdir -p app/src/main/assets/localization
```

### Step 3: ဖိုင်များထည့်သွင်းခြင်း
```bash
# Configuration files
cp gradle.properties app/src/main/assets/config/
cp local.properties app/src/main/assets/config/

# Templates
cp -r templates/* app/src/main/assets/templates/

# Documentation
cp docs/*.md app/src/main/assets/docs/

# Icons
cp icons/* app/src/main/assets/icons/
```

### Step 4: Code တွင် Assets ကို Access လုပ်ခြင်း

```kotlin
// Assets folder ကို access လုပ်ခြင်း
val assetManager = context.assets

// ဖိုင်ကို ဖတ်ခြင်း
val inputStream = assetManager.open("config/gradle.properties")
val content = inputStream.bufferedReader().use { it.readText() }

// Template ကို ကူးယူခြင်း
val templateStream = assetManager.open("templates/empty-project/build.gradle.kts")
val templateContent = templateStream.bufferedReader().use { it.readText() }
```

---

## Assets ဖိုင်များ အသုံးပြုခြင်း

### 1. Configuration Loading
```kotlin
fun loadGradleProperties(): Map<String, String> {
    val properties = mutableMapOf<String, String>()
    val inputStream = context.assets.open("config/gradle.properties")
    
    inputStream.bufferedReader().forEachLine { line ->
        if (!line.startsWith("#") && line.contains("=")) {
            val (key, value) = line.split("=")
            properties[key.trim()] = value.trim()
        }
    }
    
    return properties
}
```

### 2. Template Loading
```kotlin
fun loadTemplate(templateName: String): String {
    val path = "templates/$templateName/build.gradle.kts"
    return context.assets.open(path).bufferedReader().use { it.readText() }
}
```

### 3. Documentation Loading
```kotlin
fun loadDocumentation(docName: String): String {
    val path = "docs/$docName.md"
    return context.assets.open(path).bufferedReader().use { it.readText() }
}
```

### 4. Icon Loading
```kotlin
fun loadIcon(iconName: String): Bitmap {
    val inputStream = context.assets.open("icons/$iconName.png")
    return BitmapFactory.decodeStream(inputStream)
}
```

---

## အကျဉ်းချုပ်

**Assets Folder ထဲ ထည့်သွင်းရမည့် အဓိကဖိုင်များ:**

| အမျိုးအစား | ဖိုင်များ | အသုံးပြုခြင်း |
|----------|---------|-----------|
| **Configuration** | gradle.properties, local.properties | Build settings |
| **Templates** | Project templates | Quick project creation |
| **Documentation** | Markdown files | User guides |
| **Snippets** | Code templates | Code generation |
| **Resources** | XML files | Colors, strings, dimensions |
| **Dependencies** | Text files | Library lists |
| **Scripts** | Shell scripts | Build automation |
| **Samples** | Sample projects | Learning examples |
| **Icons** | PNG images | UI elements |
| **Localization** | XML files | Multi-language support |

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Language**: Myanmar (Burmese)
