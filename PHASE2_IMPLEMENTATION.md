# Phase 2: Code Editor Integration & Business Logic

## Overview
Phase 2 implements the core business logic managers and integrates Sora-Editor for advanced code editing capabilities.

## Components Implemented

### 1. CodeEditor.kt - Code Editing Engine
**Purpose**: Manages all code editing operations with Sora-Editor

**Key Features**:
- TextMate language support for Java, Kotlin, XML
- Syntax highlighting with custom color scheme
- Dark theme optimized for mobile
- Auto-completion
- Code folding
- Error indicators
- Line numbers
- Bracket matching
- Find & Replace functionality
- Code formatting (basic indentation)

**Main Methods**:
```kotlin
initializeEditor(codeEditor: CodeEditor)
setLanguage(language: String)
loadCode(code: String)
getCode(): String
undo() / redo()
findText(query: String): Int
replaceText(oldText: String, newText: String)
getCursorPosition(): Pair<Int, Int>
formatCode()
getLineCount(): Int
getCharCount(): Int
hasUnsavedChanges(): Boolean
```

**Color Scheme**:
- Keywords: Blue (#569CD6)
- Strings: Orange (#CE9178)
- Comments: Green (#6A9955)
- Numbers: Light Green (#B5CEA8)
- Functions: Yellow (#DCDCAA)
- Classes: Cyan (#4EC9B0)
- Operators: Light Gray (#D4D4D4)
- Errors: Red (#F44336)
- Warnings: Orange (#FF9800)

**Supported Languages**:
- Java (.java)
- Kotlin (.kt)
- XML (.xml)
- Gradle (.gradle)
- Properties (.properties)
- JSON (.json)
- Markdown (.md)

---

### 2. ProjectManager.kt - Project Management
**Purpose**: Handles project creation, opening, and file operations

**Key Features**:
- Create new Android projects with standard structure
- Open existing projects
- Load project files recursively
- File operations (create, read, write, delete, rename)
- Project templates
- Package name extraction
- Project listing and deletion

**Project Structure Created**:
```
MyProject/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/app/
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── build.gradle.kts
```

**Main Methods**:
```kotlin
createProject(projectName, packageName, minSdk, targetSdk): Project
openProject(projectPath): Project?
getCurrentProject(): Project?
saveFile(filePath, content): Boolean
createFile(projectPath, fileName, content): Boolean
deleteFile(filePath): Boolean
renameFile(oldPath, newName): Boolean
getAllProjects(): List<Project>
deleteProject(projectName): Boolean
exportProjectAsZip(projectPath, outputPath): Boolean
importProjectFromZip(zipPath): Boolean
```

**Data Classes**:
```kotlin
data class Project(
    val name: String,
    val path: String,
    val packageName: String,
    val minSdk: Int = 26,
    val targetSdk: Int = 34,
    val createdAt: Long,
    val files: MutableList<ProjectFile>
)

data class ProjectFile(
    val name: String,
    val path: String,
    val content: String,
    val isDirectory: Boolean,
    val children: MutableList<ProjectFile>
)
```

---

### 3. BuildManager.kt - Build Pipeline
**Purpose**: Orchestrates the complete build process

**Build Pipeline**:
```
1. Prepare Build Environment
   ↓
2. Compile Resources (aapt2 simulation)
   ├── Generate R.java
   └── Copy resources
   ↓
3. Compile Java/Kotlin (javac simulation)
   └── Generate .class files
   ↓
4. Convert to DEX (d8 simulation)
   └── Generate classes.dex
   ↓
5. Package APK
   └── Create APK structure
   ↓
6. Sign APK (apksigner simulation)
   └── Generate signed APK
```

**Key Features**:
- Complete build pipeline simulation
- Build logging with multiple log levels
- Build caching
- Build time tracking
- Error and warning collection
- Clean build functionality
- Build cache management

**Main Methods**:
```kotlin
build(project: Project): BuildResult
prepareBuild(project: Project)
compileResources(project: Project)
compileJavaSource(project: Project)
convertToDex(project: Project)
packageApk(project: Project): String
signApk(apkPath: String)
cleanBuild(project: Project)
getBuildLogs(): List<LogEntry>
getBuildLogsAsString(): String
clearBuildCache()
isBuilding(): Boolean
getLastBuildResult(): BuildResult?
```

**Data Classes**:
```kotlin
data class BuildResult(
    val success: Boolean,
    val message: String,
    val apkPath: String? = null,
    val errors: List<String>,
    val warnings: List<String>,
    val buildTime: Long
)

data class LogEntry(
    val timestamp: Long,
    val level: LogLevel,
    val message: String,
    val tag: String
)

enum class LogLevel {
    INFO, WARNING, ERROR, DEBUG
}
```

---

### 4. FileManager.kt - File Operations
**Purpose**: Handles all file system operations

**Key Features**:
- File read/write operations
- Directory operations
- File search and filtering
- File metadata (size, modified time)
- File templates
- Backup and restore
- Storage Access Framework integration

**Main Methods**:
```kotlin
readFile(filePath: String): String?
writeFile(filePath: String, content: String): Boolean
createDirectory(dirPath: String): Boolean
deleteFile(filePath: String): Boolean
deleteDirectory(dirPath: String): Boolean
copyFile(sourcePath: String, destPath: String): Boolean
moveFile(sourcePath: String, destPath: String): Boolean
renameFile(filePath: String, newName: String): Boolean
fileExists(filePath: String): Boolean
getFileSize(filePath: String): Long
getFileSizeFormatted(filePath: String): String
listFiles(dirPath: String): List<File>
listFilesRecursive(dirPath: String): List<File>
searchFiles(dirPath: String, query: String): List<File>
searchFilesByExtension(dirPath: String, extension: String): List<File>
createFileFromTemplate(filePath: String, templateType: String): Boolean
getDirectorySize(dirPath: String): Long
createBackup(filePath: String): Boolean
restoreFromBackup(filePath: String): Boolean
```

**File Templates**:
- Java Class
- Kotlin Class
- XML Layout
- Android Manifest

---

## Integration Points

### CodeEditor Integration
```kotlin
// Initialize
val codeEditorManager = CodeEditorManager(context)
codeEditorManager.initializeEditor(codeEditor)

// Load file
val content = fileManager.readFile(filePath)
codeEditorManager.loadCode(content)

// Save file
val code = codeEditorManager.getCode()
fileManager.writeFile(filePath, code)
```

### Project Management Integration
```kotlin
// Create project
val project = projectManager.createProject(
    "MyApp",
    "com.example.myapp"
)

// Load project files
projectManager.openProject(project.path)

// Save edited file
projectManager.saveFile(filePath, editedContent)
```

### Build Integration
```kotlin
// Build project
val result = buildManager.build(project)

// Get logs
val logs = buildManager.getBuildLogs()

// Handle result
if (result.success) {
    installApk(result.apkPath)
} else {
    showErrors(result.errors)
}
```

---

## Dependencies Added

```gradle
// Code Editor
implementation("io.github.rosemoe:editor:0.24.4")
implementation("io.github.rosemoe:language-textmate:0.24.4")

// File operations
implementation("androidx.documentfile:documentfile:1.0.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Serialization
implementation("org.json:json:20231013")
```

---

## Next Phase (Phase 3)

Phase 3 will integrate these managers into the UI:
- Connect CodeEditor to UI
- Implement project creation/opening dialogs
- Add build button and progress indicator
- Display build logs in bottom panel
- Implement file tree interaction
- Add save/undo/redo functionality

---

## Testing Checklist

- [ ] CodeEditor initializes with correct theme
- [ ] Syntax highlighting works for all languages
- [ ] Project creation generates correct structure
- [ ] File operations work correctly
- [ ] Build pipeline completes successfully
- [ ] Build logs display correctly
- [ ] File templates generate correct content
- [ ] Project opening loads all files
- [ ] Undo/redo functionality works
- [ ] Find and replace works

---

## Performance Considerations

- **Memory**: Limit editor buffer for large files
- **Build**: Run builds on background thread
- **File I/O**: Use coroutines for file operations
- **Caching**: Cache compiled resources
- **Storage**: Monitor cache size and cleanup

---

## Known Limitations

1. **Build Simulation**: Current build pipeline is simulated. Real implementation requires:
   - Actual javac/kotlinc integration
   - Real d8 dexer
   - Real aapt2 resource compiler
   - APK signing with keystore

2. **Editor**: Sora-Editor integration is basic. Can be enhanced with:
   - Advanced auto-completion
   - Code refactoring
   - Debugging support
   - Real-time error checking

3. **File System**: Uses app's private storage. Can be extended with:
   - Storage Access Framework for external storage
   - Cloud storage integration
   - Version control support

---

## Future Enhancements

1. Real build tools integration
2. Gradle support
3. Maven dependency resolution
4. Git integration
5. Remote debugging
6. Emulator integration
7. Device testing
8. Performance profiling
9. Memory analysis
10. Code analysis and linting
