# Phase 4: Build Engine Integration & System Integration

## Overview
Phase 4 integrates the build system with the UI, adds state management, APK installation, and device simulation capabilities.

## Components Created

### 1. AppViewModel.kt - State Management
**Purpose**: Central state management using Jetpack Compose StateFlow

**Key Responsibilities**:
- Manage application state
- Coordinate between UI and business logic
- Handle async operations with coroutines
- Manage dialogs and user interactions

**Main Functions**:
```kotlin
// Project Operations
createProject(projectName, packageName)
openProject(projectPath)
getAllProjects()

// File Operations
selectFile(file)
closeTab(index)
switchTab(index)
updateFileContent(filePath, content)
saveFile(filePath, content)
saveCurrentFile()
createNewFile(fileName, fileType)
deleteFile(filePath)

// Build Operations
buildProject()
cleanBuild()

// Dialog Management
showNewProjectDialog() / hideNewProjectDialog()
showNewFileDialog() / hideNewFileDialog()
showSettingsDialog() / hideSettingsDialog()

// Error Handling
clearErrorMessage()
```

**State Flow**:
```
User Action
    ↓
ViewModel Method
    ↓
Manager Operation (async)
    ↓
Update AppState
    ↓
Recompose UI
```

**AppState Data Class**:
```kotlin
data class AppState(
    val currentProject: Project? = null,
    val projectFiles: List<ProjectFile> = emptyList(),
    val openFiles: List<EditorTab> = emptyList(),
    val activeTabIndex: Int = 0,
    val buildLogs: List<LogEntry> = emptyList(),
    val isBuilding: Boolean = false,
    val buildProgress: Int = 0,
    val buildResult: BuildResult? = null,
    val selectedFile: ProjectFile? = null,
    val showNewProjectDialog: Boolean = false,
    val showNewFileDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val errorMessage: String? = null
)
```

---

### 2. MainActivity.kt - Updated Integration
**Purpose**: Main activity with full ViewModel integration

**Features**:
- ViewModel instantiation with factory
- State collection and UI updates
- Dialog management
- Build progress display
- Error handling

**Integration Points**:
```kotlin
// Create ViewModel
val viewModel: AppViewModel = viewModel(factory = ...)

// Collect state
val state by viewModel.appState.collectAsState()

// Pass to UI
IDEScreen(
    projectName = state.currentProject?.name ?: "No Project",
    projectFiles = state.projectFiles,
    buildLogs = state.buildLogs,
    onNewProject = { viewModel.showNewProjectDialog() },
    onSave = { viewModel.saveCurrentFile() },
    onRun = { viewModel.buildProject() },
    onSettings = { viewModel.showSettingsDialog() }
)

// Show dialogs
if (state.showNewProjectDialog) {
    NewProjectDialog(...)
}

// Build progress
BuildProgressDialog(
    isBuilding = state.isBuilding,
    buildProgress = state.buildProgress
)
```

---

### 3. APKInstaller.kt - APK Management
**Purpose**: Install, uninstall, and manage APK files

**Key Features**:
- Install APK on device
- Uninstall applications
- Launch installed apps
- Get APK information
- Check package installation status
- List installed packages

**Main Methods**:
```kotlin
// Installation
installAPK(apkPath: String): Boolean
uninstallAPK(packageName: String): Boolean

// App Management
launchApp(packageName: String): Boolean
isPackageInstalled(packageName: String): Boolean
getInstalledPackages(): List<String>

// Information
getAPKInfo(apkPath: String): APKInfo?
```

**APKInfo Data Class**:
```kotlin
data class APKInfo(
    val name: String,
    val path: String,
    val size: Long,
    val lastModified: Long
) {
    fun getSizeFormatted(): String
}
```

**Usage Example**:
```kotlin
val installer = APKInstaller(context)

// Install built APK
installer.installAPK("/path/to/app-signed.apk")

// Launch app
installer.launchApp("com.example.myapp")

// Check if installed
if (installer.isPackageInstalled("com.example.myapp")) {
    // App is installed
}
```

---

### 4. DeviceSimulator.kt - Device Information
**Purpose**: Get device capabilities and provide build recommendations

**Key Features**:
- Get device information (manufacturer, model, Android version)
- Get device capabilities (memory, CPU, storage)
- Check build feasibility
- Provide build recommendations

**Main Methods**:
```kotlin
// Device Info
getDeviceInfo(): DeviceInfo
getDeviceCapabilities(): DeviceCapabilities
getStorageInfo(): StorageInfo

// Build Checks
canBuild(): Boolean
getBuildRecommendations(): List<String>
```

**Data Classes**:
```kotlin
data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val device: String,
    val androidVersion: String,
    val sdkInt: Int,
    ...
)

data class DeviceCapabilities(
    val totalMemory: Long,
    val freeMemory: Long,
    val usedMemory: Long,
    val processorCount: Int,
    val maxMemory: Long
)

data class StorageInfo(
    val cacheSize: Long,
    val filesSize: Long,
    val totalSize: Long
)
```

**Usage Example**:
```kotlin
val simulator = DeviceSimulator(context)

// Check device
val deviceInfo = simulator.getDeviceInfo()
println("Device: ${deviceInfo.getDisplayName()}")

// Check capabilities
val capabilities = simulator.getDeviceCapabilities()
println("Free Memory: ${capabilities.getFreeMemoryMB()}MB")

// Check if can build
if (simulator.canBuild()) {
    // Proceed with build
} else {
    // Show recommendations
    simulator.getBuildRecommendations().forEach { println(it) }
}
```

---

### 5. BuildUtils.kt - Build Utilities
**Purpose**: Helper functions for build process

**Key Features**:
- Validate project structure
- Get source and resource files
- Estimate build time
- Detect compilation errors
- Check missing dependencies
- Generate build summary

**Main Methods**:
```kotlin
// Validation
validateProjectStructure(projectPath): ValidationResult

// File Operations
getSourceFiles(projectPath): List<File>
getResourceFiles(projectPath): List<File>

// Build Analysis
estimateBuildTime(projectPath): Long
getBuildCacheSize(projectPath): Long
cleanBuildCache(projectPath): Boolean
getCompilationErrors(projectPath): List<CompilationError>

// Dependency Check
checkMissingDependencies(projectPath): List<String>

// Output Formatting
formatBuildOutput(logs): String
parseBuildErrors(output): List<String>
getBuildSummary(result): String
```

**Data Classes**:
```kotlin
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)

data class CompilationError(
    val file: String,
    val line: Int,
    val message: String,
    val severity: ErrorSeverity
)

enum class ErrorSeverity {
    INFO, WARNING, ERROR
}
```

**Usage Example**:
```kotlin
// Validate project
val validation = BuildUtils.validateProjectStructure(projectPath)
if (!validation.isValid) {
    validation.errors.forEach { println("Error: $it") }
}

// Get build info
val sourceCount = BuildUtils.getSourceFiles(projectPath).size
val estimatedTime = BuildUtils.estimateBuildTime(projectPath)
println("$sourceCount files, estimated ${estimatedTime}ms")

// Check dependencies
val missing = BuildUtils.checkMissingDependencies(projectPath)
if (missing.isNotEmpty()) {
    println("Missing: ${missing.joinToString()}")
}
```

---

## Integration Flow

### Build Workflow
```
User clicks "Run"
    ↓
ViewModel.buildProject()
    ↓
Save all open files
    ↓
BuildManager.build(project)
    ├── Validate project structure
    ├── Compile resources
    ├── Compile Java/Kotlin
    ├── Convert to DEX
    ├── Package APK
    └── Sign APK
    ↓
Update AppState with result
    ↓
Display build logs
    ↓
If successful: APKInstaller.installAPK()
```

### File Operations Workflow
```
User selects file
    ↓
ViewModel.selectFile(file)
    ↓
Add to open files list
    ↓
Update AppState
    ↓
UI displays file in editor
    ↓
User edits and saves
    ↓
ViewModel.saveFile()
    ↓
FileManager.writeFile()
    ↓
Update tab modified state
```

---

## State Management Pattern

### Reactive Flow
```
UI Event
    ↓
ViewModel Method
    ↓
Coroutine Launch
    ↓
Manager Operation
    ↓
Update MutableStateFlow
    ↓
Collect in Composable
    ↓
Recompose
```

### Example: Build Project
```kotlin
// In Composable
Button(onClick = { viewModel.buildProject() })

// In ViewModel
fun buildProject() {
    viewModelScope.launch {
        _appState.value = _appState.value.copy(isBuilding = true)
        val result = buildManager.build(project)
        _appState.value = _appState.value.copy(
            isBuilding = false,
            buildResult = result,
            buildLogs = buildManager.getBuildLogs()
        )
    }
}

// In Composable
BuildProgressDialog(
    isBuilding = state.isBuilding,
    buildProgress = state.buildProgress
)
```

---

## Error Handling

### Error Flow
```
Exception occurs
    ↓
Catch in try-catch
    ↓
Update errorMessage in AppState
    ↓
UI displays error
    ↓
User dismisses
    ↓
ViewModel.clearErrorMessage()
```

### Example
```kotlin
try {
    val project = projectManager.createProject(name, package)
} catch (e: Exception) {
    _appState.value = _appState.value.copy(
        errorMessage = "Failed: ${e.message}"
    )
}
```

---

## Performance Considerations

1. **Coroutines**: All heavy operations run on background threads
2. **State Updates**: Minimal state updates to avoid unnecessary recompositions
3. **Lazy Loading**: Large file lists use LazyColumn
4. **Memory**: Build cache is limited and cleaned regularly
5. **Threading**: Build operations don't block UI

---

## Testing Checklist

- [ ] ViewModel creates and opens projects
- [ ] File selection works
- [ ] Tab switching works
- [ ] Build process completes
- [ ] Build logs display correctly
- [ ] APK installation works
- [ ] Device info displays
- [ ] Error messages show
- [ ] Dialogs open/close
- [ ] State updates correctly

---

## Next Phase (Phase 5)

Phase 5 will add:
- Project templates
- Advanced build options
- Gradle integration
- Dependency management
- Real-time error checking
- Code analysis
- Performance profiling

---

## Known Limitations

1. **Build Simulation**: Current build is simulated
   - Real implementation needs actual javac/d8 integration

2. **APK Installation**: Requires user confirmation
   - System handles actual installation

3. **Device Info**: Read-only
   - Cannot modify device settings

4. **Build Cache**: Limited cleanup
   - May need manual cleanup for large projects

---

## Future Enhancements

1. Gradle build system integration
2. Maven dependency resolution
3. Real javac/kotlinc compilation
4. Real d8 dexer
5. Real aapt2 resource compiler
6. Incremental builds
7. Build caching
8. Parallel compilation
9. Build profiling
10. Remote build support
