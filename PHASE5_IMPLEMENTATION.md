# Phase 5: Project Templates & Advanced Features

## Overview
Phase 5 implements project templates, advanced build options, and dependency management for complete project scaffolding and configuration.

## Components Created

### 1. ProjectTemplates.kt - Project Template System
**Purpose**: Pre-built project templates for quick project creation

**Available Templates**:

#### 1.1 Empty Project
- Minimal Android project structure
- Basic build configuration
- Single MainActivity
- Empty layout file
- Resources (strings, colors)

#### 1.2 Basic Activity
- Multiple activities (MainActivity, SecondActivity)
- Activity navigation
- Layout files for each activity
- Button interactions
- Intent-based navigation

#### 1.3 Jetpack Compose
- Modern Compose-based UI
- Material Design 3 theme
- Composable functions
- Preview support
- State management with remember

#### 1.4 Android Library
- Library module configuration
- Reusable utilities
- Unit tests
- No launcher activity
- Exportable as AAR

#### 1.5 Multi-Module Project
- App module
- Library module
- Module dependencies
- Shared utilities
- Modular architecture

**Key Features**:
```kotlin
// Get all templates
val templates = ProjectTemplates.getAvailableTemplates()

// Create project from template
ProjectTemplates.createProjectFromTemplate(
    projectPath = "/path/to/project",
    packageName = "com.example.app",
    template = templates[0]
)
```

**Template Content**:
- build.gradle.kts (root and app/lib)
- settings.gradle.kts
- AndroidManifest.xml
- Kotlin source files
- XML layout files
- Resource files (strings, colors)

---

### 2. TemplateSelectorDialog.kt - Template Selection UI
**Purpose**: User-friendly template selection interface

**Components**:
- **TemplateSelectorDialog**: Main dialog
- **TemplateCard**: Individual template card
- **TemplateSelectorPreview**: Preview component

**Features**:
- Visual template list
- Icon and description for each template
- Selection indicator (checkmark)
- Cancel/Create buttons
- Scrollable list for many templates

**Usage**:
```kotlin
TemplateSelectorDialog(
    templates = ProjectTemplates.getAvailableTemplates(),
    onDismiss = { /* close dialog */ },
    onTemplateSelected = { template ->
        // Create project with template
    }
)
```

---

### 3. BuildOptions.kt - Advanced Build Configuration
**Purpose**: Configure build behavior and optimization

**Build Options**:
```kotlin
data class BuildOptions(
    val minifyEnabled: Boolean = false,      // Shrink & obfuscate
    val debuggable: Boolean = true,          // Enable debugging
    val shrinkResources: Boolean = false,    // Remove unused resources
    val multiDex: Boolean = false,           // Support 65K+ methods
    val incremental: Boolean = true,         // Incremental builds
    val parallel: Boolean = true,            // Parallel compilation
    val cacheEnabled: Boolean = true,        // Build caching
    val optimizeEnabled: Boolean = false     // Code optimization
)
```

**UI Components**:
- **BuildOptionsDialog**: Settings dialog
- **BuildOptionItem**: Individual option with checkbox

**Features**:
- Checkbox for each option
- Description for each setting
- Reset to defaults button
- Apply/Cancel buttons
- Scrollable option list

**Usage**:
```kotlin
BuildOptionsDialog(
    onDismiss = { /* close */ },
    onApply = { options ->
        // Apply build options
    },
    initialOptions = BuildOptions()
)
```

---

### 4. DependencyManager.kt - Dependency Management
**Purpose**: Manage project dependencies and versions

**Key Methods**:
```kotlin
// Get dependencies
val deps = dependencyManager.getDependencies()

// Add dependency
dependencyManager.addDependency(dependency)

// Remove dependency
dependencyManager.removeDependency(dependencyId)

// Update version
dependencyManager.updateDependency(dependencyId, newVersion)

// Check conflicts
val conflicts = dependencyManager.checkConflicts()

// Get dependency tree
val tree = dependencyManager.getDependencyTree()
```

**Available Dependencies**:

**AndroidX**:
- androidx.core:core-ktx:1.12.0
- androidx.appcompat:appcompat:1.6.1
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.2

**Jetpack Compose**:
- androidx.compose.ui:ui:1.5.0
- androidx.compose.material3:material3:1.1.0
- androidx.activity:activity-compose:1.7.2

**Material Design**:
- com.google.android.material:material:1.10.0

**Networking**:
- com.squareup.okhttp3:okhttp:4.11.0
- com.squareup.retrofit2:retrofit:2.9.0

**JSON**:
- com.google.code.gson:gson:2.10.1

**Database**:
- androidx.room:room-runtime:2.5.2

**Testing**:
- junit:junit:4.13.2
- androidx.test.ext:junit:1.1.5

**Features**:
- Parse dependencies from build.gradle
- Add/remove dependencies
- Update versions
- Detect conflicts
- Generate dependency tree

---

## Integration with ViewModel

### Updated AppViewModel Methods

```kotlin
// Template operations
fun createProjectFromTemplate(
    projectName: String,
    packageName: String,
    template: ProjectTemplate
)

// Build options
fun applyBuildOptions(options: BuildOptions)

// Dependency management
fun addDependency(dependency: Dependency)
fun removeDependency(dependencyId: String)
fun updateDependency(dependencyId: String, newVersion: String)
fun getDependencies(): List<Dependency>
fun checkDependencyConflicts(): List<String>
```

---

## Workflow Examples

### Creating Project from Template

```
1. User clicks "New Project"
    ↓
2. TemplateSelectorDialog opens
    ↓
3. User selects template
    ↓
4. User enters project name and package
    ↓
5. ViewModel.createProjectFromTemplate() called
    ↓
6. ProjectTemplates.createProjectFromTemplate() executes
    ↓
7. All template files created
    ↓
8. Project opens in IDE
```

### Configuring Build Options

```
1. User clicks "Build Options"
    ↓
2. BuildOptionsDialog opens
    ↓
3. User configures options
    ↓
4. User clicks "Apply"
    ↓
5. Options saved to build.gradle
    ↓
6. Next build uses new options
```

### Managing Dependencies

```
1. User clicks "Dependencies"
    ↓
2. Dependency list displayed
    ↓
3. User searches for dependency
    ↓
4. User clicks "Add"
    ↓
5. DependencyManager.addDependency() called
    ↓
6. Dependency added to build.gradle
    ↓
7. Gradle sync triggered
```

---

## Template File Structure

### Empty Project
```
MyProject/
├── build.gradle.kts
├── settings.gradle.kts
└── app/
    ├── build.gradle.kts
    ├── src/main/
    │   ├── AndroidManifest.xml
    │   ├── java/com/example/app/
    │   │   └── MainActivity.kt
    │   └── res/
    │       ├── layout/activity_main.xml
    │       ├── values/strings.xml
    │       └── values/colors.xml
```

### Compose Project
```
MyProject/
├── build.gradle.kts
├── settings.gradle.kts
└── app/
    ├── build.gradle.kts (with Compose dependencies)
    ├── src/main/
    │   ├── AndroidManifest.xml
    │   ├── java/com/example/compose/
    │   │   ├── MainActivity.kt
    │   │   └── ui/
    │   │       ├── theme/Theme.kt
    │   │       └── screens/HomeScreen.kt
    │   └── res/
    │       ├── values/strings.xml
    │       └── values/colors.xml
```

### Multi-Module Project
```
MyProject/
├── build.gradle.kts
├── settings.gradle.kts
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/...
└── lib/
    ├── build.gradle.kts
    ├── src/main/
    │   ├── AndroidManifest.xml
    │   └── java/...
    └── src/test/
        └── java/...
```

---

## Gradle Content

### Root build.gradle.kts
```kotlin
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
```

### App build.gradle.kts
```kotlin
plugins {
    id("com.android.application")
    id("kotlin-android")
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
    // ... more configuration
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    // ... more dependencies
}
```

---

## Performance Considerations

1. **Template Loading**: Templates loaded from memory
2. **File Creation**: Batch file operations for efficiency
3. **Dependency Parsing**: Regex-based parsing for speed
4. **Build Options**: Stored in build.gradle for persistence

---

## Testing Checklist

- [ ] All templates create successfully
- [ ] Template files are correct
- [ ] Package names are replaced correctly
- [ ] Build options dialog displays
- [ ] Build options apply correctly
- [ ] Dependencies add/remove correctly
- [ ] Dependency conflicts detected
- [ ] Dependency tree generates
- [ ] Template selector UI works
- [ ] Build options persist

---

## Known Limitations

1. **Template Customization**: Limited to predefined templates
   - Can be extended with custom templates

2. **Dependency Versions**: Hardcoded versions
   - Should fetch from Maven Central

3. **Gradle Syntax**: KTS format only
   - Groovy format not supported

4. **Conflict Detection**: Basic implementation
   - Should use dependency graph analysis

---

## Future Enhancements

1. Custom template creation
2. Maven Central dependency search
3. Dependency version checking
4. Gradle plugin management
5. Build variant configuration
6. Signing configuration
7. ProGuard rules editor
8. Manifest editor
9. Resource manager
10. Gradle properties editor

---

## Integration Points

### With AppViewModel
```kotlin
val projectManager = ProjectManager(context)
val dependencyManager = DependencyManager(projectPath)

// Create from template
val template = ProjectTemplates.getAvailableTemplates()[0]
ProjectTemplates.createProjectFromTemplate(
    projectPath,
    packageName,
    template
)

// Add dependency
dependencyManager.addDependency(dependency)
```

### With UI
```kotlin
// Show template selector
if (state.showTemplateSelector) {
    TemplateSelectorDialog(
        templates = ProjectTemplates.getAvailableTemplates(),
        onTemplateSelected = { template ->
            viewModel.createProjectFromTemplate(name, package, template)
        }
    )
}

// Show build options
if (state.showBuildOptions) {
    BuildOptionsDialog(
        onApply = { options ->
            viewModel.applyBuildOptions(options)
        }
    )
}
```

---

## Summary

Phase 5 provides:
- ✅ 5 pre-built project templates
- ✅ Template selection UI
- ✅ Advanced build options
- ✅ Dependency management system
- ✅ Gradle file generation
- ✅ Complete project scaffolding

Ready for Phase 6: Testing & APK Build
