# Android Studio Mini - Technology Stack အသေးစိတ်

## အကျဉ်းချုပ်

ဤ document သည် Android Studio Mini IDE ကို ဖန်တီးရန် အသုံးပြုထားတဲ့ အားလုံးသည့် နည်းပညာများကို အသေးစိတ်ရှင်းပြထားသည်။

---

## 1. Programming Languages

### 1.1 Kotlin (Primary Language)

**အသုံးပြုခြင်း:**
- IDE application ကို ရေးသားခြင်း
- Business logic implementation
- UI components ဖန်တီးခြင်း

**Version:** 1.9.0+

**အကျိုးအရှိုးများ:**
- ✅ Java interoperability
- ✅ Null safety
- ✅ Concise syntax
- ✅ Coroutines support
- ✅ Android-first language

**Code Example:**
```kotlin
// Kotlin coroutines for async operations
suspend fun buildProject(projectPath: String) = withContext(Dispatchers.IO) {
    try {
        val result = compileResources(projectPath)
        if (result) {
            addLog("[SUCCESS] Build completed")
            true
        } else {
            addLog("[ERROR] Build failed")
            false
        }
    } catch (e: Exception) {
        addLog("[ERROR] ${e.message}")
        false
    }
}
```

### 1.2 Java

**အသုံးပြုခြင်း:**
- Legacy code compatibility
- Android framework integration
- File operations

**Version:** 11+

### 1.3 XML

**အသုံးပြုခြင်း:**
- Android manifest configuration
- Resource files (strings, colors)
- Layout definitions

---

## 2. Android Framework & Libraries

### 2.1 Android SDK

**Version:** API 26 (Android 8.0) - API 34 (Android 14)

**Components:**
- ✅ Android Core
- ✅ Android Resources
- ✅ Android Manifest
- ✅ Android Permissions

### 2.2 Jetpack Compose

**Version:** Latest stable

**အသုံးပြုခြင်း:**
- Modern UI framework
- Declarative UI components
- Reactive state management

**Components Used:**
- `Composable` - UI components
- `State` - State management
- `LazyColumn/LazyRow` - Scrollable lists
- `Dialog` - Modal dialogs
- `TextField` - Text input
- `Button` - Interactive buttons

**Code Example:**
```kotlin
@Composable
fun IDEScreen(viewModel: AppViewModel) {
    val state by viewModel.appState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(viewModel)
        
        Row(modifier = Modifier.weight(1f)) {
            ProjectExplorer(state.files, viewModel::selectFile)
            CodeEditorPanel(state.currentFile, viewModel::updateCode)
            InspectorPanel(state.selectedFile)
        }
        
        BottomPanels(state.buildLogs)
    }
}
```

### 2.3 AndroidX Libraries

**Core Libraries:**
- `androidx.appcompat:appcompat` - App compatibility
- `androidx.core:core-ktx` - Kotlin extensions
- `androidx.lifecycle:lifecycle-runtime-ktx` - Lifecycle management
- `androidx.lifecycle:lifecycle-viewmodel-ktx` - ViewModel

**UI Libraries:**
- `androidx.constraintlayout:constraintlayout` - Layout system
- `androidx.recyclerview:recyclerview` - List views
- `androidx.cardview:cardview` - Card components

**Data Libraries:**
- `androidx.room:room-runtime` - Database
- `androidx.datastore:datastore-preferences` - Data persistence

---

## 3. Build System

### 3.1 Gradle

**Version:** 8.1.0+

**အသုံးပြုခြင်း:**
- Project build management
- Dependency management
- Build configuration

**Key Configuration:**
```gradle
android {
    compileSdk = 34
    minSdk = 26
    targetSdk = 34
    
    buildTypes {
        debug {
            debuggable = true
        }
        release {
            minifyEnabled = true
            proguardFiles(...)
        }
    }
}

dependencies {
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
}
```

### 3.2 Kotlin Gradle Plugin

**Version:** 1.9.0+

**Features:**
- Kotlin compilation
- Kapt annotation processing
- Kotlin DSL support

---

## 4. Code Editor Integration

### 4.1 Sora-Editor

**Version:** 0.24.4+

**အခန်းကဏ္ဍ:**
- Advanced code editing
- Syntax highlighting
- Code folding
- Auto-completion

**Supported Languages:**
- Java (.java)
- Kotlin (.kt)
- XML (.xml)
- Gradle (.gradle)
- Properties (.properties)
- JSON (.json)

**Features:**
```kotlin
// Sora-Editor integration
val editor = CodeEditor(context)
editor.setEditorLanguage("java")
editor.setText(sourceCode)
editor.enableSyntaxHighlight(true)
editor.enableAutoCompletion(true)
editor.enableCodeFolding(true)
```

### 4.2 TextMate Language Support

**အသုံးပြုခြင်း:**
- Syntax highlighting definitions
- Language grammar files
- Color schemes

---

## 5. Android Build Tools

### 5.1 aapt2 (Android Asset Packaging Tool 2)

**Version:** 7.4.0+

**အခန်းကဏ္ဍ:**
- Compile resources
- Create resource table
- Package resources

**Usage:**
```bash
# Compile resources
aapt2 compile --dir res/ -o resources.zip

# Link resources
aapt2 link -o app.apk --manifest AndroidManifest.xml resources.zip
```

### 5.2 javac (Java Compiler)

**Version:** 11+

**အခန်းကဏ္ဍ:**
- Compile Java source files
- Generate bytecode

**Usage:**
```bash
javac -d classes/ -source 11 -target 11 src/**/*.java
```

### 5.3 kotlinc (Kotlin Compiler)

**Version:** 1.9.0+

**အခန်းကဏ္ဍ:**
- Compile Kotlin source files
- Generate bytecode

**Usage:**
```bash
kotlinc -d classes/ -jvm-target 11 src/**/*.kt
```

### 5.4 d8 (DEX Compiler)

**Version:** 7.4.0+

**အခန်းကဏ္ဍ:**
- Convert bytecode to DEX format
- Optimize code
- Support multi-DEX

**Usage:**
```bash
d8 --output=dex/ --min-api=26 classes/**/*.class
```

### 5.5 apksigner (APK Signer)

**Version:** 7.4.0+

**အခန်းကဏ္ဍ:**
- Sign APK files
- Verify signatures
- Support v1 & v2 signing

**Usage:**
```bash
apksigner sign \
  --ks keystore.jks \
  --ks-pass pass:password \
  --out app-signed.apk \
  app-unsigned.apk
```

### 5.6 zipalign (APK Alignment)

**အခန်းကဏ္ဍ:**
- Align APK for optimization
- Improve performance
- Reduce memory usage

**Usage:**
```bash
zipalign -v 4 app-signed.apk app-aligned.apk
```

---

## 6. State Management

### 6.1 Kotlin Coroutines

**Version:** 1.7.3+

**အသုံးပြုခြင်း:**
- Async operations
- Non-blocking I/O
- Structured concurrency

**Code Example:**
```kotlin
// Coroutines for async build
viewModelScope.launch {
    val result = withContext(Dispatchers.IO) {
        buildToolsManager.buildProject(projectPath)
    }
    
    when (result) {
        is BuildResult.Success -> updateUI(result.apkPath)
        is BuildResult.Failure -> showError(result.error)
    }
}
```

### 6.2 StateFlow

**အသုံးပြုခြင်း:**
- Reactive state management
- Observable state changes
- Compose integration

**Code Example:**
```kotlin
// StateFlow for reactive state
class AppViewModel : ViewModel() {
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()
    
    fun updateBuildLog(message: String) {
        _appState.update { state ->
            state.copy(buildLogs = state.buildLogs + message)
        }
    }
}
```

---

## 7. File System & Storage

### 7.1 Android File System

**အသုံးပြုခြင်း:**
- Project file management
- Resource storage
- Build artifact storage

**Scoped Storage:**
- Android 11+ compatibility
- File access permissions
- URI-based file access

### 7.2 FileProvider

**အသုံးပြုခြင်း:**
- Secure file sharing
- APK installation
- File access control

---

## 8. CI/CD & Automation

### 8.1 GitHub Actions

**အသုံးပြုခြင်း:**
- Automated APK building
- Testing & quality checks
- Release automation

**Workflows:**
- `build-apk.yml` - Gradle-based build
- `build-apk-custom.yml` - Custom build tools
- `test-and-lint.yml` - Testing & quality

**Configuration:**
```yaml
name: Build APK
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v3
      - run: ./gradlew build
```

### 8.2 Gradle Build System

**အသုံးပြုခြင်း:**
- Project compilation
- Dependency resolution
- Build configuration

---

## 9. Testing & Quality

### 9.1 Unit Testing

**Frameworks:**
- JUnit 4+
- Mockito
- Robolectric

**Code Example:**
```kotlin
@Test
fun testBuildProject() {
    val result = buildManager.buildProject(testProjectPath)
    assertTrue(result)
}
```

### 9.2 Lint Checking

**အသုံးပြုခြင်း:**
- Code quality analysis
- Best practices checking
- Performance optimization

---

## 10. Architecture Patterns

### 10.1 MVVM (Model-View-ViewModel)

**အသုံးပြုခြင်း:**
- Separation of concerns
- Testability
- Reusability

**Components:**
- **Model**: Data & business logic
- **View**: UI components (Compose)
- **ViewModel**: State management

**Code Example:**
```kotlin
// MVVM Architecture
class AppViewModel : ViewModel() {
    private val projectManager = ProjectManager()
    private val buildManager = BuildManager()
    
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()
    
    fun buildProject(projectPath: String) {
        viewModelScope.launch {
            val result = buildManager.buildProject(projectPath)
            _appState.update { state ->
                state.copy(buildResult = result)
            }
        }
    }
}
```

### 10.2 Repository Pattern

**အသုံးပြုခြင်း:**
- Data abstraction
- Dependency injection
- Testability

---

## 11. Development Tools

### 11.1 Android Studio

**အသုံးပြုခြင်း:**
- IDE for development
- Debugging
- Emulation

### 11.2 Gradle Wrapper

**အသုံးပြုခြင်း:**
- Version consistency
- Offline builds
- CI/CD integration

### 11.3 ProGuard/R8

**အသုံးပြုခြင်း:**
- Code obfuscation
- Size optimization
- Performance improvement

---

## 12. Configuration Files

### 12.1 build.gradle.kts

```kotlin
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

dependencies {
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
}
```

### 12.2 AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:label="@string/app_name"
        android:icon="@mipmap/ic_launcher">
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

---

## 13. Technology Summary Table

| Category | Technology | Version | Purpose |
|----------|-----------|---------|---------|
| **Language** | Kotlin | 1.9.0+ | Primary development |
| **UI Framework** | Jetpack Compose | Latest | Modern UI |
| **Build System** | Gradle | 8.1.0+ | Project building |
| **Editor** | Sora-Editor | 0.24.4+ | Code editing |
| **Build Tools** | aapt2, d8, apksigner | 7.4.0+ | APK compilation |
| **State Mgmt** | Coroutines/StateFlow | 1.7.3+ | Async & reactive |
| **Testing** | JUnit, Mockito | Latest | Quality assurance |
| **CI/CD** | GitHub Actions | Latest | Automation |
| **Database** | Room | Latest | Data persistence |
| **Networking** | OkHttp/Retrofit | Latest | API calls |

---

## 14. Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                   Android Studio Mini IDE               │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────────────────────────────────────┐  │
│  │         Jetpack Compose UI Layer                │  │
│  │  (TopAppBar, CodeEditor, ProjectExplorer, etc)  │  │
│  └──────────────────────────────────────────────────┘  │
│                          ↑                              │
│                          │                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │         AppViewModel (State Management)          │  │
│  │  (StateFlow, Coroutines, MVVM Pattern)          │  │
│  └──────────────────────────────────────────────────┘  │
│                          ↑                              │
│                          │                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │         Business Logic Layer                     │  │
│  │  ┌────────────────────────────────────────────┐ │  │
│  │  │ ProjectManager │ BuildManager │ FileManager│ │  │
│  │  │ CodeEditor     │ APKInstaller │ TestRunner │ │  │
│  │  └────────────────────────────────────────────┘ │  │
│  └──────────────────────────────────────────────────┘  │
│                          ↑                              │
│                          │                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │         Build Tools Integration Layer            │  │
│  │  ┌────────────────────────────────────────────┐ │  │
│  │  │ aapt2 │ javac │ kotlinc │ d8 │ apksigner  │ │  │
│  │  └────────────────────────────────────────────┘ │  │
│  └──────────────────────────────────────────────────┘  │
│                          ↑                              │
│                          │                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │         Android Framework & File System          │  │
│  │  (Android SDK, File I/O, Permissions)           │  │
│  └──────────────────────────────────────────────────┘  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## 15. Data Flow

```
User Input (UI)
    ↓
Jetpack Compose Event Handler
    ↓
AppViewModel Method Call
    ↓
Business Logic Manager (Project/Build/File)
    ↓
Build Tools (aapt2/d8/apksigner)
    ↓
Android Framework / File System
    ↓
APK Generation / File Output
    ↓
StateFlow Update
    ↓
UI Recomposition (Compose)
    ↓
Updated Display
```

---

## 16. Build Pipeline

```
Source Code (.java/.kt)
    ↓
Compile Resources (aapt2)
    ↓
Compile Sources (javac/kotlinc)
    ↓
Generate Bytecode (.class)
    ↓
Convert to DEX (d8)
    ↓
Package APK
    ↓
Sign APK (apksigner)
    ↓
Align APK (zipalign)
    ↓
Final APK
```

---

## အကျဉ်းချုပ်

### အသုံးပြုထားတဲ့ နည်းပညာများ

**Frontend:**
- Kotlin + Jetpack Compose
- MVVM Architecture
- StateFlow + Coroutines

**Build System:**
- Gradle 8.1.0
- aapt2, d8, apksigner
- Custom build tools

**State Management:**
- Kotlin Coroutines
- StateFlow
- ViewModel

**CI/CD:**
- GitHub Actions
- Automated APK building
- Testing & quality checks

**Development:**
- Android Studio
- Gradle Wrapper
- ProGuard/R8

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Language**: Myanmar (Burmese)
