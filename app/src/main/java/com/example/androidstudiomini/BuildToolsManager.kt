package com.example.androidstudiomini

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.HttpURLConnection

/**
 * BuildToolsManager - Android build tools integration
 * 
 * Manages integration of:
 * - aapt2: Resource compilation
 * - javac/kotlinc: Source compilation
 * - d8: DEX conversion
 * - apksigner: APK signing
 * - zipalign: APK alignment
 */
class BuildToolsManager(private val context: Context) {
    
    private val toolsDir = "${context.filesDir}/build-tools"
    private val buildLog = mutableListOf<String>()
    
    // ==================== Tool Download & Setup ====================
    
    /**
     * Ensure all build tools are available
     */
    suspend fun ensureBuildToolsAvailable(): Boolean = withContext(Dispatchers.IO) {
        try {
            File(toolsDir).mkdirs()
            
            val tools = mapOf(
                "aapt2" to "https://dl.google.com/android/repository/aapt2-7.4.0.zip",
                "d8" to "https://dl.google.com/android/repository/d8-7.4.0.zip",
                "apksigner" to "https://dl.google.com/android/repository/apksigner-7.4.0.zip"
            )
            
            for ((toolName, url) in tools) {
                val toolPath = "$toolsDir/$toolName"
                if (!File(toolPath).exists()) {
                    addLog("[INFO] Downloading $toolName...")
                    downloadFile(url, toolPath)
                    File(toolPath).setExecutable(true)
                }
            }
            
            true
        } catch (e: Exception) {
            addLog("[ERROR] Failed to ensure build tools: ${e.message}")
            false
        }
    }
    
    /**
     * Download file from URL
     */
    private fun downloadFile(url: String, destinationPath: String) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        
        val inputStream = connection.inputStream
        val outputStream = FileOutputStream(destinationPath)
        
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
    }
    
    // ==================== Resource Compilation (aapt2) ====================
    
    /**
     * Compile Android resources using aapt2
     */
    suspend fun compileResources(projectPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            addLog("[INFO] Compiling resources...")
            
            val resPath = "$projectPath/app/src/main/res"
            val outputPath = "$projectPath/app/build/intermediates/compiled_res"
            
            File(outputPath).mkdirs()
            
            val aapt2Path = "$toolsDir/aapt2"
            val command = arrayOf(
                "sh", "-c",
                "$aapt2Path compile --dir $resPath -o $outputPath/resources.zip"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                addLog("[SUCCESS] Resources compiled")
                true
            } else {
                addLog("[ERROR] Resource compilation failed (exit code: $exitCode)")
                false
            }
        } catch (e: Exception) {
            addLog("[ERROR] Resource compilation error: ${e.message}")
            false
        }
    }
    
    /**
     * Link compiled resources
     */
    suspend fun linkResources(projectPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            addLog("[INFO] Linking resources...")
            
            val compiledRes = "$projectPath/app/build/intermediates/compiled_res/resources.zip"
            val manifest = "$projectPath/app/src/main/AndroidManifest.xml"
            val outputApk = "$projectPath/app/build/intermediates/linked_res/resources.apk"
            
            File("$projectPath/app/build/intermediates/linked_res").mkdirs()
            
            val aapt2Path = "$toolsDir/aapt2"
            val command = arrayOf(
                "sh", "-c",
                "$aapt2Path link -o $outputApk --manifest $manifest $compiledRes"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                addLog("[SUCCESS] Resources linked")
                true
            } else {
                addLog("[ERROR] Resource linking failed")
                false
            }
        } catch (e: Exception) {
            addLog("[ERROR] Resource linking error: ${e.message}")
            false
        }
    }
    
    // ==================== Source Compilation ====================
    
    /**
     * Compile Java source files
     */
    suspend fun compileJavaSource(projectPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            addLog("[INFO] Compiling Java sources...")
            
            val srcPath = "$projectPath/app/src/main/java"
            val outputPath = "$projectPath/app/build/intermediates/classes"
            
            File(outputPath).mkdirs()
            
            val javaFiles = File(srcPath).walk()
                .filter { it.isFile && it.extension == "java" }
                .map { it.absolutePath }
                .toList()
            
            if (javaFiles.isEmpty()) {
                addLog("[INFO] No Java files found")
                return@withContext true
            }
            
            val command = arrayOf(
                "sh", "-c",
                "javac -d $outputPath -source 11 -target 11 ${javaFiles.joinToString(" ")}"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                addLog("[SUCCESS] Java compilation complete")
                true
            } else {
                addLog("[ERROR] Java compilation failed")
                false
            }
        } catch (e: Exception) {
            addLog("[ERROR] Java compilation error: ${e.message}")
            false
        }
    }
    
    /**
     * Compile Kotlin source files
     */
    suspend fun compileKotlinSource(projectPath: String): Boolean = withContext(Dispatchers.IO) {
        try {
            addLog("[INFO] Compiling Kotlin sources...")
            
            val srcPath = "$projectPath/app/src/main/java"
            val outputPath = "$projectPath/app/build/intermediates/classes"
            
            File(outputPath).mkdirs()
            
            val kotlinFiles = File(srcPath).walk()
                .filter { it.isFile && it.extension == "kt" }
                .map { it.absolutePath }
                .toList()
            
            if (kotlinFiles.isEmpty()) {
                addLog("[INFO] No Kotlin files found")
                return@withContext true
            }
            
            val command = arrayOf(
                "sh", "-c",
                "kotlinc -d $outputPath -jvm-target 11 ${kotlinFiles.joinToString(" ")}"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                addLog("[SUCCESS] Kotlin compilation complete")
                true
            } else {
                addLog("[ERROR] Kotlin compilation failed")
                false
            }
        } catch (e: Exception) {
            addLog("[ERROR] Kotlin compilation error: ${e.message}")
            false
        }
    }
    
    // ==================== DEX Conversion (d8) ====================
    
    /**
     * Convert bytecode to DEX format using d8
     */
    suspend fun convertToDex(projectPath: String, multiDex: Boolean = false): Boolean = 
        withContext(Dispatchers.IO) {
            try {
                addLog("[INFO] Converting to DEX format...")
                
                val classesPath = "$projectPath/app/build/intermediates/classes"
                val outputPath = "$projectPath/app/build/intermediates/dex"
                
                File(outputPath).mkdirs()
                
                val classFiles = File(classesPath).walk()
                    .filter { it.isFile && it.extension == "class" }
                    .map { it.absolutePath }
                    .toList()
                
                if (classFiles.isEmpty()) {
                    addLog("[ERROR] No class files found")
                    return@withContext false
                }
                
                val d8Path = "$toolsDir/d8"
                val multiDexFlag = if (multiDex) "--multi-dex" else ""
                
                val command = arrayOf(
                    "sh", "-c",
                    "$d8Path --output=$outputPath --min-api=26 $multiDexFlag ${classFiles.joinToString(" ")}"
                )
                
                val process = Runtime.getRuntime().exec(command)
                val exitCode = process.waitFor()
                
                if (exitCode == 0) {
                    addLog("[SUCCESS] DEX conversion complete")
                    true
                } else {
                    addLog("[ERROR] DEX conversion failed")
                    false
                }
            } catch (e: Exception) {
                addLog("[ERROR] DEX conversion error: ${e.message}")
                false
            }
        }
    
    // ==================== APK Packaging ====================
    
    /**
     * Package DEX and resources into APK
     */
    suspend fun packageApk(projectPath: String): String? = withContext(Dispatchers.IO) {
        try {
            addLog("[INFO] Packaging APK...")
            
            val dexPath = "$projectPath/app/build/intermediates/dex/classes.dex"
            val resourcesPath = "$projectPath/app/build/intermediates/linked_res/resources.apk"
            val outputPath = "$projectPath/app/build/outputs/apk/app-unsigned.apk"
            
            File("$projectPath/app/build/outputs/apk").mkdirs()
            
            // Copy resources as base APK
            File(resourcesPath).copyTo(File(outputPath), overwrite = true)
            
            // Add DEX to APK using zip
            val command = arrayOf(
                "sh", "-c",
                "cd ${File(outputPath).parent} && zip -q app-unsigned.apk $dexPath"
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                addLog("[SUCCESS] APK packaged: $outputPath")
                outputPath
            } else {
                addLog("[ERROR] APK packaging failed")
                null
            }
        } catch (e: Exception) {
            addLog("[ERROR] APK packaging error: ${e.message}")
            null
        }
    }
    
    // ==================== APK Signing (apksigner) ====================
    
    /**
     * Create debug keystore
     */
    suspend fun createDebugKeystore(): Boolean = withContext(Dispatchers.IO) {
        try {
            val keystorePath = "$toolsDir/debug.keystore"
            
            if (File(keystorePath).exists()) {
                return@withContext true
            }
            
            addLog("[INFO] Creating debug keystore...")
            
            val command = arrayOf(
                "sh", "-c",
                """
                    keytool -genkey -v -keystore $keystorePath \
                    -keyalg RSA -keysize 2048 -validity 10000 \
                    -alias debug -storepass android -keypass android \
                    -dname "CN=Android Studio Mini, O=Development, C=MM"
                """.trimIndent()
            )
            
            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()
            
            if (exitCode == 0) {
                addLog("[SUCCESS] Debug keystore created")
                true
            } else {
                addLog("[ERROR] Debug keystore creation failed")
                false
            }
        } catch (e: Exception) {
            addLog("[ERROR] Keystore creation error: ${e.message}")
            false
        }
    }
    
    /**
     * Sign APK using apksigner
     */
    suspend fun signApk(apkPath: String, outputPath: String): Boolean = 
        withContext(Dispatchers.IO) {
            try {
                addLog("[INFO] Signing APK...")
                
                val keystorePath = "$toolsDir/debug.keystore"
                val apksignerPath = "$toolsDir/apksigner"
                
                val command = arrayOf(
                    "sh", "-c",
                    """
                        $apksignerPath sign \
                        --ks $keystorePath \
                        --ks-pass pass:android \
                        --ks-key-alias debug \
                        --key-pass pass:android \
                        --out $outputPath \
                        $apkPath
                    """.trimIndent()
                )
                
                val process = Runtime.getRuntime().exec(command)
                val exitCode = process.waitFor()
                
                if (exitCode == 0) {
                    addLog("[SUCCESS] APK signed")
                    true
                } else {
                    addLog("[ERROR] APK signing failed")
                    false
                }
            } catch (e: Exception) {
                addLog("[ERROR] APK signing error: ${e.message}")
                false
            }
        }
    
    // ==================== APK Alignment ====================
    
    /**
     * Align APK for optimal performance
     */
    suspend fun alignApk(inputApk: String, outputApk: String): Boolean = 
        withContext(Dispatchers.IO) {
            try {
                addLog("[INFO] Aligning APK...")
                
                val command = arrayOf(
                    "sh", "-c",
                    "zipalign -v 4 $inputApk $outputApk"
                )
                
                val process = Runtime.getRuntime().exec(command)
                val exitCode = process.waitFor()
                
                if (exitCode == 0) {
                    addLog("[SUCCESS] APK aligned")
                    true
                } else {
                    addLog("[ERROR] APK alignment failed")
                    false
                }
            } catch (e: Exception) {
                addLog("[ERROR] APK alignment error: ${e.message}")
                false
            }
        }
    
    // ==================== Complete Build Pipeline ====================
    
    /**
     * Complete build process from source to signed APK
     */
    suspend fun buildProject(projectPath: String): BuildResult {
        try {
            buildLog.clear()
            
            // Ensure tools are available
            if (!ensureBuildToolsAvailable()) {
                return BuildResult.Failure("Failed to ensure build tools")
            }
            
            // Create debug keystore
            if (!createDebugKeystore()) {
                return BuildResult.Failure("Failed to create debug keystore")
            }
            
            // Compile resources
            if (!compileResources(projectPath) || !linkResources(projectPath)) {
                return BuildResult.Failure("Resource compilation failed")
            }
            
            // Compile sources
            val javaOk = compileJavaSource(projectPath)
            val kotlinOk = compileKotlinSource(projectPath)
            
            if (!javaOk && !kotlinOk) {
                return BuildResult.Failure("Source compilation failed")
            }
            
            // Convert to DEX
            if (!convertToDex(projectPath)) {
                return BuildResult.Failure("DEX conversion failed")
            }
            
            // Package APK
            val apkPath = packageApk(projectPath) ?: 
                return BuildResult.Failure("APK packaging failed")
            
            // Sign APK
            val signedApk = apkPath.replace("unsigned", "signed")
            if (!signApk(apkPath, signedApk)) {
                return BuildResult.Failure("APK signing failed")
            }
            
            // Align APK
            val alignedApk = signedApk.replace("signed", "aligned")
            if (!alignApk(signedApk, alignedApk)) {
                return BuildResult.Failure("APK alignment failed")
            }
            
            addLog("[SUCCESS] Build completed successfully!")
            addLog("[INFO] APK: $alignedApk")
            
            return BuildResult.Success(alignedApk, buildLog)
            
        } catch (e: Exception) {
            addLog("[ERROR] Build failed: ${e.message}")
            return BuildResult.Failure(e.message ?: "Unknown error")
        }
    }
    
    // ==================== Logging ====================
    
    private fun addLog(message: String) {
        buildLog.add(message)
    }
    
    fun getBuildLogs(): List<String> = buildLog.toList()
    
    // ==================== Result Classes ====================
    
    sealed class BuildResult {
        data class Success(val apkPath: String, val logs: List<String>) : BuildResult()
        data class Failure(val error: String) : BuildResult()
    }
}
