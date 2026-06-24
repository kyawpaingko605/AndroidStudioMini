# Android Studio Mini - Architecture

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│                   (Jetpack Compose UI)                      │
├──────────────┬──────────────┬──────────────┬────────────────┤
│   Top Bar    │  Project     │   Code       │   Inspector    │
│              │  Explorer    │   Editor     │   Panel        │
├──────────────┴──────────────┴──────────────┴────────────────┤
│                    BOTTOM PANELS                            │
│  ┌──────────┬──────────┬──────────┬──────────┐             │
│  │  Build   │ Logcat   │Terminal  │Problems  │             │
│  └──────────┴──────────┴──────────┴──────────┘             │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                  BUSINESS LOGIC LAYER                       │
│  ┌──────────────┬──────────────┬──────────────┐            │
│  │ Project      │ Build        │ File         │            │
│  │ Manager      │ Manager      │ Manager      │            │
│  └──────────────┴──────────────┴──────────────┘            │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                   BUILD ENGINE LAYER                        │
│  ┌──────────┬──────────┬──────────┬──────────┐             │
│  │ Resource │ Java     │ Dex      │ APK      │             │
│  │ Compiler │ Compiler │ Converter│ Packager │             │
│  │ (aapt2)  │ (javac)  │ (d8)     │          │             │
│  └──────────┴──────────┴──────────┴──────────┘             │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│              STORAGE & SYSTEM LAYER                         │
│  ┌──────────────┬──────────────┬──────────────┐            │
│  │ File System  │ Keystore     │ Package      │            │
│  │ (SAF)        │ Management   │ Manager      │            │
│  └──────────────┴──────────────┴──────────────┘            │
└─────────────────────────────────────────────────────────────┘
```

## Component Diagram

```
MainActivity
    ├── TopAppBar
    │   ├── Logo & Title
    │   ├── New Project Button
    │   ├── Save Button
    │   ├── Run Button
    │   └── Settings Button
    │
    ├── Main Content Row
    │   ├── ProjectExplorerPanel
    │   │   ├── Header
    │   │   └── ProjectTreeView
    │   │       └── ProjectTreeItem (recursive)
    │   │
    │   ├── CodeEditorPanel
    │   │   ├── EditorTabBar
    │   │   │   └── EditorTab (multiple)
    │   │   └── CodeEditor
    │   │       ├── LineNumbers
    │   │       └── CodeArea
    │   │
    │   └── InspectorPanel
    │       ├── Header
    │       └── PropertiesView
    │
    └── BottomPanel
        ├── BottomTabBar
        │   └── BottomTab (Build, Logcat, Terminal, Problems)
        └── PanelContent
            ├── BuildPanel
            ├── LogcatPanel
            ├── TerminalPanel
            └── ProblemsPanel
```

## Data Flow

```
User Input
    ↓
UI Event Handler
    ↓
Business Logic Manager
    ↓
Build Engine / File System
    ↓
Update UI State
    ↓
Recompose UI
```

## Build Pipeline

```
Java/Kotlin Source Code
    ↓
Resource Files (XML, PNG, etc.)
    ↓
┌─────────────────────────────┐
│ Resource Compilation (aapt2)│
│ Generates: R.java           │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│ Java Compilation (javac)    │
│ Input: .java + R.java       │
│ Output: .class files        │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│ Dex Conversion (d8)         │
│ Input: .class files         │
│ Output: classes.dex         │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│ APK Packaging               │
│ Input: .dex + resources     │
│ Output: unsigned.apk        │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│ APK Alignment (zipalign)    │
│ Output: aligned.apk         │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│ APK Signing (apksigner)     │
│ Output: signed.apk          │
└──────────────┬──────────────┘
               ↓
Ready to Install
```

## File Structure

```
AndroidStudioMini/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/androidstudiomini/
│   │   │   ├── MainActivity.kt
│   │   │   ├── managers/
│   │   │   │   ├── ProjectManager.kt
│   │   │   │   ├── BuildManager.kt
│   │   │   │   └── FileManager.kt
│   │   │   ├── models/
│   │   │   │   ├── Project.kt
│   │   │   │   ├── BuildResult.kt
│   │   │   │   └── LogEntry.kt
│   │   │   ├── ui/
│   │   │   │   ├── screens/
│   │   │   │   ├── components/
│   │   │   │   └── theme/
│   │   │   └── utils/
│   │   │       ├── FileUtils.kt
│   │   │       └── BuildUtils.kt
│   │   ├── res/
│   │   │   ├── values/
│   │   │   ├── drawable/
│   │   │   ├── layout/
│   │   │   └── xml/
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Technology Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose |
| Language | Kotlin |
| Build System | Gradle |
| Code Editor | Sora-Editor |
| File Management | Storage Access Framework |
| Compilation | javac, d8, aapt2 |
| Minimum SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

## Key Features by Phase

### Phase 1: UI ✅
- Compose-based IDE interface
- Project explorer
- Code editor with tabs
- Bottom panels (Build, Logcat, Terminal, Problems)

### Phase 2: Code Editor Integration
- Sora-Editor integration
- Syntax highlighting
- Auto-completion
- Error indicators

### Phase 3: File Management
- Project creation/opening
- File operations (create, delete, rename)
- Project templates
- Storage Access Framework integration

### Phase 4: Build Engine
- Resource compilation (aapt2)
- Java compilation (javac)
- Dex conversion (d8)
- APK packaging and signing

### Phase 5: Advanced Features
- Dependency management
- Logcat integration
- APK installation
- Debugging support

## Performance Considerations

- **Memory**: Limit JVM heap to 256MB for compilation tools
- **Storage**: Cache compiled artifacts and dependencies
- **CPU**: Use incremental builds when possible
- **UI**: Offload heavy operations to background threads

## Security

- File isolation using app's private directory
- Keystore management for APK signing
- Minimal permissions requested
- FileProvider for secure file sharing
