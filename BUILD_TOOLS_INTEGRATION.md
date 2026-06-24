# Android Studio Mini - Build Tools Integration Guide

## အကျဉ်းချုပ်

Android Studio Mini IDE အတွင်း **aapt2, d8, apksigner** စသည့် Android build tools များကို အသုံးပြုရန် အကြံပြုချက်များ ရှင်းပြထားသည်။

---

## 1. aapt2 (Android Asset Packaging Tool 2)

### အခန်းကဏ္ဍ
- Resource files (XML, images, etc.) ကို compile လုပ်ခြင်း
- Resource table ဖန်တီးခြင်း
- APK ထဲသို့ resources ထည့်သွင်းခြင်း

### IDE အတွင်း အသုံးပြုခြင်း

#### Step 1: aapt2 Binary ကို Download လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် aapt2 ကို download လုပ်ခြင်း
fun downloadAapt2() {
    val aapt2Url = "https://dl.google.com/android/repository/aapt2-7.4.0-linux.zip"
    val destinationPath = "/data/data/com.example.androidstudiomini/aapt2"
    
    // Download လုပ်ခြင်း
    downloadFile(aapt2Url, destinationPath)
    
    // Execute permission ပေးခြင်း
    File(destinationPath).setExecutable(true)
}
```

#### Step 2: Resource Compilation

```kotlin
// BuildManager.kt တွင် resource compile လုပ်ခြင်း
fun compileResources(projectPath: String): Boolean {
    val resPath = "$projectPath/app/src/main/res"
    val outputPath = "$projectPath/app/build/intermediates/compiled_res"
    
    val command = """
        ./aapt2 compile \
            --dir $resPath \
            -o $outputPath/resources.zip
    """.trimIndent()
    
    return executeCommand(command)
}
```

#### Step 3: Resource Linking

```kotlin
// BuildManager.kt တွင် resource link လုပ်ခြင်း
fun linkResources(projectPath: String): Boolean {
    val compiledRes = "$projectPath/app/build/intermediates/compiled_res/resources.zip"
    val manifest = "$projectPath/app/src/main/AndroidManifest.xml"
    val outputApk = "$projectPath/app/build/intermediates/linked_res/resources.apk"
    
    val command = """
        ./aapt2 link \
            -o $outputApk \
            --manifest $manifest \
            $compiledRes
    """.trimIndent()
    
    return executeCommand(command)
}
```

### IDE UI Integration

```kotlin
// BuildManager.kt တွင် UI update လုပ်ခြင်း
fun compilResourcesWithProgress(projectPath: String) {
    viewModel.updateBuildLog("[INFO] Compiling resources...")
    
    val success = compileResources(projectPath)
    if (success) {
        viewModel.updateBuildLog("[SUCCESS] Resources compiled")
        viewModel.updateBuildProgress(25) // 25% complete
    } else {
        viewModel.updateBuildLog("[ERROR] Resource compilation failed")
    }
}
```

---

## 2. javac (Java Compiler)

### အခန်းကဏ္ဍ
- Java source files (.java) ကို compile လုပ်ခြင်း
- Bytecode (.class) ဖန်တီးခြင်း

### IDE အတွင်း အသုံးပြုခြင်း

#### Step 1: Java Source Files ကို Compile လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် Java compile လုပ်ခြင်း
fun compileJavaSource(projectPath: String): Boolean {
    val srcPath = "$projectPath/app/src/main/java"
    val outputPath = "$projectPath/app/build/intermediates/classes"
    val classpath = getAndroidClasspath()
    
    val command = """
        javac \
            -d $outputPath \
            -cp $classpath \
            -source 11 \
            -target 11 \
            $(find $srcPath -name "*.java" | tr '\n' ' ')
    """.trimIndent()
    
    return executeCommand(command)
}

fun getAndroidClasspath(): String {
    val androidJar = "/path/to/android/sdk/platforms/android-34/android.jar"
    val supportLibs = "/path/to/android/sdk/extras/android/support"
    
    return "$androidJar:$supportLibs/*"
}
```

#### Step 2: Kotlin Source Files ကို Compile လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် Kotlin compile လုပ်ခြင်း
fun compileKotlinSource(projectPath: String): Boolean {
    val srcPath = "$projectPath/app/src/main/java"
    val outputPath = "$projectPath/app/build/intermediates/classes"
    val classpath = getAndroidClasspath()
    
    val command = """
        kotlinc \
            -d $outputPath \
            -cp $classpath \
            -jvm-target 11 \
            $(find $srcPath -name "*.kt" | tr '\n' ' ')
    """.trimIndent()
    
    return executeCommand(command)
}
```

### IDE UI Integration

```kotlin
// BuildManager.kt တွင် compilation progress ပြသခြင်း
fun compileSourcesWithProgress(projectPath: String) {
    viewModel.updateBuildLog("[INFO] Compiling Java/Kotlin sources...")
    
    val javaSuccess = compileJavaSource(projectPath)
    val kotlinSuccess = compileKotlinSource(projectPath)
    
    if (javaSuccess && kotlinSuccess) {
        viewModel.updateBuildLog("[SUCCESS] Source compilation complete")
        viewModel.updateBuildProgress(50) // 50% complete
    } else {
        viewModel.updateBuildLog("[ERROR] Source compilation failed")
    }
}
```

---

## 3. d8 (DEX Compiler)

### အခန်းကဏ္ဍ
- Java bytecode (.class) ကို DEX format သို့ convert လုပ်ခြင်း
- Multi-DEX support ပေးခြင်း
- Optimization လုပ်ခြင်း

### IDE အတွင်း အသုံးပြုခြင်း

#### Step 1: d8 Binary ကို Download လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် d8 ကို download လုပ်ခြင်း
fun downloadD8() {
    val d8Url = "https://dl.google.com/android/repository/d8-7.4.0.zip"
    val destinationPath = "/data/data/com.example.androidstudiomini/d8"
    
    downloadFile(d8Url, destinationPath)
    File(destinationPath).setExecutable(true)
}
```

#### Step 2: Bytecode ကို DEX သို့ Convert လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် DEX conversion လုပ်ခြင်း
fun convertToDex(projectPath: String): Boolean {
    val classesPath = "$projectPath/app/build/intermediates/classes"
    val outputPath = "$projectPath/app/build/intermediates/dex"
    
    val command = """
        ./d8 \
            --output=$outputPath \
            --min-api=26 \
            $(find $classesPath -name "*.class" | tr '\n' ' ')
    """.trimIndent()
    
    return executeCommand(command)
}
```

#### Step 3: Multi-DEX Support

```kotlin
// BuildManager.kt တွင် Multi-DEX support လုပ်ခြင်း
fun convertToDexWithMultiDex(projectPath: String): Boolean {
    val classesPath = "$projectPath/app/build/intermediates/classes"
    val outputPath = "$projectPath/app/build/intermediates/dex"
    
    val command = """
        ./d8 \
            --output=$outputPath \
            --min-api=26 \
            --multi-dex \
            $(find $classesPath -name "*.class" | tr '\n' ' ')
    """.trimIndent()
    
    return executeCommand(command)
}
```

### IDE UI Integration

```kotlin
// BuildManager.kt တွင် DEX conversion progress ပြသခြင်း
fun convertToDexWithProgress(projectPath: String) {
    viewModel.updateBuildLog("[INFO] Converting to DEX format...")
    
    val success = convertToDex(projectPath)
    if (success) {
        viewModel.updateBuildLog("[SUCCESS] DEX conversion complete")
        viewModel.updateBuildProgress(75) // 75% complete
    } else {
        viewModel.updateBuildLog("[ERROR] DEX conversion failed")
    }
}
```

---

## 4. apksigner (APK Signer)

### အခန်းကဏ္ဍ
- APK ဖိုင်ကို sign လုပ်ခြင်း
- Security verification လုပ်ခြင်း
- Release/Debug signing ပေးခြင်း

### IDE အတွင်း အသုံးပြုခြင်း

#### Step 1: apksigner Binary ကို Download လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် apksigner ကို download လုပ်ခြင်း
fun downloadApksigner() {
    val apksignerUrl = "https://dl.google.com/android/repository/apksigner-7.4.0.zip"
    val destinationPath = "/data/data/com.example.androidstudiomini/apksigner"
    
    downloadFile(apksignerUrl, destinationPath)
    File(destinationPath).setExecutable(true)
}
```

#### Step 2: Debug Key ဖန်တီးခြင်း

```kotlin
// BuildManager.kt တွင် debug key ဖန်တီးခြင်း
fun createDebugKey(): Boolean {
    val keyPath = "/data/data/com.example.androidstudiomini/debug.keystore"
    
    val command = """
        keytool -genkey \
            -v -keystore $keyPath \
            -keyalg RSA \
            -keysize 2048 \
            -validity 10000 \
            -alias debug \
            -storepass android \
            -keypass android \
            -dname "CN=Android Studio Mini, O=Development, C=MM"
    """.trimIndent()
    
    return executeCommand(command)
}
```

#### Step 3: APK ကို Sign လုပ်ခြင်း

```kotlin
// BuildManager.kt တွင် APK sign လုပ်ခြင်း
fun signApk(apkPath: String, outputPath: String): Boolean {
    val keyPath = "/data/data/com.example.androidstudiomini/debug.keystore"
    
    val command = """
        ./apksigner sign \
            --ks $keyPath \
            --ks-pass pass:android \
            --ks-key-alias debug \
            --key-pass pass:android \
            --out $outputPath \
            $apkPath
    """.trimIndent()
    
    return executeCommand(command)
}
```

#### Step 4: APK Alignment

```kotlin
// BuildManager.kt တွင် APK align လုပ်ခြင်း
fun alignApk(inputApk: String, outputApk: String): Boolean {
    val command = """
        zipalign -v 4 $inputApk $outputApk
    """.trimIndent()
    
    return executeCommand(command)
}
```

### IDE UI Integration

```kotlin
// BuildManager.kt တွင် APK signing progress ပြသခြင်း
fun signApkWithProgress(apkPath: String, outputPath: String) {
    viewModel.updateBuildLog("[INFO] Signing APK...")
    
    val signed = signApk(apkPath, outputPath)
    if (signed) {
        viewModel.updateBuildLog("[SUCCESS] APK signed")
        viewModel.updateBuildProgress(90) // 90% complete
        
        // APK align လုပ်ခြင်း
        val aligned = alignApk(outputPath, outputPath.replace(".apk", "-aligned.apk"))
        if (aligned) {
            viewModel.updateBuildLog("[SUCCESS] APK aligned")
            viewModel.updateBuildProgress(100) // 100% complete
        }
    } else {
        viewModel.updateBuildLog("[ERROR] APK signing failed")
    }
}
```

---

## 5. Complete Build Pipeline Integration

### Full Build Process

```kotlin
// BuildManager.kt တွင် အပြည့်အစုံ build process
fun buildProject(projectPath: String): Boolean {
    try {
        // Step 1: Validate project
        viewModel.updateBuildLog("[INFO] Validating project...")
        if (!validateProject(projectPath)) {
            viewModel.updateBuildLog("[ERROR] Project validation failed")
            return false
        }
        viewModel.updateBuildProgress(10)
        
        // Step 2: Compile resources
        viewModel.updateBuildLog("[INFO] Compiling resources...")
        if (!compileResources(projectPath)) {
            viewModel.updateBuildLog("[ERROR] Resource compilation failed")
            return false
        }
        viewModel.updateBuildProgress(25)
        
        // Step 3: Compile sources
        viewModel.updateBuildLog("[INFO] Compiling sources...")
        if (!compileSourcesWithProgress(projectPath)) {
            viewModel.updateBuildLog("[ERROR] Source compilation failed")
            return false
        }
        viewModel.updateBuildProgress(50)
        
        // Step 4: Convert to DEX
        viewModel.updateBuildLog("[INFO] Converting to DEX...")
        if (!convertToDex(projectPath)) {
            viewModel.updateBuildLog("[ERROR] DEX conversion failed")
            return false
        }
        viewModel.updateBuildProgress(75)
        
        // Step 5: Package APK
        viewModel.updateBuildLog("[INFO] Packaging APK...")
        val apkPath = packageApk(projectPath)
        if (apkPath == null) {
            viewModel.updateBuildLog("[ERROR] APK packaging failed")
            return false
        }
        viewModel.updateBuildProgress(85)
        
        // Step 6: Sign APK
        viewModel.updateBuildLog("[INFO] Signing APK...")
        val signedApk = "$projectPath/app/build/outputs/apk/app-signed.apk"
        if (!signApk(apkPath, signedApk)) {
            viewModel.updateBuildLog("[ERROR] APK signing failed")
            return false
        }
        viewModel.updateBuildProgress(95)
        
        // Step 7: Align APK
        viewModel.updateBuildLog("[INFO] Aligning APK...")
        val alignedApk = "$projectPath/app/build/outputs/apk/app-aligned.apk"
        if (!alignApk(signedApk, alignedApk)) {
            viewModel.updateBuildLog("[ERROR] APK alignment failed")
            return false
        }
        viewModel.updateBuildProgress(100)
        
        viewModel.updateBuildLog("[SUCCESS] Build completed successfully!")
        viewModel.updateBuildLog("[INFO] APK: $alignedApk")
        
        return true
    } catch (e: Exception) {
        viewModel.updateBuildLog("[ERROR] Build failed: ${e.message}")
        return false
    }
}
```

---

## 6. IDE UI Components အတွက် Integration

### Build Progress Dialog

```kotlin
// ui/BuildProgressDialog.kt တွင် progress ပြသခြင်း
@Composable
fun BuildProgressDialog(
    isBuilding: Boolean,
    buildProgress: Int,
    buildLogs: List<String>
) {
    if (isBuilding) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Building Project...",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Progress bar
                    LinearProgressIndicator(
                        progress = buildProgress / 100f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(
                        "$buildProgress%",
                        modifier = Modifier.align(Alignment.End)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Build logs
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        items(buildLogs) { log ->
                            Text(
                                log,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}
```

### Build Output Panel

```kotlin
// ui/BottomPanels.kt တွင် build output ပြသခြင်း
@Composable
fun BuildOutputPanel(buildLogs: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x1E1E1E))
            .padding(8.dp)
    ) {
        items(buildLogs) { log ->
            val color = when {
                log.contains("[ERROR]") -> Color.Red
                log.contains("[WARNING]") -> Color.Yellow
                log.contains("[SUCCESS]") -> Color.Green
                else -> Color.White
            }
            
            Text(
                log,
                color = color,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
```

---

## 7. File System အတွင်း Binary Files ကို Manage လုပ်ခြင်း

### Binary Download & Cache

```kotlin
// BuildManager.kt တွင် binary management
fun ensureBuildToolsAvailable(): Boolean {
    val toolsDir = "/data/data/com.example.androidstudiomini/build-tools"
    
    val tools = listOf(
        "aapt2" to "https://dl.google.com/android/repository/aapt2-7.4.0.zip",
        "d8" to "https://dl.google.com/android/repository/d8-7.4.0.zip",
        "apksigner" to "https://dl.google.com/android/repository/apksigner-7.4.0.zip"
    )
    
    for ((toolName, url) in tools) {
        val toolPath = "$toolsDir/$toolName"
        
        if (!File(toolPath).exists()) {
            viewModel.updateBuildLog("[INFO] Downloading $toolName...")
            downloadFile(url, toolPath)
            File(toolPath).setExecutable(true)
        }
    }
    
    return true
}

fun downloadFile(url: String, destinationPath: String) {
    val connection = URL(url).openConnection() as HttpURLConnection
    val inputStream = connection.inputStream
    val outputStream = FileOutputStream(destinationPath)
    
    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()
}
```

---

## 8. Error Handling & Logging

### Comprehensive Error Handling

```kotlin
// BuildManager.kt တွင် error handling
fun buildProjectWithErrorHandling(projectPath: String): BuildResult {
    return try {
        val result = buildProject(projectPath)
        
        if (result) {
            BuildResult.Success(
                apkPath = "$projectPath/app/build/outputs/apk/app-aligned.apk",
                duration = System.currentTimeMillis() - startTime
            )
        } else {
            BuildResult.Failure("Build failed - check logs for details")
        }
    } catch (e: IOException) {
        BuildResult.Failure("I/O Error: ${e.message}")
    } catch (e: SecurityException) {
        BuildResult.Failure("Security Error: ${e.message}")
    } catch (e: Exception) {
        BuildResult.Failure("Unexpected Error: ${e.message}")
    }
}

sealed class BuildResult {
    data class Success(val apkPath: String, val duration: Long) : BuildResult()
    data class Failure(val error: String) : BuildResult()
}
```

---

## အကျဉ်းချုပ်

### Build Tools Integration Checklist

| Tool | အခန်းကဏ္ဍ | IDE Integration |
|------|---------|-----------------|
| **aapt2** | Resource compilation | ✅ Integrated |
| **javac** | Java compilation | ✅ Integrated |
| **kotlinc** | Kotlin compilation | ✅ Integrated |
| **d8** | DEX conversion | ✅ Integrated |
| **apksigner** | APK signing | ✅ Integrated |
| **zipalign** | APK alignment | ✅ Integrated |

### အသုံးပြုနိုင်သည့် Build Pipeline

```
1. Resource Compilation (aapt2)
   ↓
2. Source Compilation (javac/kotlinc)
   ↓
3. DEX Conversion (d8)
   ↓
4. APK Packaging
   ↓
5. APK Signing (apksigner)
   ↓
6. APK Alignment (zipalign)
   ↓
7. Final APK Ready
```

---

**Version**: 1.0  
**Last Updated**: June 24, 2026  
**Language**: Myanmar (Burmese)
