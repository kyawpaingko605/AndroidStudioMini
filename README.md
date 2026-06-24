# Android Studio Mini - On-Device IDE

A complete Android IDE that runs entirely on Android devices, allowing developers to write, compile, and deploy Android applications without a PC.

## Project Structure

```
AndroidStudioMini/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/androidstudiomini/
│   │   │   └── MainActivity.kt (Main UI with Compose)
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   └── xml/
│   │   │       └── file_paths.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Features (Phase 1 - UI)

✅ **Main IDE Interface**
- Top App Bar with project controls
- Left Sidebar: Project Explorer
- Center: Code Editor with tabs
- Right Sidebar: Properties Inspector
- Bottom Panel: Build, Logcat, Terminal, Problems tabs

✅ **Code Editor**
- Syntax-highlighted code display
- Line numbers
- Multiple file tabs
- Dark theme optimized for mobile

✅ **Project Explorer**
- Hierarchical project structure
- Folder and file icons
- Expandable tree view

✅ **Bottom Panels**
- Build Output
- Logcat Viewer
- Terminal
- Problems Panel

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Minimum SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)
- **Build System**: Gradle 8.1.0

## Key Dependencies

```kotlin
// Jetpack Compose
androidx.compose.ui:ui
androidx.compose.material3:material3

// Code Editor
io.github.rosemoe:editor (Sora-Editor)

// Navigation
androidx.navigation:navigation-compose

// File Operations
androidx.documentfile:documentfile
```

## Build Instructions

### Prerequisites
- Android Studio Flamingo or later
- Android SDK 34
- Kotlin 1.9.0+

### Build APK

```bash
cd AndroidStudioMini
./gradlew assembleDebug
```

### Install APK

```bash
./gradlew installDebug
```

## Next Phases

### Phase 2: Code Editor Integration
- Integrate Sora-Editor for advanced code editing
- Add syntax highlighting for Java/Kotlin
- Implement auto-completion

### Phase 3: File Manager
- Create/open/save projects
- File browser with Storage Access Framework
- Project templates

### Phase 4: Build Engine
- Java compilation (javac)
- Dex conversion (d8)
- APK packaging
- APK signing

### Phase 5: Advanced Features
- Dependency management
- Resource compilation (aapt2)
- Logcat integration
- APK installation

## Architecture

```
UI Layer (Compose)
    ↓
Business Logic (Project Manager, Build Manager)
    ↓
Build Engine (javac, d8, aapt2)
    ↓
File System & Storage
```

## Permissions Required

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

## Device Requirements

- **Minimum RAM**: 4GB
- **CPU**: ARM64
- **Storage**: 1GB free space
- **Android Version**: 8.0 (API 26) or higher

## License

MIT License

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and feature requests, please open an issue on GitHub.
