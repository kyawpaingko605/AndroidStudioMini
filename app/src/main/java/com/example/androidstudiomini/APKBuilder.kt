package com.example.androidstudiomini

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * APK Builder - Builds and signs APK files
 */
class APKBuilder(private val projectPath: String) {

    /**
     * Build APK
     */
    fun buildAPK(
        buildType: BuildType = BuildType.DEBUG,
        onProgress: (String, Int) -> Unit = { _, _ -> }
    ): APKBuildResult {
        return try {
            val startTime = System.currentTimeMillis()

            onProgress("Validating project structure", 10)
            val validation = validateProject()
            if (!validation.isValid) {
                return APKBuildResult(
                    success = false,
                    errorMessage = validation.errors.joinToString("\n"),
                    duration = System.currentTimeMillis() - startTime
                )
            }

            onProgress("Preparing build environment", 20)
            prepareBuildEnvironment()

            onProgress("Compiling resources", 30)
            compileResources()

            onProgress("Compiling Java/Kotlin", 40)
            compileJavaKotlin()

            onProgress("Converting to DEX", 50)
            convertToDEX()

            onProgress("Packaging APK", 60)
            val apkPath = packageAPK(buildType)

            onProgress("Signing APK", 80)
            val signedAPKPath = signAPK(apkPath, buildType)

            onProgress("Aligning APK", 90)
            val alignedAPKPath = alignAPK(signedAPKPath)

            onProgress("Build complete", 100)

            val duration = System.currentTimeMillis() - startTime

            APKBuildResult(
                success = true,
                apkPath = alignedAPKPath,
                apkSize = File(alignedAPKPath).length(),
                duration = duration,
                buildType = buildType
            )
        } catch (e: Exception) {
            e.printStackTrace()
            APKBuildResult(
                success = false,
                errorMessage = e.message ?: "Unknown error",
                duration = System.currentTimeMillis() - startTime
            )
        }
    }

    /**
     * Validate project
     */
    private fun validateProject(): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        val projectDir = File(projectPath)
        if (!projectDir.exists()) {
            errors.add("Project directory not found")
            return ValidationResult(false, errors, warnings)
        }

        // Check required files
        if (!File(projectDir, "build.gradle.kts").exists()) {
            errors.add("build.gradle.kts not found")
        }

        if (!File(projectDir, "app/build.gradle.kts").exists()) {
            errors.add("app/build.gradle.kts not found")
        }

        if (!File(projectDir, "app/src/main/AndroidManifest.xml").exists()) {
            errors.add("AndroidManifest.xml not found")
        }

        // Check source files
        val srcDir = File(projectDir, "app/src/main/java")
        if (!srcDir.exists() || srcDir.listFiles()?.isEmpty() != false) {
            warnings.add("No source files found")
        }

        return ValidationResult(errors.isEmpty(), errors, warnings)
    }

    /**
     * Prepare build environment
     */
    private fun prepareBuildEnvironment() {
        val buildDir = File(projectPath, "build")
        if (!buildDir.exists()) {
            buildDir.mkdirs()
        }

        val appBuildDir = File(projectPath, "app/build")
        if (!appBuildDir.exists()) {
            appBuildDir.mkdirs()
        }
    }

    /**
     * Compile resources
     */
    private fun compileResources() {
        // Simulate resource compilation
        val resDir = File(projectPath, "app/src/main/res")
        if (resDir.exists()) {
            val compiledDir = File(projectPath, "app/build/compiled-res")
            compiledDir.mkdirs()
        }
    }

    /**
     * Compile Java/Kotlin
     */
    private fun compileJavaKotlin() {
        // Simulate Java/Kotlin compilation
        val srcDir = File(projectPath, "app/src/main/java")
        if (srcDir.exists()) {
            val classesDir = File(projectPath, "app/build/classes")
            classesDir.mkdirs()
        }
    }

    /**
     * Convert to DEX
     */
    private fun convertToDEX() {
        // Simulate DEX conversion
        val dexDir = File(projectPath, "app/build/dex")
        dexDir.mkdirs()

        val dexFile = File(dexDir, "classes.dex")
        dexFile.createNewFile()
    }

    /**
     * Package APK
     */
    private fun packageAPK(buildType: BuildType): String {
        val apkDir = File(projectPath, "app/build/outputs/apk")
        apkDir.mkdirs()

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val apkName = "app-${buildType.name.lowercase()}-$timestamp.apk"
        val apkPath = File(apkDir, apkName).absolutePath

        // Create dummy APK file
        File(apkPath).createNewFile()

        return apkPath
    }

    /**
     * Sign APK
     */
    private fun signAPK(apkPath: String, buildType: BuildType): String {
        val signedApkPath = apkPath.replace(".apk", "-signed.apk")

        // Simulate APK signing
        File(apkPath).copyTo(File(signedApkPath), overwrite = true)

        return signedApkPath
    }

    /**
     * Align APK
     */
    private fun alignAPK(apkPath: String): String {
        val alignedApkPath = apkPath.replace("-signed.apk", "-aligned.apk")

        // Simulate APK alignment
        File(apkPath).copyTo(File(alignedApkPath), overwrite = true)

        return alignedApkPath
    }

    /**
     * Clean build
     */
    fun cleanBuild() {
        try {
            val buildDir = File(projectPath, "build")
            if (buildDir.exists()) {
                buildDir.deleteRecursively()
            }

            val appBuildDir = File(projectPath, "app/build")
            if (appBuildDir.exists()) {
                appBuildDir.deleteRecursively()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Get build artifacts
     */
    fun getBuildArtifacts(): List<File> {
        val apkDir = File(projectPath, "app/build/outputs/apk")
        return if (apkDir.exists()) {
            apkDir.listFiles()?.filter { it.extension == "apk" } ?: emptyList()
        } else {
            emptyList()
        }
    }

    /**
     * Get last built APK
     */
    fun getLastBuiltAPK(): File? {
        return getBuildArtifacts().maxByOrNull { it.lastModified() }
    }
}

/**
 * Build Type enum
 */
enum class BuildType {
    DEBUG, RELEASE
}

/**
 * APK Build Result
 */
data class APKBuildResult(
    val success: Boolean,
    val apkPath: String? = null,
    val apkSize: Long = 0,
    val duration: Long = 0,
    val buildType: BuildType = BuildType.DEBUG,
    val errorMessage: String? = null
) {
    fun getApkSizeFormatted(): String {
        return when {
            apkSize <= 0L -> "0 B"
            apkSize < 1024 -> "$apkSize B"
            apkSize < 1024 * 1024 -> "${apkSize / 1024} KB"
            else -> "${apkSize / (1024 * 1024)} MB"
        }
    }

    fun getDurationFormatted(): String {
        return "${duration / 1000}s ${duration % 1000}ms"
    }

    fun getSummary(): String {
        return if (success) {
            "Build successful: ${getApkSizeFormatted()} in ${getDurationFormatted()}"
        } else {
            "Build failed: $errorMessage"
        }
    }
}
